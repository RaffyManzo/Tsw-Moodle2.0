$(document).ready(() => {
    const images = $('img:not(#profile-pic)');

    images.each(function(k) {
        $(this).on("error", function() {
            $(this).attr('src', 'file?file=default.png&c=course');
            imageNotFound();
        });
    });
})

function imageNotFound() {
    console.log("Image doesn't load, loaded a placeholder");
}
