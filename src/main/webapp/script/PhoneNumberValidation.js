document.addEventListener('DOMContentLoaded', (event) => {
    const phoneNumberInput = document.querySelector('input[name="phoneNumber"]');

    phoneNumberInput.addEventListener('change', validatePhoneNumber);

    function validatePhoneNumber() {
        const phoneNumber = phoneNumberInput.value;
        const phoneNumberPattern = /^[0-9]{3}\s[0-9]{3}\s[0-9]{4}$/;

        // Check if the input is valid
        if (phoneNumberPattern.test(phoneNumber)) {
            phoneNumberInput.setCustomValidity('');
        } else {
            phoneNumberInput.setCustomValidity('Inserisci un numero di telefono valido. Formato accettato: 333 456 2211.');
        }

        // Check for exact 10 digits without considering formatting
        const digitsOnly = phoneNumber.replace(/\D/g, '');
        if (digitsOnly.length !== 10) {
            phoneNumberInput.setCustomValidity('Il numero di telefono deve contenere esattamente 10 cifre.');
        }

        // If all validations pass
        phoneNumberInput.reportValidity();
    }
});
