document.addEventListener('DOMContentLoaded', () => {
    const images = document.querySelectorAll('img:not(#profile-pic)');
    images.forEach((img) => {
        img.onerror = () => {
            const newSrc = `file?file=default.png&c=course&cache_bust=${new Date().getTime()}`;
            img.src = newSrc;
            imageNotFound();
        };

        if (img.complete && img.naturalWidth === 0) {
            img.dispatchEvent(new Event('error'));
        }
    });
});

function imageNotFound() {
    console.log("Image doesn't load, loaded a placeholder");
}
