/**
 * Recalculates the styles of the DIVs of the enterprise page basing on the states of the switches.
 */
let menuDivOpen;
function recalculate(menuOpen) {
	menuDivOpen = menuOpen;
    recalculateSidenav();
    recalculateMenuSwitchDiv();
    recalculateWmDiv();
}

function recalculateSidenav() {
    let menuDiv = top.document.getElementById('sidenav');
    
    //menuDiv.style.top = headerHeight + "px";
    //menuDiv.style.width = menuWidth - 1 + "px";

    if (menuDivOpen) {
        menuDiv.classList.add('sidenav');
        menuDiv.classList.remove('hide');
    } else {
    	menuDiv.classList.add('hide');
        menuDiv.classList.remove('sidenav');
    }
}

function recalculateMenuSwitchDiv() {
    let menuDiv = top.document.getElementById('enterpriseMenuSwitchDiv');
    let chevron = top.document.getElementById('menuSliderArrow');
    //enterpriseMenuSwitchDiv.style.top = headerHeight + "px";
    //enterpriseMenuSwitchDiv.style.width = handle + "px";

    if (menuDivOpen) {
    	menuDiv.classList.add('enterpriseMenuSwitch');
        menuDiv.classList.remove('enterpriseMenuSwitch-closed');
        chevron.classList.add('menu-chevron');
        chevron.classList.remove('menu-chevron-closed');
    } else {
    	menuDiv.classList.add('enterpriseMenuSwitch-closed');
        menuDiv.classList.remove('enterpriseMenuSwitch');
        chevron.classList.add('menu-chevron-closed');
        chevron.classList.remove('menu-chevron');
    }
}

function recalculateWmDiv() {
    var contentDiv = top.document.getElementById('main');
    //contentDiv.style.top = headerHeight + "px";

    if (menuDivOpen) {
    	contentDiv.classList.add('main');
    	contentDiv.classList.remove('main-full');
    } else {
    	contentDiv.classList.add('main-full');
    	contentDiv.classList.remove('main');
    }
}