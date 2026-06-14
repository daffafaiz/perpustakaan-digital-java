// =========================================================
//  Sistem Perpustakaan Digital - JavaScript
// =========================================================

// --- Tema (Dark / Light) ---
(function () {
    const saved = localStorage.getItem('theme') || 'light';
    document.documentElement.setAttribute('data-theme', saved);
})();

function toggleTheme() {
    const root = document.documentElement;
    const next = root.getAttribute('data-theme') === 'dark' ? 'light' : 'dark';
    root.setAttribute('data-theme', next);
    localStorage.setItem('theme', next);
    updateThemeIcon();
}

function updateThemeIcon() {
    const icon = document.getElementById('themeIcon');
    if (!icon) return;
    const dark = document.documentElement.getAttribute('data-theme') === 'dark';
    icon.className = dark ? 'bi bi-sun' : 'bi bi-moon-stars';
}

// --- Sidebar (mobile) ---
function toggleSidebar() {
    document.querySelector('.sidebar')?.classList.toggle('show');
    document.querySelector('.sidebar-overlay')?.classList.toggle('show');
}

document.addEventListener('DOMContentLoaded', function () {
    updateThemeIcon();

    document.getElementById('themeToggle')?.addEventListener('click', toggleTheme);
    document.getElementById('sidebarToggle')?.addEventListener('click', toggleSidebar);
    document.querySelector('.sidebar-overlay')?.addEventListener('click', toggleSidebar);

    // Auto-hide flash alerts
    document.querySelectorAll('.alert-auto').forEach(function (el) {
        setTimeout(() => {
            el.style.transition = 'opacity .4s';
            el.style.opacity = '0';
            setTimeout(() => el.remove(), 400);
        }, 4000);
    });

    // Konfirmasi aksi hapus / pengembalian
    document.querySelectorAll('form[data-confirm]').forEach(function (form) {
        form.addEventListener('submit', function (e) {
            if (!confirm(form.getAttribute('data-confirm'))) {
                e.preventDefault();
            }
        });
    });
});
