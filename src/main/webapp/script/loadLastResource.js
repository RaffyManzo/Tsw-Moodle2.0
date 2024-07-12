document.addEventListener("DOMContentLoaded", function () {
    // Recupera l'ultima richiesta dal localStorage
    const lastRequest = JSON.parse(localStorage.getItem('lastRequest'));

    if (lastRequest) {
        // Rende visibile l'elemento "last-course"
        document.getElementsByClassName('last-course')[0].style.display = 'flex';

        // Crea una nuova richiesta XMLHttpRequest
        var xhr = new XMLHttpRequest();

        var url = new URL(lastRequest.url)



        // Configura la richiesta
        xhr.open('GET', `last-resource?lesson=${lastRequest.lesson}&topic=${lastRequest.topic}&filename=${url.searchParams.get("file").replace(" ", "%20")}`, true);

        // Imposta l'header della richiesta
        xhr.setRequestHeader('Content-Type', 'application/json');

        // Gestisci la risposta
        xhr.onload = function () {
            if (xhr.status === 200) {
                // Parse the JSON response
                var data = JSON.parse(xhr.responseText)[0];

                // Aggiorna l'HTML con i dati ricevuti
                document.querySelector(".course-details").textContent = data.lessonCourse
                document.querySelector('.resource-link[href="#"]').href = data.textResourceUrl;
                document.querySelector('.resource-name').textContent = data.textResourceName;

                document.querySelector('.teacher-info .teacher-name').textContent = data.teacherName;
                document.querySelector('.teacher-info img').src = data.teacherPhotoUrl;

                // Rimuovi le classi skeleton
                document.querySelectorAll('.skeleton').forEach(el => el.classList.remove('skeleton'));
            } else {
                document.getElementsByClassName('last-course')[0].style.display = 'none';
            }
        };

        // Invia la richiesta
        xhr.send();
    } else {
        document.getElementsByClassName('last-course')[0].style.display = 'none';
    }
});
