class CourseSlider {
    constructor(containerSelector, url, itemsPerPage = 5) {
        this.container = $(containerSelector);
        this.sliderEl =
            "<a class='course-box' href='course?courseID={0}'> " +
            "<div class='master-course-info'>" +
            "<img src='file?file={0}/{2}&c=course' alt=''>" +
            "<label>{1}</label>" +
            "<label style='font-size: 70%; color: #515151'>Docente: {5} {6}</label>" +
            "</div>" +
            "<div class='sub-course-info'>" +
            "<label>{3} - {4}</label>" +
            "</div>" +
            "</a>";
        this.containerSelector = containerSelector
        this.courseList = [];
        this.currentIndex = 0;
        this.url = url
        this.itemsPerPage = itemsPerPage;

        this.init();
    }

    // Funzione per formattare una stringa con argomenti
    formatString(str, args) {
        return str.replace(/{(\d+)}/g, function (match, number) {
            return typeof args[number] != "undefined" ? args[number] : match;
        });
    }

    init() {
        $(document).ready(() => {
            this.loadCourses();

            // Gestione dei pulsanti di scorrimento
            this.container.parent().find(" .move-slider-left").click(() => {
                //console.log("Pressed left")
                if (this.currentIndex > 0) {
                    this.currentIndex--;
                    this.updateSlider();
                }
            });

            this.container.parent().find(".move-slider-right").click(() => {
                //console.log("Pressed right")
                if (this.currentIndex < this.courseList.length - this.itemsPerPage) {
                    this.currentIndex++;
                    this.updateSlider();
                }
            });

            // Calcola il numero di elementi per pagina all'avvio e al ridimensionamento della finestra
            $(window).resize(() => {
                this.updateItemsPerPage();
                this.updateSlider();
            });
        });
    }

    // Funzione per caricare i corsi
    loadCourses() {


        /*$.ajax({
            url: this.url, // Endpoint URL
            method: 'GET',
            dataType: 'json',
            success: (courses) => {
                this.courseList = courses; // Sovrascrive la lista di corsi con quella ottenuta dalla risposta
                this.renderCourses(); // Chiama la funzione per renderizzare i corsi
                //console.log(this.courseList)
            },
            error: (xhr, status, error) => {
                // Gestione degli errori
                alert('Errore nel recupero dei dati: ' + error);
            }
        });*/

        fetch(this.url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Errore nel recupero dei dati: ' + response.statusText);
                }
                console.log('Consuming response body...');
                return response.json();
            })
            .then(courses => {
                console.log('Courses received:', courses);
                this.courseList = courses; // Sovrascrive la lista di corsi con quella ottenuta dalla risposta
                this.renderCourses(); // Chiama la funzione per renderizzare i corsi
            })
            .catch(error => {
                // Gestione degli errori
                console.error('Errore nel recupero dei dati:', error.message);
                alert('Errore nel recupero dei dati: ' + error.message);
                // Risali a 'your-courses'
                var yourCourses = this.container.closest('.courses-section');
                yourCourses.css("display", "none")

                // Risali a 'section-header'
                var sectionHeader = this.container.closest('.section-header');
                sectionHeader.css("display", "none")
            });
    }

        // Funzione per renderizzare i corsi
    renderCourses() {
        this.container.empty(); // Svuota il container prima di aggiungere i nuovi corsi
        $.each(this.courseList, (index, course) => {
            let formattedSliderEl = this.formatString(this.sliderEl, [
                course.id,
                course.name,
                course.image,
                course.creationDate,
                course.category,
                course.creator.nome, // Accedi al nome del creator
                course.creator.cognome // Accedi al cognome del creator
            ]);
            this.container.append(formattedSliderEl);
        });

        this.updateItemsPerPage();
        this.updateSlider();
        let courseBox = $(".course-box");
        courseBox.css("min-width", "300px");
        courseBox.css("max-width", "300px");
        this.updateItemsPerPage();
        this.updateSlider();

        // Aggiungi l'evento error qui, dopo che gli elementi img sono stati aggiunti al DOM
        const images = $('img:not(#profile-pic)');
        images.each(function(k) {
            $(this).on("error", function() {
                $(this).attr('src', 'file?file=default.png&c=course');
                imageNotFound();
            });
        });
    }

    // Funzione per calcolare il numero di elementi per pagina
    updateItemsPerPage() {
        var windowWidth = $(window).width();
        var sliderWidth = (windowWidth * 0.9) - 100; // Calcola la larghezza dello slider considerando i margini
        this.itemsPerPage = Math.floor((sliderWidth / 320) - 0.8); // Ogni scheda ha una larghezza di 300px

        if (this.itemsPerPage <= 0) {
            this.itemsPerPage = 1;
        }
        //console.log("Items per page:", this.itemsPerPage);
    }

    // Funzione per aggiornare la visibilità degli elementi
    updateSlider() {
        var elements = this.container.children();
        elements.hide(); // Nasconde tutti gli elementi
        for (var i = this.currentIndex; i < this.currentIndex + this.itemsPerPage && i < elements.length; i++) {
            $(elements[i]).show(); // Mostra solo gli elementi nella finestra visibile
        }

        //console.log(this.currentIndex);
        //console.log(this.itemsPerPage);
        //console.log(this.courseList.length);
    }
}


