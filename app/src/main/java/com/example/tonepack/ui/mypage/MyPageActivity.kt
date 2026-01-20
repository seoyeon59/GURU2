package com.example.tonepack.ui.mypage

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tonepack.App
import com.example.tonepack.R
import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.navigation.IntentKeys
import com.example.tonepack.ui.detail.DetailActivity

class MyPageActivity : AppCompatActivity() {

    private val viewModel: MyPageViewModel by viewModels()

    private lateinit var tvNickname: TextView
    private lateinit var tvUserId: TextView
    private lateinit var rvMyPosts: RecyclerView

    private lateinit var myPostAdapter: MyPostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        initViews()
        setupRecyclerView()
        bindUserInfo()
        observeViewModel()
    }

    private fun initViews() {
        tvNickname = findViewById(R.id.tvNickname)
        tvUserId = findViewById(R.id.tvUserId)
        rvMyPosts = findViewById(R.id.rvMyPosts)

        // 설정 아이콘 클릭은 아직 기능 없으니 토스트만
        val btnSettings = findViewById<android.widget.FrameLayout>(R.id.btnSettings)
        btnSettings.setOnClickListener {
            Toast.makeText(this, "설정 기능은 추후 추가 예정입니다.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 상단 프로필 영역에 로그인한 유저 ID 반영
     * (닉네임은 아직 따로 필드가 없으니, 일단 ID 기반으로 표시)
     */
    private fun bindUserInfo() {
        val app = application as App
        val sessionManager = app.sessionManager
        val userId = sessionManager.getUserId()

        if (userId.isNullOrEmpty()) {
            tvNickname.text = "로그인 정보 없음"
            tvUserId.text = ""
        } else {
            // 닉네임이 별도로 없다면, 일단 아이디를 닉네임처럼 보여줘도 됨
            tvNickname.text = userId
            tvUserId.text = "ID: $userId"
        }
    }

    private fun setupRecyclerView() {
        myPostAdapter = MyPostAdapter(
            onItemClick = { template ->
                openDetail(template)
            },
            onDeleteClick = { template ->
                viewModel.deletePost(template)
            }
        )

        rvMyPosts.apply {
            layoutManager = LinearLayoutManager(this@MyPageActivity)
            adapter = myPostAdapter
        }
    }

    private fun observeViewModel() {
        // 내가 쓴 글 리스트
        viewModel.myPosts.observe(this) { list: List<Template> ->
            myPostAdapter.submitList(list)
        }

        // 에러 메시지
        viewModel.errorMessage.observe(this) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    /**
     * 리스트 항목 클릭 시 상세 화면으로 이동
     */
    private fun openDetail(template: Template) {
        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra(IntentKeys.TEMPLATE_ID, template.index)
        }
        startActivity(intent)
    }
}
