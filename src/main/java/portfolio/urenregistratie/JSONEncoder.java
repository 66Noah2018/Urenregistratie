/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 *
 * @author RLvan
 */
public class JSONEncoder {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    public static String encodeAssignment(Assignment assignment){
        LocalDateTime deadline = assignment.getDeadline();
        String deadlineString = "null";
        if (deadline != null) { deadlineString = deadline.format(formatter); }
        String result = "{\"assignmentId\":\"" 
                + assignment.getAssignmentId()
                + "\",\"assignmentName\":\""
                + assignment.getAssignmentName()
                + "\",\"assignmentDetails\":\""
                + assignment.getAssignmentDetails()
                + "\",\"supervisor\":\""
                + assignment.getSupervisor()
                + "\",\"correspondingProjectId\":\""
                + assignment.getCorrespondingProjectId()
                + "\",\"hoursWorked\":"
                + encodeTimeSlots(assignment.getHoursWorked())
                + ",\"assignmentState\":\""
                + assignment.getAssignmentState()
                + "\",\"deadline\":\""
                + deadlineString
                + "\"}";
        return result; 
    }
    
    public static String encodeAssignments(ArrayList<Assignment> assignments){ 
        if (assignments.isEmpty()) { return "[]"; }
        String result = "[";
        
        for (Assignment assignment : assignments){
            result += encodeAssignment(assignment) + ",";
        }
        result = result.substring(0, result.length() - 1) + "]";
        
        return result; 
    }
    
    public static String encodeProject(Project project){ 
        String result = "{\"projectId\":\""
                + project.getProjectId()
                + "\",\"projectName\":\""
                + project.getProjectName()
                + "\",\"projectCode\":\""
                + project.getProjectCode()
                + "\",\"projectDisplayColor\":\""
                + project.getProjectDisplayColor()
                + "\"}";
        return result; 
    }
    
    public static String encodeProjects(ArrayList<Project> projects){ 
        if (projects.isEmpty()) { return "[]"; }
        String result = "[";
        
        for (Project project : projects){
            result += encodeProject(project) + ",";
        }
        result = result.substring(0, result.length() - 1) + "]";
        
        return result; 
    }
    
    public static String encodeTimeSlot(TimeSlot timeSlot) { 
        String endTime = "null";
        if (timeSlot.getEndTime() != null) { endTime = timeSlot.getEndTime().format(formatter); }
        String result = "{\"timeSlotId\":\""
                + timeSlot.getTimeSlotId()
                + "\",\"startTime\":\""
                + timeSlot.getStartTime().format(formatter)
                + "\",\"endTime\":\""
                + endTime
                + "\",\"hoursWritten\":\""
                + timeSlot.getHoursWritten().toString()
                + "\"}";
        return result; 
    }
    
    public static String encodeTimeSlots(ArrayList<TimeSlot> timeSlots) { 
        if (timeSlots.isEmpty()) { return "[]"; }
        String result = "[";
        
        for (TimeSlot timeSlot : timeSlots){
            result += encodeTimeSlot(timeSlot) + ",";
        }
        result = result.substring(0, result.length() - 1) + "]";
        return result; 
    }
}
