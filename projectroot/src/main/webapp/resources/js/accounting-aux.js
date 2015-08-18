function formatQuantity(val) {
	return accounting.formatMoney(val, "", 0, ",", ".");
}

function formatCurrency(val) {
	return accounting.formatMoney(val, "&#8369;", 2, ",", ".");
}

function sumColumn(data) {
    var i, total = 0, elem;
    for (i = 0; i < data.length; i++) {
        total += data[i];
    }
    return total;
}