function getFollowedCheckBoxes() {
    let container = document.querySelector("#unfollow");
    let selectedCheckBoxes = container.querySelectorAll('input.btn-check:checked');

    let checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);

    document.getElementById("unfollowSubmitButton").value = checkedValues;
    return checkedValues;
}

function getUnfollowedCheckBoxesFollow() {
    let container = document.querySelector("#follow");
    let selectedCheckBoxes = container.querySelectorAll('input.btn-check:checked');

    let checkedValues = Array.from(selectedCheckBoxes).map(cb => cb.value).toLocaleString();

    console.log(checkedValues);
    document.getElementById("followSubmitButton").value = checkedValues;
    return checkedValues;
}