function onFileSelected(event) {

    var selectedFile = event.target.files[0];
    var reader = new FileReader();

    var imgtag = document.getElementById("myimage");
    imgtag.title = selectedFile.name;

    reader.onload = function(event) {
      imgtag.src = event.target.result;
      imgtag.width="375";
      imgtag.height="300";
    };

    reader.readAsDataURL(selectedFile);
}

function photo(){
    document.getElementById("editTitle").innerHTML = "Add a photo";
    document.getElementById("editInfo").innerHTML = "Share your photos with other people.";

    document.getElementById("editForm").style.display = "none";
    document.getElementById("addPhotoForm").style.display = "block";

}