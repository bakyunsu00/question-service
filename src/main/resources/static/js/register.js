let usernameChecked = false;
let lastCheckedUsername = "";
function onUsernameChange() {
    const current = document.getElementById("username").value;

    if (current !== lastCheckedUsername) {
        usernameChecked = false;
        document.getElementById("username-msg").innerText =
            "아이디 중복 확인이 필요합니다.";
        document.getElementById("username-msg").style.color = "gray";
    }
}
function checkUsername() {
    const username = document.getElementById("username").value;

    if (!username) {
        alert("아이디를 입력하세요");
        return;
    }

    fetch(`/api/auth/check?username=${username}`)
        .then(res => res.json())
        .then(data => {
            if (data.available) {
                usernameChecked = true;
                lastCheckedUsername = username;
                document.getElementById("username-msg").innerText =
                    "사용 가능한 아이디입니다.";
                document.getElementById("username-msg").style.color = "green";
            } else {
                usernameChecked = false;
                lastCheckedUsername = "";
                document.getElementById("username-msg").innerText =
                    "이미 사용 중인 아이디입니다.";
                document.getElementById("username-msg").style.color = "red";
            }
        })
        .catch(() => {
            document.getElementById("username-msg").innerText =
                "중복 확인 중 오류 발생";
            document.getElementById("username-msg").style.color = "red";
        });
}
function register() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
    const nickname = document.getElementById("nickname").value;

    if (!usernameChecked || username !== lastCheckedUsername) {
        alert("아이디 중복 확인을 해주세요");
        return;
    }

    fetch("/api/auth/register", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({
            username,
            password,
            nickname
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("회원가입 실패");
        return res.text();
    })
    .then(msg => {
        alert(msg); // "회원가입 성공"
        location.href = "/login.html";
    })
    .catch(() => {
        document.getElementById("error").innerText =
            "회원가입에 실패했습니다.";
    });
}