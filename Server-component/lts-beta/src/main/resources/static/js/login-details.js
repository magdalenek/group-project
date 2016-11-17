$(function () {
    $("#butt").click(function () {
        console.log($("#butt").text());
        $.ajax({
            url: 'http://localhost:8080/process',
            type: 'POST',
            dataType: "json",
            data: JSON.stringify({
                userId: user_id,
                facebookToken: "token"
            }),
            contentType: "application/json"
        }).complete(function(data, statusText, xhr){
            if(data.status === 200){
            }
        });
    });

});
