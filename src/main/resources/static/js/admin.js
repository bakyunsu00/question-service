/**
 * [admin.js] 관리자 페이지 전용 스크립트
 */

const token = localStorage.getItem('accessToken') || localStorage.getItem('token'); 

document.addEventListener('DOMContentLoaded', () => {
    if (!token) {
        alert("로그인이 필요합니다.");
        window.location.href = '/login.html';
        return;
    }
    loadCategories(); 
    loadQuestions(0); // 초기 로딩 시 0페이지 호출
});

// 1. 카테고리 불러오기
async function loadCategories() {
    try {
        const response = await fetch('/api/categories', {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
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
    } catch (error) { console.error('카테고리 로드 에러:', error); }
}

// 2. 문제 목록 불러오기 (수정됨: renderPagination 호출 추가)
async function loadQuestions(page = 0) {
    try {
        const response = await fetch(`/api/admin/questions?page=${page}&size=10&sort=id,desc`, {
            method: 'GET',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        });

        if (response.status === 401 || response.status === 403) {
            handleAuthError();
            return;
        }

        if (!response.ok) throw new Error('데이터 로드 실패');

        const data = await response.json(); // Spring의 Page 객체
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

        // ✅ 핵심: 하단에 페이징 버튼을 그리는 함수 호출
        renderPagination(data);

    } catch (error) { console.error('문제 목록 로드 에러:', error); }
}

// 3. 페이징 버튼 생성 로직 (추가됨)
function renderPagination(data) {
    const paginationEl = document.getElementById('pagination');
    if (!paginationEl) return;
    
    paginationEl.innerHTML = '';

    const totalPages = data.totalPages; // 전체 페이지 수
    const currentPage = data.number;   // 현재 페이지 (0부터 시작)

    // [이전] 버튼
    const prevDisabled = currentPage === 0 ? 'disabled' : '';
    paginationEl.innerHTML += `
        <li class="page-item ${prevDisabled}">
            <a class="page-link" href="#" onclick="loadQuestions(${currentPage - 1}); return false;">이전</a>
        </li>
    `;

    // [숫자] 버튼
    for (let i = 0; i < totalPages; i++) {
        const activeClass = i === currentPage ? 'active' : '';
        paginationEl.innerHTML += `
            <li class="page-item ${activeClass}">
                <a class="page-link" href="#" onclick="loadQuestions(${i}); return false;">${i + 1}</a>
            </li>
        `;
    }

    // [다음] 버튼
    const nextDisabled = currentPage >= totalPages - 1 ? 'disabled' : '';
    paginationEl.innerHTML += `
        <li class="page-item ${nextDisabled}">
            <a class="page-link" href="#" onclick="loadQuestions(${currentPage + 1}); return false;">다음</a>
        </li>
    `;
}

// 4. 문제 저장하기
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
            loadQuestions(0); // 첫 페이지로 리로드
            document.getElementById('question-form').reset(); 
        } else {
            alert("등록 실패");
        }
    } catch (error) { console.error("저장 중 에러:", error); }
}

// 5. 문제 삭제하기
async function deleteQuestion(id) {
    if (!confirm("정말 삭제하시겠습니까?")) return;
    try {
        const response = await fetch(`/api/admin/questions/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': 'Bearer ' + token }
        });
        if (response.ok) {
            alert("삭제되었습니다.");
            loadQuestions(0); 
        } else {
            alert("삭제 실패 (참조된 데이터가 있을 수 있습니다)");
        }
    } catch (error) { console.error(error); }
}

// 6. 기타 유틸리티 함수
function handleAuthError() {
    alert("세션이 만료되었습니다. 다시 로그인해주세요.");
    localStorage.clear();
    window.location.href = '/login.html';
}

function logout() {
    localStorage.clear();
    alert("로그아웃 되었습니다.");
    window.location.href = '/login.html';
}

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
    document.getElementById('objective-section').style.display = type === 'OBJECTIVE' ? 'block' : 'none';
    document.getElementById('subjective-section').style.display = type === 'SUBJECTIVE' ? 'block' : 'none';
}