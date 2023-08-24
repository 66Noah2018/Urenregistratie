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
        disableFilters();
        disableExport();
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
        disableFilters();
        disableExport();
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

function saveRegistration(){ 
    showNotification("Saving", "Trying to save your registration...", infoBoxProperties.neutral);
    const saveStatus = JSON.parse(servletRequest(SERVLET_URL + "?function=saveRegistration")).status;
    if (saveStatus === "OK"){ showNotification("Success", "Registration saved", infoBoxProperties.success); }
    else { showNotification("Something went wrong", "Please try again", infoBoxProperties.warning); }
}

function showRegistrationProperties(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/RegistrationProperties.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters();
        disableExport();
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
        disableFilters();
        disableExport();
        fillPreferencesFields();
    };
    xhr.send();
}

function fillPreferencesFields(){
    const defaultWorkingDir = JSON.parse(servletRequest(SERVLET_URL + "?function=getDefaultWorkingDirectory")).defaultWorkingDirectory;
    document.getElementById("defaultWorkingDirectory").value = defaultWorkingDir.replaceAll("\\\\", "\\");
    document.getElementById("checkDirBtn").classList.add("success");
}

function validatePreferencesForm(){
    const defaultWorkingDir = document.getElementById("defaultWorkingDirectory").value;
    if (defaultWorkingDir === null || defaultWorkingDir === "null" || defaultWorkingDir === "") { 
        document.getElementById("selected-working-dir-edit").style.display = "block";
        return false; 
    }
    document.getElementById("selected-working-dir-edit").style.display = "none";
    return true;
}

function savePreferencesChanges(){
    showNotification("Saving", "Trying to update your default working directory...", infoBoxProperties.neutral);
    const validationPassed = validatePreferencesForm();
    if (validationPassed){
        const defaultWorkingDir = document.getElementById("defaultWorkingDirectory").value;
        let status = JSON.parse(servletRequestPost(SERVLET_URL + "?function=setDefaultWorkingDirectory", defaultWorkingDir)).status;
        if (status === "OK"){ 
            showNotification("Success", "Default working directory updated", infoBoxProperties.success); 
            document.getElementById("assignment-view").click();    
        } else { showNotification("Something went wrong", "Please try again", infoBoxProperties.warning); }
    } else {
        showNotification("Form validation failed", "Please check your input", infoBoxProperties.warning);
        return;
    }
}

function closeForm(){ document.getElementById("assignment-view").click(); }

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
    
    let directoryExists = JSON.parse(servletRequestPost(SERVLET_URL + "?function=directoryExists", directory)).directoryExists;
    if (directoryExists && directory !== "") { 
        targetBtn.classList.add("success"); 
        document.getElementById("selected-working-dir-edit").style.display = 'none';
    } 
    else { 
        targetBtn.classList.add("alert"); 
        document.getElementById("selected-working-dir-edit").style.display = 'block';
    }
}

function removeClassesFromDirBtn(){
    let dirBtn = document.getElementById("checkDirBtn");
    dirBtn.classList.remove("error");
    dirBtn.classList.remove("success");
}

function exportHoursWorked(){  
    let boxesTarget = document.getElementById("hours-worked-boxes-pdf");
    let assignmentTarget = document.getElementById("hours-worked-per-project-pdf");
    if (projects !== null && projects.length > 0){
        const currentDate = new Date();
        const year = ("0" + currentDate.getFullYear()).slice(-4); // ugly trick to get '2023' instead of 2023
        const month = ("0" + (currentDate.getMonth() + 1)).slice(-2);
        let boxes = "<table><tbody><tr><p class='hours-worked-header-pdf'>Hours worked per project</p></tr><tr>";
        let boxesCount = 0;
        for (let project of projects) { 
            boxes += "<td>" + processHoursWorkedProjectPDF(project, month, year) + "</td>"; 
            boxesCount += 1;
            if (boxesCount%3 === 0) {
                boxes += "</tr></tbody></table><table><tbody><tr>";
            }
        }
        boxes += "<td>" + processHoursWorkedProjectPDF({"projectId": null, "projectName":"No project", "projectCode":"-"}, month, year) + "</td></tbody></table>";
        boxesTarget.innerHTML = boxes;
        
        let lists = "<p class='hours-worked-header-pdf'>Hours worked per assignment</p>";
        for (let project of projects){ lists += processHoursWorkedPerAssignment(project, month, year); }
        lists += processHoursWorkedPerAssignment({"projectId": null, "projectName":"No project", "projectCode":"-"}, month, year);
        assignmentTarget.innerHTML = lists;
        
        window.print();
    }
}

function processHoursWorkedPerAssignment(project, month, year){
    let assignments = JSON.parse(servletRequest(SERVLET_URL + "?function=getAssignmentsByProjectId&projectId=" + project.projectId));
    let listContent = `<table><tbody><tr><div class="pdf-project-assignment-box"><p class='project-assignments-header-pdf'>Project ${project.projectName}</p><p class='project-assignments-subheader-pdf'>${project.projectCode}</p></tr><tr>`;
    let boxesCount = 0;
    for (assignment of assignments) {
        const assignmentHoursWorked = assignment.hoursWorked;
        const assignmentGroupedHours = groupHoursPerAssignment(assignmentHoursWorked, month, year);
        if (Object.keys(assignmentGroupedHours).length > 0){
            listContent += `<td><div class="pdf-assignment-box"><p class="pdf-assignment-header">${assignment.assignmentName}</p><table class="table" id="hours-worked-table-pdf">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Hours worked</th>
                </tr>
            </thead>
            <tbody>`;
            let totalHoursEpoch = 0;
            Object.entries(assignmentGroupedHours).forEach(([key, value]) => {
                listContent += hourOverviewTimeslotToTableRow(key, value);
            totalHoursEpoch += value;
            });
            listContent += "</tbody>" + totalRow(totalHoursEpoch) + "</table></div></td>";
            boxesCount += 1;
            if (boxesCount%3 == 0) {
                boxes += "</tr></tbody></table><table><tbody><tr>";
            }
        }
    }
    return listContent + "</tr></tbody></table>";
}

function groupHoursPerAssignment(timeslots, month, year){
    let groupedHours = {};
    for (timeslot of timeslots){
        const date = timeslot.startTime.split(" ")[0];
        if ((date.split("-")[1] === month) && (date.split("-")[2] === year)){
            const startDate = new Date('August 19, 1975 ' + timeslot.startTime.split(" ")[1]);
            const endDate = new Date('August 19, 1975 ' + timeslot.endTime.split(" ")[1]);
            const diffEpoch = endDate - startDate;
            if (groupedHours.hasOwnProperty(date)) { groupedHours[date] = groupedHours[date] + diffEpoch; }
            else { groupedHours[date] = diffEpoch; }
        }
    }
    return groupedHours;
}

function processHoursWorkedProjectPDF(project, month, year){
    let hours = JSON.parse(servletRequest(SERVLET_URL + "?function=getHoursByProjectId&projectId=" + project.projectId));
    let box = `<div class="hours-box" id="PDF-${project.projectId}"><p class="hours-box-header">${project.projectName}</p><p class="hours-box-subheader">${project.projectCode}</p>`;
    if (hours === null || hours.length === 0 ) { box += "<p class='no-hours-for-project'>No hours for this project</p>"; }
    else{
        box += `<table class="table" id="hours-worked-table-pdf">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Hours worked</th>
                </tr>
            </thead>
            <tbody>`;
        let hoursGrouped = groupHours(project.projectId, hours, month, year);
        let totalHoursEpoch = 0;
        Object.entries(hoursGrouped).forEach(([key, value]) => {
            box += hourOverviewTimeslotToTableRow(key, value);
            totalHoursEpoch += value;
        });
        box += "</tbody>" + totalRow(totalHoursEpoch) + "</table>";
    }
    return box += "</div>";
}