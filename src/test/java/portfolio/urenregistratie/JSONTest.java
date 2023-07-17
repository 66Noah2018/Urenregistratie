/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.assertj.core.api.SoftAssertions;

/**
 *
 * @author RLvan
 */
public class JSONTest {
    
    public JSONTest() {
    }
    private static Project testProject1;
    private static Project testProject2;
    private static String project1Id;
    private static String project2Id;
    private static final String project1Name = "CAPABLE";
    private static final String project1Code = "C1234";
    private static final String project1DisplayColor = "red";
    private static final String project2Name = "testP";
    private static final String project2Code = "test5678";
    private static final String project2DisplayColor = "blue";
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static String assignmentId2;
    private static String assignmentId3;
    private static final String assignmentName1 = "assgName1";
    private static final String assignmentName2 = "assgName2";
    private static final String assignmentName3 = "assgName3";
    private static final String assignmentDetails1 = "Some very long string";
    private static final String assignmentDetails2 = "With a lot of details";
    private static final String assignmentDetails3 = "About a certain assignment";
    private static final String assignmentSupervisor1 = "sup1";
    private static final String assignmentSupervisor2 = "sup2";
    private static final String assignmentSupervisor3 = "sup3";
    private static final String assignmentProjectId1 = "123";
    private static final String assignmentProjectId2 = "456";
    private static final String assignmentProjectId3 = "789";
    private static final Utils.AssignmentState assignmentState2 = Utils.AssignmentState.NOT_STARTED;
    private static final Utils.AssignmentState assignmentState3 = Utils.AssignmentState.INSUFFICIENT_INFORMATION;
    private static LocalDateTime time1;
    private static LocalDateTime time2;
    private static LocalDateTime time3;
    private static LocalDateTime time4;
    private static LocalDateTime time5;
    private static LocalDateTime time6;
    private static Assignment testAssignment1;
    private static Assignment testAssignment2;
    private static Assignment testAssignment3;
    private static ArrayList<TimeSlot> unorderedTimeSlotList = new ArrayList<>();
    private static ArrayList<TimeSlot> orderedTimeSlotList = new ArrayList<>();
    private static ArrayList<TimeSlot> singleTimeSlot = new ArrayList<>();
    private static ArrayList<TimeSlot> shorterTimeSlotList = new ArrayList<>();
    private static TimeSlot testTimeSlot1;
    private static TimeSlot testTimeSlot2;
    private static TimeSlot testTimeSlot3;
    private static LocalDateTime deadline1;
    private static ArrayList<Assignment> assignments = new ArrayList<>();
    private static ArrayList<Project> projects = new ArrayList<>();
    
    @BeforeClass
    public static void setUpClass() {
        UUID newUuid = randomUUID();
        project1Id = newUuid.toString();
        UUID newUuid2 = randomUUID();
        project2Id = newUuid2.toString();
        testProject1 = new Project(project1Id, project1Name, project1Code, project1DisplayColor);
        testProject2 = new Project(project2Id, project2Name, project2Code, project2DisplayColor);
        projects.add(testProject1);
        projects.add(testProject2);
        
        UUID newUuid3 = randomUUID();
        assignmentId2 = newUuid3.toString();
        UUID newUuid4 = randomUUID();
        assignmentId3 = newUuid4.toString();
        time1 = LocalDateTime.parse("14-07-2022 17:00:05", formatter);
        time2 = LocalDateTime.parse("14-07-2022 19:00:10", formatter);
        time3 = LocalDateTime.parse("09-02-2093 21:30:32", formatter);
        time4 = LocalDateTime.parse("09-02-2093 23:00:00", formatter);
        time5 = LocalDateTime.parse("03-08-1983 11:55:30", formatter);
        time6 = LocalDateTime.parse("03-08-1983 14:00:00", formatter);
        testTimeSlot1 = new TimeSlot(time1, time2);
        testTimeSlot2 = new TimeSlot(time3, time4);
        testTimeSlot3 = new TimeSlot(time5, time6);
        unorderedTimeSlotList.add(testTimeSlot1);
        unorderedTimeSlotList.add(testTimeSlot2);
        unorderedTimeSlotList.add(testTimeSlot3);
        orderedTimeSlotList.add(testTimeSlot3);
        orderedTimeSlotList.add(testTimeSlot1);
        orderedTimeSlotList.add(testTimeSlot2);
        singleTimeSlot.add(testTimeSlot1);
        shorterTimeSlotList.add(testTimeSlot1);
        shorterTimeSlotList.add(testTimeSlot2);
        deadline1 = time2;
        testAssignment1 = new Assignment(assignmentName1, assignmentDetails1, assignmentSupervisor1, assignmentProjectId1);
        testAssignment2 = new Assignment(assignmentId2, assignmentName2, assignmentDetails2, assignmentSupervisor2, assignmentProjectId2, unorderedTimeSlotList, assignmentState2);
        testAssignment3 = new Assignment(assignmentId3, assignmentName3, assignmentDetails3, assignmentSupervisor3, assignmentProjectId3, assignmentState3, deadline1);
        assignments.add(testAssignment1);
        assignments.add(testAssignment2);
        assignments.add(testAssignment3);
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    @Test
    public void testAssignmentCoding() throws JsonProcessingException{
        ArrayList<Assignment> result = JSONDecoder.decodeAssignments(JSONEncoder.encodeAssignments(assignments));
        final SoftAssertions soft = new SoftAssertions();
        for (int i = 0; i < result.size(); i++){
            soft.assertThat(result.get(i)).isEqualToComparingFieldByFieldRecursively(assignments.get(i));
        }
        soft.assertAll();
    }
    
    @Test
    public void testProjectCoding() throws JsonProcessingException{
        ArrayList<Project> result = JSONDecoder.decodeProjects(JSONEncoder.encodeProjects(projects));
        final SoftAssertions soft = new SoftAssertions();
        for (int i = 0; i < result.size(); i++){
            soft.assertThat(result.get(i)).isEqualToComparingFieldByFieldRecursively(projects.get(i));
        }
        soft.assertAll();
    }
}
