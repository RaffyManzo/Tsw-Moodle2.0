document.addEventListener("DOMContentLoaded", function (){
    var cardDrop = document.getElementById('card-dropdown');
    var activeDropdown;
    cardDrop.addEventListener('click', function () {
        var node;
        for (var i = 0; i < this.childNodes.length - 1; i++)
            node = this.childNodes[i];
        if (node.className === 'dropdown-select') {
            node.classList.add('visible');
            activeDropdown = node;
        }
        ;
    })

    window.onclick = function (e) {
        console.log(e.target.tagName)
        console.log('dropdown');
        console.log(activeDropdown)
        if (e.target.tagName === 'LI' && activeDropdown) {
            if (e.target.innerHTML === 'Master Card') {
                document.getElementById('credit-card-image').src = 'https://dl.dropboxusercontent.com/s/2vbqk5lcpi7hjoc/MasterCard_Logo.svg.png';
                activeDropdown.classList.remove('visible');
                activeDropdown = null;
                e.target.innerHTML = document.getElementById('current-card').innerHTML;
                document.getElementById('current-card').innerHTML = 'Master Card';
            } else if (e.target.innerHTML === 'American Express') {
                document.getElementById('credit-card-image').src = 'https://dl.dropboxusercontent.com/s/f5hyn6u05ktql8d/amex-icon-6902.png';
                activeDropdown.classList.remove('visible');
                activeDropdown = null;
                e.target.innerHTML = document.getElementById('current-card').innerHTML;
                document.getElementById('current-card').innerHTML = 'American Express';
            } else if (e.target.innerHTML === 'Visa') {
                document.getElementById('credit-card-image').src = 'https://dl.dropboxusercontent.com/s/ubamyu6mzov5c80/visa_logo%20%281%29.png';
                activeDropdown.classList.remove('visible');
                activeDropdown = null;
                e.target.innerHTML = document.getElementById('current-card').innerHTML;
                document.getElementById('current-card').innerHTML = 'Visa';
            }
        } else if (e.target.className !== 'dropdown-btn' && activeDropdown) {
            activeDropdown.classList.remove('visible');
            activeDropdown = null;
        }
    }
})

function validateForm() {
    const cardNumber = document.getElementById('card-number');
    const cardHolder = document.getElementById('card-holder');
    const expiryDate = document.getElementById('expiry-date');
    const cvc = document.getElementById('cvc');

    if (!cardNumber.checkValidity()) {
        alert('Please enter a valid card number (13-16 digits).');
        return false;
    }

    if (!cardHolder.checkValidity()) {
        alert('Please enter the card holder\'s name.');
        return false;
    }

    if (!expiryDate.checkValidity()) {
        alert('Please enter a valid expiry date (MM/YY).');
        return false;
    }

    if (!cvc.checkValidity()) {
        alert('Please enter a valid CVC (3-4 digits).');
        return false;
    }

    alert('Payment submitted successfully!');
    return true;
}
