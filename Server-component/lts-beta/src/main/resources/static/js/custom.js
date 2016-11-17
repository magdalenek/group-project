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
      console.log($("#val").text());
      $.ajax({
        url: 'http://localhost:8080/process',
        type: 'POST',
        dataType: "json",
        data: JSON.stringify({
          userId: "someRandomUser",
          facebookToken: "someRandomToken",
          sensitivityLevel: $("#val").text()
        }),
        contentType: "application/json"
      }).complete(function(data, statusText, xhr){
        if(data.status === 200){
        }
      });
    });

  });

});


