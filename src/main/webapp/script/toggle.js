document.getElementById('toggle-switch').addEventListener('change', function() {
    var accountTypeInput = document.getElementById('account-type');
    if (this.checked) {
        accountTypeInput.value = 'D';
    } else {
        accountTypeInput.value = 'S';
    }
});

document.getElementById('toggle-form').addEventListener('submit', function(e) {
    e.preventDefault();
    alert("Submitted account type: " + document.getElementById('account-type').value);
});