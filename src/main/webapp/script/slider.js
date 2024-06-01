var courseList = []

// Funzione per formattare una stringa con argomenti
function formatString(str, args) {
    return str.replace(/{(\d+)}/g, function(match, number) {
        return typeof args[number] != "undefined" ? args[number] : match;
    });
}

$(document).ready(function() {
    var container = $(".slider-element-container");
    var sliderEl =
        "<div class='course-box'> " +
        "<div class='master-course-info'>" +
        "<img src='/file?file={0}&c=course' alt=''>" +
        "<label>{1} - {2}</label>" +
        "</div>" +
        "<div class='sub-course-info'>" +
        "<label>{3} - {4}</label>" +
        "</div>" +
        "</div>";


    // Funzione per caricare i corsi
    function loadCourses() {
        $.ajax({
            url: 'courses', // Endpoint URL
            method: 'GET',
            dataType: 'json',
            success: function(courses) {
                courseList = courses; // Sovrascrive la lista di corsi con quella ottenuta dalla risposta
                renderCourses(); // Chiama la funzione per renderizzare i corsi
            },
            error: function(xhr, status, error) {
                // Gestione degli errori
                alert('Errore nel recupero dei dati: ' + error);
            }
        });
    }

    // Funzione per renderizzare i corsi
    function renderCourses() {
        container.empty(); // Svuota il container prima di aggiungere i nuovi corsi
        $.each(courseList, function(index, course) {
            var formattedSliderEl = formatString(sliderEl, [course.image, course.name, course.description, course.creationDate, course.category]);
            container.append(formattedSliderEl);
        });
    }

    loadCourses(); // Carica i corsi all'avvio del documento
});

console.log(courseList)



