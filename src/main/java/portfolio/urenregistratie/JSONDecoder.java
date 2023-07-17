/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author RLvan
 */
public class JSONDecoder {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    public static Assignment decodeAssignment (String encodedAssignment) throws JsonProcessingException {
        encodedAssignment = encodedAssignment.substring(1, encodedAssignment.length() - 1);
        Pattern assignmentPattern = Pattern.compile("\"assignmentId\":\"(.*)\",\"assignmentName\":\"(.*)\",\"assignmentDetails\":\"(.*)\",\"supervisor\":\"(.*)\",\"correspondingProjectId\":\"(.*)\",\"hoursWorked\":(.*),\"assignmentState\":\"(.*)\",\"deadline\":\"(.*)\"");
        Matcher assignmentMatcher = assignmentPattern.matcher(encodedAssignment);
        Boolean assignmentMatchFound = assignmentMatcher.find();
        
        if (assignmentMatchFound){
            String assignmentId = assignmentMatcher.group(1);
            String assignmentName = assignmentMatcher.group(2);
            String assignmentDetails = assignmentMatcher.group(3);
            String supervisor = assignmentMatcher.group(4);
            String correspondingProjectId = assignmentMatcher.group(5);
            String hoursWorkedString = assignmentMatcher.group(6);
            String assignmentStateString = assignmentMatcher.group(7);
            LocalDateTime deadline = (assignmentMatcher.group(8).equals("null")) ? null : LocalDateTime.parse(assignmentMatcher.group(8), formatter);
            
            ArrayList<TimeSlot> hoursWorked = decodeTimeSlots(hoursWorkedString);
            Utils.AssignmentState assignmentState = null;
            switch(assignmentStateString){
                case "NOT_STARTED":
                    assignmentState = Utils.AssignmentState.NOT_STARTED;
                    break;
                case "STARTED":
                    assignmentState = Utils.AssignmentState.STARTED;
                    break;
                case "INSUFFICIENT_INFORMATION":
                    assignmentState = Utils.AssignmentState.INSUFFICIENT_INFORMATION;
                    break;
                case "FINISHED":
                    assignmentState = Utils.AssignmentState.FINISHED;
                    break;
                default:
                    assignmentState = Utils.AssignmentState.NOT_STARTED;
                    break;
            }
            
            return new Assignment(assignmentId, assignmentName, assignmentDetails, supervisor, correspondingProjectId, hoursWorked, assignmentState, deadline);
        }
        return null;
    }
    
    public static ArrayList<Assignment> decodeAssignments (String encodedAssignments) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode assignmentList = (ArrayNode) objectMapper.readTree(encodedAssignments);
        ArrayList<Assignment> assignments = new ArrayList<>();
        for (int i = 0; i < assignmentList.size(); i++){
            assignments.add(decodeAssignment(assignmentList.get(i).toString()));
        }
        return assignments; 
    }
    
    public static Project decodeProject (String encodedProject) throws JsonProcessingException {
        Pattern projectPattern = Pattern.compile("\"projectId\":\"(.*)\",\"projectName\":\"(.*)\",\"projectCode\":\"(.*)\",\"projectDisplayColor\":\"(.*)\"");
        Matcher projectMatcher = projectPattern.matcher(encodedProject);
        Boolean projectMatchFound = projectMatcher.find();
        if (projectMatchFound){
            String projectId = projectMatcher.group(1);
            String projectName = projectMatcher.group(2);
            String projectCode = projectMatcher.group(3);
            String projectDisplayColor = projectMatcher.group(4);
            return new Project(projectId, projectName, projectCode, projectDisplayColor);
        }
        return null;
    }
    
    public static ArrayList<Project> decodeProjects (String encodedProjects) throws JsonProcessingException { 
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode projectList = (ArrayNode) objectMapper.readTree(encodedProjects);
        ArrayList<Project> projects = new ArrayList<>();
        for (int i = 0; i < projectList.size(); i++){
            projects.add(decodeProject(projectList.get(i).toString()));
        }
        return projects;
    }
    
    public static TimeSlot decodeTimeSlot (String encodedTimeSlot) throws JsonProcessingException { 
        Pattern timeSlotPattern = Pattern.compile("\"timeSlotId\":\"(.*)\",\"startTime\":\"(.*)\",\"endTime\":\"(.*)\",\"hoursWritten\":\"(.*)\"");
        Matcher timeSlotMatcher = timeSlotPattern.matcher(encodedTimeSlot);
        Boolean timeSlotMatchFound = timeSlotMatcher.find();
        if (timeSlotMatchFound){
            String timeSlotId = timeSlotMatcher.group(1);
            LocalDateTime startTime = LocalDateTime.parse(timeSlotMatcher.group(2), formatter);
            LocalDateTime endTime = LocalDateTime.parse(timeSlotMatcher.group(3), formatter);
            Boolean hoursWritten = Boolean.parseBoolean(timeSlotMatcher.group(4));
            return new TimeSlot(timeSlotId, startTime, endTime, hoursWritten);
        }
        return null; }
    
    public static ArrayList<TimeSlot> decodeTimeSlots (String encodedTimeSlots) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode timeSlotList = (ArrayNode) objectMapper.readTree(encodedTimeSlots);
        ArrayList<TimeSlot> timeSlots = new ArrayList<>();
        for (int i = 0; i < timeSlotList.size(); i++){
            timeSlots.add(decodeTimeSlot(timeSlotList.get(i).toString()));
        }
        return timeSlots;
    }
}
