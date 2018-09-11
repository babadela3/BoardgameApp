var reservationDTO = {
    id : undefined,
    message : undefined,
    option : undefined
};

function approve(id){
    reservationDTO["id"] = id;
    reservationDTO["message"] = document.getElementById("message" + id).value;
    reservationDTO["option"] = "ACCEPTED";
    $.ajax({
                    url: "/changeReservationStatus",
                    type: "POST",
                    data: JSON.stringify(reservationDTO),
                    dataType: 'json',
                    contentType: "application/json",
                    success : function(data) {
                        document.getElementById("approve" + id).style.display = "none";
                        document.getElementById("reject" + id).style.display = "none";
                        document.getElementById("message" + id).style.disabled = true;
                        alert("The reservation has been approved");
                     },
                    error: function(jqXHR, textStatus) {
                        document.getElementById("approve" + id).style.display = "none";
                        document.getElementById("reject" + id).style.display = "none";
                        document.getElementById("message" + id).style.disabled = true;
                        alert("The reservation has been approved");
                    }
           });

}

function reject(id){
    reservationDTO["id"] = id;
    reservationDTO["message"] = document.getElementById("message" + id).value;
    reservationDTO["option"] = "REJECTED";
    $.ajax({
                    url: "/changeReservationStatus",
                    type: "POST",
                    data: JSON.stringify(reservationDTO),
                    dataType: 'json',
                    contentType: "application/json",
                    success : function(data) {},
                    error: function(jqXHR, textStatus) {
                        document.getElementById("approve" + id).style.display = "none";
                        document.getElementById("reject" + id).style.display = "none";
                        document.getElementById("message" + id).style.disabled = true;
                        alert("The reservation has been rejected");
                    }
           });

}