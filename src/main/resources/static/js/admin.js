/**
 * [admin.js] 관리자 페이지 전용 스크립트
 */

// 1. 토큰 가져오기 (키 이름 확인: accessToken 또는 token)
const token = localStorage.getItem('accessToken') || localStorage.getItem('token'); 

// 2. 페이지 로딩 시 권한 검사 및 초기화
document.addEventListener('DOMContentLoaded', () => {
    // 토큰이 아예 없으면 페이지 로딩 즉시 쫓아냄
    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = '/login.html';
        return;
    }

    // 데이터 불러오기 시작
    loadCategories(); 
    loadQuestions();  
});

// 3. 카테고리 불러오기
async function loadCategories() {
    try {
        const response = await fetch('/api/categories', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token, // 필수 헤더
                'Content-Type': 'application/json'
            }
        }); 

        if (!response.ok) throw new Error('카테고리 로드 실패');

        const categories = await response.json();
        const select = document.getElementById('inputCategory');
        
        select.innerHTML = '<option value="">카테고리 선택</option>'; 
        categories.forEach(cat => {
            select.innerHTML += `<option value="${cat.id}">${cat.title}</option>`;
        });
    } catch (error) {
        console.error('카테고리 로드 에러:', error);
    }
}

// 4. 문제 목록 불러오기
async function loadQuestions(page = 0) {
    try {
        const response = await fetch(`/api/admin/questions?page=${page}&size=10&sort=id,desc`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        });

        // 401(토큰만료) 또는 403(권한없음) 발생 시 처리
        if (response.status === 401 || response.status === 403) {
            handleAuthError();
            return;
        }

        if (!response.ok) throw new Error('데이터 로드 실패');

        const data = await response.json();
        const tbody = document.getElementById('question-table-body');
        tbody.innerHTML = '';

        if (data.content.length === 0) {
            tbody.innerHTML = '<tr><td colspan="6" class="text-center">등록된 문제가 없습니다.</td></tr>';
            return;
        }

        data.content.forEach(q => {
            const shortContent = q.content.length > 40 ? q.content.substring(0, 40) + '...' : q.content;
            tbody.innerHTML += `
                <tr>
                    <td>${q.id}</td>
                    <td><span class="badge bg-secondary">${q.categoryTitle || '-'}</span></td>
                    <td>${q.difficulty}</td>
                    <td>${q.type === 'OBJECTIVE' ? '객관식' : '주관식'}</td>
                    <td class="text-start" title="${q.content}">${shortContent}</td>
                    <td>
                        <button class="btn btn-sm btn-outline-danger" onclick="deleteQuestion(${q.id})">삭제</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error('문제 목록 로드 에러:', error);
    }
}

// 5. 문제 저장하기
async function saveQuestion() {
    const categoryId = document.getElementById('inputCategory').value;
    const content = document.getElementById('inputContent').value;
    const explanation = document.getElementById('inputExplanation').value;
    const difficulty = document.getElementById('inputDifficulty').value;
    const type = document.getElementById('inputType').value;

    if (!categoryId) { alert("카테고리를 선택해주세요."); return; }
    if (!content) { alert("문제 지문을 입력해주세요."); return; }

    const requestData = {
        categoryId: parseInt(categoryId),
        content: content,
        explanation: explanation,
        difficulty: difficulty,
        type: type,
        choices: []
    };

    if (type === 'OBJECTIVE') {
        const choiceInputs = document.querySelectorAll('.choice-input');
        const radios = document.getElementsByName('correctAnswer');
        let hasAnswer = false;
        
        choiceInputs.forEach((input, index) => {
            if (input.value.trim() !== "") {
                const isAns = radios[index].checked;
                if (isAns) hasAnswer = true;
                requestData.choices.push({ content: input.value, isAnswer: isAns });
            }
        });

        if (requestData.choices.length < 2) { alert("객관식은 최소 2개의 보기가 필요합니다."); return; }
        if (!hasAnswer) { alert("정답을 하나 선택해야 합니다."); return; }
    }

    try {
        const response = await fetch('/api/admin/questions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify(requestData)
        });

        if (response.ok) {
            alert("문제가 성공적으로 등록되었습니다!");
            showList(); 
            loadQuestions(); 
            document.getElementById('question-form').reset(); 
            document.getElementById('choice-area').style.display = 'block';
        } else if (response.status === 403 || response.status === 401) {
            handleAuthError();
        } else {
            alert("등록 실패: 입력값을 확인해주세요.");
        }
    } catch (error) {
        console.error("저장 중 에러:", error);
    }
}

// 6. 문제 삭제하기
async function deleteQuestion(id) {
    if (!confirm("정말 삭제하시겠습니까? 복구할 수 없습니다.")) return;

    try {
        const response = await fetch(`/api/admin/questions/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });

        if (response.ok) {
            alert("삭제되었습니다.");
            loadQuestions(); 
        } else if (response.status === 403 || response.status === 401) {
            handleAuthError();
        } else {
            alert("삭제 실패: 서버 오류가 발생했습니다.");
        }
    } catch (error) {
        console.error(error);
    }
}

// 7. 로그아웃
function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('accessToken');
    alert("로그아웃 되었습니다.");
    window.location.href = '/login.html';
}

// --- [중요] 인증 에러 처리 (복구 완료) ---
function handleAuthError() {
    // 디버깅용 로그는 지우고, 실제 차단 로직 활성화
    alert("로그인 세션이 만료되었거나 권한이 없습니다.\n다시 로그인해주세요.");
    localStorage.removeItem('token'); 
    localStorage.removeItem('accessToken');
    window.location.href = '/login.html';
}

// --- 화면 전환 유틸리티 ---
function showCreateForm() {
    document.getElementById('list-section').style.display = 'none';
    document.getElementById('form-section').style.display = 'block';
}

function showList() {
    document.getElementById('list-section').style.display = 'block';
    document.getElementById('form-section').style.display = 'none';
}

function toggleChoices() {
    const type = document.getElementById('inputType').value;
    const choiceArea = document.getElementById('choice-area');
    choiceArea.style.display = (type === 'SUBJECTIVE') ? 'none' : 'block';
}