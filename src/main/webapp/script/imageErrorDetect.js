$(document).ready(() => {
    const images = $('img:not(#profile-pic)');

    images.each(function(k) {

        $(this).onload = function() {
            if ('naturalHeight' in this) {
                if ($(this).naturalHeight + $(this).naturalWidth === 0) {
                    $(this).onerror();
                    return;
                }
            } else if ($(this).width + $(this).height === 0) {
                $(this).onerror();
                return;
            }
        }

        $(this).onerror = function() {$(this).attr('src', 'file?file=default.png&c=course');}

        $(this).src = $(this).attr("src")
    });

    function closeIt()
    {
        $(this).load($(this))
    }
    window.onbeforeunload = closeIt;


})

function imageNotFound() {
    console.log("Image doesn't load, loaded a placeholder");
}
