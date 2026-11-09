
package com.tikview.android

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.*
import android.content.Intent
import android.net.Uri

class MainActivity : Activity() {

    private var currentIndex = 0

    private val videos = listOf(
        "Khám phá video cùng TikView",
        "Video yêu thích của bạn",
        "Khám phá nội dung mới"
    )

    private lateinit var videoTitle: TextView
    private lateinit var counter: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
        }

        val header = TextView(this).apply {
            text = "TikView                         LIVE"
            textSize = 22f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
            setPadding(12, 18, 12, 18)
        }

        val videoArea = FrameLayout(this).apply {
            setBackgroundColor(Color.rgb(28, 28, 28))
        }

        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        videoTitle = TextView(this).apply {
            text = videos[0]
            textSize = 23f
            gravity = Gravity.CENTER
            setTextColor(Color.WHITE)
        }

        counter = TextView(this).apply {
            text = "Video 1 / ${videos.size}"
            textSize = 14f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        content.addView(videoTitle)
        content.addView(counter)

        videoArea.addView(
            content,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        val controls = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 10, 12, 10)
        }

        val nextButton = Button(this).apply {
            text = "VIDEO TIẾP THEO ↓"
            setOnClickListener {
                currentIndex = (currentIndex + 1) % videos.size
                videoTitle.text = videos[currentIndex]
                counter.text = "Video ${currentIndex + 1} / ${videos.size}"
            }
        }

        val shareButton = Button(this).apply {
            text = "CHIA SẺ VIDEO"
            setOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    "Chức năng chia sẻ sẽ được tích hợp khi có video thật",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val downloadButton = Button(this).apply {
            text = "TẢI VIDEO"
            setOnClickListener {
                Toast.makeText(
                    this@MainActivity,
                    "Chưa có nguồn video để tải. Chức năng tải sẽ được tích hợp ở bước tiếp theo.",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val bottom = TextView(this).apply {
            text = "Trang chủ          Khám phá          Hồ sơ"
            gravity = Gravity.CENTER
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(10, 18, 10, 18)
        }

        controls.addView(nextButton)
        controls.addView(shareButton)
        controls.addView(downloadButton)

        root.addView(header)

        root.addView(
            videoArea,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        )

        root.addView(controls)
        root.addView(bottom)

        setContentView(root)
    }
}
