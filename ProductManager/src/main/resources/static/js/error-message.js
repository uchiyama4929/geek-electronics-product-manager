document.addEventListener("DOMContentLoaded", function () {
    const errorMessage = document.getElementById('error-message');
    if (errorMessage) {
        setTimeout(() => {
            errorMessage.classList.add('fade-out');
        }, 1000);
    }
});