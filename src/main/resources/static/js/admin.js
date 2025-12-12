// 페이지 로딩 시 실행
document.addEventListener('DOMContentLoaded', () => {
    loadCategories(); // 카테고리 콤보박스 채우기
    loadQuestions();  // 문제 리스트 채우기
});

// 1. 카테고리 불러오기 (Dropdown용)
async function loadCategories() {
    try {
        const response = await fetch('/api/categories'); // 공통 API 활용
        const categories = await response.json();
        
        const select = document.getElementById('inputCategory');
        select.innerHTML = ''; // 초기화
        categories.forEach(cat => {
            select.innerHTML += `<option value="${cat.id}">${cat.title}</option>`;
        });
    } catch (error) {
        console.error('카테고리 로드 실패:', error);
    }
}

// 2. 문제 목록 불러오기 (Read)
async function loadQuestions(page = 0) {
    try {
        // 페이징 파라미터 적용 (page=0, size=10)
        const response = await fetch(`/api/admin/questions?page=${page}&size=10&sort=id,desc`);
        const data = await response.json(); // Page 객체 반환됨
        
        const tbody = document.getElementById('question-table-body');
        tbody.innerHTML = ''; // 기존 내용 비우기

        // data.content가 실제 리스트임 (Spring Page 객체 구조)
        data.content.forEach(q => {
            // 지문이 너무 길면 자르기
            const shortContent = q.content.length > 30 ? q.content.substring(0, 30) + '...' : q.content;
            
            tbody.innerHTML += `
                <tr>
                    <td>${q.id}</td>
                    <td><span class="badge bg-info text-dark">${q.categoryTitle}</span></td>
                    <td>${q.difficulty}</td>
                    <td>${q.type === 'OBJECTIVE' ? '객관식' : '주관식'}</td>
                    <td class="text-start">${shortContent}</td>
                    <td>
                        <button class="btn btn-sm btn-danger" onclick="deleteQuestion(${q.id})">삭제</button>
                    </td>
                </tr>
            `;
        });
    } catch (error) {
        console.error('문제 로드 실패:', error);
    }
}

// 3. 문제 저장하기 (Create)
async function saveQuestion() {
    // 1. 입력값 가져오기
    const categoryId = document.getElementById('inputCategory').value;
    const content = document.getElementById('inputContent').value;
    const explanation = document.getElementById('inputExplanation').value;
    const difficulty = document.getElementById('inputDifficulty').value;
    const type = document.getElementById('inputType').value;

    if (!content) {
        alert("문제 지문을 입력해주세요.");
        return;
    }

    // 2. 데이터 객체 만들기 (DTO 구조와 맞춰야 함!)
    const requestData = {
        categoryId: parseInt(categoryId),
        content: content,
        explanation: explanation,
        difficulty: difficulty,
        type: type,
        choices: []
    };

    // 3. 객관식이라면 보기 데이터 추가
    if (type === 'OBJECTIVE') {
        const choiceInputs = document.querySelectorAll('.choice-input');
        const radios = document.getElementsByName('correctAnswer');
        
        // 4개의 보기를 돌면서 리스트 생성
        choiceInputs.forEach((input, index) => {
            requestData.choices.push({
                content: input.value,
                isAnswer: radios[index].checked // 라디오버튼 체크 여부
            });
        });
    }

    // 4. API 전송 (POST)
    try {
        const response = await fetch('/api/admin/questions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });

        if (response.ok) {
            alert("문제가 등록되었습니다!");
            showList(); // 목록 화면으로 복귀
            loadQuestions(); // 목록 새로고침
            document.getElementById('question-form').reset(); // 폼 초기화
        } else {
            alert("등록 실패: 서버 오류");
        }
    } catch (error) {
        console.error("에러:", error);
    }
}

// 4. 문제 삭제하기 (Delete)
async function deleteQuestion(id) {
    if (!confirm("정말 이 문제를 삭제하시겠습니까?")) return;

    try {
        const response = await fetch(`/api/admin/questions/${id}`, {
            method: 'DELETE'
        });

        if (response.ok) {
            alert("삭제되었습니다.");
            loadQuestions(); // 목록 새로고침
        } else {
            alert("삭제 실패");
        }
    } catch (error) {
        console.error(error);
    }
}

// --- 화면 전환 유틸리티 함수 ---
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
    if (type === 'SUBJECTIVE') {
        choiceArea.style.display = 'none';
    } else {
        choiceArea.style.display = 'block';
    }
}