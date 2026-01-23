package com.example.tonepack.ui.community

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tonepack.App
import com.example.tonepack.R
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

        // 리사이클러뷰 설정
        binding.rvCommunity.layoutManager = LinearLayoutManager(this)
        binding.rvCommunity.adapter = adapter
        binding.rvCommunity.setHasFixedSize(true)

        // 필터 스피너 설정 함수 호출
        setupFilterSpinners()

        // 관찰자 설정: 데이터 변경 시 어댑터에 전달
        viewModel.allTemplates.observe(this) { list ->
            adapter.submit(list)
        }
    }


    // 상황(Situation) 및 상대(Target) 필터 스피너를 초기화하고 리스너 연결
    private fun setupFilterSpinners() {
        // 1. 상황 스피너 설정 (strings.xml의 situation_categories 사용)
        val situations = resources.getStringArray(R.array.situation_categories)
        val sitAdapter = ArrayAdapter(this, R.layout.spinner_item, situations)
        sitAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerFilterSituation.adapter = sitAdapter

        // 2. 상대 스피너 설정 (strings.xml의 target_categories 사용)
        val targets = resources.getStringArray(R.array.target_categories)
        val tarAdapter = ArrayAdapter(this, R.layout.spinner_item, targets)
        tarAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.spinnerFilterTarget.adapter = tarAdapter

        // 3. 스피너 선택 이벤트 처리
        val filterListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // 현재 선택된 필터 값 가져오기
                val selectedSit = binding.spinnerFilterSituation.selectedItem.toString()
                val selectedTar = binding.spinnerFilterTarget.selectedItem.toString()

                // ViewModel에 필터링된 데이터 요청
                // ViewModel에 이 함수(fetchFilteredTemplates)가 구현되어 있어야 합니다.
                viewModel.fetchFilteredTemplates(selectedSit, selectedTar)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // 아무것도 선택되지 않았을 때의 처리 (보통 비워둠)
            }
        }

        // 리스너 등록
        binding.spinnerFilterSituation.onItemSelectedListener = filterListener
        binding.spinnerFilterTarget.onItemSelectedListener = filterListener
    }
}