
let expanded = false;

function toggleDropdown() {
    const checkboxes = document.getElementById("checkboxes");
    checkboxes.style.display = expanded ? "none" : "block";
    expanded = !expanded;
}

function updateSelectedAuthors(checkbox) {
    const checkboxes = document.querySelectorAll("#checkboxes input[type='checkbox']");
    const selected = [];

    checkboxes.forEach(cb => {
        if (cb.checked) {
            const label = cb.nextElementSibling.textContent;
            selected.push(label);
        }
    });

    document.getElementById("selected-authors").textContent = selected.length > 0
        ? selected.join(", ")
        : "Seleziona autori";

    // Chiudi il dropdown dopo una selezione
    document.getElementById("checkboxes").style.display = "none";
    expanded = false;
}

// Chiudi dropdown cliccando fuori
document.addEventListener("click", function (event) {
    const target = event.target;
    const box = document.querySelector(".custom-multiselect");

    if (!box.contains(target)) {
        document.getElementById("checkboxes").style.display = "none";
        expanded = false;
    }
});
