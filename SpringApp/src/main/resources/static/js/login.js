var account = {
    username : undefined,
    password : undefined
    };
var data = {}


function login() {
    account["username"] = document.getElementById("email").value;
    account["password"] = document.getElementById("pass").value;

    $.ajax({
            url: "/getPub",
            type: "POST",
            data: JSON.stringify(account),
            dataType: 'json',
            contentType: "application/json"
            }).done(function(data) {
                alert("login successfully");
            }).fail(function(jqXHR, textStatus) {
                var data = JSON.parse(jqXHR.responseText);
                if(data["message"] == "Pub is not registred in the application"){
                    document.getElementById("divErrorLogin").style.visibility = "visible";
                    document.getElementById("divErrorLogin").style.marginTop = "30px";
                    document.getElementById("divForgetPassword").style.marginTop = "35px";
                    document.getElementById("divButton").style.marginTop = "25px";
                }
            });

}

function resetPassword(){
    window.location.href = "http://localhost:8081/accounts/password/reset";
}