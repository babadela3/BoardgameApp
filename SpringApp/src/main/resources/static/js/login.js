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

                }).fail(function(jqXHR, textStatus) {
                    var data = JSON.parse(jqXHR.responseText);
                    if(data["message"] == "Pub is not registred in the application"){

                    }
                });

}

function resetPassword(){
    window.location.href = "http://localhost:8182/accounts/password/reset";
}

function signUp(){
    document.getElementById("divMenu").style.height = "430px";
    document.getElementById("divButtonSignUp").style.display = "block";
    document.getElementById("divButton").style.display = "none";
    document.getElementById("divForgetPassword").style.display = "none";
    document.getElementById("divEnterAccount").style.display = "flex";
    document.getElementById("divCreateAccount").style.display = "none";
    document.getElementById("loginForm").style.display = "none";
    document.getElementById("signUpForm").style.display = "block";
}

function loginInRedirect(){
    document.getElementById("divMenu").style.height = "400px";
    document.getElementById("divButtonSignUp").style.display = "none";
    document.getElementById("divButton").style.display = "block";
    document.getElementById("divForgetPassword").style.display = "block";
    document.getElementById("divEnterAccount").style.display = "none";
    document.getElementById("divCreateAccount").style.display = "flex";
    document.getElementById("loginForm").style.display = "block";
    document.getElementById("signUpForm").style.display = "none";
}