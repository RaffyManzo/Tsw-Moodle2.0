let countdown;
let countdownTime = 62; // Time in seconds

function startCountdown() {
    countdown = setInterval(function() {
        countdownTime--;
        const minutes = Math.floor(countdownTime / 60);
        const seconds = countdownTime % 60;
        document.getElementById('countdown').innerHTML = `${minutes} min ${seconds} secs`;

        if (countdownTime <= 0) {
            clearInterval(countdown);
            logOff();
        }
    }, 780000); // TEST default=780000 test=10000
}

function showPopup() {
    document.getElementById('sessionPopup').style.display = 'block';
    startCountdown();
}

function hidePopup() {
    document.getElementById('sessionPopup').style.display = 'none';
}

function renewSession() {
    fetch('sessionController', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=renew'
    })
        .then(response => response.text())
        .then(data => {
            countdownTime = 62; // TEST default=62 test=10
            hidePopup();
        });
}

function logOff() {
    fetch('sessionController', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: 'action=logoff'
    })
        .then(response => response.text())
        .then(data => {
            window.location.href = 'login.html';
        });
}

window.onload = function() {
    // Show popup after 13 minutes of inactivity
    setTimeout(showPopup, 10000); // 13 minutes in milliseconds TEST
};