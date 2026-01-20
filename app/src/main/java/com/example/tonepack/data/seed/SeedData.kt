package com.example.tonepack.data.seed

import com.example.tonepack.data.local.entity.Template
import com.example.tonepack.data.local.entity.User

object SeedData {
    // 유저 ID 직접 생성
    val adminUser = User(
        id = "guru2",
        password = "guru2"
    )

    // authorId를 위 admin 유저와 연결
    // 메일 팀프릿 10개 생성 - 직장 상사
    val templates = listOf(
        Template(
            authorId = adminUser.id,
            title = "[입사인사] 신입사원 OOO입니다. 잘 부탁드립니다.",
            content = "안녕하세요, 오늘 마케팅팀으로 입사하게 된 OOO입니다. 앞으로 많이 배우며 팀에 기여할 수 있도록 노력하겠습니다. 조만간 한 분 한 분 뵙고 인사드리겠습니다!",
            situation = "출근", target = "동료"
        ),
        Template(
            authorId = adminUser.id,
            title = "[보고] 주간 업무 진행 현황 및 일정 공유",
            content = "팀장님, 금주 업무 진행 현황 보고드립니다. 현재 A 프로젝트 기획안 검토 단계이며, 차주 수요일까지 최종안 전달드릴 예정입니다. 상세 내용은 첨부파일 확인 부탁드립니다.",
            situation = "업무", target = "상사"
        ),
        Template(
            authorId = adminUser.id,
            title = "[요청] 신규 프로젝트 관련 회의 일정 조율",
            content = "대리님, 이번 프로젝트 기획 건으로 회의를 진행하고자 합니다. 이번 주 목요일 오후 2시 혹은 금요일 오전 10시 중 가능하신 시간이 있으실까요?",
            situation = "업무", target = "동기"
        ),
        Template(
            authorId = adminUser.id,
            title = "[사과] 공유드린 보고서 내 수치 기입 오류 정정",
            content = "부장님, 금일 오전 공유드린 결산 보고서 3페이지의 매출 수치에 오기입이 발견되어 수정본을 다시 첨부합니다. 혼선을 드려 진심으로 사과드립니다. 다시는 이런 일이 없도록 철저히 확인하겠습니다.",
            situation = "실수보고", target = "상사"
        ),
        Template(
            authorId = adminUser.id,
            title = "[인사] 그동안 감사했습니다. (OOO 드림)",
            content = "안녕하세요, OOO입니다. 저는 오늘부로 퇴사하게 되었습니다. 그동안 함께 일하며 주신 많은 도움과 배려 잊지 않겠습니다. 모두 건강하시고 건승하시길 기원합니다.",
            situation = "퇴사", target = "동료"
        ),
        Template(
            authorId = adminUser.id,
            title = "[협조] 외부 제휴 관련 기초 자료 요청의 건",
            content = "안녕하세요 대리님, 타 팀 프로젝트 협업을 위해 지난달 사용자 분석 데이터 공유가 가능할까요? 기획서 보완을 위해 꼭 필요한 자료라 확인 부탁드립니다.",
            situation = "업무", target = "업체"
        ),
        Template(
            authorId = adminUser.id,
            title = "[피드백] 기획안 리뷰 및 수정 제안",
            content = "OO님, 기획안 작성하느라 고생 많았습니다. 전반적인 흐름은 좋으나 사용자 타겟팅 부분을 조금 더 구체화하면 좋을 것 같아요. 오후에 잠깐 이야기 나눌까요?",
            situation = "업무", target = "후배"
        ),
        Template(
            authorId = adminUser.id,
            title = "[보고] 지하철 지연으로 인한 출근 지연 사유 보고",
            content = "팀장님, 현재 지하철 2호선 장애로 인해 운행이 중단되어 부득이하게 20분 정도 출근이 늦어질 것 같습니다. 최대한 빠르게 도착하여 업무 시작하겠습니다. 죄송합니다.",
            situation = "출근", target = "상사"
        ),
        Template(
            authorId = adminUser.id,
            title = "[완료] 요청하신 시장 조사 자료 정리 완료",
            content = "팀장님, 어제 요청하신 경쟁사 시장 조사 자료 정리가 완료되었습니다. 엑셀 파일 첨부하오니 검토 부탁드립니다. 추가로 궁금하신 점은 말씀해 주세요.",
            situation = "업무", target = "상사"
        ),
        Template(
            authorId = adminUser.id,
            title = "[제안] 팀 런칭 축하 점심 회식 일정 문의",
            content = "대리님, 이번 프로젝트 런칭을 축하하며 팀 점심 회식을 진행하고자 합니다. 혹시 다음 주 화요일 점심에 다 같이 가보고 싶으셨던 식당 어떠신가요?",
            situation = "업무", target = "상사"
        ),

        // 실제 작성했던 메일 탬플릿 10개 - 대학생 버전
        Template(
            authorId = adminUser.id,
            title = "회장 임기 만료 및 감사 인사.",
            content = "안녕하십니까, 000 박사님\n" +
                    "\n" +
                    "0000 소학회장 000입니다.\n" +
                    "\n" +
                    "다름이 아니라, 12월 2일부로 소학회장의 임기가 만료됨을 보고드리며 박사님께 감사 인사를 드리고자 연락드렸습니다.\n" +
                    "지난 3학기 동안 소학회가 원활히 활동할 수 있도록 베풀어 주셔서 진심으로 감사합니다.\n" +
                    "박사님의 도움 덕분에 저희 학우들이 경험을 쌓고 성장할 수 있었습니다.\n" +
                    "\n" +
                    "항상 건강하시고 평안하시기를 기원합니다.\n" +
                    "\n" +
                    "000 드림.",
            situation = "감사, 인사", target = "박사님, 교수님"
        ),
        Template(
            authorId = adminUser.id,
            title = "논문 피드백 요청",
            content = "안녕하십니까, 000교수님.\n" +
                    "(cc. 000, 000)\n" +
                    "\n" +
                    "'캡스톤디자인2(1 분반)' 2팀 000입니다.\n" +
                    "\n" +
                    "지난 24일(금)에 진행된 학술대회를 마무리하고 논문 최종본을 교수님께 전달드리고자 메일을 드리게 되었습니다.\n" +
                    "파일 첨부로 논문 최종본을 보내드렸습니다.\n" +
                    "\n" +
                    "또한, 최종 논문 제출 전 논문 최종 피드백을 받고자 교수님께 면담을 요청드리고 싶습니다.\n" +
                    "\n" +
                    "12월 4일(목) 오후 3시에 면담이 가능하실지 여쭤보고 싶습니다.\n" +
                    "\n" +
                    "바쁘신 와중에 메일 읽어주셔서 감사합니다.\n" +
                    "\n" +
                    "-000 올림-\n",
            situation = "문의", target = "교수님"
        ),
        Template(
            authorId = adminUser.id,
            title = "학생회 홍보 자료 첨부",
            content = "안녕하십니까,\n" +
                    "\n" +
                    "서울여대 00학과 소속 소학회 00000 소학회장 000입니다.\n" +
                    "\n" +
                    "새내기 새로배움터 소학회 홍보 자료 첨부드립니다.\n" +
                    "\n" +
                    "설 연휴 가족들과 행복한 날을 보내시길 바랍니다.\n" +
                    "\n" +
                    "감사합니다.\n" +
                    "\n" +
                    "000 드림.",
            situation = "업무", target = "학생회장"
        ),
        Template(
            authorId = adminUser.id,
            title = "신청서 제출 문의",
            content = "안녕하십니까, 00학과 [학번] 000입니다.\n" +
                    "\n" +
                    "이번 학기에 소학회 신청서를 학과사무실에 제출하고자 이렇게 메일을 넣게되었습니다. 11월 2일(목) 11시에 학과사무실로 찾아뵈어도 괜찮으실까요?\n" +
                    "\n" +
                    "확인해주셔서 감사합니다. 좋은 하루 보내시길 바랍니다.\n" +
                    "\n" +
                    "000 드림",
            situation = "업무", target = "학과"
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        ),
        Template(
            authorId = adminUser.id,
            title = "",
            content = "",
            situation = "", target = ""
        )
    )
}
