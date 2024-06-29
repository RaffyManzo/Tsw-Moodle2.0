// Quando la pagina viene caricata
window.onload = function() {
    // Recupera i dati dal Local Storage
    const inputs = document.querySelectorAll('input');
    inputs.forEach(input => {
        if (localStorage.getItem(input.name)) {
            input.value = localStorage.getItem(input.name);
        }
    });
}

// Quando un input cambia
document.querySelectorAll('input').forEach(input => {
    input.addEventListener('change', function() {
        // Salva i dati nel Local Storage
        localStorage.setItem(input.name, input.value);
    });
});
