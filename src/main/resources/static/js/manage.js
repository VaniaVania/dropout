function getCheckedCheckBoxes() {
    var selectedCheckBoxes = document.querySelectorAll('input.btn-check:checked');

    var checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);

    document.getElementById("submitButton").value = checkedValues;
    return checkedValues;
}

function getCheckedCheckBoxesFollow() {
    var selectedCheckBoxes = document.querySelectorAll('input.btn-check:checked');

    var checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);
    document.getElementById("submitButton2").value = checkedValues;
    return checkedValues;
}