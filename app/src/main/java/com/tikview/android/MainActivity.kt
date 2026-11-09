
package com.tikview.android

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.net.Uri
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.statusBarColor = Color.BLACK
        window.navigationBarColor = Color.BLACK

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.BLACK)
            setPadding(20, 24, 20, 20)
        }

        val title = TextView(this).apply {
            text = "TikView"
            textSize = 30f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        val subtitle = TextView(this).apply {
            text = "Khám phá video yêu thích"
            textSize = 15f
            setTextColor(Color.LTGRAY)
            gravity = Gravity.CENTER
        }

        val input = EditText(this).apply {
            hint = "Dán đường link TikTok tại đây"
            setHintTextColor(Color.GRAY)
            setTextColor(Color.WHITE)
            setSingleLine(true)
            setPadding(15, 12, 15, 12)
        }

        val openButton = Button(this).apply {
            text = "MỞ VIDEO"
            setOnClickListener {
                val link = input.text.toString().trim()

                if (link.startsWith("https://www.tiktok.com/") ||
                    link.startsWith("https://vm.tiktok.com/") ||
                    link.startsWith("https://vt.tiktok.com/")) {

                    try {
                        startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(link))
                        )
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,
                            "Không thể mở đường link",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Vui lòng nhập link TikTok hợp lệ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        val info = TextView(this).apply {
            text = "\nVideo đã lưu\n\nChưa có video nào được lưu."
            textSize = 17f
            setTextColor(Color.WHITE)
            gravity = Gravity.CENTER
        }

        root.addView(title)
        root.addView(subtitle)

        root.addView(
            input,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 30
            }
        )

        root.addView(openButton)
        root.addView(info)

        setContentView(root)
    }
}
