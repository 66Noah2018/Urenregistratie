/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function showAssignmentsForSupervisor(supervisor){
    document.getElementById("assignments-for-supervisor-header").innerHTML = "Assignments for supervisor: " + supervisor;
    let assignmentsForSupervisor = JSON.parse(servletRequest(SERVLET_URL + "?function=getAssignmentsBySupervisor&supervisor=" + supervisor));
    let list = `<div class="no-assignments">No assignments for this supervisor</div>`;
    if (assignmentsForSupervisor !== null && assignmentsForSupervisor.length > 0){
        list = '<ul class="items-list" id="assignment-list" data-role="list" data-show-search="true"></li>';
        assignmentsForSupervisor.forEach(assignment => { list += supervisorAssignmentToListItem(assignment); });
        list += "</ul>";
    }
    document.getElementById("supervisor-assignment-list").innerHTML = list;
}

function supervisorAssignmentToListItem(assignment){
    let html = `<li id="${assignment.assignmentId}" onclick="showSupervisorAssignmentDetails('${assignment.assignmentId}')">
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

function showSupervisorAssignmentDetails(assignmentId){
    document.getElementById("assignment-view").click();
    showAssignmentDetails(assignmentId);
}
