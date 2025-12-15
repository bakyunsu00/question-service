document.addEventListener("DOMContentLoaded", checkLoginStatus);
function checkLoginStatus() {
    const token = localStorage.getItem("token");

    if (!token) {
        showLoginForm();
        return;
    }

    fetch("/api/user/me", {
        method: "GET",
        headers: {
            "X-AUTH-TOKEN": token
        }
    })
    .then(res => {
        if (!res.ok) {
            throw new Error("토큰 무효");
        }
        return res.json();
    })
    .then(user => {
        showLogoutForm(user.username);
    })
    .catch(() => {
        localStorage.removeItem("token");
        showLoginForm();
    });
}
function showLoginForm() {
    document.getElementById("login-area").style.display = "block";
    document.getElementById("logout-area").style.display = "none";
}
function showLogoutForm(username) {
    document.getElementById("login-area").style.display = "none";
    document.getElementById("logout-area").style.display = "block";
    document.getElementById("login-user").innerText =
        "로그인 사용자: " + username;
}