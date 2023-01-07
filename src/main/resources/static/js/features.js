document.addEventListener('DOMContentLoaded', function () { // При загрузке документа
    let sel = document.querySelector('.select-filter'); // Получаем селект
    let buttons = document.querySelectorAll('.button-value'); // Получаем кнопки с классом
    buttons.forEach(function (c) { // Для каждой кнопки
        c.onclick = function () { // Слушаем нажатие
            sel.value = c.innerText; // Если нажата, то выбирает тот option, который в тексте кнопки.
        }
    })
})