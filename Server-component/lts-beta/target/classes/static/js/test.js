// Unique ID for the className.
var MOUSE_VISITED_CLASSNAME = 'crx_mouse_visited';

// Previous dom, that we want to track, so we can remove the previous styling.
var prevDOM = null;
var last_moved=0;

// Mouse listener for any move event on the current document.
document.addEventListener('mousemove', function (e) {
$('rc.u_0_v').on('hover', function() {
 var x = e.clientX;
  var y = e.clientY;
  var coor = "My 1st Coordinates: (" + x + "," + y + ")";
  console.log(coor);


});

  var srcElement = e.srcElement;
  var x = e.clientX;
  var y = e.clientY;
  var coor = "My 1st Coordinates: (" + x + "," + y + ")";
  console.log(coor);
// Lets check if our underlying element is a DIV.
  if (srcElement.nodeName == 'DIV') {
    // For NPE checking, we check safely. We need to remove the class name
    // Since we will be styling the new one after.
    if (prevDOM != null) {
      prevDOM.classList.remove(MOUSE_VISITED_CLASSNAME);
    }

    // Add a visited class name to the element. So we can style it.
    srcElement.classList.add(MOUSE_VISITED_CLASSNAME);

    // The current element is now the previous. So we can remove the class
    // during the next iteration.
    prevDOM = srcElement;

    var now = event.timeStamp;
    if (now - last_moved > 1000) {
        console.log('mouse moved<br/>');
		console.log(coor);
        last_moved = now;
    }

  }
}, false);

