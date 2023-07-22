/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author RLvan
 */
class SortByDeadline implements Comparator<Assignment>{
    @Override
    public int compare(Assignment a, Assignment b){
        return a.getDeadline().compareTo(b.getDeadline());
    }
}

public class Utils {
    public static String sourcePath = null;
    
    enum AssignmentState {
        NOT_STARTED,
        STARTED,
        FINISHED,
        INSUFFICIENT_INFORMATION
    }
    
    public static Project getProjectById(ArrayList<Project> projects, String projectId) {
        return projects.stream().filter(project -> projectId.equals(project.getProjectId())).findFirst().orElse(null);
    }
    
    public static ArrayList<TimeSlot> getHoursByProjectId(ArrayList<Assignment> assignments, String projectId){
        ArrayList<TimeSlot> hoursWritten = new ArrayList<>();
        for (Assignment assignment : assignments){
            if (projectId.equals(assignment.getCorrespondingProjectId())) { hoursWritten.addAll(assignment.getHoursWorked()); }
        }
        return hoursWritten;
    }
    
    public static HashMap<String, ArrayList<TimeSlot>> getHoursGroupedByProject(ArrayList<Assignment> assignments){
        HashMap<String, ArrayList<TimeSlot>> groupedHours = new HashMap<>();
        ArrayList<String> projectIds = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (!projectIds.contains(assignment.getCorrespondingProjectId())) { projectIds.add(assignment.getCorrespondingProjectId()); }
        }
        
        for (String projectId : projectIds){
            ArrayList<TimeSlot> hoursForProject = getHoursByProjectId(assignments, projectId);
            groupedHours.put(projectId, hoursForProject);
        }
        return groupedHours;
    }
    
    public static ArrayList<TimeSlot> getUnwrittenHoursByProjectId(ArrayList<Assignment> assignments, String projectId){
        ArrayList<TimeSlot> hoursWritten = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++){
            if (projectId.equals(assignments.get(i).getCorrespondingProjectId())) {
                ArrayList<TimeSlot> hoursForProject = assignments.get(i).getHoursWorked();
                for (TimeSlot timeslot : hoursForProject) {
                    if (!timeslot.getHoursWritten()) { hoursWritten.add(timeslot); }
                }
            }
        }
        return hoursWritten;
    }
    
    public static HashMap<String, ArrayList<TimeSlot>> getUnwrittenHoursGroupedByProject(ArrayList<Assignment> assignments){
        HashMap<String, ArrayList<TimeSlot>> groupedHours = new HashMap<>();
        ArrayList<String> projectIds = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (!projectIds.contains(assignment.getCorrespondingProjectId())) { projectIds.add(assignment.getCorrespondingProjectId()); }
        }
        
        for (String projectId : projectIds){
            ArrayList<TimeSlot> hoursForProject = getUnwrittenHoursByProjectId(assignments, projectId);
            groupedHours.put(projectId, hoursForProject);
        }
        return groupedHours;
    }

    public static ArrayList<Assignment> getAssignmentsByState(ArrayList<Assignment> assignments, ArrayList<String> states){ // use strings to avoid extra conversions
        ArrayList<Assignment> selectedAssignments = new ArrayList<>();
        for (Assignment assignment : assignments){
            if (states.contains(assignment.getAssignmentState())) { selectedAssignments.add(assignment); }
        }
        return selectedAssignments;
    }
    
    public static ArrayList<Assignment> getAssignmentsByProjectId (ArrayList<Assignment> assignments, String projectId) {
        ArrayList<Assignment> selectedAssignments = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (projectId.equals(assignment.getCorrespondingProjectId())) { selectedAssignments.add(assignment); }
        }
        return selectedAssignments;
    }
    
    public static ArrayList<Assignment> getAssignmentsBySupervisor (ArrayList<Assignment> assignments, String supervisor) {
        ArrayList<Assignment> selectedAssignments = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (supervisor.equals(assignment.getSupervisor())) { selectedAssignments.add(assignment); }
        }
        return selectedAssignments;
    }
    
    public static ArrayList<String> getSupervisors(ArrayList<Assignment> assignments) {
        ArrayList<String> supervisors = new ArrayList<>();
        for (Assignment assignment : assignments){
            if (!supervisors.contains(assignment.getSupervisor())) { supervisors.add(assignment.getSupervisor()); }
        }
        return supervisors;
    }
    
    public static ArrayList<Assignment> sortAssignmentsByDeadline(ArrayList<Assignment> assignments){
        ArrayList<Assignment> sortedAssignments = new ArrayList<>();
        ArrayList<Assignment> assignmentsWithDeadline = new ArrayList<>();
        ArrayList<Assignment> assignmentsWithoutDeadline = new ArrayList<>();
        for (Assignment assignment : assignments) {
            if (assignment.getDeadline() != null) { assignmentsWithDeadline.add(assignment); } 
            else { assignmentsWithoutDeadline.add(assignment); }
        }
        
        sortedAssignments.addAll(assignmentsWithDeadline);
        Collections.sort(sortedAssignments, new SortByDeadline());
        sortedAssignments.addAll(assignmentsWithoutDeadline);
        return sortedAssignments;
    }
    
    public static ArrayList<Assignment> getAssignmentsWithoutDeadline(ArrayList<Assignment> assignments){
        ArrayList<Assignment> assignmentsWithoutDeadline = new ArrayList<>();
        for (Assignment assignment : assignments){
            if (assignment.getDeadline() == null) { assignmentsWithoutDeadline.add(assignment); }
        }
        return assignmentsWithoutDeadline;
    }
    
    public static ArrayList<Assignment> getAssignmentsWithDeadline(ArrayList<Assignment> assignments){
        ArrayList<Assignment> assignmentsWithDeadline = new ArrayList<>();
        for (Assignment assignment : assignments){
            if (assignment.getDeadline() != null) { assignmentsWithDeadline.add(assignment); }
        }
        Collections.sort(assignmentsWithDeadline, new SortByDeadline());
        return assignmentsWithDeadline;
    }
}
