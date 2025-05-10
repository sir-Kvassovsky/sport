(function () {
    const mode = localStorage.getItem('theme') || 'light';
    document.body.classList.add(`${mode}-mode`);
})();

function toggleDarkMode() {
    const isDark = document.body.classList.contains('dark-mode');
    document.body.classList.toggle('dark-mode', !isDark);
    document.body.classList.toggle('light-mode', isDark);
    localStorage.setItem('theme', isDark ? 'light' : 'dark');
}