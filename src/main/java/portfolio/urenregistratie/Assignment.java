/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author RLvan
 */

class SortByStartTime implements Comparator<TimeSlot>{
    @Override
    public int compare(TimeSlot a, TimeSlot b){
        return a.getStartTime().compareTo(b.getStartTime());
    }
}

public class Assignment {
    private String assignmentId;
    private String assignmentName;
    private String assignmentDetails;
    private String supervisor;
    private String correspondingProjectId;
    private ArrayList<TimeSlot> hoursWorked;
    private Utils.AssignmentState assignmentState; // should NEVER be null!
    private LocalDateTime deadline;
    
    public Assignment (String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId) {
        UUID newUuid = randomUUID();
        this.assignmentId = newUuid.toString();
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = new ArrayList<>();
        this.assignmentState = Utils.AssignmentState.NOT_STARTED;
        this.deadline = null;
    }
    
    public Assignment (String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId, LocalDateTime deadline) {
        UUID newUuid = randomUUID();
        this.assignmentId = newUuid.toString();
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = new ArrayList<>();
        this.assignmentState = Utils.AssignmentState.NOT_STARTED;
        this.deadline = deadline;
    }
    
    public Assignment (String assignmentId, String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId, ArrayList<TimeSlot> hoursWorked, Utils.AssignmentState assignmentState) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = hoursWorked;
        this.assignmentState = assignmentState;
        this.deadline = null;
    }
    
    public Assignment (String assignmentId, String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId, ArrayList<TimeSlot> hoursWorked, Utils.AssignmentState assignmentState, LocalDateTime deadline) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = hoursWorked;
        this.assignmentState = assignmentState;
        this.deadline = deadline;
    }
    
    public Assignment (String assignmentId, String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId, Utils.AssignmentState assignmentState) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = new ArrayList<>();
        this.assignmentState = assignmentState;
        this.deadline = null;
    }
    
    public Assignment (String assignmentId, String assignmentName, String assignmentDetails, String supervisor, String correspondingProjectId, Utils.AssignmentState assignmentState, LocalDateTime deadline) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.assignmentDetails = assignmentDetails;
        this.supervisor = supervisor;
        this.correspondingProjectId = correspondingProjectId;
        this.hoursWorked = new ArrayList<>();
        this.assignmentState = assignmentState;
        this.deadline = deadline;
    }
    
    public String getAssignmentId() { return this.assignmentId; }
    public String getAssignmentName() { return this.assignmentName; }
    public String getAssignmentDetails() { return this.assignmentDetails; }
    public String getSupervisor() { return this.supervisor; }
    public String getCorrespondingProjectId() { return this.correspondingProjectId; }
    public ArrayList<TimeSlot> getHoursWorked() { return this.hoursWorked; }
    public String getAssignmentState() { return this.assignmentState.name(); }
    public LocalDateTime getDeadline() { return this.deadline; }
    
    public void setAssignmentName (String assignmentName) { this.assignmentName = assignmentName; }
    public void setAssignmentDetails (String assignmentDetails) { this.assignmentDetails = assignmentDetails; }
    public void setSupervisor ( String supervisor) { this.supervisor = supervisor; }
    public void setCorrespondingProjectId ( String correspondingProjectId) { this.correspondingProjectId = correspondingProjectId; }
    public void setAssignmentState (String assignmentState) { 
        switch(assignmentState){
            case "NOT_STARTED":
                this.assignmentState = Utils.AssignmentState.NOT_STARTED;
                break;
            case "STARTED":
                this.assignmentState = Utils.AssignmentState.STARTED;
                break;
            case "FINISHED":
                this.assignmentState = Utils.AssignmentState.FINISHED;
                break;
            case "INSUFFICIENT_INFORMATION":
                this.assignmentState = Utils.AssignmentState.INSUFFICIENT_INFORMATION;
                break;
            default:
                this.assignmentState = Utils.AssignmentState.NOT_STARTED;
                break;
        }
    }
    public void setDeadline (LocalDateTime deadline) { this.deadline = deadline; }
    
    public void addHoursWorked (TimeSlot newHoursWorked) { this.hoursWorked.add(newHoursWorked); }
    public void removeHoursWorked (String removedHoursWorkedId) { 
        for (int i = 0; i < this.hoursWorked.size(); i++){
            if (this.hoursWorked.get(i).getTimeSlotId().equals(removedHoursWorkedId)) {
                this.hoursWorked.remove(i);
                return;
            }
        }
    }
    public void updateHoursWorked(TimeSlot updatedHoursWorked){
        for (int i = 0; i < this.hoursWorked.size(); i++){
            if (this.hoursWorked.get(i).getTimeSlotId().equals(updatedHoursWorked.getTimeSlotId())) {
                this.hoursWorked.set(i, updatedHoursWorked);
                return;
            }
        }
    }

    public void sortTimeSlots() {
        Collections.sort(this.hoursWorked, new SortByStartTime());
    }
}
