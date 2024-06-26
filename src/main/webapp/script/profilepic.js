document.addEventListener("DOMContentLoaded", function() {
    const img = document.querySelector('.account-button img');
    const initialsDiv = document.querySelector('.account-button .initials');

    img.addEventListener('error', function() {
        img.style.display = 'none';
        initialsDiv.style.display = 'flex';
    });

    if (img.complete && img.naturalHeight === 0) {
        img.dispatchEvent(new Event('error'));
    }
});


document.addEventListener("DOMContentLoaded", function() {
    const img = document.querySelector('#creator-profile-pic');
    const initialsDiv = document.querySelector('.creator-profile-pic .initials');

    img.addEventListener('error', function() {
        img.style.display = 'none';
        initialsDiv.style.display = 'flex';
    });

    if (img.complete && img.naturalHeight === 0) {
        img.dispatchEvent(new Event('error'));
    }
});