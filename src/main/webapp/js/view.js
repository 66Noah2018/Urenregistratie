/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let projects = null;
let assignments = null;
const SERVLET_URL = "/Urenregistratie/urenregistratieServlet";
let timeSlotsPerDate = {};

function loadData(){
    let result = JSON.parse(servletRequest(SERVLET_URL + "?function=getState"));
    if (result !== null) {
        projects = result.projects;
        assignments = result.assignments;
    }
    document.getElementById("assignment-view").click();
}

function removeViewSelections(){
    document.getElementById("assignment-view").classList.remove("active");
    document.getElementById("assignment-view").classList.remove("js-active");
    document.getElementById("project-view").classList.remove("active");
    document.getElementById("project-view").classList.remove("js-active");
    document.getElementById("supervisor-view").classList.remove("active");
    document.getElementById("supervisor-view").classList.remove("js-active");
    document.getElementById("hours-worked-view").classList.remove("active");
    document.getElementById("hours-worked-view").classList.remove("js-active");
}

// assignment view
function showAssignmentView(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/AssignmentView.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        processAssignments();
    };
    xhr.send();
}

function assignmentToListItem(assignment){
    let html = `<li id="${assignment.assignmentId}" onclick="showAssignmentDetails('${assignment.assignmentId}')">
        <span class="label">${assignment.assignmentName}</span>
        <span class="second-label">${getProjectName(assignment.correspondingProjectId)}</span>`;
    if (assignment.assignmentState !== "FINISHED" && assignment.deadline !== null) {
        const re = /(.*?)-(.*?)-(.*?) (.*?):(.*?):(.*)/gm;
        deadlineArray = re.exec(assignment.deadline);
        const deadlineDate = new Date(deadlineArray[3], deadlineArray[2] - 1, deadlineArray[1], deadlineArray[4], deadlineArray[5], deadlineArray[6], 0);
        const currentDate = Date.now();
        const epochWithinThreeDays = 3*24*60*60*1000;
        const epochWithinWeek = 7*24*60*60*1000;
        const diffNowDeadline = (deadlineDate.valueOf()) - currentDate.valueOf();
        if (diffNowDeadline <= epochWithinThreeDays) { html += '<span class="second-action mif-alarm fg-red"></span>'; }
        else if (diffNowDeadline <= epochWithinWeek) { html += '<span class="second-action mif-alarm"></span>'; }
    }
    return html + "</li>";
}

function processAssignments(){
    let list = '<ul class="items-list" id="assignment-list" data-role="list" data-show-search="true"></li>';
    if (assignments !== null && assignments.length > 0) {
        assignments.forEach(assignment => {
            list += assignmentToListItem(assignment);
        });
        list += "</ul>";
    } else { list = "<div class='no-assignment-selected'>No assignments yet</div>"; }
    
    
    document.getElementById("assignment-overview").innerHTML = list;
}

// project view
function showProjectView(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/ProjectView.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        processProjects();
    };
    xhr.send();
}

function processProjects(){
    let list = '<ul class="items-list" id="project-list" data-role="list" data-show-search="true"></li>';
    if (projects !== null && projects.length > 0) {
        projects.forEach(project => {
            list += projectToListItem(project);
        });
        list += "</ul>";
    } else { list = "<div class='no-project-selected'>No projects yet</div>"; }
       
    document.getElementById("project-overview").innerHTML = list;
}

function projectToListItem(project){
    return `<li id="${project.projectId}" onclick="showProjectDetails('${project.projectId}')">
        <span class="label">${project.projectName}</span></li>`;
}

// supervisor view

function showSupervisorView(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/SupervisorView.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        processSupervisors();
    };
    xhr.send();
}

function processSupervisors(){
    let supervisors = JSON.parse(servletRequest(SERVLET_URL + "?function=getSupervisors"));
    let list = '<ul class="items-list" id="project-list" data-role="list" data-show-search="true"></li>';;
    if (supervisors !== null && supervisors.length > 0) {
        supervisors.forEach(supervisor => {
            list += supervisorToListItem(supervisor);
        });
        list += "</ul>";
    } else { list = "<div class='.no-supervisor-selected'>No supervisors yet</div>"; }
       
    document.getElementById("supervisors-overview").innerHTML = list;
}

function supervisorToListItem(supervisor){
    return `<li id="${supervisor}" onclick="showAssignmentsForSupervisor('${supervisor}')">
        <span class="label">${supervisor}</span></li>`;
}

// hours worked view

function showHoursWorkedView(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/HoursWorkedView.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        const currentDate = new Date();
        const year = ("0" + currentDate.getFullYear()).slice(-4); // ugly trick to get '2023' instead of 2023
        const month = ("0" + (currentDate.getMonth() + 1)).slice(-2);
        processHoursWorkedForHoursWorkedView(month, year);
    };
    xhr.send();
}

function processHoursWorkedForHoursWorkedView(month, year, unwrittenOnly = false){
   let target = document.getElementById("hours-worked-project-box");
   if (projects !== null && projects.length > 0){
        let boxes = "";
        for (let project of projects){
            if (unwrittenOnly) { boxes += processUnwrittenHoursProject(project, month, year); }
            else { boxes += processHoursWorkedProject(project, month, year); }
        }
        target.innerHTML = boxes;
   } else {
       target.innerHTML = "<div class='no-project-selected'>No projects yet</div>";
   }
}

function processHoursWorkedProject(project, month, year){
    let hours = JSON.parse(servletRequest(SERVLET_URL + "?function=getHoursByProjectId&projectId=" + project.projectId));
    let box = `<div class="hours-box" id="${project.projectId}"><p class="hours-box-header">${project.projectName}</p>`;
    if (hours === null || hours.length === 0) { box += "<div class='no-hours-for-project'>No hours for this project</div>"; }
    else{
        box += `<button class="button success outline mark-all-as-written-switch" onclick="markAllAsWritten('${project.projectId}')" id="hours-written-switch-${project.projectId}">Mark all hours as written</button>
        <table class="table .row-hover" id="hours-worked-table">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Hours worked</th>
                </tr>
            </thead>
            <tbody>`;
        let hoursGrouped = groupHours(project.projectId, hours, month, year);
        Object.entries(hoursGrouped).forEach(([key, value]) => {
            box += hourOverviewTimeslotToTableRow(key, value);
        });
        box += "</tbody></table>";
    }
    return box += "</div>";
}

function markAllAsWritten(projectId){
    const timeSlotsToUpdate = timeSlotsPerDate[projectId];
    let result;
    Object.entries(timeSlotsToUpdate).forEach(([_, value]) => {
        if (value.length > 0) {
            for (let timeSlotId of value){
                result = servletRequest(SERVLET_URL + "?function=updateTimeSlotState&timeSlotId=" + timeSlotId + "&state=true");
            }
        }
    });
    result = JSON.parse(result);
    assignments = result.assignments;
    projects = result.projects;
    updateHoursWorkedView();
}

function updateHoursWorkedView(){
    const month = (document.getElementById("hours-worked-date-picker").value).split("-")[1];
    const year = (document.getElementById("hours-worked-date-picker").value).split("-")[0];
    const unwrittenHoursOnly = document.getElementById("written-hours-switch").checked;
    if (month !== "undefined-" && month !== undefined && year !== undefined) { processHoursWorkedForHoursWorkedView(month, year, unwrittenHoursOnly); }
}

function processUnwrittenHoursProject(project, month, year){
    let hours = JSON.parse(servletRequest(SERVLET_URL + "?function=getUnwrittenHoursByProjectId&projectId=" + project.projectId));
    let box = `<div class="hours-box" id="${project.projectId}"><p class="hours-box-header">${project.projectName}</p>`;
    if (hours === null || hours.length === 0) { box += "<div class='no-hours-for-project'>No unwritten hours for this project</div>"; }
    else {
        box += `<button class="button success outline mark-all-as-written-switch" onclick="markAllAsWritten('${project.projectId}')" id="hours-written-switch-${project.projectId}">Mark all hours as written</button><table class="table .row-hover" id="hours-worked-table">
            <thead>
                <tr>
                    <th>Date</th>
                    <th>Hours worked</th>
                </tr>
            </thead>
            <tbody>`;
        let hoursGrouped = groupHours(project.projectId, hours, month, year);
        Object.entries(hoursGrouped).forEach(([key, value]) => {
            box += hourOverviewTimeslotToTableRow(key, value);
        });
        box += "</tbody></table>";
    }
    return box += "</div>";
}

function groupHours(projectId, hours, month, year){
    let groupedHours = {};
    let timeSlotsForDate = {};
    for (let timeslot of hours){
        const date = timeslot.startTime.split(" ")[0];
        if ((date.split("-")[1] === month) && (date.split("-")[2] === year)){
            const startDate = new Date('August 19, 1975 ' + timeslot.startTime.split(" ")[1]);
            const endDate = new Date('August 19, 1975 ' + timeslot.endTime.split(" ")[1]);
            const diffEpoch = endDate - startDate;
            if (groupedHours.hasOwnProperty(date)) { groupedHours[date] = groupedHours[date] + diffEpoch; }
            else { groupedHours[date] = diffEpoch; }
            if (timeSlotsForDate.hasOwnProperty(date)) { timeSlotsForDate[date].push(timeslot.timeSlotId); }
            else {timeSlotsForDate[date] = new Array(timeslot.timeSlotId); }
        }
    }
    timeSlotsPerDate[projectId] = timeSlotsForDate;
    return groupedHours;
}

function hourOverviewTimeslotToTableRow(key, value){
    let diffEpoch = value;
    let hh = Math.floor(diffEpoch / 1000 / 60 / 60);
    hh = "0" + hh;
    diffEpoch -= hh * 1000 * 60 * 60;
    let mm = (diffEpoch / 1000 / 60);
    mm = Math.round((mm * 100 / 60));
//    mm = "0" + mm;
//    diffEpoch -= mm * 1000 * 60;
//    let ss = Math.floor(diffEpoch / 1000);
//    ss = "0" + ss;
    let code = `<tr>
        <td>${key}</td><td>${hh.slice(-2)}.${mm}h</td>`;
    return code + "</td></tr>";
}