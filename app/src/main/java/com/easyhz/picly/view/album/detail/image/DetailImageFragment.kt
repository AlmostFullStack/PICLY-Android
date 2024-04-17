package com.easyhz.picly.view.album.detail.image

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentDetailImageBinding
import com.easyhz.picly.domain.model.result.AlbumResult
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.showAlertDialog
import com.easyhz.picly.view.dialog.LoadingDialog
import com.easyhz.picly.view.navigation.NavControllerManager
import com.github.chrisbanes.photoview.PhotoView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class DetailImageFragment : Fragment() {
    private lateinit var binding: FragmentDetailImageBinding
    private lateinit var viewModel: DetailImageViewModel
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var loading: LoadingDialog
    private val args: DetailImageFragmentArgs by navArgs()
    private val galleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) downloadImage()
        else showGalleryPermissionDialog()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postponeEnterTransition()
        val enterAnimation = TransitionInflater.from(context).inflateTransition(R.transition.detail_image)
        val returnAnimation = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = enterAnimation
        sharedElementReturnTransition = returnAnimation
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailImageBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity())[DetailImageViewModel::class.java]
        imageSliderAdapter = ImageSliderAdapter(args.images, ::startEnterTransition, ::onChangeScale)
        loading = LoadingDialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        view.doOnPreDraw {
            setImageSliderPosition()
        }
    }

    private fun setUp() {
        setImageSlider()
        setToolbarTitle()
        onClickBackButton()
        onClickSaveButton()
    }

    private fun setImageSlider() {
        binding.imageSlider.adapter = imageSliderAdapter
    }

    private fun setImageSliderPosition() {
        binding.imageSlider.apply {
            setCurrentItem(
                args.images.imageList.indexOf(args.images.current),
                false
            )
            overScrollMode = ViewPager2.OVER_SCROLL_NEVER
        }
    }

    private fun setToolbarTitle() {
        binding.imageSlider.registerOnPageChangeCallback(object: OnPageChangeCallback() {
            @SuppressLint("SetTextI18n")
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.toolbar.toolbarTitle.text = "${position + 1}/${args.images.imageList.size}"
            }
        })
    }

    private fun onClickBackButton() {
        binding.toolbar.backButton.setOnClickListener {
            NavControllerManager.navigateToBack()
        }
    }
    private fun onClickSaveButton() {
        binding.toolbar.saveTextView.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                downloadImage()
                return@setOnClickListener
            }
            if (checkPermission()) {
                downloadImage()
            } else {
                galleryPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }
    }

    private fun downloadImage() {
        CoroutineScope(Dispatchers.Main).launch {
            loading.show(true)
            val imageViewDrawable = binding.imageSlider.findViewById<PhotoView>(R.id.detailImageView).drawable as BitmapDrawable
            when(val result = viewModel.download(requireContext(), imageViewDrawable.bitmap)) {
                is AlbumResult.Success<*> -> showSnackBar(result.value as String)
                is AlbumResult.Error -> showSnackBar(result.errorMessage)
            }
            loading.show(false)
        }
    }

    private fun startEnterTransition(currentItem: String) {
        if (args.images.current == currentItem) {
            loading.show(true)
            startPostponedEnterTransition()
            loading.show(false)
        }
    }

    private fun onChangeScale(scale: Float) {
        binding.imageSlider.isUserInputEnabled = scale < (ImageSliderAdapter.MEDIUM_SCALE - 0.2F)   // 일정 범위 이상 확대 되면 스크롤 방지
    }

    private fun checkPermission(): Boolean = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun showSnackBar(message: String) {
        BlueSnackBar.make(binding.root, message).show()
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
}

