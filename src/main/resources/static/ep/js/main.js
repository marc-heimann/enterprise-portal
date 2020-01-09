var button = document.getElementById("srcChange");
var buttonPoll = document.getElementById("poll");
var iframe = document.getElementById("frame");
var start = document.getElementById("portalHead");
var sideNav = document.getElementById("sidenav");
var searchField = document.getElementById("searchService");
var autocompleteFieldd = document.getElementById("autocomplete");
var menuSwitch = document.getElementById("enterpriseMenuSwitchDiv"); 
var buttonAbout = document.getElementById("button-about");
var buttonMode = document.getElementById("button-mode");
var buttonAboutClose = document.getElementById("aboutButtonClose");
var buttonAboutAb = document.getElementById("aboutButtonAbout");
var buttonAboutLib = document.getElementById("aboutButtonLibraries");
var buttonCookieAgree = document.getElementById("cookie-banner-btn-agree");
var buttonCookieDisagree = document.getElementById("cookie-banner-btn-disagree");
var boxAbout = document.getElementById("aboutMessageBoxForm");
var tabAbout = document.getElementById("aboutTabAbout");
var tabLib = document.getElementById("aboutTabLibraries");
var overlay = document.getElementById("overlay");
var cookieBanner = document.getElementById("cookie-banner");
var serviceArray = [];
var styleToggle = 0;
var asnUrl = "";
var ownUrl = window.location.protocol + '//' + window.location.host;
//let socket = new WebSocket("ws://localhost:81/service/socket/websocket");
let socket = new WebSocket("ws://ui.swisslog.net/service/socket/websocket");
var coll;
let menuOpen = true;
activateTheme();
if(getCookie("cookies")){
	cookieBanner.classList.add('hide');
	buttonMode.disabled = false;
}



socket.onopen = function(e) {
  console.log("[open] Connection established");
};

socket.onmessage = function(event) {
	var message = event.data;
	if(message.substring(0,5)=="time:"){
		console.log("time recieved: "+message.substring(5));
	}
	else if(message == " " || message == ""){
		removeElementsFromSide();
	}
	else{
		var services = JSON.parse(message);
		serviceArray = services;
		removeElementsFromSide();
		console.log("services "+services);
		services.forEach(function(service) {
			  console.log("service "+service);
			  sideNav.insertAdjacentHTML('beforeend', service[0]);
			  let div = document.createElement('div');
			  div.setAttribute('class', 'content');
			  for(let i = 1; i < service.length; i++){
				  div.innerHTML += service[i];
			  }
			  sideNav.appendChild(div);
			  coll = document.getElementsByClassName("collapsible");
		});
		for (let i = 0; i < coll.length; i++) {
			  coll[i].addEventListener("click", function() {
			    this.classList.toggle("active");
			    var content = this.nextElementSibling;
			    if (content.style.display === "block") {
			      content.style.display = "none";
			    } else {
			      content.style.display = "block";
			    }
			  });
		}
		console.log('[message] Data received from server: '+ event.data);
	}
};

socket.onclose = function(event) {
  if (event.wasClean) {
	console.log('[close] Connection closed cleanly, code=' +event.code+ ' reason= '+event.reason);
  }
  else {
    // e.g. server process killed or network down
    // event.code is usually 1006 in this case
	console.log('[close] Connection died '+event);
  }
};

socket.onerror = function(error) {
	console.log('[error] '+ error.message);
};

function removeElementsFromSide(){
	var childC = sideNav.getElementsByClassName('collapsible')[0];
	while (childC) {
	    sideNav.removeChild(childC);
	    childC = sideNav.getElementsByClassName('collapsible')[0];
	}
	var classContent = sideNav.getElementsByClassName('content')[0];
	while (classContent) {
		sideNav.removeChild(classContent);
	    classContent = sideNav.getElementsByClassName('content')[0];
	}
	iframe.src = "./images/portalBackground.png";
}

buttonCookieAgree.addEventListener("click", function (e) {
	document.cookie = "cookies=true";
	cookieBanner.classList.add('hide');
	buttonMode.disabled = false;
});

buttonCookieDisagree.addEventListener("click", function (e) {
	cookieBanner.classList.add('hide');
});

buttonAbout.addEventListener("click", function (e) {
	boxAbout.classList.remove('hide');
	overlay.classList.remove('hide');
	
});

buttonMode.addEventListener("click", function (e) {
	var actualTheme = getCookie("theme");
	if(actualTheme == ""){
		actualTheme = "old";
	}
	if(actualTheme == "old"){
		document.cookie = "theme=dark";
	}
	else if(actualTheme == "dark"){
		document.cookie = "theme=light";
	}
	else if(actualTheme == "light"){
		document.cookie = "theme=old";
	}
	activateTheme();
});

function linkClicked(e){
	iframe.contentWindow.location.href = e.toElement.href;
}

function activateTheme(){
	var theme = getCookie("theme");
	
	//disable all styles
	var allStyles = document.getElementsByClassName("custom-style");
	
	for(var i = 0; i < allStyles.length; i++){
		allStyles[i].disabled=true;
	}
	
	//enable style
	var activateStyle = document.getElementsByClassName("old-style");
	
	if(theme != ""){
		if(theme == "dark" || theme == "light"){
			activateStyle = document.getElementsByClassName(theme+"-style");
		}
		
		for(var j = 0; j < activateStyle.length; j++){
			activateStyle[j].disabled=false;
		}
		
		var siteBody = document.getElementById("frame").contentWindow.document.body.classList;
		if(theme == "dark"){
			siteBody.remove("light");
			siteBody.add("dark");
		}
		else{
			siteBody.remove("dark");
			siteBody.add("light");
		}
	}
	else{
		activateStyle = document.getElementsByClassName("old-style");
		for(var j = 0; j < activateStyle.length; j++){
			activateStyle[j].disabled=false;
		}
	}
}

function getCookie(cname) {
	  var name = cname + "=";
	  var decodedCookie = decodeURIComponent(document.cookie);
	  var ca = decodedCookie.split(';');
	  for(var i = 0; i <ca.length; i++) {
	    var c = ca[i];
	    while (c.charAt(0) == ' ') {
	      c = c.substring(1);
	    }
	    if (c.indexOf(name) == 0) {
	      return c.substring(name.length, c.length);
	    }
	  }
	  return "";
	}

menuSwitch.addEventListener("click", function (e) {
	toggleMenu();
});

buttonAboutClose.addEventListener("click", function (e) {
	boxAbout.classList.add('hide');
	overlay.classList.add('hide');
});

buttonAboutAb.addEventListener("click", function (e) {
	toggleTab(true);
});

buttonAboutLib.addEventListener("click", function (e) {
	toggleTab(false);
});

function toggleTab(aboutActive) {
	if(aboutActive == false){
		var tabActive = document.getElementById("aboutTabLibraries");
		var tabInactive = document.getElementById("aboutTabAbout");
		var buttonActive = buttonAboutLib;
		var buttonInactive = buttonAboutAb;
	}
	else {
		var tabActive = document.getElementById("aboutTabAbout");
		var tabInactive = document.getElementById("aboutTabLibraries");
		var buttonActive = buttonAboutAb;
		var buttonInactive = buttonAboutLib;
	}
	buttonActive.classList.add('ui-tabs-selected');
	buttonActive.classList.add('ui-state-active');
	buttonInactive.classList.remove('ui-tabs-selected');
	buttonInactive.classList.remove('ui-state-active');
	
	buttonActive.setAttribute('aria-expanded', 'true');
	buttonActive.setAttribute('aria-selected', 'true');
	buttonInactive.setAttribute('aria-expanded', 'false');
	buttonInactive.setAttribute('aria-selected', 'false');
	tabActive.removeAttribute("hidden");
	tabInactive.setAttribute("hidden", true);
}

/**
 * Toggles menu div.
 */
function toggleMenu() {
	menuOpen = !menuOpen;
    recalculate(menuOpen);
    var sliderArrow = top.document.getElementById('menuSliderArrow');
    /*
    if (menuDivOpen) {
        sliderArrow.src = "./icons/github/black/chevron-left.svg";
        sliderArrow.alt = "slider left";
    } else {
        sliderArrow.src = "./icons/github/black/chevron-right.svg";
        sliderArrow.alt = "slider right";
    }
    */
}

function showSearched(text){
	var childs = sideNav.getElementsByTagName('a');
	for(var i = 0; i < childs.length; i++){
	    if(childs[i].text.includes(text)){
	    	childs[i].style.display = 'block';
	    }
	    else{
	    	childs[i].style.display = 'none';
	    }
	}
}


function autocomplete(searchField){
	var currentFocus;
	/*execute a function when someone writes in the text field:*/
	searchField.addEventListener("input", function(e) {
		var a, b, i, val = this.value;
		serviceArray = sideNav.getElementsByTagName('a');
		/*close any already open lists of autocompleted values*/
		closeAllLists();
		if (!val) { return false;}
		currentFocus = -1;
		/*create a DIV element that will contain the items (values):*/
		a = document.createElement("DIV");
		a.setAttribute("id", this.id + "autocomplete-list");
		a.setAttribute("class", "autocomplete-items");
		/*append the DIV element as a child of the autocomplete container:*/
		this.parentNode.appendChild(a);
		/*for each item in the array...*/
		for (i = 0; i < serviceArray.length; i++) {
			/*check if the item starts with the same letters as the text field value:*/
			if (serviceArray[i].text.includes(val)) {
				/*create a DIV element for each matching element:*/
				b = document.createElement("DIV");
				/*make the matching letters:*/
				b.innerHTML = serviceArray[i].text;
				//b.innerHTML = serviceArray[i].text.substr(0, val.length);
				//b.innerHTML += serviceArray[i].text.substr(val.length);
				/*insert a input field that will hold the current array item's value:*/
				b.innerHTML += "<input type='hidden' value='" + serviceArray[i] + "'>";
				/*execute a function when someone clicks on the item value (DIV element):*/
				b.addEventListener("click", function(e) {
					/*insert the value for the autocomplete text field:*/
					iframe.src = this.getElementsByTagName("input")[0].value;
					/*close the list of autocompleted values,
            	(or any other open lists of autocompleted values:*/
					closeAllLists();
				});
				a.appendChild(b);
			}
		}
	});
	/*execute a function presses a key on the keyboard:*/
	searchField.addEventListener("keydown", function(e) {
		var x = document.getElementById(this.id + "autocomplete-list");
		if (x) x = x.getElementsByTagName("div");
		if (e.keyCode == 40) {
			/*If the arrow DOWN key is pressed,
      	increase the currentFocus variable:*/
			currentFocus++;
			/*and and make the current item more visible:*/
			addActive(x);
		} 
		else if (e.keyCode == 38) { //up
			/*If the arrow UP key is pressed,
      		decrease the currentFocus variable:*/
			currentFocus--;
			/*and and make the current item more visible:*/
			addActive(x);
		} 
		else if (e.keyCode == 13) {
			/*If the ENTER key is pressed, prevent the form from being submitted,*/
			e.preventDefault();
			if (currentFocus > -1) {
				/*and simulate a click on the "active" item:*/
				if (x) x[currentFocus].click();
			}
		}
	});
	function addActive(x) {
		/*a function to classify an item as "active":*/
		if (!x) return false;
		/*start by removing the "active" class on all items:*/
		removeActive(x);
		if (currentFocus >= x.length) currentFocus = 0;
		if (currentFocus < 0) currentFocus = (x.length - 1);
		/*add class "autocomplete-active":*/
		x[currentFocus].classList.add("autocomplete-active");
	}
	function removeActive(x) {
		/*a function to remove the "active" class from all autocomplete items:*/
		for (var i = 0; i < x.length; i++) {
			x[i].classList.remove("autocomplete-active");
		}
	}
	function closeAllLists(elmnt) {
		/*close all autocomplete lists in the document,
  		except the one passed as an argument:*/
		var x = document.getElementsByClassName("autocomplete-items");
		for (var i = 0; i < x.length; i++) {
			if (elmnt != x[i] && elmnt != searchField) {
				x[i].parentNode.removeChild(x[i]);
			}
		}
	}
	/*execute a function when someone clicks in the document:*/
	document.addEventListener("click", function (e) {
		searchField.value = "";
		closeAllLists(e.target);
	});
}