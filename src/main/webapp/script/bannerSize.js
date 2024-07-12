document.addEventListener("DOMContentLoaded", function () {
    let banner = document.querySelector(".banner");
    updateBannerSize();

    window.addEventListener("resize", function () {
        updateBannerSize();
    });

    function updateBannerSize() {
        let h = window.innerWidth / 3;
        banner.style.height = h + "px";
    }

    let innerBannerRect = document.getElementById("banner-rect-slider-content");

    const headerText = "I migliori corsi sul mercato";

    const text = [
        "Basi e approfondimenti in numerosi ambiti",
        "Competenze utili nel mondo del lavoro",
        "Docenti accuratamente selezionati per offrire le giuste conoscenze"
    ];

    // Indice per tenere traccia dell'elemento corrente dell'array
    let currentIndex = 0;
    updateInnerHTML();

    // Funzione per aggiornare l'inner HTML dell'elemento target
    function updateInnerHTML() {
        // Modifica l'inner HTML dell'elemento target con la stringa corrente dell'array
        innerBannerRect.innerHTML = "<h5>" + headerText + "</h5><p>" + text[currentIndex] + "</p>";

        // Incrementa l'indice, reimpostandolo a 0 se supera la lunghezza dell'array
        currentIndex = (currentIndex + 1) % text.length;
    }

    // Imposta un intervallo per chiamare la funzione updateInnerHTML ogni 5 secondi (5000 millisecondi)
    setInterval(updateInnerHTML, 5000);
});
