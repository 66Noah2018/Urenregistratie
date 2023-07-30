/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package portfolio.urenregistratie;

import java.util.UUID;
import static java.util.UUID.randomUUID;

/**
 *
 * @author RLvan
 */
public class Project {
    private String projectId;
    private String projectName;
    private String projectCode;
    
    public Project (String projectName, String projectCode){
        UUID newUuid = randomUUID();
        this.projectId = newUuid.toString();
        this.projectName = projectName;
        this.projectCode = projectCode;
    }
    
    public Project (String projectId, String projectName, String projectCode){
        this.projectId = projectId;
        this.projectName = projectName;
        this.projectCode = projectCode;
    }
    
    public String getProjectId() { return this.projectId; }
    public String getProjectName() { return this.projectName; }
    public String getProjectCode() { return this.projectCode; }
    
    public void setProjectName (String projectName) { this.projectName = projectName; }
    public void setProjectCode (String projectCode) { this.projectCode = projectCode; }
    
}
