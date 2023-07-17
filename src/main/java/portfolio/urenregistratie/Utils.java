/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.util.ArrayList;

/**
 *
 * @author RLvan
 */
public class Utils {
//    public static ArrayList<Assignment> assignments = new ArrayList<>();
//    public static ArrayList<Project> projects = new ArrayList<>(); TODO move to servlet
    
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
}
