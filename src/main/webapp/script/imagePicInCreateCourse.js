document.addEventListener("DOMContentLoaded", function () {


    document.getElementById('image-upload').addEventListener('click', function() {
        document.getElementById('immagine').click();
    });

    document.getElementById('immagine').addEventListener('change', function() {
        const file = this.files[0];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(event) {
                document.getElementById('preview').setAttribute('src', event.target.result);
                document.getElementById('preview').style.display = 'block';
                document.querySelector('.image-upload p').style.display = 'none';
            }
            reader.readAsDataURL(file);
        }
    });
});
