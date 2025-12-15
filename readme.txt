-- -----------------------------------------------------------
-- 1. 인증 및 공통 API (Public/Shared)
-- -----------------------------------------------------------
-- 회원가입: POST /api/auth/register
-- 로그인:  POST /api/auth/login
-- 카테고리 목록: GET /api/categories
-- -----------------------------------------------------------
-- 2. 사용자 영역 API (ROLE_USER)
-- -----------------------------------------------------------
-- 시험 생성/시작:   POST /api/user/exams
-- 응시 중 문제 조회:  GET /api/user/exams/{examId}
-- 답안 제출 및 채점:  POST /api/user/exams/{examId}/submit
-- 결과 리포트 조회:  GET /api/user/exams/{examId}/report
-- 응시 기록 목록:   GET /api/user/attempts
-- -----------------------------------------------------------
-- 3. 관리자 영역 API (ROLE_ADMIN)
-- -----------------------------------------------------------
-- 문제 등록:      POST /api/admin/questions
-- 문제 리스트 조회:  GET /api/admin/questions
-- 문제 수정:      PUT /api/admin/questions/{id}
-- 문제 삭제:      DELETE /api/admin/questions/{id}
-- 카테고리 생성:    POST /api/admin/categories
-- 카테고리 수정:    PUT /api/admin/categories/{id}
-- 카테고리 삭제:    DELETE /api/admin/categories/{id}

12/11 예정
로그인/회원가입 기능 디테일 추가
security에 다른 api 설정 추가
jwt 적용