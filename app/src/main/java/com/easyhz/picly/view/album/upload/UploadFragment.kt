package com.easyhz.picly.view.album.upload

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentUploadBinding
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem
import com.easyhz.picly.domain.model.album.upload.gallery.GalleryImageItem.Companion.toGalleryImageItem
import com.easyhz.picly.provider.PiclyFileProvider
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.animateGrow
import com.easyhz.picly.util.calculatePeriod
import com.easyhz.picly.util.convertToDateFormat
import com.easyhz.picly.util.convertToTimeFormat
import com.easyhz.picly.util.getNextWeek
import com.easyhz.picly.util.getNextYear
import com.easyhz.picly.util.getTime
import com.easyhz.picly.util.getToday
import com.easyhz.picly.util.showAlertDialog
import com.easyhz.picly.util.toDateFormat
import com.easyhz.picly.util.toGalleryImageItem
import com.easyhz.picly.util.toMs
import com.easyhz.picly.util.toPx
import com.easyhz.picly.util.toTimeFormat
import com.easyhz.picly.util.toTimeFormat24
import com.easyhz.picly.view.album.SharedAlbumStateViewModel
import com.easyhz.picly.view.album.upload.gallery.GalleryBottomSheetFragment
import com.easyhz.picly.view.album.upload.gallery.GalleryImageAdapter.Companion.MAX_SELECTED
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class UploadFragment: Fragment() {
    private lateinit var binding: FragmentUploadBinding
    private lateinit var viewModel: UploadViewModel
    private lateinit var sharedViewModel: SharedAlbumStateViewModel
    private lateinit var tagAdapter: TagAdapter
    private lateinit var uploadImageAdapter: UploadImageAdapter
    private lateinit var loading: LoadingDialog
    private var isShowCalendar: Boolean = false
    private var isShowTimePicker: Boolean = false
    private var isGranted: Boolean = false
    private val args: UploadFragmentArgs by navArgs()
    private val galleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        this.isGranted = isGranted
        if (isGranted) getIncomingImages()
        else showGalleryPermissionDialog()
    }
    private var bottomSheetFragment: GalleryBottomSheetFragment? = null
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[UploadViewModel::class.java]
        sharedViewModel = ViewModelProvider(requireActivity())[SharedAlbumStateViewModel::class.java]
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        initSelectedImageList()
        initTagList()
    }

    private fun setUp() {
        checkPermission()
        initCalendarView()
        initTimePicker()
        setTagField()
        setTagRecyclerView()
        setUploadImageRecyclerView()
        observeGallerySelectedImageList()
        setExpireButtonText(binding.expireDateButton, getNextWeek().toDateFormat())
        setExpireButtonText(binding.expireTimeButton, getTime().toTimeFormat24())
        setPeriod()
        onClickExpireDateButton()
        onClickExpireTimeButton()
        onClickBackground()
        onClickUploadButton()
        onClickBackButton()
        setActivityResultLauncher()
    }

    private fun checkPermission() {
        if (args.incomingImages.isNullOrEmpty()) return
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isGranted = true
            getIncomingImages()
        } else {
            galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun getIncomingImages() {
        if (!isStoragePermissionGranted()) {
            showGalleryPermissionDialog()
            return
        }
        CoroutineScope(Dispatchers.Main).launch {
            loading.show(true)
            try {
                val incomingImages = args.incomingImages?.mapNotNull {
                    PiclyFileProvider.getIncomingImageUri(requireContext(), it)
                }
                incomingImages?.toGalleryImageItem(requireActivity())?.let {
                    viewModel.addSelectedImageList(it)
                }
            } catch (e: Exception) {
                BlueSnackBar.make(binding.root, getString(R.string.incoming_image_error)).show()
            } finally {
                loading.show(false)
            }
        }
    }

    private fun initCalendarView() {
        binding.calendarView.visibility = View.INVISIBLE
    }

    private fun initTimePicker() {
        binding.timePicker.visibility = View.INVISIBLE
    }

    private fun setTagField() {
        binding.tagField.apply {
            textViewLabel.text = getString(R.string.tag)
            editText.hint = getString(R.string.tag_hint)
            editText.textSize = 18F
            editTextField.endIconMode = TextInputLayout.END_ICON_NONE
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.icon_tag_24,
                0,
                0,
                0
            )
            editText.inputType = InputType.TYPE_CLASS_TEXT
            editText.imeOptions = EditorInfo.IME_ACTION_DONE
            editText.setOnEditorActionListener { editText, action, _ ->

                if (action == EditorInfo.IME_ACTION_DONE) {
                    addTag(editText.text.toString())
                }
                true
            }
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.contains(" ") == true) {
                        addTag(editText.text.toString().replace(" ",""))
                    }
                }
                override fun afterTextChanged(s: Editable?) { }
            })
        }
    }

    private fun addTag(text: String) {
        viewModel.addTag(text)
        resetTagList()
        binding.tagField.editText.setText("")
    }


    private fun setTagRecyclerView() {
        tagAdapter = TagAdapter { tag ->
            viewModel.removeTag(tag)
            resetTagList()
        }
        binding.tagRecyclerView.apply {
            adapter = tagAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }
    private fun setUploadImageRecyclerView() {
        uploadImageAdapter = UploadImageAdapter(
            onClickAdd = ::onClickAddImage
        ) {
            viewModel.deleteSelectedImageList(it)
        }
        binding.albumField.uploadImageRecyclerView.apply {
            adapter = uploadImageAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun resetTagList() {
        viewModel.tags.value?.let {
            tagAdapter.setTagList(it)
        }
    }

    private fun onClickAddImage() {
        showGallery()
//        if (isStoragePermissionGranted()) {
//        showGalleryBottomSheet()
//        } else {
//            showGalleryPermissionDialog()
//        }
    }


    private fun isStoragePermissionGranted(): Boolean {
        return isGranted || ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun showGalleryBottomSheet() {
        if (bottomSheetFragment != null && bottomSheetFragment!!.isAdded) return
        bottomSheetFragment = GalleryBottomSheetFragment()
        bottomSheetFragment!!.show(requireActivity().supportFragmentManager, "Gallery_Bottom_Sheet")
    }

    private fun showGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.apply {
            type = "image/**"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
        getResult.launch(intent)
    }

    private fun setActivityResultLauncher() {
        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) return@registerForActivityResult
            val data: Intent? = result.data
            val selectedImages: ArrayList<GalleryImageItem> = arrayListOf()
            data?.clipData?.let { clipData ->
                if (clipData.itemCount + (viewModel.selectedImageList.value?.size ?: 0) > MAX_SELECTED) {
                    BlueSnackBar.make(binding.root, getString(R.string.over_selected)).show()
                    return@registerForActivityResult
                }
                CoroutineScope(Dispatchers.Main).launch {
                    for (i in 0 until clipData.itemCount) {
                        val item = clipData.getItemAt(i).uri.toGalleryImageItem(requireActivity())
                        item?.let {
                            selectedImages.add(it)
                        }
                    }
                    viewModel.addSelectedImageList(selectedImages)
                }
            }
        }
    }


    private fun observeGallerySelectedImageList() {
        viewModel.selectedImageList.observe(viewLifecycleOwner) {
            uploadImageAdapter.setUploadImageList(it)
            binding.albumField.subTextView.text = it.size.toSelected()
        }
    }

    private fun initSelectedImageList() {
        viewModel.initSelectedImageList()
    }

    private fun initTagList() {
        viewModel.initTagList()
    }

    private fun manageCalendarDialog() {
        binding.calendarView.apply {
            setMarginTop()
            animateGrow(isShowCalendar)
            minDate = getToday().toMs()
            maxDate = getNextYear().toMs()
            date = getNextWeek().toMs()
            requestLayout()

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                setExpireButtonText(binding.expireDateButton, convertToDateFormat(year, month + 1, dayOfMonth))
                setPeriod()
                setExpireTimeAtDate()
            }
        }
    }

    private fun manageTimePicker() {
        binding.timePicker.apply {
            setMarginTop()
            animateGrow(isShowTimePicker, tx = -0.2f)
            setOnTimeChangedListener { _, hourOfDay, minute ->
                val (minHour, minMinute) = getTime().toTimeFormat().split(":")
                if (binding.expireDateButton.text == getToday().toDateFormat() && (hourOfDay < minHour.toInt() || (hourOfDay == minHour.toInt() && minute < minMinute.toInt()))) {
                    this.hour = minHour.toInt()
                    this.minute = minMinute.toInt()
                }
                setExpireButtonText(binding.expireTimeButton, convertToTimeFormat(this.hour, this.minute))
                setPeriod()
            }
        }
    }

    private fun setMarginTop(){
        if (viewModel.tags.value.isNullOrEmpty()) {
            binding.calendarViewTop.layoutParams.height = SubView.CALENDAR.defaultMargin.toPx(requireContext())
            binding.timePickerTop.layoutParams.height = SubView.TIME.defaultMargin.toPx(requireContext())
        } else {
            binding.calendarViewTop.layoutParams.height = SubView.CALENDAR.secondMarin.toPx(requireContext())
            binding.timePickerTop.layoutParams.height = SubView.TIME.secondMarin.toPx(requireContext())
        }
    }

    private fun setExpireButtonText(button: Button, newText: String) {
        button.text = newText
    }

    private fun setPeriod() {
        binding.apply {
            period.text = calculatePeriod(expireDateButton.text.toString(), expireTimeButton.text.toString())
        }
    }

    private fun onClickExpireDateButton() {
        binding.expireDateButton.apply {
            setOnClickListener {
                isShowCalendar = !isShowCalendar
                setDoNotShowTimePicker()
                manageCalendarDialog()
                setButtonTextColor(this, isShowCalendar)
            }
        }
    }

    private fun onClickExpireTimeButton() {
        binding.expireTimeButton.apply {
            setOnClickListener {
                isShowTimePicker = !isShowTimePicker
                setDoNotShowCalendarView()
                manageTimePicker()
                setButtonTextColor(this, isShowTimePicker)
            }
        }
    }

    private fun setButtonTextColor(button: Button, isShow: Boolean) {
        button.setTextColor(ContextCompat.getColor(requireContext(), if (isShow) R.color.highlightBlue else R.color.mainText))
    }

    private fun onClickBackground() {
        binding.relativeLayout.setOnClickListener {
            setDoNotShowCalendarView()
            setDoNotShowTimePicker()
            if (binding.tagField.editText.text.isNullOrBlank()) return@setOnClickListener
            addTag(binding.tagField.editText.text.toString())
        }
    }

    private fun onClickUploadButton() {
        binding.toolbar.uploadTextView.setOnClickListener {
            if (viewModel.selectedImageList.value.isNullOrEmpty()) {
                BlueSnackBar.make(binding.root, getString(R.string.no_select_image)).show()
                return@setOnClickListener
            }
            val remainText = binding.tagField.editText.text
            viewModel.selectedImageList.value?.let { imageList ->
                loading.show(true)
                CoroutineScope(Dispatchers.Main).launch {
                    if (!remainText.isNullOrBlank()) { addTag(remainText.toString()) }
                    val result = viewModel.writeAlbums(
                        imageList,
                        binding.expireDateButton.text.toString(),
                        binding.expireTimeButton.text.toString(),
                    )
                    when(result) {
                        is AlbumResult.Success<*> -> onSuccess(result.value as String)
                        is AlbumResult.Error -> onFailure(result.errorMessage)
                    }
                }
            }
        }
    }

    private fun setExpireTimeAtDate() {
        val (minHour, minMinute) = getTime().toTimeFormat().split(":")
        val expireTimeParts = binding.expireTimeButton.text.split(":", " ")

        var hour = expireTimeParts[0].toInt()
        val min = expireTimeParts[1].toInt()
        if (expireTimeParts[2] == "오후") {
            hour += 12
        }

        val isSameDay = binding.expireDateButton.text == getToday().toDateFormat()
        val isBeforeMinTime = hour < minHour.toInt() || (hour == minHour.toInt() && min < minMinute.toInt())

        if (isSameDay && isBeforeMinTime) {
            binding.timePicker.hour = minHour.toInt()
            binding.timePicker.minute = minMinute.toInt()
        }
    }

    private fun onSuccess(message: String) {
        showUrlDialog(message)
        loading.show(false)
    }

    private fun onFailure(message: String) {
        BlueSnackBar.make(binding.root, message).show()
        loading.show(false)
    }

    private fun onClickBackButton() {
        binding.toolbar.backButton.setOnClickListener {
            NavControllerManager.navigateToBack()
        }
    }

    private fun setDoNotShowCalendarView() {
        if (isShowCalendar) {
            isShowCalendar = false
            manageCalendarDialog()
            setButtonTextColor(binding.expireDateButton, isShowCalendar)
        }
    }
    private fun setDoNotShowTimePicker() {
        if (isShowTimePicker) {
            isShowTimePicker = false
            manageTimePicker()
            setButtonTextColor(binding.expireTimeButton, isShowTimePicker)
        }
    }

    private fun showGalleryPermissionDialog() {
        showAlertDialog(
            context = requireContext(),
            title= R.string.dialog_gallery_permission_title,
            message = R.string.dialog_gallery_permission_message,
            positiveButtonText = R.string.dialog_gallery_permission_positive,
            onContinue = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:" + requireActivity().packageName)
                startActivity(intent)
            },
            negativeButtonText = R.string.dialog_upload_negative,
            onCancel = {
                BlueSnackBar.make(binding.root, getString(R.string.incoming_image_error))
            }
        )
    }

    private fun showUrlDialog(url: String) {
        showAlertDialog(
            context = requireContext(),
            title= R.string.dialog_upload_title,
            message = R.string.dialog_upload_message,
            positiveButtonText = R.string.dialog_upload_positive,
            onContinue = { onContinueUrlDialog(url) },
            negativeButtonText = R.string.dialog_upload_negative,
            onCancel = { NavControllerManager.navigateToBack() }
        )
        sharedViewModel.setIsUpload(true)
    }

    private fun onContinueUrlDialog(url: String) {
        val clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText(PICLY, url)
        clipboardManager.setPrimaryClip(clipData)
        NavControllerManager.navigateToBack()
    }

    enum class SubView(val defaultMargin: Int,val secondMarin: Int ) {
        CALENDAR(defaultMargin = 12, secondMarin = 12 + 48),
        TIME(defaultMargin = 148, secondMarin = 148 + 48)
    }
}

private fun Int.toSelected(): String = "$this/$MAX_SELECTED"
