function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
    .then(res => {
        if (!res.ok) throw new Error("로그인 실패");
        return res.json();
    })
    .then(data => {
        // 1. 토큰 저장
        localStorage.setItem("token", data.token);
        document.cookie = `token=${data.token}; path=/; Secure; SameSite=Strict`;
        
        alert("로그인 성공");

        // 2. [추가] 권한(role)에 따라 목적지 변경
        if (data.role === "ROLE_ADMIN") {
            // 관리자면 관리자 페이지로
            location.href = "/admin"; 
        } else {
            // 일반 유저(ROLE_USER)면 index.html로
            location.href = "/index.html"; 
        }
    })
    .catch(() => {
        document.getElementById("error").innerText = "아이디 또는 비밀번호가 잘못되었습니다.";
    });
}
function logout() {
    localStorage.removeItem("token");
    alert("로그아웃 되었습니다");
    location.href = "/login";
}