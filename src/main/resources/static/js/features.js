document.addEventListener('DOMContentLoaded', function () {
    let sel = document.querySelector('.select-filter'); // Get Select
    let buttons = document.querySelectorAll('.button-value'); // Get Buttons with class
    buttons.forEach(function (c) { //
        c.onclick = function () { // Listen click
            sel.value = c.innerText; // If pressed selects option with text inside
        }
    })
})