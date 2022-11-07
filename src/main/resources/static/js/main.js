
let xhttp2 = new XMLHttpRequest();

function myFunction2() {
    xhttp.onreadystatechange = function () {
        if (this.readyState === 4 && this.status === 200) {
            xhttp2.open("POST", "http://localhost:8082/token");
            xhttp2.send();
        }
    }
}
