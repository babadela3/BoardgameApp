function profile(){

}

function upload(){
    var elements = document.getElementsByClassName("divImg");

    Array.prototype.forEach.call(elements, function(element) {
        element.setAttribute("style", "display: none;");
    });
    document.getElementById("optionCreate").style.fontWeight = "bold";
    document.getElementById("optionPhotos").style.fontWeight = "normal";
}

function store() {
    var elements = document.getElementsByClassName("divImg");

    Array.prototype.forEach.call(elements, function(element) {
        element.setAttribute("style", "display: inline;");
    });
    document.getElementById("optionCreate").style.fontWeight = "normal";
    document.getElementById("optionPhotos").style.fontWeight = "bold";
}
