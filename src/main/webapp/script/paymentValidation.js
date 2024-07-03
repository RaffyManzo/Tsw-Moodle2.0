document.addEventListener('DOMContentLoaded', function () {
    function formatCardNumber() {
        const cardNumberField = document.getElementById('card-number');
        const value = cardNumberField.value.replace(/\D/g, '').substring(0, 16);
        const formattedValue = value.replace(/(\d{4})(?=\d)/g, '$1 ');
        cardNumberField.value = formattedValue;
    }

    function formatExpiryDate() {
        const expiryDateField = document.getElementById('expiry-date');
        const value = expiryDateField.value.replace(/\D/g, '').substring(0, 4);
        const formattedValue = value.length > 2 ? value.substring(0, 2) + '/' + value.substring(2) : value;
        expiryDateField.value = formattedValue;
    }

    function validate(form) {
        // Card Number Validation
        const cardNumberField = document.getElementById('card-number');
        const cardNumber = cardNumberField.value.replace(/\s/g, '');
        if (!/^\d{16}$/.test(cardNumber)) {
            return "Numero di carta non valido";
        }

        // Card Holder Validation
        const cardHolderField = document.getElementById('card-holder');
        const cardHolder = cardHolderField.value.trim();
        if (!/^[A-Za-z\s]+$/.test(cardHolder)) {
            return "Intestatario non valido";
        }

        // Expiry Date Validation
        const expiryDateField = document.getElementById('expiry-date');
        const expiryDate = expiryDateField.value;
        const expiryDateRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
        const [month, year] = expiryDate.split('/');
        const currentYear = new Date().getFullYear() % 100; // Get last two digits of current year
        if (!expiryDateRegex.test(expiryDate) || parseInt(year) < currentYear) {
            return "Data di scadenza non valida";
        }

        // CVC Validation
        const cvcField = document.getElementById('cvc');
        const cvc = cvcField.value.trim();
        if (!/^\d{3}$/.test(cvc)) {
            return "CVC non valido";
        }

        return null;
    }

    function validateCard(event) {
        event.preventDefault();
        const error = validate(event.target);
        if (error) {
            alert(error);
            return false;
        } else {
            alert('Form inviato con successo');
            return true;
        }
    }

    const form = document.getElementById('credit-info');
    form.addEventListener('submit', validateCard);

    const cardNumberField = document.getElementById('card-number');
    cardNumberField.addEventListener('input', formatCardNumber);

    const expiryDateField = document.getElementById('expiry-date');
    expiryDateField.addEventListener('input', formatExpiryDate);

    const cvcField = document.getElementById('cvc');
    cvcField.setAttribute('maxlength', '3');
    cvcField.addEventListener('input', function() {
        this.value = this.value.replace(/[^0-9]/g, '');
    });
});
