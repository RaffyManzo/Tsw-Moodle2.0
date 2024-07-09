document.addEventListener('DOMContentLoaded', (event) => {
    const phoneNumberInput = document.querySelector('input[name="phoneNumber"]');
    const emailInput = document.querySelector('input[name="email"]');
    const indirizzoInput = document.querySelector('input[name="address"]');
    const passwordInput = document.querySelector('input[name="password"]');
    const passwordCheckInput = document.querySelector('input[name="password-check"]');
    const dateInput = document.querySelector('input[name="birth-date"]');
    dateInput.addEventListener('change', validateDate);
    passwordCheckInput.addEventListener('change', validatePassword);
    passwordInput.addEventListener('change', validatePassword);
    indirizzoInput.addEventListener('change', validateIndirizzo);
    phoneNumberInput.addEventListener('change', validatePhoneNumber);
    emailInput.addEventListener('change', validateEmail);

    function formatPhoneNumber() {
        const phoneNumberField = document.getElementById('phoneNumber');
        const value = phoneNumberField.value.replace(/\D/g, '').substring(0, 10);
        const formattedValue = value.replace(/(\d{3})(\d{3})(\d{0,4})/, '$1 $2 $3');
        phoneNumberField.value = formattedValue;
    }

    function formatName() {
        const nameField = document.getElementById('name');
        const value = nameField.value.replace(/[^a-zA-Z]/g, '').replace(/[0-9]/g, '');
        const formattedValue = value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
        nameField.value = formattedValue;
    }

    function formatSurname() {
        const nameField = document.getElementById('surname');
        const value = nameField.value = nameField.value.replace(/[^a-zA-Z\s]/g, '');
        const formattedValue = value.charAt(0).toUpperCase() + value.slice(1).toLowerCase();
        nameField.value = formattedValue;
    }

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

    function validateEmail() {
        const email = emailInput.value;
        const emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

        // Check if the input is valid
        if (emailPattern.test(email)) {
            emailInput.setCustomValidity('');
        } else {
            emailInput.setCustomValidity('Inserisci un indirizzo email valido.');
        }

        // If all validations pass
        emailInput.reportValidity();
    }

    function validateDate() {
        const dateInput = document.getElementById('dateInput');
        const dateValue = dateInput.value;
        const inputDate = new Date(dateValue);
        const currentDate = new Date();

        // Check if the input date is before the current date
        if (inputDate < currentDate) {
            dateInput.setCustomValidity('');
        } else {
            dateInput.setCustomValidity('Inserisci una data di nascita valida.');
        }

        // If all validations pass
        dateInput.reportValidity();
    }

    function validateIndirizzo() {
        const indirizzo = indirizzoInput.value;
        const indirizzoPattern = /^(Via|Viale|Piazza|Corso)\s[a-zA-Z\s]*\s\d+$/;

        // Check if the input is valid
        if (indirizzoPattern.test(indirizzo)) {
            indirizzoInput.setCustomValidity('');
        } else {
            indirizzoInput.setCustomValidity('Inserisci un indirizzo valido.');
        }

        // If all validations pass
        indirizzoInput.reportValidity();
    }

    function validatePassword() {
        const password = passwordInput.value;
        const passwordCheck = passwordCheckInput.value;
        const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;

        // Check if the input is valid
        if (passwordPattern.test(password)) {
            passwordInput.setCustomValidity('');
        } else {
            passwordInput.setCustomValidity('Inserisci una password valida.');
        }

        passwordInput.reportValidity();

        if(passwordCheck!=null) {
            if (!password.equals(passwordCheck))
                passwordCheckInput.setCustomValidity('Le password non corrispondono.');
            else
                passwordCheckInput.setCustomValidity('');

            passwordCheckInput.reportValidity();
        }
    }

    function validate(form){
        //ritorna null se i dati sono corretti, una stringa altrimenti
        //nome e cognome
        var letterRGEX = /^[A-Za-z]+$/;
        if(!letterRGEX.test(form["name"].value))
            return "Inserire un nome valido";
        if(!letterRGEX.test(form["surname"].value))
            return "Inserire un cognome valido";

        //indirizzo
        var addressRGEX = /^(Via|Viale|Piazza|Corso)\s[a-zA-Z\s]*\s\d+$/;
        if(!addressRGEX.test(form["address"].value))
            return "Inserire un indirizzo valido";

        //email
        var emailRGEX = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
        if(!emailRGEX.test(form["email"].value))
            return "Inserire un indirizzo email valido";

        //password
        var passwordRGEX = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;
        if (!passwordRGEX.test(form["password"].value)) {
            return "Inserire una password valida";
        }
        if (form["password"].value !== form["password-check"].value) {
            return "Le password non corrispondono";
        }

        //numero di telefono
        var phoneRGEX = /^\d{3}\s\d{3}\s\d{4}$/;
        if(!phoneRGEX.test(form["phoneNumber"].value))
            return "Inserire un numero di telefono valido";

        return null;

    }

    /* function validateForm(event) {
         event.preventDefault();
         const error = validate(event.target);
         if (error) {
             alert(error);
             return false;
         } else {
             event.target.submit(); // Proceed with the form submission
         }
     }

     const form = document.getElementById('reg-form');
     form.addEventListener('submit', validateForm);*/

    const phoneField = document.getElementById('phoneNumber');
    phoneField.addEventListener('input', formatPhoneNumber);

    const nameField = document.getElementById('name');
    nameField.addEventListener('input', formatName);

    const surnameField = document.getElementById('surname');
    surnameField.addEventListener('input', formatSurname);
});
