$(document).ready(() => {
    const images = $('img:not(#profile-pic)');
    images.each(function(k) {
        $(this).on("error", function() {
            const newSrc = `file?file=default.png&c=course&cache_bust=${new Date().getTime()}`;
            $(this).attr('src', newSrc);
            imageNotFound();
        });

        if (this.complete && this.naturalWidth === 0) {
            $(this).trigger('error');
        }
    });
});

function imageNotFound() {
    console.log("Image doesn't load, loaded a placeholder");
}
