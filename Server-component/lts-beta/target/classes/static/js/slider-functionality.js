$(function () {

    $("#flat-slider").slider({
        max: 5,
        min: 0,
        orientation: "horizontal",
        change: function (event, ui) {
            $("#val").html(ui.value);
        }
    });

    $(function () {
        $("#butt").click(function () {
            var token = document.cookie;
            console.log("token " + token);
            console.log("Sensitivity level: " + $("#val").text());
        });
    });

});




