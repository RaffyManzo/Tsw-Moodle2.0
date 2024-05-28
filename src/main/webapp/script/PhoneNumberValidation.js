document.addEventListener('DOMContentLoaded', (event) => {
    const phoneNumberInput = document.querySelector('input[name="phoneNumber"]');

    phoneNumberInput.addEventListener('change', validatePhoneNumber);

    function validatePhoneNumber() {
        const phoneNumber = phoneNumberInput.value;
        const phoneNumberPattern = /^[\+]?[(]?[0-9]{3}[)]?[-\s\.]?[0-9]{3}[-\s\.]?[0-9]{4,6}$/im;

        // Remove spaces for validation
        const phoneNumberWithoutSpaces = phoneNumber.replace(/\s+/g, '');

        // Check if the input is valid
        if (phoneNumberPattern.test(phoneNumberWithoutSpaces)) {
            phoneNumberInput.setCustomValidity('');
        } else {
            phoneNumberInput.setCustomValidity('Inserisci un numero di telefono valido. Formati accettati: 1234567890, 123-456-7890, 123.456.7890, (123)456-7890.');
        }

        // Check for exact 10 digits without considering formatting
        const digitsOnly = phoneNumberWithoutSpaces.replace(/\D/g, '');
        if (digitsOnly.length !== 10) {
            phoneNumberInput.setCustomValidity('Il numero di telefono deve contenere esattamente 10 cifre.');
        }

        // Ensure no spaces in the phone number
        if (/\s/.test(phoneNumber)) {
            phoneNumberInput.setCustomValidity('Il numero di telefono non deve contenere spazi.');
        }

        // Check for special characters (excluding allowed format characters)
        if (/[^0-9().-]/.test(phoneNumber)) {
            phoneNumberInput.setCustomValidity('Il numero di telefono contiene caratteri non validi.');
        }

        // If all validations pass
        phoneNumberInput.reportValidity();
    }
});