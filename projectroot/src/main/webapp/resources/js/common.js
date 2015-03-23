// Toggle display of two divs.
function switchDisplay(div1, div2) {
	toggleVisibility(div1);
	toggleVisibility(div2);
}

// Toggle hide/show of object.
function toggleVisibility(obj) {
	obj = $(obj); 
	if(obj.is(":visible")){
		obj.hide();
	} else {
		obj.show();
	}
}

// Sleep for a few seconds.
function sleep(milliseconds) {
  var start = new Date().getTime();
  for (var i = 0; i < 1e7; i++) {
    if ((new Date().getTime() - start) > milliseconds){
      break;
    }
  }
}

// Submit a form.
function submitForm(id) {
	$('#'+id).submit();
}