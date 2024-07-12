class CourseSlider {
    constructor(containerSelector, url, itemsPerPage = 5) {
        this.container = document.querySelector(containerSelector);
        this.sliderEl =
            "<a class='course-box' href='course?courseID={0}'> " +
            "<div class='master-course-info'>" +
            "<img src='file?file={2}&id={0}&c=course' alt=''>" +
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
        document.addEventListener('DOMContentLoaded', () => {
            this.loadCourses();

            // Gestione dei pulsanti di scorrimento
            this.container.parentElement.querySelector(".move-slider-left").addEventListener('click', () => {
                if (this.currentIndex > 0) {
                    this.currentIndex--;
                    this.updateSlider();
                }
            });

            this.container.parentElement.querySelector(".move-slider-right").addEventListener('click', () => {
                if (this.currentIndex < this.courseList.length - this.itemsPerPage) {
                    this.currentIndex++;
                    this.updateSlider();
                }
            });

            // Calcola il numero di elementi per pagina all'avvio e al ridimensionamento della finestra
            window.addEventListener('resize', () => {
                this.updateItemsPerPage();
                this.updateSlider();
            });
        });
    }

    // Funzione per caricare i corsi
    loadCourses() {
        fetch(this.url)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Errore nel recupero dei dati: ' + response.statusText);
                }
                return response.json();
            })
            .then(courses => {
                this.courseList = courses; // Sovrascrive la lista di corsi con quella ottenuta dalla risposta
                this.renderCourses(); // Chiama la funzione per renderizzare i corsi
            })
            .catch(error => {
                // Gestione degli errori
                alert('Errore nel recupero dei dati: ' + error.message);
                // Risali a 'your-courses'
                var yourCourses = this.container.closest('.courses-section');
                yourCourses.style.display = "none"

                // Risali a 'section-header'
                var sectionHeader = this.container.closest('.section-header');
                sectionHeader.style.display = "none"
            });
    }

    // Funzione per renderizzare i corsi
    renderCourses() {
        this.container.innerHTML = ''; // Svuota il container prima di aggiungere i nuovi corsi
        this.courseList.forEach((course, index) => {
            let formattedSliderEl = this.formatString(this.sliderEl, [
                course.id,
                course.name,
                course.image,
                course.creationDate,
                course.category,
                course.creator.nome, // Accedi al nome del creator
                course.creator.cognome // Accedi al cognome del creator
            ]);
            this.container.insertAdjacentHTML('beforeend', formattedSliderEl);
        });

        this.updateItemsPerPage();
        this.updateSlider();
        let courseBox = document.querySelectorAll(".course-box");
        courseBox.forEach((box) => {
            box.style.minWidth = "300px";
            box.style.maxWidth = "300px";
        });
        this.updateItemsPerPage();
        this.updateSlider();

        // Aggiungi l'evento error qui, dopo che gli elementi img sono stati aggiunti al DOM
        const images = document.querySelectorAll('img:not(#profile-pic)');
        images.forEach((img) => {
            img.addEventListener('error', function() {
                this.src = 'file?file=default.png&c=course';
                imageNotFound();
            });
        });
    }

    // Funzione per calcolare il numero di elementi per pagina
    updateItemsPerPage() {
        var windowWidth = window.innerWidth;
        var sliderWidth = (windowWidth * 0.9) - 100; // Calcola la larghezza dello slider considerando i margini
        this.itemsPerPage = Math.floor((sliderWidth / 320) - 0.8); // Ogni scheda ha una larghezza di 300px

        if (this.itemsPerPage <= 0) {
            this.itemsPerPage = 1;
        }
    }

    // Funzione per aggiornare la visibilità degli elementi
    updateSlider() {
        var elements = Array.from(this.container.children);
        elements.forEach((el) => el.style.display = 'none'); // Nasconde tutti gli elementi
        for (var i = this.currentIndex; i < this.currentIndex + this.itemsPerPage && i < elements.length; i++) {
            elements[i].style.display = 'block'; // Mostra solo gli elementi nella finestra visibile
        }
    }
}
