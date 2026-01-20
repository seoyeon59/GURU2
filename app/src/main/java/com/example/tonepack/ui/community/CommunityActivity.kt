package com.example.tonepack.ui.community

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tonepack.App
import com.example.tonepack.databinding.ActivityCommunityBinding

class CommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding

    // ViewModel 생성 (Factory를 통해 Repository 전달)
    private val viewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory((application as App).templateRepository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. RecyclerView 설정
        setupRecyclerView()

        // 2. ViewModel 관찰 (데이터가 오면 리스트 갱신)
        viewModel.allTemplates.observe(this) { templateList ->
            templateList?.let {
                binding.rvCommunity.adapter = CommunityAdapter(it)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvCommunity.apply {
            layoutManager = LinearLayoutManager(this@CommunityActivity)
            setHasFixedSize(true) // 성능 최적화
        }
    }
}