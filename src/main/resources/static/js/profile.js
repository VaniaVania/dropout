document.addEventListener('DOMContentLoaded', function () {
    let input = document.getElementById('mySelect');

    input.onchange = function () {
    localStorage['mySelect'] = this.value; // change localStorage on change
    this.form.submit()
}
});


