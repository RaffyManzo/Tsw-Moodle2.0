document.addEventListener("DOMContentLoaded", function () {
    let form = document.getElementById('uploadForm');

    // Handle file selection
    document.getElementById("profilePicture").onchange = function () {
        form.submit();
    };

    // Handle delete action
    document.getElementById("delete-button").onclick = function () {
        document.getElementById("deletePicture").value = "TRUE";
        form.submit();
    };
})