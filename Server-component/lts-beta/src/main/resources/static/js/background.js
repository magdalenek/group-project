

function openConnection(){

    var token = document.cookie;

    if(token != null){
        $.ajax({
            url: 'http://localhost:8080/process',
            type: 'POST',
            dataType: "json",
            data: JSON.stringify({
                facebookToken: token
            }),
            contentType: "application/json"
        }).complete(function(data, statusText, xhr){
            if(data.status === 200){
                console.log("data received " + JSON.stringify(data, null, 2));
            } else {
                console.log("failed to send to server");
            }
        });

    }

    //chrome.windows.getCurrent(function(w) {
    //    chrome.tabs.getSelected(w.id, function(t) {
    //        var port = chrome.tabs.connect(t.id);
    //        port.postMessage("created");
    //        console.log("interval " + t.id);
    //    })
    //});

}

var interval = setInterval(openConnection, 9000);

