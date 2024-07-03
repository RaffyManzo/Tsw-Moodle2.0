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
                    const utenti = data.utenti.map(utente => {
                        utente.corsi = utente.corsi || 0;
                        return utente;
                    }).sort((a, b) => b.corsi - a.corsi);
                    const corsi = data.corsi.map(corso => {
                        corso.numberPurchases = corso.numberPurchases || 0;
                        return corso;
                    }).sort((a, b) => b.numberPurchases - a.numberPurchases);
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
                    <div class="search-result-element-info">
                    <img src="file?file=${utente.image}&id=${utente.id}&c=user" alt="${utente.name} ${utente.surname}" class="dropdown-img">
                    <strong  class="search-element-name">${utente.name} ${utente.surname}</strong>
                    </div>
                    <div class="search-result-element-details">
                    <span class="sub-search-element">(${utente.corsi} corsi creati)</span>
                    </div>
                `;
                    } else {
                        a.innerHTML = `
                    <div class="search-result-element-info">

                    <div class="initials">${initials.toUpperCase()}</div>
                    <strong  class="search-element-name">${utente.name} ${utente.surname}</strong>
                    </div>
                    <div class="search-result-element-details">
                    <span class="sub-search-element">(${utente.corsi} corsi creati)</span>
                    </div>
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
                        <div class="search-result-element-info">
                        <img src="file?file=${corso.image}&id=${corso.id}&c=course" alt="${corso.name}" class="dropdown-img">
                        <strong class="search-element-name">${corso.name} (${corso.category})</strong>
                        </div>
                        <div class="search-result-element-details">
                        <span class="sub-search-element">(Acquisti: ${corso.numberPurchases})</span>
                        <span class="sub-search-element search-bar-price">Prezzo: ${corso.price}$</span>
                        </div>
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
