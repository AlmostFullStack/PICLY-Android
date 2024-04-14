package com.easyhz.picly.view.album.detail


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentAlbumDetailBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.album.detail.bottom_sheet.DetailMenuBottomSheet
import com.easyhz.picly.view.navigation.NavControllerManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlbumDetailFragment : Fragment() {
    private lateinit var binding: FragmentAlbumDetailBinding
    private lateinit var viewModel: AlbumDetailViewModel
    private lateinit var clipboardManager: ClipboardManager
    private lateinit var imageAdapter: DetailImageAdapter
    private val tagAdapter = DetailTagAdapter()
    private val args: AlbumDetailFragmentArgs by navArgs()
    private var bottomSheetFragment: DetailMenuBottomSheet? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailBinding.inflate(layoutInflater)
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        viewModel = ViewModelProvider(requireActivity())[AlbumDetailViewModel::class.java]
        imageAdapter = DetailImageAdapter(requireActivity(), onClick = ::onImageClick)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
        setDetailTransition(view)
    }
    override fun onResume() {
        super.onResume()
        viewModel.scrollPosition.observe(viewLifecycleOwner) { position ->
            binding.scrollView.post {
                binding.scrollView.smoothScrollTo(0, position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setScrollPosition(binding.scrollView.scrollY)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.setScrollPosition(0)
    }

    private fun setUp() {
        setAlbumInfo()
        setTagRecyclerView()
        setImageRecyclerView()
        onClickBackButton()
        onClickLinkButton()
        onClickMoreButton()
    }

    private fun setAlbumInfo() {
        binding.detailInfoField.apply {
            data = args.albumItem
        }
    }

    private fun setTagRecyclerView() {
        binding.detailInfoField.tagListRecyclerView.apply {
            adapter = tagAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        }
        tagAdapter.setList(args.albumItem.tags)
    }

    private fun setImageRecyclerView() {
        binding.imageRecyclerView.apply {
            adapter = imageAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            scrollState
        }
        imageAdapter.setList(args.albumItem.toDetailImageItem())
        imageAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
    }

    private fun onClickBackButton() {
        binding.toolbar.backButton.setOnClickListener {
            NavControllerManager.navigateToBack()
        }
    }

    private fun onClickLinkButton() {
        binding.detailInfoField.linkButton.setOnClickListener {
            val clipData = ClipData.newPlainText(PICLY, args.albumItem.documentId.toPICLY())
            clipboardManager.setPrimaryClip(clipData)
            BlueSnackBar.make(binding.root, getString(R.string.link_copy)).show()
        }
    }

    private fun onClickMoreButton() {
        binding.toolbar.moreButton.setOnClickListener {
            if (bottomSheetFragment != null && bottomSheetFragment!!.isAdded) return@setOnClickListener
            bottomSheetFragment = DetailMenuBottomSheet()
            val bundle = Bundle()
            bundle.putString("documentId", args.albumItem.documentId)
            bottomSheetFragment!!.arguments = bundle
            bottomSheetFragment!!.show(requireActivity().supportFragmentManager, bottomSheetFragment!!.tag)
        }
    }

    private fun onImageClick(view: View, id: String) {
        val extras = FragmentNavigatorExtras(
            view to id
        )
        val item = args.albumItem.toDetailImageItem(id)
        NavControllerManager.navigationDetailToImage(extras, item)
    }

    private fun setDetailTransition(view: View) {
        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }

}