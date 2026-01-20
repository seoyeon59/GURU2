package com.example.tonepack.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.tonepack.R
import com.example.tonepack.ui.community.CommunityActivity
import com.example.tonepack.ui.editor.AddTemplateActivity // 템플릿 작성 화면
import com.example.tonepack.ui.mypage.MyPageActivity


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 1. 템플릿 작성하기 클릭 시 이동
        findViewById<View>(R.id.actionWrite).setOnClickListener {
            val intent = Intent(this, AddTemplateActivity::class.java)
            startActivity(intent)
        }

        // 2. 커뮤니티 둘러보기 클릭 시 이동
        findViewById<View>(R.id.actionCommunity).setOnClickListener {
             val intent = Intent(this, CommunityActivity::class.java)
             startActivity(intent)
        }

        // 3. 마이페이지 들어가기 클릭 시 이동
        findViewById<View>(R.id.actionMyPage).setOnClickListener {
             val intent = Intent(this, MyPageActivity::class.java)
             startActivity(intent)
        }
    }
}