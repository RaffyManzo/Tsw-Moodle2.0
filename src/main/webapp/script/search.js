document.addEventListener('DOMContentLoaded', function (){
    const searchBar = document.getElementById('search-bar');
    const searchResults = document.getElementById('search-results');
    const dropdown = document.getElementById('dropdown');

    searchBar.oninput = search;
    document.getElementById('search-button').onclick = search;


    function search() {
        // your logicfunction() {
        const query = searchBar.value;

        if (query.length >= 1) {
            fetch(`search?q=${encodeURIComponent(query)}`)
                .then(response => response.json())
                .then(data => {
                    const utenti = data.utenti.sort((a, b) => a.surname.localeCompare(b.surname));
                    const corsi = data.corsi.sort((a, b) => a.name.localeCompare(b.name));
                    const categorie = data.categorie.sort((a, b) => a.name.localeCompare(b.name));
                    renderDropdown(utenti, corsi, categorie);
                });
        } else {
            const item = document.createElement('li');
            item.classList.add('dropdown-item');
            item.innerHTML =
                        '<span>Nessun risultato per ' + query + '</span>';
            searchResults.appendChild(item);
            dropdown.style.display = 'none';
        }
    }

    function renderDropdown(utenti, corsi, categorie) {
        searchResults.innerHTML = ''
        if (utenti.length > 0 || corsi.length > 0 || categorie.length > 0) {
            if (utenti.length > 0) {
                const utentiHeader = document.createElement('li');
                utentiHeader.classList.add('dropdown-header');
                utentiHeader.textContent = 'Docenti';
                searchResults.appendChild(utentiHeader);
                utenti.forEach(utente => {
                    const item = document.createElement('li');
                    const a = document.createElement("a");
                    item.classList.add('dropdown-item');
                    let initials = '';
                    if (utente.name && utente.surname) {
                        initials = utente.name.charAt(0) + utente.surname.charAt(0);
                    }

                    if (utente.image) {
                        a.innerHTML = `
                    <img src="file?file=${utente.image}&id=${utente.id}&c=user" alt="${utente.name} ${utente.surname}" class="dropdown-img">
                    <span>${utente.name} ${utente.surname}</span>
                `;
                    } else {
                        a.innerHTML = `
                    <div class="initials">${initials.toUpperCase()}</div>
                    <span>${utente.name} ${utente.surname}</span>
                `;
                    }

                    a.href = `profile?id=${utente.id}`;
                    item.appendChild(a)
                    searchResults.appendChild(item);

                });
            }

            if (corsi.length > 0) {
                const corsiHeader = document.createElement('li');
                corsiHeader.classList.add('dropdown-header');
                corsiHeader.textContent = 'Corsi';
                searchResults.appendChild(corsiHeader);
                corsi.forEach(corso => {
                    const item = document.createElement('li');
                    item.classList.add('dropdown-item');
                    const a = document.createElement("a");
                    a.innerHTML = `
                        <img src="file?file=${corso.image}&id=${corso.id}&c=course" alt="${corso.name}" class="dropdown-img">
                        <span>${corso.name} (${corso.category})</span>
                    `;

                    a.href = `
                        course?courseID=${corso.id}
                            `;
                    item.appendChild(a)
                    searchResults.appendChild(item);
                });
            }

            if (categorie.length > 0) {
                const categorieHeader = document.createElement('li');
                categorieHeader.classList.add('dropdown-header');
                categorieHeader.textContent = 'Categorie';
                searchResults.appendChild(categorieHeader);
                categorie.forEach(categoria => {
                    const item = document.createElement('li');
                    item.classList.add('dropdown-item');
                    const a = document.createElement("a");
                    a.innerHTML = `
                        <span>${categoria.name}</span>
                    `;
                    a.href = `
                            `;
                    item.appendChild(a)
                    searchResults.appendChild(item);
                });
            }

            dropdown.style.display = 'block';
        } else {
            dropdown.style.display = 'none';
        }
    }
});
