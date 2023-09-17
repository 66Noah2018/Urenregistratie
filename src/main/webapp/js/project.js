/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let selectedProjectId = null;

function createNewProject() {
    removeViewSelections();
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/NewProject.html', true);
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

function getAllProjectNames(){
    let projectNames = [];
    if (projects === null) { return projectNames; }
    for (const project of projects){
        projectNames.push(project.projectName);
    }
    return projectNames;
}

function getProjectName(projectId){
    if (projectId === "null") { return "No project"; }
    for (const project of projects){
        if (project.projectId === projectId) { return project.projectName; }
    }
}

function getProjectIdByName(projectName){
    if (projectName === "no-project") { return "null"; }
    for (const project of projects){
        if (project.projectName === projectName) { return project.projectId; }
    }
}

function saveNewProject(){
    const formValidationPassed = validateProjectForm();
    if (formValidationPassed) {
        const projectName = document.getElementById("project-name").value;
        const projectCode = document.getElementById("project-code").value;
        let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=addProject", {
            "projectName": projectName,
            "projectCode": projectCode
        }));
        assignments = result.assignments;
        projects = result.projects;
        document.getElementById("project-view").click();
        showNotification("Saved", "New project created", infoBoxProperties.success);
    }
}

function showProjectDetails(projectId){
    selectedProjectId = projectId;
    const projectToDisplay = projects.filter(project => project.projectId === projectId)[0];
    var xhr= new XMLHttpRequest();
    xhr.open('GET', '/Urenregistratie/html/ProjectDetails.html', true);
    xhr.onreadystatechange= function() {
        if (this.readyState!==4) return;
        if (this.status!==200) return;
        let parser = new DOMParser();
        let htmlDoc = parser.parseFromString(this.responseText,"text/html");
        document.getElementById("project-details-box").innerHTML = htmlDoc.body.innerHTML;
        document.getElementById("project-name").value = projectToDisplay.projectName;
        document.getElementById("project-code").value = projectToDisplay.projectCode;
        showProjectAssignments();
    };
    xhr.send();
}

function showProjectAssignments(){
    let assignmentsForProject = JSON.parse(servletRequest(SERVLET_URL + "?function=getAssignmentsByProjectId&projectId=" + selectedProjectId));
    let list = `<div class="no-assignments">No assignments for this project</div>`;
    if (assignmentsForProject !== null && assignmentsForProject.length > 0){
        list = '<ul class="items-list" id="assignment-list" data-role="list" data-show-search="true"></li>';
        assignmentsForProject.forEach(assignment => { list += projectAssignmentToListItem(assignment); });
        list += "</ul>";
    }
        
    document.getElementById("project-assignments-box").innerHTML = list;
}

function projectAssignmentToListItem(assignment){
    let html = `<li id="${assignment.assignmentId}" onclick="showProjectAssignmentDetails('${assignment.assignmentId}')">
        <span class="label">${assignment.assignmentName}</span>
        <span class="second-label">${getProjectName(assignment.correspondingProjectId)}</span>`;
    if (assignment.assignmentState !== "FINISHED" && assignment.deadline !== null && assignment.deadline !== "null" & assignment.deadline !== "") {
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

function showProjectAssignmentDetails(assignmentId){
    document.getElementById("assignment-view").click();
    showAssignmentDetails(assignmentId);
}

function saveUpdatedProject(){
    const formValidationPassed = validateProjectForm();
    if (formValidationPassed) {
        const projectName = document.getElementById("project-name").value;
        const projectCode = document.getElementById("project-code").value;
        let result = JSON.parse(servletRequestPost(SERVLET_URL + "?function=updateProject", {
            "projectId": selectedProjectId,
            "projectName": projectName,
            "projectCode": projectCode
        }));
        assignments = result.assignments;
        projects = result.projects;
        processProjects();
        showProjectAssignments();
        showNotification("Saved", "Project updated", infoBoxProperties.success);
    }
}

function deleteProject(){
     Metro.dialog.create({
        clsDialog: "delete-project-dialog",
        title: "Delete project",
        content: "<div>Are you sure you want to delete this project? This action cannot be undone.</div>",
        actions: [
            {
                caption: "Delete",
                cls: "js-dialog-close alert",
                onclick: function(){
                    processDeletedProject();
                }
            },
            {
                caption: "Cancel",
                cls: "js-dialog-close"
            }
        ]
    });
}

function processDeletedProject(){   
    let result = JSON.parse(servletRequest(SERVLET_URL + "?function=deleteProject&projectId=" + selectedProjectId));
    assignments = result.assignments;
    projects = result.projects;
    selectedProjectId = null;
    document.getElementById("project-view").click();
    showNotification("Deleted", "Project deleted", infoBoxProperties.success);
}

function validateProjectForm(){
    let allFieldsOK = true;
    const projectName = document.getElementById("project-name").value;
    const nameErrorDiv = document.getElementById("project-name-group");
    const projectCode = document.getElementById("project-code").value;
    const codeErrorDiv = document.getElementById("project-code-group");
    
    if (projectName === "" || projectName === null || projectName === undefined){
        allFieldsOK = false;
        nameErrorDiv.style.display = "block";
    } else { nameErrorDiv.style.display = "none"; }
    
    if (projectCode === "" || projectCode === null || projectCode === undefined){
        allFieldsOK = false;
        codeErrorDiv.style.display = "block";
    } else { codeErrorDiv.style.display = "none"; }    
    
    return allFieldsOK;
}