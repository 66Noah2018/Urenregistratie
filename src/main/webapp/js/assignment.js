/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let selectedAssignmentId = null;

function createNewAssignment(){
    removeViewSelections();
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/NewAssignment.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("content").innerHTML = htmlDoc.body.innerHTML;
        disableFilters();
        disableExport();
        fillProjectSelectBox();
    };
    xhr.send();
}

function validateAssignmentForm(){
    let allFieldsOK = true;
    const assignmentName = document.getElementById("assignment-name").value;
    const nameErrorDiv = document.getElementById("assignment-name-group");
    const supervisor = document.getElementById("assignment-supervisor").value;
    const supervisorErrorDiv = document.getElementById("assignment-supervisor-group");
    if (assignmentName === "" || assignmentName === null || assignmentName === undefined){
        allFieldsOK = false;
        nameErrorDiv.style.display = "block";
    } else { nameErrorDiv.style.display = "none"; }
    
    if (supervisor === "" || supervisor === null || supervisor === undefined){
        allFieldsOK = false;
        supervisorErrorDiv.style.display = "block";
    } else { supervisorErrorDiv.style.display = "none"; }

    return allFieldsOK;
}

function validateTimeslotForm(){
    let allFieldsOK = true;
    const startDate = document.getElementById("start-date-picker").value;
    const startDateErrorDiv = document.getElementById("start-date-picker-group");
    if (startDate === "" || startDate === null || startDate === undefined){
        allFieldsOK = false;
        startDateErrorDiv.style.display = "block";
    } else { startDateErrorDiv.style.display = "none"; }

    return allFieldsOK;
}

function saveNewAssignment(){
    const assignmentName = document.getElementById("assignment-name").value;
    const assignmentDetails = document.getElementById("assignment-details").value;
    const assignmentSupervisor = document.getElementById("assignment-supervisor").value;
    const correspondingProjectName = document.getElementById("corresponding-project-selectbox").value;
    const correspondingProjectId = getProjectIdByName(correspondingProjectName);
    const assignmentDeadline = document.getElementById("assignment-deadline").value;
    const assignmentDeadlineFull = (assignmentDeadline === "" || assignmentDeadline === null) ? "null" : (assignmentDeadline + " 00:00:00");
    const formValidationPassed = validateAssignmentForm();
    if (formValidationPassed){
        const result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=addAssignment", {
            "assignmentName": assignmentName,
            "assignmentDetails": assignmentDetails,
            "supervisor": assignmentSupervisor,
            "correspondingProjectId": correspondingProjectId,
            "deadline": assignmentDeadlineFull
        }));
        assignments = result.assignments;
        projects = result.projects;
        document.getElementById("assignment-view").click();
        showNotification("Saved", "New assignment created", infoBoxProperties.success);
        saveRegistration();
    }  
}

function fillProjectSelectBox(){
   let selectBoxDiv = document.getElementById("corresponding-project-select");
   let selectBoxCode = '<select data-role="select" id="corresponding-project-selectbox"><option value="no-project">No project</option>';
   let projectNames = getAllProjectNames();
   projectNames.forEach(projectName => {
       selectBoxCode += `<option value="${projectName}">${projectName}</option>`;
   });
   selectBoxCode += "</select>";
   selectBoxDiv.innerHTML = selectBoxCode;
}

function showAssignmentDetails(assignmentId){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/AssignmentDetails.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("assignment-details-box").innerHTML = htmlDoc.body.innerHTML;
        fillProjectSelectBox();
        fillAssignmentDetailsFields(assignmentId);
    };
    xhr.send();
}

function fillAssignmentDetailsFields(assignmentId){
    selectedAssignmentId = assignmentId;
    const assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === assignmentId)[0];
    document.getElementById("assignment-id-hidden").innerHTML = assignmentId;
    document.getElementById("assignment-name").value = assignmentToDisplay.assignmentName;
    document.getElementById("assignment-details").value = assignmentToDisplay.assignmentDetails;
    document.getElementById("assignment-supervisor").value = assignmentToDisplay.supervisor;
    if (assignmentToDisplay.correspondingProjectId === "null") { document.getElementById("corresponding-project-selectbox").value = "no-project"; }
    else { document.getElementById("corresponding-project-selectbox").value = getProjectName(assignmentToDisplay.correspondingProjectId); }
    if (assignmentToDisplay.deadline !== "null" && assignmentToDisplay.deadline !== null) { document.getElementById("assignment-deadline").value = assignmentToDisplay.deadline.split(" ")[0]; }
    document.getElementById("assignment-state-selectbox").value = assignmentToDisplay.assignmentState;
    processHoursWorked(assignmentToDisplay.hoursWorked);
}

function processHoursWorked(hoursWorked){
    let tableBody = document.getElementById("hours-worked-table").children[1];
    let tableRows = "";
    hoursWorked = hoursWorked.reverse();
    hoursWorked.forEach(timeslot => { tableRows += timeslotToTableRow(timeslot); });
    tableBody.innerHTML = tableRows;
}

function timeslotToTableRow(timeslot){
    let code = `<tr id="${timeslot.timeSlotId}">
        <td>
            <button class="button square mini" onclick="editTimeslot('${timeslot.timeSlotId}')"><span class="mif-pencil"></span></button>
            <button class="button square mini" onclick="deleteTimeslot('${timeslot.timeSlotId}')"><span class="mif-bin"></span></button>
        </td>
        <td>${timeslot.startTime}</td>`;
    if (timeslot.endTime !== null && timeslot.endTime !== "null"){
        code += `<td>${timeslot.endTime}</td><td>`;
    } else {
        code += `<td>
            <button class="button" onclick="finishStopwatch('${timeslot.timeSlotId}')">End</button>
        </td><td>`;
    }
    if (timeslot.hoursWritten === "true"){
        code += `<input type="checkbox" checked
        data-role="checkbox"
        data-style="2"
        data-cls-check="hours-written-checkbox"
        onclick="updateHoursWritten('${timeslot.timeSlotId}')"
        id="hours-written-${timeslot.timeSlotId}">`;
    } else {
        code += `<input type="checkbox"
        data-role="checkbox"
        data-style="2"
        data-cls-check="hours-written-checkbox"
        onclick="updateHoursWritten('${timeslot.timeSlotId}')"
        id="hours-written-${timeslot.timeSlotId}">`;
    }
    return code + "</td></tr>";
}

function editTimeslot(timeslotId){
    const hoursWorked = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0].hoursWorked;
    const timeSlot = hoursWorked.filter(timeslot => timeslot.timeSlotId === timeslotId)[0];
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/AddTimeslotDialog.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html").body.innerHTML;
        Metro.dialog.create({
            title: "Edit timeslot",
            content: htmlDoc,
            actions: [
                    {
                        caption: "Save",
                        cls: "js-dialog-close",
                        onclick: function(){
                            processUpdatedTimeslot(timeSlot);
                        }
                    },
                    {
                        caption: "Cancel",
                        cls: "js-dialog-close"
                    }
                ]
        });
        document.getElementById("start-date-picker").value = timeSlot.startTime.split(" ")[0];
        document.getElementById("start-time-picker").value = timeSlot.startTime.split(" ")[1];
        if (timeSlot.endTime.split(" ")[1] === undefined){
            document.getElementById("timeslot-ongoing-checkbox").checked = true;
            document.getElementById("end-time-picker").disabled = true;
        } else { document.getElementById("end-time-picker").value = timeSlot.endTime.split(" ")[1]; }
        
    };
    xhr.send();
}

function processUpdatedTimeslot(timeslot){
    let startDate = document.getElementById("start-date-picker").value;
    let startTime = document.getElementById("start-time-picker").value;
    let startTimeNormalized = new Date('August 19, 1975 ' + startTime).toLocaleTimeString('it-IT');
    let completeEndTime = "null";
    let ongoing = document.getElementById("timeslot-ongoing-checkbox").checked;
    if (!ongoing){
        let endTime = document.getElementById("end-time-picker").value;
        let endTimeNormalized = new Date('August 19, 1975 ' + endTime).toLocaleTimeString('it-IT');
        completeEndTime = startDate + " " + endTimeNormalized;
    }
    
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=updateTimeSlot", {
        "assignmentId": selectedAssignmentId,
        "timeSlotId": timeslot.timeSlotId,
        "startTime": startDate + " " + startTimeNormalized,
        "endTime": completeEndTime,
        "hoursWritten": timeslot.hoursWritten,
        "ongoing": ongoing.toString()
    }));
    assignments = result.assignments;
    projects = result.projects;
    assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Saved", "Timeslot updated", infoBoxProperties.success);
    saveRegistration();
}

function deleteTimeslot(timeslotId){
    Metro.dialog.create({
        clsDialog: "delete-timeslot-dialog",
        title: "Delete timeslot",
        content: "<div>Are you sure you want to delete this timeslot? This action cannot be undone.</div>",
        actions: [
            {
                caption: "Delete",
                cls: "js-dialog-close alert",
                onclick: function(){
                    processDeletedTimeslot(timeslotId);
                }
            },
            {
                caption: "Cancel",
                cls: "js-dialog-close"
            }
        ]
    });
}

function processDeletedTimeslot(timeslotId){
    const URL = SERVLET_URL + "?function=deleteTimeSlot&assignmentId=" + selectedAssignmentId + "&timeSlotId=" + timeslotId;
    let result = JSON.parse(servletRequest(URL));
    assignments = result.assignments;
    projects = result.projects;
    assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Deleted", "Timeslot deleted", infoBoxProperties.success);
    saveRegistration();
}

function newTimeslot(){
    const currentDate = new Date();
    const date = "0" + currentDate.getDate();
    const month = "0" + (currentDate.getMonth() + 1);
    const hour = "0" + currentDate.getHours();
    const minutes = "0" + currentDate.getMinutes();
    const seconds = "0" + currentDate.getSeconds();
    const dateString = date.slice(-2) + "-" + month.slice(-2) + "-" + currentDate.getFullYear() + " " + hour.slice(-2) + ":" + minutes.slice(-2) + ":" + seconds.slice(-2);
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=addTimeSlot", {
        "assignmentId": selectedAssignmentId,
        "startTime": dateString,
        "endTime": "null"
    }));
    assignments = result.assignments;
    projects = result.projects;
    assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Saved", "New timeslot created", infoBoxProperties.success);   
    saveRegistration();
}

function finishStopwatch(timeslotId){
    let assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    const timeslot = assignmentToDisplay.hoursWorked.filter(timeslot => timeslot.timeSlotId === timeslotId)[0]; 
    const currentDate = new Date();
    const date = "0" + currentDate.getDate();
    const month = "0" + (currentDate.getMonth() + 1);
    const hour = "0" + currentDate.getHours();
    const minutes = "0" + currentDate.getMinutes();
    const seconds = "0" + currentDate.getSeconds();
    const dateString = date.slice(-2) + "-" + month.slice(-2) + "-" + currentDate.getFullYear() + " " + hour.slice(-2) + ":" + minutes.slice(-2) + ":" + seconds.slice(-2);
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=updateTimeSlot", {
        "assignmentId": selectedAssignmentId,
        "timeSlotId": timeslotId,
        "startTime": timeslot.startTime,
        "endTime": dateString,
        "hoursWritten": timeslot.hoursWritten
    }));
    assignments = result.assignments;
    projects = result.projects;
    assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Saved", "Timeslot updated", infoBoxProperties.success);
    saveRegistration();
}

function updateHoursWritten(timeslotId){
    let assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    const timeslot = assignmentToDisplay.hoursWorked.filter(timeslot => timeslot.timeSlotId === timeslotId)[0];
    const hoursWritten = document.getElementById("hours-written-" + timeslotId).checked.toString();
    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=updateTimeSlot", {
        "assignmentId": selectedAssignmentId,
        "timeSlotId": timeslotId,
        "startTime": timeslot.startTime,
        "endTime": timeslot.endTime,
        "hoursWritten": hoursWritten
    }));
    
    assignments = result.assignments;
    projects = result.projects;
    assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Saved", "Timeslot updated", infoBoxProperties.success);
    saveRegistration();
}

function addTimeslot(){
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/AddTimeslotDialog.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html").body.innerHTML;
        Metro.dialog.create({
            title: "Add timeslot",
            content: htmlDoc,
            actions: [
                    {
                        caption: "Save",
                        cls: "js-dialog-close",
                        onclick: function(){
                            const formValidationPassed = validateTimeslotForm();
                            if (formValidationPassed){
                                processAddedTimeslot();
                            } else { event.stopPropagation();  }
                        }
                    },
                    {
                        caption: "Discard",
                        cls: "js-dialog-close"
                    }
                ]
        });
    };
    xhr.send();
}

function processAddedTimeslot(){
    let startDate = document.getElementById("start-date-picker").value;
    let startTime = document.getElementById("start-time-picker").value;
    let startTimeNormalized = new Date('August 19, 1975 ' + startTime).toLocaleTimeString('it-IT');
    let endTime = document.getElementById("end-time-picker").value;
    let endTimeNormalized = new Date('August 19, 1975 ' + endTime).toLocaleTimeString('it-IT');

    let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=addTimeSlot", {
        "assignmentId": selectedAssignmentId,
        "startTime": startDate + " " + startTimeNormalized,
        "endTime": startDate + " " + endTimeNormalized
    }));
    assignments = result.assignments;
    projects = result.projects;

    let assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
    processHoursWorked(assignmentToDisplay.hoursWorked);
    showNotification("Saved", "New timeslot created", infoBoxProperties.success);
    saveRegistration();
}

function saveUpdatedAssignment(){
    // timeslot changes are processed onchange, so we only need to process the details
    const formValidationPassed = validateAssignmentForm();
    if (formValidationPassed){
        let assignmentToDisplay = assignments.filter(assignment => assignment.assignmentId === selectedAssignmentId)[0];
        let assignmentDeadline = document.getElementById("assignment-deadline").value;
        const assignmentDeadlineFull = (assignmentDeadline === "" || assignmentDeadline === null) ? "null" : (assignmentDeadline + " 00:00:00");
        let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=updateAssignment", {
            "assignmentId": selectedAssignmentId,
            "assignmentName": document.getElementById("assignment-name").value,
            "assignmentDetails": document.getElementById("assignment-details").value,
            "supervisor": document.getElementById("assignment-supervisor").value,
            "correspondingProjectId": getProjectIdByName(document.getElementById("corresponding-project-selectbox").value),
            "hoursWorked": assignmentToDisplay.hoursWorked,
            "assignmentState": document.getElementById("assignment-state-selectbox").value,
            "deadline": assignmentDeadlineFull
        }));
        assignments = result.assignments;
        projects = result.projects;
        filterAssignments(null);
        showNotification("Saved", "Assignment updated", infoBoxProperties.success);
        saveRegistration();
    }
}

function deleteAssignment(){
    Metro.dialog.create({
        clsDialog: "delete-assignment-dialog",
        title: "Delete assignment",
        content: "<div>Are you sure you want to delete this assignment? This action cannot be undone.</div>",
        actions: [
            {
                caption: "Delete",
                cls: "js-dialog-close alert",
                onclick: function(){
                    processDeletedAssignment();
                }
            },
            {
                caption: "Cancel",
                cls: "js-dialog-close"
            }
        ]
    });
}

function processDeletedAssignment(){
    let result = JSON.parse(servletRequest(SERVLET_URL + "?function=deleteAssignment&assignmentId=" + selectedAssignmentId));
    assignments = result.assignments;
    projects = result.projects;
    selectedAssignmentId = null;
    showAssignmentView();
    showNotification("Deleted", "Assignment deleted", infoBoxProperties.success);
    saveRegistration();
}

function filterAssignments(checkboxClicked){
    if (checkboxClicked !== null){
        let targetClicked = document.getElementById(checkboxClicked);
        if (targetClicked.classList.contains("checked")) { targetClicked.classList.remove("checked"); }
        else { targetClicked.classList.add("checked"); }
    }
    
    const showNotStarted = document.getElementById("not-started-checkbox").classList.contains("checked");
    const showStarted = document.getElementById("started-checkbox").classList.contains("checked");
    const showFinished = document.getElementById("finished-checkbox").classList.contains("checked");
    const showInsufficientInformation = document.getElementById("insufficient-information-checkbox").classList.contains("checked");
    
    let statesToRequest = "[";
    if (showNotStarted) { statesToRequest += "NOT_STARTED,"; }
    if (showStarted) { statesToRequest += "STARTED,"; }
    if (showFinished) { statesToRequest += "FINISHED,"; } 
    if (showInsufficientInformation) { statesToRequest += "INSUFFICIENT_INFORMATION,"; }
    if (statesToRequest === "[]") { 
        assignments = []; 
        return;
    } else {
        statesToRequest = statesToRequest.substring(0, statesToRequest.length - 1) + "]";
    }
    assignments = JSON.parse(servletRequestPost(SERVLET_URL + "?function=getAssignmentsByState", statesToRequest));
    processAssignments();
}

function showAllAssignments(){
    document.getElementById("not-started-checkbox").classList.add("checked");
    document.getElementById("started-checkbox").classList.add("checked");
    document.getElementById("finished-checkbox").classList.remove("checked");
    document.getElementById("insufficient-information-checkbox").classList.add("checked");
    assignments = JSON.parse(servletRequestPost(SERVLET_URL + "?function=getAssignmentsByState", "[NOT_STARTED, STARTED, INSUFFICIENT_INFORMATION]"));
    processAssignments();
}

function updateTimeslotType(){
    const timeslotTypeOngoing = document.getElementById("timeslot-ongoing-checkbox").checked;
    if (timeslotTypeOngoing){ document.getElementById("end-time-picker").disabled = true; }
    else { document.getElementById("end-time-picker").disabled = false; }
}
