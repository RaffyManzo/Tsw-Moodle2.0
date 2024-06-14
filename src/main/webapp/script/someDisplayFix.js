document.addEventListener("DOMContentLoaded" , function() {
    // Seleziona l'elemento desiderato
    var slider = document.getElementById('course-section');

// Crea un'istanza di ResizeObserver
    var resizeObserver = new ResizeObserver(entries => {
        for (let entry of entries) {
            if (entry.target === slider) {
                document.getElementById("categories-container").style.width = '' +
                    entry.contentRect.width

            }
        }
    });

// Funzione per iniziare ad osservare gli elementi
    function startObserving() {
        resizeObserver.observe(slider)
    }


    // Inizia ad osservare gli elementi
    startObserving();

})


