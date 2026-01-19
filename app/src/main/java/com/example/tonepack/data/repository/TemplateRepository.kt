package com.example.tonepack.data.repository

import com.example.tonepack.data.local.dao.TemplateDao
import com.example.tonepack.data.local.entity.Template
import kotlinx.coroutines.flow.Flow

/**
 * TemplateRepository
 *
 * 역할:
 *  - TemplateDao와 UI 레이어 사이의 중간 다리 역할
 *  - DAO의 복잡한 쿼리 로직을 캡슐화하여 ViewModel에 깔끔한 인터페이스 제공
 *  - 필요시 여러 DAO를 조합하거나 데이터 가공 가능
 *
 * 사용하는 곳:
 *  - MainViewModel: 필터링된 템플릿 리스트 조회
 *  - DetailViewModel: 특정 템플릿 상세 조회 + 추천/비추천
 *  - EditorViewModel: 새 템플릿 저장
 *  - CommunityViewModel: 커뮤니티 글 조회
 *
 */
class TemplateRepository(private val templateDao: TemplateDao) {

    /**
     * 1. 필터링된 템플릿 목록 조회
     *
     * 사용 시나리오:
     *  - 상황만 선택: situation="업무", target="%%"
     *  - 상대만 선택: situation="%%", target="상사"
     *  - 둘 다 선택: situation="업무", target="상사"
     *  - 둘 다 미선택: situation="%%", target="%%" (전체 조회)
     *
     * @param situation 상황 필터 (null/빈값이면 전체)
     * @param target 상대 필터 (null/빈값이면 전체)
     * @return Flow로 감싸진 템플릿 리스트 (자동 갱신됨)
     */
    fun getFilteredTemplates(situation: String?, target: String?): Flow<List<Template>> {
        // null이나 빈값을 "%%"로 변환 (SQL LIKE 절에서 전체 매칭)
        val sitQuery = if (situation.isNullOrEmpty()) "%%" else situation
        val targetQuery = if (target.isNullOrEmpty()) "%%" else target

        // DAO의 필터링 쿼리 호출
        return templateDao.getFilteredTemplates(sitQuery, targetQuery)
    }

    /**
     * 2. 전체 템플릿 목록 조회 (필터 없음)
     *
     * 사용 시나리오:
     *  - 커뮤니티 전체 글 보기
     *  - 최신순 정렬된 모든 템플릿
     *
     * @return Flow로 감싸진 전체 템플릿 리스트
     */
    fun getAllTemplates(): Flow<List<Template>> {
        return templateDao.getAllTemplates()
    }

    /**
     * 3. [민경 담당] 특정 ID로 템플릿 하나만 조회
     *
     * 사용 시나리오:
     *  - 리스트에서 아이템 클릭 → 상세 화면 이동
     *  - DetailActivity에서 템플릿 내용 표시
     *
     * @param id 조회할 템플릿의 인덱스 번호 (PK)
     * @return 해당 템플릿 객체 (없으면 null)
     */
    suspend fun getTemplateById(id: Int): Template? {
        return templateDao.getTemplateById(id)
    }

    /**
     * 4. [민경 담당] 새 템플릿 저장
     *
     * 사용 시나리오:
     *  - AddTemplateActivity에서 사용자가 작성한 글 저장
     *  - EditorViewModel이 호출
     *
     * @param template 저장할 Template 객체
     */
    suspend fun insertTemplate(template: Template) {
        // insertAll은 리스트를 받으므로 단일 객체를 리스트로 감싸서 전달
        templateDao.insertAll(listOf(template))
    }

    /**
     * 5. [민경 담당] 추천 수 증가
     *
     * 사용 시나리오:
     *  - 상세 화면에서 추천 버튼 클릭
     *  - DB의 likeCount 컬럼을 +1 증가
     *
     * @param id 추천할 템플릿 ID
     */
    suspend fun updateLike(id: Int) {
        templateDao.updateLikeCount(id)
    }

    /**
     * 6. [민경 담당] 비추천 수 증가
     *
     * 사용 시나리오:
     *  - 상세 화면에서 비추천 버튼 클릭
     *  - DB의 dislikeCount 컬럼을 +1 증가
     *
     * @param id 비추천할 템플릿 ID
     */
    suspend fun updateDislike(id: Int) {
        templateDao.updateDislikeCount(id)
    }

    /**
     * 7. (선택) 템플릿 삭제
     *
     * 사용 시나리오:
     *  - 마이페이지에서 내가 쓴 글 삭제
     *  - 현주님이 사용 예정
     *
     * @param id 삭제할 템플릿 ID
     */
    suspend fun deleteTemplate(id: Int) {
        templateDao.deleteTemplate(id)
    }
}