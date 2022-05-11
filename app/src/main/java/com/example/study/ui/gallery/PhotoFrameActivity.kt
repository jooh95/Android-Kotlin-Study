package com.example.study.ui.gallery

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.study.databinding.ActivityPhotoFrameBinding
import java.util.*
import kotlin.concurrent.timer

class PhotoFrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPhotoFrameBinding

    private var imageList = mutableListOf<Uri>()
    private var currentPosition = 0
    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPhotoFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUriListSize = intent.getIntExtra("imageUriListSize", 0)

        for (i in 0 until imageUriListSize) {
            intent.getStringExtra("imageUri$i").let {
                imageList.add(Uri.parse(it))
            }
        }
    }

    private fun startPhotoframe() {
        // 5초에 한번씩 실행
        timer = timer(period = 5 * 1000) {
            runOnUiThread {
                val next = if (imageList.size <= currentPosition + 1) 0 else currentPosition + 1

                binding.photoView.setImageURI(imageList[currentPosition])
                binding.photoView.alpha = 0f
                binding.photoView.animate()
                    .alpha(1.0f)
                    .setDuration(1000)
                    .start()

                currentPosition = next
            }
        }
    }

    override fun onStart() {
        super.onStart()

        startPhotoframe()
    }

    override fun onStop() {
        super.onStop()
        timer?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer?.cancel()
    }
}