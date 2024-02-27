package com.easyhz.picly.view.album.upload

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentUploadBinding
import com.easyhz.picly.util.animateGrow
import com.easyhz.picly.util.calculatePeriod
import com.easyhz.picly.util.convertToDateFormat
import com.easyhz.picly.util.convertToTimeFormat
import com.easyhz.picly.util.getNextWeek
import com.easyhz.picly.util.getNextYear
import com.easyhz.picly.util.getTime
import com.easyhz.picly.util.getToday
import com.easyhz.picly.util.toDateFormat
import com.easyhz.picly.util.toMs
import com.easyhz.picly.util.toTimeFormat
import com.easyhz.picly.util.toTimeFormat24
import com.easyhz.picly.view.album.upload.gallery.GalleryBottomSheetFragment
import com.easyhz.picly.view.album.upload.gallery.GalleryBottomSheetViewModel
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadFragment: Fragment() {
    private lateinit var binding: FragmentUploadBinding
    private lateinit var viewModel: UploadViewModel
    private lateinit var galleryViewModel: GalleryBottomSheetViewModel
    private lateinit var tagAdapter: TagAdapter
    private lateinit var uploadImageAdapter: UploadImageAdapter
    private var isShowCalendar: Boolean = false
    private var isShowTimePicker: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[UploadViewModel::class.java]
        galleryViewModel = ViewModelProvider(requireActivity())[GalleryBottomSheetViewModel::class.java]
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        initSelectedImageList()
    }

    private fun setUp() {
        initCalendarView()
        initTimePicker()
        setTagField()
        setTagRecyclerView()
        setUploadImageRecyclerView()
        addImage()
        observeSelectedImageList()
        setExpireButtonText(binding.expireDateButton, getNextWeek().toDateFormat())
        setExpireButtonText(binding.expireTimeButton, getTime().toTimeFormat24())
        setPeriod()
        onClickExpireDateButton()
        onClickExpireTimeButton()
        onClickRelativeLayout()
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
                    viewModel.addTag(editText.text.toString())
                    resetTagList()
                    editText.text = ""
                }
                true
            }
        }
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
        uploadImageAdapter = UploadImageAdapter {
            println(it)
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

    private fun addImage() {
        binding.albumField.addImage.setOnClickListener {
            val bottomSheetFragment = GalleryBottomSheetFragment()
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        }
    }
    private fun observeSelectedImageList() {
        galleryViewModel.selectedImageList.observe(viewLifecycleOwner) {
            uploadImageAdapter.setUploadImageList(it)
        }
    }

    private fun initSelectedImageList() {
        galleryViewModel.initSelectedImageList()
    }

    private fun manageCalendarDialog() {
        binding.calendarView.apply {
            animateGrow(isShowCalendar)
            minDate = getToday().toMs()
            maxDate = getNextYear().toMs()
            date = getNextWeek().toMs()
            requestLayout()

            setOnDateChangeListener { _, year, month, dayOfMonth ->
                setExpireButtonText(binding.expireDateButton, convertToDateFormat(year, month + 1, dayOfMonth))
                setPeriod()
            }
        }
    }

    private fun manageTimePicker() {
        binding.timePicker.apply {
            animateGrow(isShowTimePicker, tx = -0.2f)
            setOnTimeChangedListener { _, hourOfDay, minute ->
                val (minHour, minMinute) = getTime().toTimeFormat().split(":")
                if (binding.expireDateButton.text == getToday().toDateFormat() && (hourOfDay < minHour.toInt() || (hourOfDay == minHour.toInt() && minute < minMinute.toInt()))) {
                    this.hour = minHour.toInt()
                    this.minute = minMinute.toInt()
                }
                setExpireButtonText(binding.expireTimeButton, convertToTimeFormat(hourOfDay, minute))
                setPeriod()
            }
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

    private fun onClickRelativeLayout() {
        binding.relativeLayout.setOnClickListener {
            setDoNotShowCalendarView()
            setDoNotShowTimePicker()
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
}