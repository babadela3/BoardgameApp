var account = {
    username : undefined,
    password : undefined
    };
var pub = {
    email : undefined,
    password : undefined,
    name : undefined,
    address: undefined,
    description: undefined,
    picture: undefined
};
var data = {};


function createAccount(){
    pub["email"] = document.getElementById("email").value;
    pub["password"] = document.getElementById("pass").value;
    pub["name"] = document.getElementById("name").value;
    pub["address"] = document.getElementById("address").value;
    pub["description"] = document.getElementById("description").value;
    pub["picture"] = null;

    $.ajax({
                url: "/createPub",
                type: "POST",
                data: JSON.stringify(pub),
                dataType: 'json',
                contentType: "application/json"
                }).done(function(data) {
                    alert("create successfully");
                }).fail(function(jqXHR, textStatus) {
                    var data = JSON.parse(jqXHR.responseText);
                    if(data["message"] == "Pub is not registred in the application"){

                    }
                });

}

function resetPassword(){
    window.location.href = "http://localhost:8181/accounts/password/reset";
}

function signUp(){
    document.getElementById("divAddress").style.display = "block";
    document.getElementById("divName").style.display = "block";
    document.getElementById("divDescription").style.display = "block";
    document.getElementById("divMenu").style.height = "430px";
    document.getElementById("divButtonSignUp").style.display = "block";
    document.getElementById("divButton").style.display = "none";
    document.getElementById("divForgetPassword").style.display = "none";
    document.getElementById("divEnterAccount").style.display = "flex";
    document.getElementById("divCreateAccount").style.display = "none";
}

function loginInRedirect(){
    document.getElementById("divAddress").style.display = "none";
    document.getElementById("divName").style.display = "none";
    document.getElementById("divDescription").style.display = "none";
    document.getElementById("divMenu").style.height = "400px";
    document.getElementById("divButtonSignUp").style.display = "none";
    document.getElementById("divButton").style.display = "block";
    document.getElementById("divForgetPassword").style.display = "block";
    document.getElementById("divEnterAccount").style.display = "none";
    document.getElementById("divCreateAccount").style.display = "flex";
}