/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

function createNewProject() {
    document.getElementById("content").setAttribute("src", "/Urenregistratie/html/NewProject.html");
}

function getAllProjectNames(){
    let projectNames = [];
    if (projects == null) { return projectNames; }
    for (const project of projects){
        console.log(project);
        projectNames.push(project.projectName);
    }
    return projectNames;
}

