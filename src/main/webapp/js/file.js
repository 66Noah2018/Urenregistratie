/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */
let prevOpened;

function servletRequestPost(url, body) {
    const http = new XMLHttpRequest();
    let response = null;
    http.open("POST", url, false);
    http.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    
    http.onload = function(){ 
        while (http.readyState !== 4) {}
        if (http.readyState === 4) {
            response = http.responseText;
        }
    };
    http.send(JSON.stringify(body));
    while (response === null) {}
    return response;
}

function servletRequest(url){
    const http = new XMLHttpRequest();
    http.open("GET", url, false);
    let response = null;
    http.onload = function() {
//        while (http.readyState !== 4) {}
        if (http.readyState === 4 && http.status === 200) {
            response = http.responseText;
        }
    };
    http.send();
//    while (response === null) {}
    return response;
}

function showNewRegistrationForm(){
    removeViewSelections();
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/CreateRegistration.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters(true);
    };
    xhr.send();
}

function showOpenRegistrationForm(){
    removeViewSelections();
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/OpenRegistration.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters(true);
        showPrevOpened();
    };
    xhr.send();
}

function showPrevOpened(){
    prevOpened = JSON.parse(servletRequest(SERVLET_URL + "?function=getPrevOpened"));
    let target = document.getElementById("prevOpenedList");
    let listCode = `<ul data-role="listview" data-view="table" data-select-node="true" data-structure='{"fileName": true, "lastEdited": true}'>`;
    target.innerHTML = "";
    prevOpened.forEach((item) => {
        listCode += `<li data-icon="<span class='far fa-file-alt'>"
                    data-caption="${item.fileName}"
                    data-fileName="${item.path}"
                    data-lastEdited="${item.date}"
                    id="fileName" ondblclick='openRecentProject("${item.path.replaceAll("\\", "\\\\")}")'></li>`;
    });
    let parser = new DOMParser();
    target.appendChild(parser.parseFromString(listCode, 'text/html').body.firstChild);
}

function openRegistration(){
    const selectedFileName = document.getElementById("openRegistrationFileUpload").value.split("C:\\fakepath\\")[1];
    showNotification("Opening", "Trying to open your registration...", infoBoxProperties.neutral);
    setTimeout(function(){ 
        processOpen(selectedFileName);
    }, 100);
}

function processOpen(path){
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=openRegistration", path.replaceAll("\\", "\\\\")));;
    removeAllNotifications();
    if (result.status !== 'File opened successfully') {
        showNotification("Could not open registration", result.status, infoBoxProperties.warning);
        document.getElementById("open-registration-load").style.display = "none";
    } else {
        assignments = result.assignments;
        projects = result.projects;
        disableRibbonMenu(true);
        location.reload();
    }
}

function openRecentProject(path){
    showNotification("Opening", "Trying to open your registration...", infoBoxProperties.neutral);
    setTimeout(function(){ 
        processOpen(path);
    }, 100);
}

function validateRegistrationForm(){
    let allFieldsOK = true;
    if (!document.getElementById("checkDirBtn").classList.contains("success")){
        document.getElementById("working-dir-group").style.display = "block";
        allFieldsOK = false;
    } else { document.getElementById("working-dir-group").style.display = "none"; }
    const registrationName = document.getElementById("name").value;
    if (registrationName === "" || registrationName === null || registrationName === undefined){
        document.getElementById("name-group").style.display = "block";
        allFieldsOK = false;
    } else { document.getElementById("name-group").style.display = "none"; }
    
    return allFieldsOK;
}

function createRegistration(){
    showNotification("Creating", "Trying to create your registration...", infoBoxProperties.neutral);
    const validationPassed = validateRegistrationForm();
    if (!validationPassed) {
        snowNotification("Form validation failed", "Please check your input", infoBoxProperties.warning);
        return;
    }
    const registrationName = document.getElementById("name").value;
    const workingDir = document.getElementById("workingDirectory").value;
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=newRegistration", {
        "registrationName": registrationName,
        "workingDir": workingDir
    }));
    assignments = [];
    projects = [];
    
    removeAllNotifications();
    if (result.status === "OK"){
        showNotification("Success", "New registration created", infoBoxProperties.success);
        disableRibbonMenu(true);
        location.reload();
    } else {
        showNotification("Something went wrong", "Please try again", infoBoxProperties.warning);
    }
  
}

function saveRegistration(){  servletRequest(SERVLET_URL + "?function=saveRegistration"); }

function showRegistrationProperties(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/RegistrationProperties.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters(true);
        fillFields();
    };
    xhr.send();
}

function fillFields(){
    let properties = JSON.parse(servletRequest(SERVLET_URL + "?function=getRegistrationProperties"));
    document.getElementById("name").value = properties.registrationName;
    document.getElementById("workingDirectory").value = properties.workingDir;
    document.getElementById("checkDirBtn").classList.add("success");
}

function updateRegistration(){
    showNotification("Saving", "Trying to update your registration...", infoBoxProperties.neutral);
    const validationPassed = validateRegistrationForm();
    if (!validationPassed) {
        showNotification("Form validation failed", "Please check your input", infoBoxProperties.warning);
        return;
    }
    const registrationName = document.getElementById("name").value;
    const workingDir = document.getElementById("workingDirectory").value;
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=editRegistrationDetails", {
        "registrationName": registrationName,
        "workingDir": workingDir
    }));
    
    if (result.status === "OK"){
        showNotification("Success", "Registration updated", infoBoxProperties.success);
        document.getElementById("assignment-view").click();
    } else {
        showNotification("Something went wrong", "Please try again", infoBoxProperties.warning);
    }
}

function showPreferences(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/Preferences.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters(true);
    };
    xhr.send();
}

function closeForm(){ document.getElementById("assignment-view").click(); }




// update all these funcs!!
function checkDirValidity(){
    event.preventDefault();
    let targetBtn = document.getElementById("checkDirBtn");
    targetBtn.classList.remove("success");
    targetBtn.classList.remove("alert");
    let directory = document.getElementById("workingDirectory").value;
    
    let directoryExists = JSON.parse(servletRequestPost(SERVLET_URL + "?function=directoryExists", directory)).directoryExists;
    if (directoryExists && directory !== "") { 
        targetBtn.classList.add("success"); 
        document.getElementById("working-dir-group").style.display = 'none';
    } 
    else { 
        targetBtn.classList.add("alert"); 
        document.getElementById("working-dir-group").style.display = 'block';
    }
}

function checkDefaultDirValidity(){
    event.preventDefault();
    let targetBtn = document.getElementById("checkDirBtn");
    targetBtn.classList.remove("success");
    targetBtn.classList.remove("alert");
    let directory = document.getElementById("defaultWorkingDirectory").value;
    
    const http = new XMLHttpRequest(); // servletrequestpost doesnt work here, loading response somehow takes too long
    http.open("POST", "../dbvisservlet?function=directoryExists", true);
    http.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    
    http.onload = function(){ 
        directoryExists = JSON.parse(http.responseText).directoryExists;
        if (directoryExists && directory !== "") { 
            targetBtn.classList.add("success"); 
            document.getElementById("selected-working-dir-edit").style.visibility = 'hidden';
        } 
        else { 
            targetBtn.classList.add("alert"); 
            document.getElementById("selected-working-dir-edit").style.visibility = 'visible';
        }
    };
    http.send(JSON.stringify(directory));
}

function removeClassesFromDirBtn(){
    let dirBtn = document.getElementById("checkDirBtn");
    dirBtn.classList.remove("error");
    dirBtn.classList.remove("success");
}
