package com.example.tonepack

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1) 첫 카드: 템플릿 작성
        setCard(
            rootId = R.id.cardWrite,
            icon = R.drawable.ic_pencil,
            title = getString(R.string.write_template),
            desc = getString(R.string.write_template_desc)
        )

        // 2) 두번째 카드: 커뮤니티
        setCard(
            rootId = R.id.cardCommunity,
            icon = R.drawable.ic_chat,
            title = getString(R.string.browse_community),
            desc = getString(R.string.browse_community_desc)
        )

        // 3) 세번째 카드: 마이페이지
        setCard(
            rootId = R.id.cardMypage,
            icon = R.drawable.ic_user,
            title = getString(R.string.go_mypage),
            desc = getString(R.string.go_mypage_desc)
        )
    }

    private fun setCard(rootId: Int, icon: Int, title: String, desc: String) {
        val root = findViewById<View>(rootId)
        root.findViewById<ImageView>(R.id.ivIcon).setImageResource(icon)
        root.findViewById<TextView>(R.id.tvTitle).text = title
        root.findViewById<TextView>(R.id.tvDesc).text = desc
    }
}
