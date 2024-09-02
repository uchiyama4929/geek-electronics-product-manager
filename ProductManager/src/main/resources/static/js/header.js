const hamburger = document.getElementById('hamburger');
const nav_menu = document.getElementById('nav-menu');

function hamburgerClose() {
    hamburger.classList.toggle('close');
    nav_menu.classList.toggle('close');
}