package com.easyhz.picly.view.album.detail


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.easyhz.picly.R
import com.easyhz.picly.databinding.FragmentAlbumDetailBinding
import com.easyhz.picly.util.BlueSnackBar
import com.easyhz.picly.util.PICLY
import com.easyhz.picly.util.toPICLY
import com.easyhz.picly.view.album.detail.bottom_sheet.DetailMenuBottomSheet
import com.easyhz.picly.view.navigation.NavControllerManager


class AlbumDetailFragment : Fragment() {
    private lateinit var binding: FragmentAlbumDetailBinding
    private lateinit var clipboardManager: ClipboardManager
    private val tagAdapter = DetailTagAdapter()
    private val imageAdapter = DetailImageAdapter()
    private val args: AlbumDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAlbumDetailBinding.inflate(layoutInflater)
        clipboardManager = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp()
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
        }
        imageAdapter.setList(args.albumItem.imageUrls)
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
            val bottomSheetFragment = DetailMenuBottomSheet.getInstance()
            val bundle = Bundle()
            bundle.putString("documentId", args.albumItem.documentId)
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(requireActivity().supportFragmentManager, bottomSheetFragment.tag)
        }
    }

}