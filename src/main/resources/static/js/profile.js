document.addEventListener('DOMContentLoaded', function () {
    var input = document.getElementById('mySelect');
    if (localStorage['mySelect']) { // if job is set
    input.value = localStorage['mySelect']; // set the value
}
    input.onchange = function () {
    localStorage['mySelect'] = this.value; // change localStorage on change
    this.form.submit()
}
});


