$(document).ready(function() {
    //    $('#search-button').on('click', function() {
    var searchBar = document.getElementById('search-bar');
    searchBar.addEventListener("onkeyup", function() {
        var searchTerm = $('#search-bar').val();
        if (searchTerm.length > 0) {
            var xhr = new XMLHttpRequest();
            var url = 'http://yourServerAddress/yourServletUrl?paramName=' + encodeURIComponent(paramValue);

            xhr.open('GET', url, true);

            xhr.onreadystatechange = function() {
                if (xhr.readyState === XMLHttpRequest.DONE) {
                    if (xhr.status === 200) {
                        var data = JSON.parse(xhr.responseText);
                        showAutocompleteResults(data);
                    } else {
                        console.error('Error:', xhr.statusText);
                    }
                }
            };

            xhr.send();
        }else {
            clearDropdown();
        }
    });

    function showAutocompleteResults(data) {
        const dropdownMenu = document.getElementById('dropdownMenu');
        const dropdownList = dropdownMenu.querySelector('ul');
        dropdownList.innerHTML = ''; // Pulisce il container dei risultati

        data.forEach(item => {
            const li = document.createElement('li');
            li.textContent = item;
            li.addEventListener('click', function() {
                document.getElementById('searchInput').value = item;
                clearDropdown();
            });
            dropdownList.appendChild(li);
        });

        if (data.length > 0) {
            dropdownMenu.style.display = 'flex';
        } else {
            clearDropdown();
        }
    }

    function clearDropdown() {
        const dropdownMenu = document.getElementById('dropdownMenu');
        dropdownMenu.style.display = 'none';
        const dropdownList = dropdownMenu.querySelector('ul');
        dropdownList.innerHTML = ''; // Pulisce il container dei risultati
    }
});
