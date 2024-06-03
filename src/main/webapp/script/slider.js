var courseList = []

// Funzione per formattare una stringa con argomenti
function formatString(str, args) {
    return str.replace(/{(\d+)}/g, function (match, number) {
        return typeof args[number] != "undefined" ? args[number] : match;
    });
}

$(document).ready(function () {
    var container = $(".slider-element-container");
    var sliderEl =
        "<div class='course-box'> " +
        "<div class='master-course-info'>" +
        "<img src='file?file={0}&c={1}' alt=''>" +
        "<label>{2} - {3}</label>" +
        "</div>" +
        "<div class='sub-course-info'>" +
        "<label>{4} - {5}</label>" +
        "</div>" +
        "</div>";

    var courseList = [];
    var currentIndex = 0;
    var itemsPerPage = 5; // Numero di elementi da mostrare per pagina

    console.log(courseList.length)
    // Funzione per caricare i corsi
    function loadCourses() {
        $.ajax({
            url: 'courses', // Endpoint URL
            method: 'GET',
            dataType: 'json',
            success: function (courses) {
                courseList = courses; // Sovrascrive la lista di corsi con quella ottenuta dalla risposta
                renderCourses(); // Chiama la funzione per renderizzare i corsi
            },
            error: function (xhr, status, error) {
                // Gestione degli errori
                alert('Errore nel recupero dei dati: ' + error);
            }
        });
    }

    // Funzione per renderizzare i corsi
    function renderCourses() {
        container.empty(); // Svuota il container prima di aggiungere i nuovi corsi
        for (let i in [0, 1, 2, 3, 4])
        $.each(courseList, function (index, course) {
            let pathImg = "course/" + course.name
            let formattedSliderEl = formatString(sliderEl, [course.image, pathImg, course.name, course.description, course.creationDate, course.category]);
            container.append(formattedSliderEl);
        });

        updateItemsPerPage()
        updateSlider()
        let courseBox = $(".course-box")
        courseBox.css("min-width", "300px)")
        courseBox.css("max-width", "300px)")
        updateItemsPerPage()
        updateSlider();
    }

    // Funzione per calcolare il numero di elementi per pagina
    function updateItemsPerPage() {
        var windowWidth = $(window).width();
        var sliderWidth = (windowWidth * 0.9) - 100; // Calcola la larghezza dello slider considerando i margini
        itemsPerPage = Math.floor((sliderWidth / 320) - 0.8) ; // Ogni scheda ha una larghezza di 300px

        if(itemsPerPage <= 0) {
            itemsPerPage = 1
        }
        console.log("Items per page:", itemsPerPage);
    }

// Funzione per aggiornare la visibilità degli elementi
    function updateSlider() {
        var elements = container.children();
        elements.hide(); // Nasconde tutti gli elementi
        for (var i = currentIndex; i < currentIndex + itemsPerPage && i < elements.length; i++) {
            $(elements[i]).show(); // Mostra solo gli elementi nella finestra visibile
        }

        console.log(currentIndex)
        console.log(itemsPerPage)
        console.log(courseList.length)
    }

    // Gestione dei pulsanti di scorrimento
    $("#move-slider-left").click(function () {
        if (currentIndex > 0) {
            currentIndex--;
            updateSlider();
        }
    });

    $("#move-slider-right").click(function () {
        if (currentIndex < (courseList.length * 5) - itemsPerPage) {
            currentIndex++;
            updateSlider();
        }
    });


    // Calcola il numero di elementi per pagina all'avvio e al ridimensionamento della finestra
    $(window).resize(function() {
        updateItemsPerPage();
        updateSlider();
    });

    loadCourses();
});