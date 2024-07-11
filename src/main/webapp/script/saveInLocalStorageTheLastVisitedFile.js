// Function to handle the click event on the link
function handleClick(event, el) {
    // Prevent the default behavior
    event.preventDefault();


    var link = el;
    // Get the href attribute of the link
    var href = link.href;

    console.log("lessonid" + link.parentNode.querySelector(".lessonid").textContent)

    // Save the request details in localStorage
    localStorage.setItem('lastRequest', JSON.stringify({
        url: href,
        lesson: link.parentNode.querySelector(".lessonid").textContent,
        topic: link.parentNode.querySelector(".topicid").textContent
    }));

    // Continue to the target of the href
    console.log(href);
    window.open(href, '_blank');
}

// Attach the event listener to the link on page load
document.addEventListener("DOMContentLoaded", function () {

    document.querySelectorAll(".resource").forEach(link => {
        link.addEventListener('click', function (ev) {
            handleClick(ev, this)
        });
    })
});