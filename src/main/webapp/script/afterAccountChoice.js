function sleep(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds){
            break;
        }
    }
}

// Selezione l'elemento .account-type, ovvero il contenitore generale per effettuare la scelta dell'account
// Creo l'animazione che itera da 0 a 500 un decremento graduale dell'opacitá
// Successivamente lo rendo non visibile ma non lo elimino completamente
$(document).ready(function(){
    var target = $(".account-type")
    $(".choice-box").click(function(){
        target.animate({
            opacity: "-=1"
        }, 600, function() {
            target.css("display", "none")


            sleep(500)
            target.css("z-index", "-10")
        });


        var formField = $(".form-fields")
        formField.removeClass("blur")
        $(".master-form-label").removeClass("blur")

        sleep(200)
        formField.css("z-index", "1")

        $("html,body").css("overflow", "visible")
    });
});


