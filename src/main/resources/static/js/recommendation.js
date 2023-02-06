function getArtistChoice() {
    var container = document.querySelector("#artists");
    let selectedCheckBoxes = container.querySelectorAll('input.btn-check:checked');

    let checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);

    document.getElementById("artistsChoice").value = checkedValues;
    return checkedValues;
}

function getSongsChoice() {
    const container = document.querySelector("#tracks");
    let selectedCheckBoxes = container.querySelectorAll('input.btn-check:checked');

    let checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);

    document.getElementById("songsChoice").value = checkedValues;
    return checkedValues;
}

function getGenresChoice() {
    const container = document.querySelector("#genres");
    let selectedCheckBoxes = container.querySelectorAll('input.btn-check:checked');

    let checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);

    document.getElementById("genresChoice").value = checkedValues;
    return checkedValues;
}

function getChoice() {
    getArtistChoice();
    getSongsChoice();
    getGenresChoice();
}

function myFunction() {
    document.getElementById("demo").innerHTML = "<div class=\"spinner-grow text-dark\" role=\"status\">\n" +
        "                                        <span class=\"visually-hidden\">Loading...</span>\n" +
        "                                    </div>";
}

function refreshPage(){
    window.location.reload();
}