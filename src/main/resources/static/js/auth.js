function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("로그인 실패");
        }
        return res.json();
    })
    .then(data => {
        localStorage.setItem("token", data.token);
		document.cookie = `token=${data.token}; path=/; Secure; SameSite=Strict`;
        alert("로그인 성공");
        location.href = "/";
    })
    .catch(() => {
        document.getElementById("error").innerText =
            "아이디 또는 비밀번호가 잘못되었습니다.";
    });
}
function logout() {
    localStorage.removeItem("token");
    alert("로그아웃 되었습니다");
    location.href = "/login";
}