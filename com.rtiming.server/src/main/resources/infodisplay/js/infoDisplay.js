function replaceData(key,value) {
	var node = document.getElementById(key);
	node.innerHTML=value;
}

function writeData() {
	var prmstr = window.location.search.substr(1);
	var prmarr = prmstr.split("&");
	var params = {};

	for (var i=0; i<prmarr.length; i++) {
		var tmparr = prmarr[i].split("=");
		replaceData(tmparr[0],decodeURIComponent(tmparr[1]));
	}
}
