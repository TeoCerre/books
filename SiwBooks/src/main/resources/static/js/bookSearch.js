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

  // --- WOW: highlight, fade, shortcut, motivazionale ---
  const motivationalMessages = [
    "Un libro è un sogno che tieni tra le mani.",
    "Cerca la tua prossima avventura!",
    "Leggere è viaggiare senza muoversi.",
    "Scopri un nuovo autore oggi!",
    "Ogni libro è una porta su un mondo diverso.",
    "La lettura è cibo per la mente."
  ];
  function showMotivational() {
    const msg = motivationalMessages[Math.floor(Math.random()*motivationalMessages.length)];
    let el = document.getElementById('motivational-message');
    if (!el) {
      el = document.createElement('div');
      el.id = 'motivational-message';
      el.className = 'motivational-message';
      input.parentNode.insertBefore(el, input.nextSibling);
    }
    el.textContent = msg;
  }
  showMotivational();

  // Shortcut: premi / per attivare la barra
  window.addEventListener('keydown', function(e) {
    if (e.key === '/' && document.activeElement !== input && !e.ctrlKey && !e.metaKey) {
      e.preventDefault();
      input.focus();
      input.select();
    }
  });

  function highlight(text, query) {
    if (!query) return text;
    const re = new RegExp('('+query.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')+')', 'gi');
    return text.replace(re, '<span class="highlight">$1</span>');
  }

  function searchBooks(query) {
    fetch(`/books/search?query=${encodeURIComponent(query)}`)
      .then(res => res.json())
      .then(books => {
        if (!books || books.length === 0) {
          resultsDiv.innerHTML = '<div class="no-results">Nessun libro trovato</div>';
          resultsDiv.style.display = 'block';
          return;
        }
        resultsDiv.innerHTML = `<div class="search-results-list">` + books.map(book => `
          <div class="search-result-item" style="animation:fadeInResults .5s cubic-bezier(.4,1.4,.6,1) both;">
            <a href="/books/${book.id}">
              <img src="/books/${book.id}/cover" alt="${book.title}" />
            </a>
            <span><a href="/books/${book.id}">`+highlight(book.title, query)+`</a></span>
          </div>
        `).join('') + `</div>`;
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

  // --- WOW: Consigliati per te (mock random) ---
  const recommendedDiv = document.getElementById('recommended-books');
  if (recommendedDiv) {
    fetch('/books/search?query=')
      .then(res => res.json())
      .then(books => {
        if (!books || books.length < 2) return;
        // Shuffle e prendi 2 random
        for (let i = books.length - 1; i > 0; i--) {
          const j = Math.floor(Math.random() * (i + 1));
          [books[i], books[j]] = [books[j], books[i]];
        }
        const picks = books.slice(0, 2);
        recommendedDiv.innerHTML = picks.map(book => `
          <div class="book" style="display:inline-block;margin:0 18px 0 0;vertical-align:top;">
            <a href="/books/${book.id}">
              <img src="/books/${book.id}/cover" alt="${book.title}" style="width:70px;height:100px;border-radius:8px;box-shadow:0 2px 8px #d4a37333;" />
              <p style="text-align:center;font-weight:600;margin:8px 0 0 0;">${book.title}</p>
            </a>
          </div>
        `).join('');
      });
  }
});
