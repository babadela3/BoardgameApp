var boardGames = [];
var boardGameId = 0;


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

function addBoardgames(){
    var ids = [];
    var emailUser = document.getElementById("profileEmail").textContent;

    var inputs = document.getElementsByTagName("INPUT");
    for (i = 0; i < inputs.length; i++) {
        if(inputs[i].type == "checkbox"){
            if(inputs[i].checked == true){
                ids.push(inputs[i].id);
            }
        }
    }
    $.ajax({
        type : "POST",
        url : "/addBoardGames",
        data : {
                boardGamesIds: ids,
                email: emailUser
            },
        success : function(response) {
           alert("Success");
        },
        error : function(e) {
           alert('Error');
        }
    });
}

function deleteBoardgames(){
    var ids = [];
    var emailUser = document.getElementById("profileEmail").textContent;

    var inputs = document.getElementsByTagName("INPUT");
    for (i = 0; i < inputs.length; i++) {
        if(inputs[i].type == "checkbox"){
            if(inputs[i].checked == true){
                ids.push(inputs[i].id);
            }
        }
    }
    $.ajax({
        type : "POST",
        url : "/deleteBoardGames",
        data : {
                boardGamesIds: ids,
                email: emailUser
            },
        success : function(response) {
           alert("Success");
           location.reload(false);
        },
        error : function(e) {
           alert('Error');
        }
    });
}

function deletePhotos(){
    var ids = [];
    var emailUser = document.getElementById("profileEmail").textContent;

    var inputs = document.getElementsByTagName("INPUT");
    for (i = 0; i < inputs.length; i++) {
        if(inputs[i].type == "checkbox"){
            if(inputs[i].checked == true){
                ids.push(inputs[i].id);
            }
        }
    }
    $.ajax({
        type : "POST",
        url : "/deletePhotos",
        data : {
                photosIds: ids,
                email: emailUser
            },
        success : function(response) {
           alert("Success");
           location.reload(false);
        },
        error : function(e) {
           alert('Error');
        }
    });
}

function photo(){
    document.getElementById("editTitle").innerHTML = "Add a photo";
    document.getElementById("editInfo").innerHTML = "Share your photos with other people.";

    document.getElementById("editForm").style.display = "none";
    document.getElementById("addPhotoForm").style.display = "block";
    document.getElementById("addBoardGameForm").style.display = "none";
    document.getElementById("deleteBoardGameForm").style.display = "none";
    document.getElementById("deletePhotoForm").style.display = "none";

}

function deletePhoto(){
    document.getElementById("editTitle").innerHTML = "Delete a photo";
    document.getElementById("editInfo").innerHTML = "Delete a photo from your collection.";

    document.getElementById("editForm").style.display = "none";
    document.getElementById("addPhotoForm").style.display = "none";
    document.getElementById("addBoardGameForm").style.display = "none";
    document.getElementById("deleteBoardGameForm").style.display = "none";
    document.getElementById("deletePhotoForm").style.display = "block";

    var email = document.getElementById("profileEmail").textContent;
    $("#tableDeletePhoto").remove();
    $("#tableDeletePhoto_paginate").remove();

    var divTable = document.getElementById("divDeletePhotoForm");
    var table = document.createElement("TABLE");
    table.setAttribute("id","tableDeletePhoto");

    var thead = document.createElement("THEAD");
    thead.setAttribute("id","theadDeletePhoto");

    var tbody = document.createElement("TBODY");
    tbody.setAttribute("id","tbodyDeletePhoto");

    table.appendChild(thead);
    table.appendChild(tbody);
    divTable.appendChild(table);

$.ajax({
         url: "/getPictures?email=" + email,
         type: "GET",
         data: email,
         success: function(data) {

            var table = document.getElementById("theadDeletePhoto");

             var tr = document.createElement("TR");
             var th = document.createElement("TH");
             th.innerHTML = "Picture";
             tr.appendChild(th);
             th.setAttribute("width", "610");
             var th = document.createElement("TH");
             th.innerHTML = "Select";
             tr.appendChild(th);
             table.appendChild(tr);


             $.each(data, function(index, picture) {

                 var table = document.getElementById("tbodyDeletePhoto");
                 var tr = document.createElement("TR");
                 var td = document.createElement("TD");

                 var img = document.createElement("IMG");
                 img.setAttribute("src", "/pubPicture?id=" + picture.id);
                 img.setAttribute("width", "150");
                 img.setAttribute("height","150");
                 td.appendChild(img);

                 tr.appendChild(td);

                 var td = document.createElement("TD");
                 var input = document.createElement("INPUT");
                 input.setAttribute("type", "checkbox");
                 input.setAttribute("id", picture.id);
                 input.addEventListener(
                            'change',
                            function() { verifyCheckboxes(this); },
                            false
                 );
                 td.appendChild(input);
                 tr.appendChild(td);
                 table.appendChild(tr);
                });
                $(document).ready(function() {
                    $('#tableDeletePhoto').DataTable({
                        "bSort": false,
                        "searching": false,
                        "lengthChange": false,
                        "bInfo" : false,
                        "pageLength": 2
                    });
                } );
         },
         error: function(data) {
             //Do Something to handle error
         alert("Fail");
         }
    });

}

function edit(){
    document.getElementById("editTitle").innerHTML = "Edit Profile";
    document.getElementById("editInfo").innerHTML = "Help us to have your updated information.";

    document.getElementById("editForm").style.display = "block";
    document.getElementById("addPhotoForm").style.display = "none";
    document.getElementById("addBoardGameForm").style.display = "none";
    document.getElementById("deleteBoardGameForm").style.display = "none";
    document.getElementById("deletePhotoForm").style.display = "none";
}

function addBoardGameMenu(){
    document.getElementById("editTitle").innerHTML = "Add a board game";
    document.getElementById("editInfo").innerHTML = "Add a new board game to your collection.";

    document.getElementById("editForm").style.display = "none";
    document.getElementById("addPhotoForm").style.display = "none";
    document.getElementById("addBoardGameForm").style.display = "block";
    document.getElementById("deleteBoardGameForm").style.display = "none";
    document.getElementById("deletePhotoForm").style.display = "none";

}

function deleteBoardGameMenu(){
    document.getElementById("editTitle").innerHTML = "Delete a board game";
    document.getElementById("editInfo").innerHTML = "Delete a board game from your collection.";

    document.getElementById("editForm").style.display = "none";
    document.getElementById("addPhotoForm").style.display = "none";
    document.getElementById("deleteBoardGameForm").style.display = "block";
    document.getElementById("deletePhotoForm").style.display = "none";
    document.getElementById("addBoardGameForm").style.display = "none";

    var email = document.getElementById("profileEmail").textContent;
    $("#tableDeleteBoardGame").remove();
    $("#tableDeleteBoardGame_paginate").remove();

    var divTable = document.getElementById("divDeleteBoardGameForm");
    var table = document.createElement("TABLE");
    table.setAttribute("id","tableDeleteBoardGame");

    var thead = document.createElement("THEAD");
    thead.setAttribute("id","theadDeleteBoardGame");

    var tbody = document.createElement("TBODY");
    tbody.setAttribute("id","tbodyDeleteBoardGame");

    table.appendChild(thead);
    table.appendChild(tbody);
    divTable.appendChild(table);

    $.ajax({
         url: "/getBoardGames?email=" + email,
         type: "GET",
         data: email,
         success: function(data) {

            var table = document.getElementById("theadDeleteBoardGame");

             var tr = document.createElement("TR");
             var th = document.createElement("TH");
             th.innerHTML = "Picture";
             tr.appendChild(th);
             var th = document.createElement("TH");
             th.innerHTML = "Name";
             th.setAttribute("width", "520");
             tr.appendChild(th);
             var th = document.createElement("TH");
             th.innerHTML = "Select";
             tr.appendChild(th);
             table.appendChild(tr);



             $.each(data, function(index, game) {

                 var table = document.getElementById("tbodyDeleteBoardGame");
                 var tr = document.createElement("TR");
                 var td = document.createElement("TD");

                 var img = document.createElement("IMG");
                 img.setAttribute("src", game.picture);
                 img.setAttribute("width", "100");
                 img.setAttribute("height", "100");
                 td.appendChild(img);

                 tr.appendChild(td);
                 var td = document.createElement("TD");
                 td.innerHTML = game.name;
                 td.setAttribute("width", "550");
                 tr.appendChild(td);

                 var td = document.createElement("TD");
                 var input = document.createElement("INPUT");
                 input.setAttribute("type", "checkbox");
                 input.setAttribute("id", game.id);
                 input.addEventListener(
                            'change',
                            function() { verifyCheckboxes(this); },
                            false
                 );
                 td.appendChild(input);
                 tr.appendChild(td);
                 table.appendChild(tr);
                });

                $(document).ready(function() {
                    $('#tableDeleteBoardGame').DataTable({
                        "bSort": false,
                        "searching": false,
                        "lengthChange": false,
                        "bInfo" : false,
                        "pageLength": 3
                    });
                } );
         },
         error: function(data) {
             //Do Something to handle error
         alert("Fail");
         }
    });

}


function keyPressListener(e) {
        if (e.keyCode == 13) {
            var search = document.getElementById("searchGameText").value;

            $("#tableAddBoardGame").remove();
            $("#tableAddBoardGame_paginate").remove();

            var divTable = document.getElementById("divTable");
            var table = document.createElement("TABLE");
            table.setAttribute("id","tableAddBoardGame");

            var thead = document.createElement("THEAD");
            thead.setAttribute("id","theadAddBoardGame");

            var tbody = document.createElement("TBODY");
            tbody.setAttribute("id","tbodyAddBoardGame");

            table.appendChild(thead);
            table.appendChild(tbody);
            divTable.appendChild(table);

            $.ajax({
              url: "/getBoardGamesAll?search=" + search,
              type: "GET",
              data: search,
              success: function(data) {
                var table = document.getElementById("theadAddBoardGame");

                var tr = document.createElement("TR");
                var th = document.createElement("TH");
                th.innerHTML = "Picture";
                tr.appendChild(th);
                var th = document.createElement("TH");
                th.innerHTML = "Name";
                th.setAttribute("width", "520");
                tr.appendChild(th);
                var th = document.createElement("TH");
                th.innerHTML = "Select";
                tr.appendChild(th);
                table.appendChild(tr);

                $.each(data, function(index, game) {
                    boardGames.push(game);
                    var table = document.getElementById("tbodyAddBoardGame");
                    var tr = document.createElement("TR");
                    var td = document.createElement("TD");

                    var img = document.createElement("IMG");
                    img.setAttribute("src", game.imageUrl);
                    img.setAttribute("width", "100");
                    img.setAttribute("height", "100");
                    td.appendChild(img);

                    tr.appendChild(td);
                    var td = document.createElement("TD");
                    td.innerHTML = game.name + "(" + game.year.value + ")";
                    td.setAttribute("width", "550");
                    tr.appendChild(td);

                    var td = document.createElement("TD");
                    var input = document.createElement("INPUT");
                    input.setAttribute("type", "checkbox");
                    input.setAttribute("id", game.id);
                    input.addEventListener(
                               'change',
                               function() { verifyCheckboxes(this); },
                               false
                            );
                    td.appendChild(input);
                    tr.appendChild(td);
                    table.appendChild(tr);
                });

                $(document).ready(function() {
                    $('#tableAddBoardGame').DataTable({
                        "bSort": false,
                        "searching": false,
                        "lengthChange": false,
                        "bInfo" : false,
                        "pageLength": 3
                    });
                } );

              },
              error: function(data) {
                //Do Something to handle error
                alert("Fail");
              }
            });
    }
}

function verifyCheckboxes(event){
    if(event.checked){
        if(boardGameId != 0){
            event.checked = false;
            alert("There is already a selected board game!");
        }
        else {
            boardGameId = event.id;
        }
    }
    else{
        boardGameId = 0;
    }
};