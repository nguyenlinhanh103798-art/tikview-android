
package com.tikview.android

import android.app.Activity
import android.app.DownloadManager
import android.os.Bundle
import android.os.Environment
import android.net.Uri
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.Gravity
import android.view.MotionEvent
import android.view.GestureDetector
import android.widget.*
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

class MainActivity : Activity() {

    private lateinit var videoView: VideoView
    private lateinit var gestureDetector: GestureDetector

    private var currentIndex = 0
    private var isLiked = false

    private val videos = listOf(
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4",
        "https://storage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK

        val root = FrameLayout(this).apply {
            setBackgroundColor(Color.BLACK)
        }

        videoView = VideoView(this).apply {
            setBackgroundColor(Color.BLACK)
        }

        root.addView(
            videoView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        val header = TextView(this).apply {
            text = "TikView       Đang theo dõi       Dành cho bạn"
            textSize = 17f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
            setPadding(8, 18, 8, 18)
        }

        root.addView(
            header,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.TOP
            )
        )

        val rightMenu = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
        }

        fun addAction(label: String, action: () -> Unit) {
            val button = Button(this).apply {
                text = label
                textSize = 13f
                isAllCaps = false
                setOnClickListener { action() }
            }

            rightMenu.addView(
                button,
                LinearLayout.LayoutParams(
                    105,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            )
        }

        addAction("♡ Thích") {
            isLiked = !isLiked
            Toast.makeText(
                this,
                if (isLiked) "Đã thích video" else "Đã bỏ thích",
                Toast.LENGTH_SHORT
            ).show()
        }

        addAction("Bình luận") {
            Toast.makeText(
                this,
                "Chức năng bình luận sẽ được phát triển tiếp",
                Toast.LENGTH_SHORT
            ).show()
        }

        addAction("Chia sẻ") {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, videos[currentIndex])
            }
            startActivity(Intent.createChooser(shareIntent, "Chia sẻ video"))
        }

        addAction("↓ Tải video") {
            downloadCurrentVideo()
        }

        root.addView(
            rightMenu,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.RIGHT or Gravity.CENTER_VERTICAL
            ).apply {
                rightMargin = 4
            }
        )

        val bottomInfo = TextView(this).apply {
            text = "TikView\nVideo mẫu thử nghiệm\nVuốt lên để xem video tiếp theo"
            textSize = 15f
            setTextColor(Color.WHITE)
            setPadding(16, 16, 16, 30)
        }

        root.addView(
            bottomInfo,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
        )

        gestureDetector = GestureDetector(
            this,
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onDown(e: MotionEvent): Boolean {
                    return true
                }

                override fun onFling(
                    e1: MotionEvent?,
                    e2: MotionEvent,
                    velocityX: Float,
                    velocityY: Float
                ): Boolean {

                    if (e1 == null) return false

                    val diffY = e2.y - e1.y

                    if (diffY < -100 && kotlin.math.abs(velocityY) > 100) {
                        nextVideo()
                        return true
                    }

                    if (diffY > 100 && kotlin.math.abs(velocityY) > 100) {
                        previousVideo()
                        return true
                    }

                    return false
                }
            }
        )

        setContentView(root)

        playCurrentVideo()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun playCurrentVideo() {
        videoView.setVideoURI(Uri.parse(videos[currentIndex]))

        videoView.setOnPreparedListener { player ->
            player.isLooping = true
            videoView.start()
        }

        videoView.setOnErrorListener { _, _, _ ->
            Toast.makeText(
                this,
                "Không thể phát video. Hãy kiểm tra Internet.",
                Toast.LENGTH_LONG
            ).show()
            true
        }
    }

    private fun nextVideo() {
        if (currentIndex < videos.size - 1) {
            currentIndex++
            playCurrentVideo()
        } else {
            Toast.makeText(
                this,
                "Đã hết video mẫu",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun previousVideo() {
        if (currentIndex > 0) {
            currentIndex--
            playCurrentVideo()
        }
    }

    private fun downloadCurrentVideo() {
        try {
            val fileName = "TikView_${System.currentTimeMillis()}.mp4"

            val request = DownloadManager.Request(
                Uri.parse(videos[currentIndex])
            ).apply {
                setTitle("TikView - $fileName")
                setDescription("Đang tải video")
                setMimeType("video/mp4")
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_MOVIES,
                    "TikView/$fileName"
                )
                setAllowedOverMetered(true)
                setAllowedOverRoaming(false)
            }

            val manager = getSystemService(
                Context.DOWNLOAD_SERVICE
            ) as DownloadManager

            manager.enqueue(request)

            Toast.makeText(
                this,
                "Đã bắt đầu tải video. Kiểm tra thông báo tải xuống.",
                Toast.LENGTH_LONG
            ).show()

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Không thể tải video: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onPause() {
        super.onPause()
        videoView.pause()
    }
}
