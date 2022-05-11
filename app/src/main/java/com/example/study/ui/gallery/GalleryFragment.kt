package com.example.study.ui.gallery

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.study.databinding.FragmentGalleryBinding

class GalleryFragment : Fragment() {

    private var _binding: FragmentGalleryBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var _requestPermission: ActivityResultLauncher<String>
    private lateinit var _activityResultLauncher: ActivityResultLauncher<Intent>

    private val imageViewList: List<ImageView> by lazy {
        mutableListOf<ImageView>().apply {
            add(binding.imageView11)
            add(binding.imageView12)
            add(binding.imageView13)
            add(binding.imageView21)
            add(binding.imageView22)
            add(binding.imageView23)
        }
    }
    private val imageUriList: MutableList<Uri> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentGalleryBinding.inflate(inflater, container, false)

        val root: View = binding.root

        _requestPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

        _activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == RESULT_OK) {
                    val uri = it.data?.data

                    if (uri != null) {
                        imageUriList.add(uri)
                        if (imageUriList.size <= imageViewList.size) {
                            // 6개까지만 추가 / 6개를 초과할 경우 마지막 사진이 수정됨.
                            imageViewList[imageUriList.size - 1].setImageURI(uri)
                        }
                    }
                }
            }

        initAddImageBtn()
        initStartPhotoFrame()

        return root
    }

    private fun initStartPhotoFrame() {
        binding.startPhotoFrameBtn.setOnClickListener {
            activity?.let {
                if (imageUriList.isNotEmpty()) {
                    val intent = Intent(it, PhotoFrameActivity::class.java)

                    imageUriList.forEachIndexed { index, uri ->
                        intent.putExtra(
                            "imageUri$index",
                            uri.toString()
                        )
                    }
                    intent.putExtra("imageUriListSize", imageUriList.size)

                    it.startActivity(intent)
                }
            }
        }
    }

    private fun initAddImageBtn() {
        binding.addImageBtn.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    navigatePhotos()
                }
                shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                    showPermissionContextPopup()
                }
                else -> {
                    // 외부 공간 접근 권한 요청
                    _requestPermission?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }
            }
        }
    }

    private fun navigatePhotos() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        _activityResultLauncher.launch(intent)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this.context)
            .setTitle("권한요청")
            .setMessage("전자액자 앱에서 사진을 불러오기 위해 권한이 필합니다.")
            .setPositiveButton("동의하기") { _, _ ->
                // 외부 공간 접근 권한 요청
                _requestPermission?.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
            .setNegativeButton("취소하기") { _, _ -> }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}