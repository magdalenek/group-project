window.fbAsyncInit = function() {
    FB.init({
        appId      : '594495640707483',
        status     : true,
        xfbml      : true,
        oauth      : true,
        cookie     : true,
        channelUrl : 'https://testprivacy.firebaseapp.com/facebooklogin.html',
        version    : 'v2.5'
    });

    FB.getLoginStatus(function (response) {
        if (response.status === 'connected') {
            var accessToken = response.authResponse.accessToken;
            var user_id = response.authResponse.userID;
            console.log("Response goes here!");
        } else if (response.status === 'not_authorized') {
            console.log("We are not Login");
        } else {
            console.log("You are not Login in facebook.");
        }
    });

};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));



$(function () {
    $("#fb-butt").click(function () {
        if (document.getElementById('terms').checked) {
            FB.login(function(response) {
                user_id = response.authResponse.userID;
                accessToken = response.authResponse.accessToken;
                console.log("the token is " + accessToken);
                document.cookie  = accessToken;

                $.ajax({
                    url: 'http://localhost:8080/track',
                    type: 'POST',
                    dataType: "json",
                    data: JSON.stringify({
                        userId: user_id,
                        facebookToken: accessToken
                    }),
                    contentType: "application/json"
                }).complete(function(data, statusText, xhr){
                    if(data.status === 200){
                        console.log("sent to server");
                    } else {
                        console.log("failed to send to server");
                    }
                });

                console.log("sent ajax call");

                if (response.status === 'connected') {
                    FB.api('/'+user_id+'/?fields=email&accesstoken='+accessToken, function(response){
                        //window.location ="logout.html";
                    });
                }

            }, { perms: 'user_birthday,email'});


        } else {
            window.alert("You have to accept the terms and conditions first!");}
    });
});


