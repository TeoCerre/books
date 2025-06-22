// bookSearch.js

document.addEventListener('DOMContentLoaded', function () {
  const input = document.getElementById('book-search-input');
  const resultsDiv = document.getElementById('search-results');
  let timeout = null;

  if (!input) return;

  input.addEventListener('input', function () {
    clearTimeout(timeout);
    const query = input.value.trim();
    if (query.length === 0) {
      resultsDiv.innerHTML = '';
      resultsDiv.style.display = 'none';
      return;
    }
    timeout = setTimeout(() => searchBooks(query), 200);
  });

  function searchBooks(query) {
    fetch(`/books/search?query=${encodeURIComponent(query)}`)
      .then(res => res.json())
      .then(books => {
        if (!books || books.length === 0) {
          resultsDiv.innerHTML = '<div class="no-results">Nessun libro trovato</div>';
          resultsDiv.style.display = 'block';
          return;
        }
        resultsDiv.innerHTML = books.map(book => `
          <div class="search-result-item">
            <a href="/books/${book.id}">
              <img src="/books/${book.id}/cover" alt="${book.title}" />
              <span>${book.title}</span>
            </a>
          </div>
        `).join('');
        resultsDiv.style.display = 'block';
      })
      .catch(() => {
        resultsDiv.innerHTML = '<div class="no-results">Errore nella ricerca</div>';
        resultsDiv.style.display = 'block';
      });
  }

  // Chiudi risultati se clicchi fuori
  document.addEventListener('click', function (e) {
    if (!resultsDiv.contains(e.target) && e.target !== input) {
      resultsDiv.style.display = 'none';
    }
  });
});
