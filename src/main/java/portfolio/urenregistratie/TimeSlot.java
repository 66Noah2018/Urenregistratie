/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author RLvan
 */
public class TimeSlot {
    private String timeSlotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Boolean hoursWritten;
    
    public TimeSlot (LocalDateTime startTime, LocalDateTime endTime){
        UUID newUuid = randomUUID();
        this.timeSlotId = newUuid.toString();
        this.startTime = startTime;
        this.endTime = endTime; // can be null!
        this.hoursWritten = false;
    }
    
    public TimeSlot (String timeSlotId, LocalDateTime startTime, LocalDateTime endTime){
        this.timeSlotId = timeSlotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursWritten = false;
    }
    
    public TimeSlot (String timeSlotId, LocalDateTime startTime, LocalDateTime endTime, Boolean hoursWritten){
        this.timeSlotId = timeSlotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.hoursWritten = hoursWritten;
    }
    
    public String getTimeSlotId() { return this.timeSlotId; }
    public LocalDateTime getStartTime() { return this.startTime; }
    public LocalDateTime getEndTime() { return this.endTime; }
    public Boolean getHoursWritten() { return this.hoursWritten; }
    
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime (LocalDateTime endTime) { this.endTime = endTime; }
    public void setHoursWritten (Boolean hoursWritten) { this.hoursWritten = hoursWritten; }
}
