/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.javatuples.Pair;

/**
 *
 * @author RLvan
 */
public class ServletSetterDecoder {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    
    public static Pair<String, TimeSlot> decodeNewTimeSlot (String encodedTimeSlot) throws JsonProcessingException {
        Pattern timeSlotPattern = Pattern.compile("\"assignmentId\":\"(.*)\",\"startTime\":\"(.*)\",\"endTime\":\"(.*)\"");
        Matcher timeSlotMatcher = timeSlotPattern.matcher(encodedTimeSlot);
        Boolean timeSlotMatchFound = timeSlotMatcher.find();
        if (timeSlotMatchFound){
            String assignmentId = timeSlotMatcher.group(1);
            LocalDateTime startTime = LocalDateTime.parse(timeSlotMatcher.group(2), formatter);
            LocalDateTime endTime = LocalDateTime.parse(timeSlotMatcher.group(3), formatter);
            TimeSlot newTimeSlot = new TimeSlot(startTime, endTime);
            return new Pair<>(assignmentId, newTimeSlot);
        }
        return null; 
    }
    
    public static Pair<String, TimeSlot> decodeUpdatedTimeSlot (String encodedTimeSlot) throws JsonProcessingException {
        Pattern timeSlotPattern = Pattern.compile("\"assignmentId\":\"(.*)\",\"timeSlotId\":\"(.*)\",\"startTime\":\"(.*)\",\"endTime\":\"(.*)\",\"hoursWritten\":\"(.*)\"");
        Matcher timeSlotMatcher = timeSlotPattern.matcher(encodedTimeSlot);
        Boolean timeSlotMatchFound = timeSlotMatcher.find();
        if (timeSlotMatchFound){
            String assignmentId = timeSlotMatcher.group(1);
            String timeSlotId = timeSlotMatcher.group(2);
            LocalDateTime startTime = LocalDateTime.parse(timeSlotMatcher.group(3), formatter);
            LocalDateTime endTime = LocalDateTime.parse(timeSlotMatcher.group(4), formatter);
            Boolean hoursWritten = Boolean.parseBoolean(timeSlotMatcher.group(5));
            TimeSlot newTimeSlot = new TimeSlot(timeSlotId, startTime, endTime, hoursWritten);
            return new Pair<>(assignmentId, newTimeSlot);
        }
        return null; 
    }
    
    public static Project decodeNewProject (String encodedProject) throws JsonProcessingException {
        Pattern projectPattern = Pattern.compile("\"projectName\":\"(.*)\",\"projectCode\":\"(.*)\",\"projectDisplayColor\":\"(.*)\"");
        Matcher projectMatcher = projectPattern.matcher(encodedProject);
        Boolean projectMatchFound = projectMatcher.find();
        if (projectMatchFound){
            String projectName = projectMatcher.group(1);
            String projectCode = projectMatcher.group(2);
            String projectDisplayColor = projectMatcher.group(3);
            return new Project(projectName, projectCode, projectDisplayColor);
        }
        return null;
    }
    
    public static Assignment decodeNewAssignment (String encodedAssignment) throws JsonProcessingException {
        encodedAssignment = encodedAssignment.substring(1, encodedAssignment.length() - 1);
        Pattern assignmentPattern = Pattern.compile("\"assignmentName\":\"(.*)\",\"assignmentDetails\":\"(.*)\",\"supervisor\":\"(.*)\",\"correspondingProjectId\":\"(.*)\",\"deadline\":\"(.*)\"");
        Matcher assignmentMatcher = assignmentPattern.matcher(encodedAssignment);
        Boolean assignmentMatchFound = assignmentMatcher.find();
        
        if (assignmentMatchFound){
            String assignmentName = assignmentMatcher.group(1);
            String assignmentDetails = assignmentMatcher.group(2);
            String supervisor = assignmentMatcher.group(3);
            String correspondingProjectId = assignmentMatcher.group(4);
            LocalDateTime deadline = (assignmentMatcher.group(5).equals("null")) ? null : LocalDateTime.parse(assignmentMatcher.group(5), formatter);
            
            return new Assignment(assignmentName, assignmentDetails, supervisor, correspondingProjectId, deadline);
        }
        return null;
    }
    
}
