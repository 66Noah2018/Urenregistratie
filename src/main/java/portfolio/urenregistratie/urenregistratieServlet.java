/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package portfolio.urenregistratie;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.javatuples.Pair;
import org.javatuples.Triplet;

/**
 *
 * @author RLvan
 */
public class urenregistratieServlet extends HttpServlet {
    public static ArrayList<Assignment> assignments = new ArrayList<>();
    public static ArrayList<Project> projects = new ArrayList<>();
    private ArrayList<Assignment> requestedAssignments = null;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        response.addHeader("Connection", "close");
        response.addHeader("Cache-Control", "no-cache, no-store");
        response.setStatus(HttpServletResponse.SC_OK);
        if (Utils.sourcePath == null) {
            ServletContext context = getServletContext();
            String realContext = context.getRealPath(context.getContextPath());
            if (realContext != null) { Utils.sourcePath = realContext.split("\\\\target")[0]; }
        }
        switch(request.getParameter("function")){
            // getters
            case "getState": // return project and sorted (by deadline) assignments
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "getProjectById":
                Project requestedProject = Utils.getProjectById(projects, request.getParameter("projectId"));
                response.getWriter().write(JSONEncoder.encodeProject(
                        requestedProject));
                break;
            case "getHoursByProjectId":
                ArrayList<TimeSlot> hoursForProject = Utils.getHoursByProjectId(assignments, request.getParameter("projectId"));
                response.getWriter().write(JSONEncoder.encodeTimeSlots(hoursForProject));
                break;
            case "getUnwrittenHoursByProjectId":
                ArrayList<TimeSlot> unwrittenHoursForProject = Utils.getHoursByProjectId(assignments, request.getParameter("projectId"));
                response.getWriter().write(JSONEncoder.encodeTimeSlots(unwrittenHoursForProject));
                break;
            case "getAllHours":
                String allHours = getHoursGrouped();
                response.getWriter().write(allHours);
                break;
            case "getAllUnwrittenHours":
                String allUnwrittenHours = getUnwrittenHoursGrouped();
                response.getWriter().write(allUnwrittenHours);
                break;
            case "getAssignmentsByState":
                ArrayList<String> states = ServletUtils.getStringArrayListFromRequestBody(request);
                requestedAssignments = Utils.getAssignmentsByState(assignments, states);
                response.getWriter().write(JSONEncoder.encodeAssignments(
                        requestedAssignments));
                break;
            case "getAssignmentsByProjectId":
                requestedAssignments = Utils.getAssignmentsByProjectId(assignments, request.getParameter("projectId"));
                response.getWriter().write(JSONEncoder.encodeAssignments(
                        requestedAssignments));
                break;
            case "getAssignmentsBySupervisor":
                requestedAssignments = Utils.getAssignmentsBySupervisor(
                        assignments, request.getParameter("supervisor"));
                response.getWriter().write(JSONEncoder.encodeAssignments(
                        requestedAssignments));
                break;
            case "getSupervisors":
                ArrayList<String> supervisors = Utils.getSupervisors(assignments);
                response.getWriter().write(supervisors.toString());
                break;
            case "getAssignmentsWithoutDeadline":
                requestedAssignments = Utils.getAssignmentsWithoutDeadline(
                        assignments);
                response.getWriter().write(JSONEncoder.encodeAssignments(
                        requestedAssignments));
                break;
            case "getAssignmentsWithDeadline":
                requestedAssignments = Utils.getAssignmentsWithDeadline(
                        assignments);
                response.getWriter().write(JSONEncoder.encodeAssignments(
                        requestedAssignments));
                break;
            // setters
            case "addTimeSlot":
                addTimeSlot(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "updateTimeSlot":
                updateTimeSlot(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "deleteTimeSlot":
                deleteTimeSlot(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "addAssignment":
                addAssignment(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "updateAssignment":
                updateAssignment(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "deleteAssignment":
                deleteAssignment(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "addProject":
                addProject(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "updateProject":
                updateProject(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "deleteProject":
                deleteProject(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            // file related requests
            case "newRegistration":
                processNewRegistration(request);
                response.getWriter().write("{\"projects\":" + JSONEncoder.encodeProjects(projects) + ",\"assignments\":" + JSONEncoder.encodeAssignments(Utils.sortAssignmentsByDeadline(assignments)) + "}");
                break;
            case "editRegistrationDetails":
                processRegistrationDetailChange(request);
                break;
            case "openRegistration":
                String registrationData = openRegistration(request);
                break;
            case "saveRegistration":
                saveRegistration();
                break;
            case "getDefaultWorkingDirectory":
                if (ServletUtils.defaultWorkingDirectory == null) { ServletUtils.loadSettings(); }
                response.getWriter().write("{\"defaultWorkingDirectory\":\"" + ServletUtils.defaultWorkingDirectory.replace("\\", "\\\\") + "\"}");
                break;
            case "setDefaultWorkingDirectory":
                processChangedDefaultWorkingDirectory(request);
                break;
            case "getSettings":
                String settings = getSettings();
                response.getWriter().write(settings);
                break;
            case "getRegistrationProperties":
                String properties = ServletUtils.readProjectFromFile();
                response.getWriter().write(properties);
                break;
            case "directoryExists":
                Boolean exists = ServletUtils.directoryExists(request);
                response.getWriter().write("{\"directoryExists\":" + exists + "}");
                break;
            case "getPrevOpened":
                ServletUtils.loadSettings();
                String prevOpenedString = ServletUtils.prevOpened.toString();
                response.getWriter().write(prevOpenedString);
                break;
            default:
                break;
        }
        requestedAssignments = null;
    }
    
private String getHoursGrouped(){
    HashMap<String, ArrayList<TimeSlot>> groupedHours = Utils.getUnwrittenHoursGroupedByProject(assignments);
    String result = "{";
    for (String i : groupedHours.keySet()){
        result += "\"" + i + "\":" + JSONEncoder.encodeTimeSlots(groupedHours.get(i)) + ",";
    }
    result = result.substring(0, result.length() - 1) + "}";
    return result;
}

private String getUnwrittenHoursGrouped(){
    HashMap<String, ArrayList<TimeSlot>> groupedUnwrittenHours = Utils.getUnwrittenHoursGroupedByProject(assignments);
    String result = "{";
    for (String i : groupedUnwrittenHours.keySet()){
        result += "\"" + i + "\":" + JSONEncoder.encodeTimeSlots(groupedUnwrittenHours.get(i)) + ",";
    }
    result = result.substring(0, result.length() - 1) + "}";
    return result;
}

private void addTimeSlot (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Pair<String,TimeSlot> newTimeSlotInfo = ServletSetterDecoder.decodeNewTimeSlot(requestBody);
    String assignmentId = newTimeSlotInfo.getValue0();
    TimeSlot newTimeSlot = newTimeSlotInfo.getValue1();
    if (newTimeSlot != null) {
        for (int i = 0; i < assignments.size(); i++){
            if (assignmentId.equals(assignments.get(i).getAssignmentId())){
                Assignment newAssignment = assignments.get(i);
                newAssignment.addHoursWorked(newTimeSlot);
                assignments.set(i, newAssignment);
                return;
            }
        }
    }
}

private void updateTimeSlot (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Pair<String,TimeSlot> updatedTimeSlotInfo = ServletSetterDecoder.decodeUpdatedTimeSlot(requestBody);
    String assignmentId = updatedTimeSlotInfo.getValue0();
    TimeSlot updatedTimeSlot = updatedTimeSlotInfo.getValue1();

    if (updatedTimeSlot != null) {
        for (int i = 0; i < assignments.size(); i++){
            if (assignmentId.equals(assignments.get(i).getAssignmentId())){
                Assignment newAssignment = assignments.get(i);
                newAssignment.updateHoursWorked(updatedTimeSlot);
                assignments.set(i, newAssignment);
                return;
            }
        }
    }
}

private void deleteTimeSlot (HttpServletRequest request){
    String assignmentId = request.getParameter("assignmentId");
    String timeSlotId = request.getParameter("timeSlotId");
    for (int i = 0; i < assignments.size(); i++){
        if (assignmentId.equals(assignments.get(i).getAssignmentId())){
            Assignment newAssignment = assignments.get(i);
            newAssignment.removeHoursWorked(timeSlotId);
            assignments.set(i, newAssignment);
            return;
        }
    }
}

private void addAssignment (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Assignment newAssignment = ServletSetterDecoder.decodeNewAssignment(requestBody);
    if (newAssignment != null) { assignments.add(newAssignment); }
}

private void updateAssignment (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Assignment updatedAssignment = JSONDecoder.decodeAssignment(requestBody);
    for (int i = 0; i < assignments.size(); i++){
        if (updatedAssignment.getAssignmentId().equals(assignments.get(i).getAssignmentId())) {
            assignments.set(i, updatedAssignment);
            return;
        }
    }
}

private void deleteAssignment (HttpServletRequest request){
    String assignmentId = request.getParameter("assignmentId");
    for (int i = 0; i < assignments.size(); i++){
        if (assignmentId.equals(assignments.get(i).getAssignmentId())) {
            assignments.remove(i);
            return;
        }
    }
}

private void addProject (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Project newProject = ServletSetterDecoder.decodeNewProject(requestBody);
    if (newProject != null) { projects.add(newProject); }
}

private void updateProject (HttpServletRequest request) throws IOException{
    String requestBody = ServletUtils.getBody(request);
    Project updatedProject = JSONDecoder.decodeProject(requestBody);
    for (int i = 0; i < projects.size(); i++){
        if (updatedProject.getProjectId().equals(projects.get(i).getProjectId())){
            projects.set(i, updatedProject);
            return;
        }
    }
}

private void deleteProject (HttpServletRequest request){
    String projectId = request.getParameter("projectId");
    for (int i = 0; i < projects.size(); i++){
        if (projectId.equals(projects.get(i).getProjectId())){
            projects.remove(i);
            // reset assignments that are part of this project
            for (int j = 0; j < assignments.size(); j++){
                if (projectId.equals(assignments.get(j).getCorrespondingProjectId())){
                    Assignment newAssignment = assignments.get(j);
                    newAssignment.setCorrespondingProjectId(null);
                    assignments.set(j, newAssignment);
                }
            }
            return;
        }
    }
}

private void processNewRegistration(HttpServletRequest request) throws IOException {
    String requestBody = ServletUtils.getBody(request);
    requestBody = requestBody.substring(1, requestBody.length() - 1);
    Pattern newRegistrationPattern = Pattern.compile("\"registrationName\":\"(.*)\",\"workingDir\":\"(.*)\"");
    Matcher newRegistrationMatcher = newRegistrationPattern.matcher(requestBody);
    Boolean newRegistrationMatchFound = newRegistrationMatcher.find();
    if (newRegistrationMatchFound){
        projects = new ArrayList<Project>();
        assignments = new ArrayList<Assignment>();
        ServletUtils.registrationName = newRegistrationMatcher.group(1);
        String workingDirStr = newRegistrationMatcher.group(2);
        ServletUtils.workingDir = Paths.get(workingDirStr);
        
        String body = "{\"registrationName\":\"" + ServletUtils.registrationName + "\",\"workingDir\":\"" + workingDirStr.replace("\\", "\\\\") + "\",\"projects\":[],\"assignments\":[]}";

        String fileLocation = ServletUtils.workingDir + "\\" + ServletUtils.registrationName + ".json";

        FileWriter file = new FileWriter(fileLocation);
        file.write(body);
        file.close();
    } 
}

private void processRegistrationDetailChange(HttpServletRequest request) throws IOException {
    String requestBody = ServletUtils.getBody(request);
    requestBody = requestBody.substring(1, requestBody.length() - 1);
    Pattern detailChangePattern = Pattern.compile("\"registrationName\":\"(.*)\",\"workingDir\":\"(.*)\"");
    Matcher detailChangeMatcher = detailChangePattern.matcher(requestBody);
    Boolean detailChangeMatchFound = detailChangeMatcher.find();
    if (detailChangeMatchFound){
        ServletUtils.registrationName = detailChangeMatcher.group(1);
        ServletUtils.workingDir = Paths.get(detailChangeMatcher.group(2));
    }
}

private String openRegistration(HttpServletRequest request) throws IOException {
    Triplet<String, String, String> result = ServletUtils.openProject(request);
    if (result.getValue2().equals("File opened successfully")) {
        projects = JSONDecoder.decodeProjects(result.getValue1());
        assignments = JSONDecoder.decodeAssignments(result.getValue0());
    } 
    return result.getValue2();
}

private void saveRegistration() throws IOException {
    String encodedProjects = JSONEncoder.encodeProjects(projects);
    String encodedAssignments = JSONEncoder.encodeAssignments(assignments);
    
    String body = "{\"registrationName\":\"" + ServletUtils.registrationName + "\",\"workingDir\":\"" + ServletUtils.workingDir.toString().replace("\\", "\\\\") + "\",\"projects\":" + encodedProjects + ",\"assignments\":" + encodedAssignments + "}";

    String fileLocation = ServletUtils.workingDir + "\\" + ServletUtils.registrationName + ".json";

    FileWriter file = new FileWriter(fileLocation);
    file.write(body);
    file.close();
}

private String getSettings() throws IOException {
    if (ServletUtils.settings == null) { ServletUtils.loadSettings(); }
    return ServletUtils.settings;
}

private void processChangedDefaultWorkingDirectory(HttpServletRequest request) throws IOException {
    String folder = ServletUtils.getBody(request).replace("\"", "");
    ServletUtils.defaultWorkingDirectory = folder;
    ServletUtils.writeSettings();
}

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
