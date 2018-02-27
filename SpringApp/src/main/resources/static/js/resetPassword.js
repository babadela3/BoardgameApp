function logIn(){
    window.location.href = "http://localhost:8181";
}

function resetPassword(){
    var email = document.getElementById("resetEmailText").value;
    $.ajax({
                url: "/pub/resetPassword",
                type: "POST",
                data: { email: email }
                }).done(function() {
                    alert("Email sent");
                    document.getElementById("resetEmailText").value = "";
                }).fail(function(jqXHR, textStatus) {
                    var data = JSON.parse(jqXHR.responseText);
                    if(data["message"] == "The email is not registred"){
                        alert(data["message"]);
                        document.getElementById("resetEmailText").value = "";
                    }
                });
}