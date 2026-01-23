package com.example.tonepack.ui.community

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tonepack.App
import com.example.tonepack.databinding.ActivityCommunityBinding

class CommunityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityBinding

    private val viewModel: CommunityViewModel by viewModels {
        CommunityViewModelFactory((application as App).templateRepository)
    }

    private val adapter = CommunityAdapter(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvCommunity.layoutManager = LinearLayoutManager(this)
        binding.rvCommunity.adapter = adapter
        binding.rvCommunity.setHasFixedSize(true)

        viewModel.allTemplates.observe(this) { list ->
            adapter.submit(list) // 여기서 오류 발생하니까 수정하지 말아주세요!(수민)
        }
    }
}
