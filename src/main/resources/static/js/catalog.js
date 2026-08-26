document.addEventListener("DOMContentLoaded", function () {
    // --- 1. ОРИГІНАЛЬНА ЛОГІКА ЗБЕРЕЖЕННЯ ТА ВІДНОВЛЕННЯ СКРОЛУ ---
    document.querySelectorAll('.coffee-card-link').forEach(link => {
        link.addEventListener('click', function () {
            sessionStorage.setItem('sidebar-scroll', window.scrollY.toString());
        });
    });

    const scrollPos = sessionStorage.getItem('sidebar-scroll');
    if (scrollPos !== null) {
        setTimeout(() => {
            window.scrollTo({
                top: parseInt(scrollPos),
                behavior: 'instant'
            });
            sessionStorage.removeItem('sidebar-scroll');
        }, 50);
    }

    // --- 2. ДИНАМІЧНИЙ ПОШУК ТА ФІЛЬТРАЦІЯ "НА ЛЬОТУ" НА КЛІЄНТІ ---
    const searchInput = document.getElementById("dynamic-search");
    const coffeeCards = Array.from(document.querySelectorAll(".coffee-card-animated"));

    function liveSearchFilter() {
        if (!searchInput) return;
        const query = searchInput.value.trim().toLowerCase();

        coffeeCards.forEach(card => {
            const name = card.getAttribute("data-name") || "";
            const brand = card.getAttribute("data-brand") || "";

            // Якщо назва або бренд містить текст з пошуку — показуємо, інакше приховуємо
            if (name.includes(query) || brand.includes(query)) {
                card.style.display = "";
            } else {
                card.style.display = "none";
            }
        });
    }

    if (searchInput) {
        searchInput.addEventListener("input", liveSearchFilter);
    }

    // --- 3. ОБРОБКА РОБОТИ РЯДКА БІГУЧОГО ПОВІДОМЛЕННЯ (Announcement Bar) ---
    const rawMsgEl = document.getElementById("raw-messages");
    const displayMsgEl = document.getElementById("display-message");
    if (rawMsgEl && displayMsgEl) {
        displayMsgEl.textContent = rawMsgEl.textContent;
    }
});

// --- 4. ОРИГІНАЛЬНІ ГЛОБАЛЬНІ ФУНКЦІЇ ДЛЯ СКРОЛУ БРЕНДІВ ---
function scrollBrands(direction) {
    const container = document.getElementById('brandsScroll');
    if (container) {
        const scrollAmount = 300;
        container.scrollBy({
            left: direction * scrollAmount,
            behavior: 'smooth'
        });
    }
}

// Заглушка для додавання в кошик, щоб уникнути ReferenceError, якщо викликається inline
window.addToCart = window.addToCart || function(button, id) {
    console.log("Додавання товару з ID в кошик:", id);
};

// Заглушка для кроків AI Сомельє
window.showStep = window.showStep || function(step) {
    const content = document.getElementById("sommelier-content");
    if (content) {
        content.innerHTML = `<p class="text-muted">Завантаження кроку сомельє: <b>${step}</b>...</p>`;
    }
};
window.changeStep = window.changeStep || function(offset) {
    console.log("Зміна кроку сомельє на:", offset);
};