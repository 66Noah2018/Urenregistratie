/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

let projects = null;
let assignments = null;
const SERVLET_URL = "/Urenregistratie/urenregistratieServlet";

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
    if (assignment.deadline !== null) {
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
    console.log(assignments);
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
    console.log(projects);
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
//        processAssignments();
    };
    xhr.send();
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
//        processAssignments();
    };
    xhr.send();
}