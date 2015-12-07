function checkAllDatatable(myDataTable, checkboxClass) {
	$(myDataTable.fnGetNodes()).find('.'+checkboxClass).prop('checked', true);
	return false;
}

function uncheckAllDatatable(myDataTable, checkboxClass) {
	$(myDataTable.fnGetNodes()).find('.'+checkboxClass).prop('checked', false);
	return false;
}

function checkAll(checkboxClass) {
	$('.'+checkboxClass).each(function() { //loop through each checkbox
         this.checked = true;  //select all checkboxes with class "checkbox1"
    });
	return false;
}

function uncheckAll(checkboxClass) {
	$('.'+checkboxClass).each(function() { //loop through each checkbox
         this.checked = false;  //select all checkboxes with class "checkbox1"
    });
	return false;
}

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