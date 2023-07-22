/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.entry;
import org.assertj.core.api.SoftAssertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author RLvan
 */
public class UtilsTest {
    
    public UtilsTest() {
    }
    
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static String assignmentId2;
    private static String assignmentId3;
    private static String assignmentId4;
    private static final String assignmentName1 = "assgName1";
    private static final String assignmentName2 = "assgName2";
    private static final String assignmentName3 = "assgName3";
    private static final String assignmentName4 = "assgName4";
    private static final String assignmentDetails1 = "Some very long string";
    private static final String assignmentDetails2 = "With a lot of details";
    private static final String assignmentDetails3 = "About a certain assignment";
    private static final String assignmentDetails4 = "Which might have a deadline";
    private static final String assignmentSupervisor1 = "sup1";
    private static final String assignmentSupervisor2 = "sup2";
    private static final String assignmentSupervisor3 = "sup3";
    private static final String assignmentSupervisor4 = "sup4";
    private static final String assignmentProjectId2 = "456";
    private static final Utils.AssignmentState assignmentState2 = Utils.AssignmentState.NOT_STARTED;
    private static final Utils.AssignmentState assignmentState3 = Utils.AssignmentState.INSUFFICIENT_INFORMATION;
    private static final Utils.AssignmentState assignmentState4 = Utils.AssignmentState.FINISHED;
    private static LocalDateTime time1;
    private static LocalDateTime time2;
    private static LocalDateTime time3;
    private static LocalDateTime time4;
    private static LocalDateTime time5;
    private static LocalDateTime time6;
    private static Assignment testAssignment1;
    private static Assignment testAssignment2;
    private static Assignment testAssignment3;
    private static Assignment testAssignment4;
    private static ArrayList<TimeSlot> unorderedTimeSlotList = new ArrayList<>();
    private static ArrayList<TimeSlot> orderedTimeSlotList = new ArrayList<>();
    private static ArrayList<TimeSlot> singleTimeSlot = new ArrayList<>();
    private static ArrayList<TimeSlot> shorterTimeSlotList = new ArrayList<>();
    private static TimeSlot testTimeSlot1;
    private static TimeSlot testTimeSlot2;
    private static TimeSlot testTimeSlot3;
    private static LocalDateTime deadline1;
    private static LocalDateTime deadline2;
    private static ArrayList<Project> projects = new ArrayList<>();
    private static ArrayList<Assignment> assignments = new ArrayList<>();
    private static Project testProject1;
    private static Project testProject2;
    private static String project2Id;
    private static final String project1Name = "CAPABLE";
    private static final String project1Code = "C1234";
    private static final String project1DisplayColor = "red";
    private static final String project2Name = "testP";
    private static final String project2Code = "test5678";
    private static final String project2DisplayColor = "blue";
    private static ArrayList<String> supervisors = new ArrayList<>();
    private static String timeSlotId;
    
    @BeforeClass
    public static void setUpClass() {
        UUID newUuid = randomUUID();
        assignmentId2 = newUuid.toString();
        UUID newUuid2 = randomUUID();
        assignmentId3 = newUuid2.toString();
        UUID newUuid3 = randomUUID();
        project2Id = newUuid3.toString();
        UUID newUuid4 = randomUUID();
        timeSlotId = newUuid4.toString();
        time1 = LocalDateTime.parse("14-07-2022 17:00:05", formatter);
        time2 = LocalDateTime.parse("14-07-2022 19:00:10", formatter);
        time3 = LocalDateTime.parse("09-02-2093 21:30:32", formatter);
        time4 = LocalDateTime.parse("09-02-2093 23:00:00", formatter);
        time5 = LocalDateTime.parse("03-08-1983 11:55:30", formatter);
        time6 = LocalDateTime.parse("03-08-1983 14:00:00", formatter);
        testTimeSlot1 = new TimeSlot(time1, time2);
        testTimeSlot2 = new TimeSlot(time3, time4);
        testTimeSlot3 = new TimeSlot(timeSlotId, time5, time6, true);
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
        deadline2 = time5;
        testProject1 = new Project(project1Name, project1Code, project1DisplayColor);
        testProject2 = new Project(project2Id, project2Name, project2Code, project2DisplayColor);
        testAssignment1 = new Assignment(assignmentName1, assignmentDetails1, assignmentSupervisor1, project1Code);
        testAssignment2 = new Assignment(assignmentId2, assignmentName2, assignmentDetails2, assignmentSupervisor2, assignmentProjectId2, unorderedTimeSlotList, assignmentState2);
        testAssignment3 = new Assignment(assignmentId3, assignmentName3, assignmentDetails3, assignmentSupervisor3, project2Code, assignmentState3, deadline1);
        testAssignment4 = new Assignment(assignmentId4, assignmentName4, assignmentDetails4, assignmentSupervisor4, project2Code, assignmentState4, deadline2);
        assignments.add(testAssignment1);
        assignments.add(testAssignment2);
        assignments.add(testAssignment3);
        projects.add(testProject1);
        projects.add(testProject2);
        supervisors.add(assignmentSupervisor1);
        supervisors.add(assignmentSupervisor2);
        supervisors.add(assignmentSupervisor3);
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

    /**
     * Test of getProjectById method, of class Utils.
     */
    @Test
    public void testGetProjectById1() {
        assertEquals(testProject1, Utils.getProjectById(projects, testProject1.getProjectId()));
    }
    @Test
    public void testGetProjectById2() {
        assertEquals(testProject2, Utils.getProjectById(projects, testProject2.getProjectId()));
    }

    /**
     * Test of getHoursByProjectId method, of class Utils.
     */
    @Test
    public void testGetHoursByProjectId() {
        assertArrayEquals(unorderedTimeSlotList.toArray(), Utils.getHoursByProjectId(assignments, assignmentProjectId2).toArray());
    }

    /**
     * Test of getUnwrittenHoursByProjectId method, of class Utils.
     */
    @Test
    public void testGetUnwrittenHoursByProjectId() {
        ArrayList<TimeSlot> unwrittenHours = new ArrayList<>();
        unwrittenHours.add(testTimeSlot1);
        unwrittenHours.add(testTimeSlot2);
        assertArrayEquals(unwrittenHours.toArray(), Utils.getUnwrittenHoursByProjectId(assignments, assignmentProjectId2).toArray());
    }

    /**
     * Test of getAssignmentsByState method, of class Utils.
     */
    @Test
    public void testGetAssignmentsByState_NOT_STARTED() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment1);
        expectedAssignments.add(testAssignment2);
        ArrayList<String> states = new ArrayList<>();
        states.add(Utils.AssignmentState.NOT_STARTED.name());
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsByState(assignments, states).toArray());
    }
    @Test
    public void testGetAssignmentsByState_INSUFFICIENT_INFORMATION() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment3);
        ArrayList<String> states = new ArrayList<>();
        states.add(Utils.AssignmentState.INSUFFICIENT_INFORMATION.name());
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsByState(assignments, states).toArray());
    }
    @Test
    public void testGetAssignmentsByState_NOT_STARTED_INSUFFICIENT_INFORMATION() {
        ArrayList<String> states = new ArrayList<>();
        states.add(Utils.AssignmentState.NOT_STARTED.name());
        states.add(Utils.AssignmentState.INSUFFICIENT_INFORMATION.name());
        assertArrayEquals(assignments.toArray(), Utils.getAssignmentsByState(assignments, states).toArray());
    }

    /**
     * Test of getAssignmentsByProjectId method, of class Utils.
     */
    @Test
    public void testGetAssignmentsByProjectId1() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment1);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsByProjectId(assignments, project1Code).toArray());
    }
    @Test
    public void testGetAssignmentsByProjectId2() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment2);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsByProjectId(assignments, assignmentProjectId2).toArray());
    }
    @Test
    public void testGetAssignmentsByProjectId3() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment3);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsByProjectId(assignments, project2Code).toArray());
    }

    /**
     * Test of getAssignmentsBySupervisor method, of class Utils.
     */
    @Test
    public void testGetAssignmentsBySupervisor1() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment1);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsBySupervisor(assignments, assignmentSupervisor1).toArray());
    }
    @Test
    public void testGetAssignmentsBySupervisor2() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment2);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsBySupervisor(assignments, assignmentSupervisor2).toArray());
    }
    @Test
    public void testGetAssignmentsBySupervisor3() {
        ArrayList<Assignment> expectedAssignments = new ArrayList<>();
        expectedAssignments.add(testAssignment3);
        assertArrayEquals(expectedAssignments.toArray(), Utils.getAssignmentsBySupervisor(assignments, assignmentSupervisor3).toArray());
    }

    /**
     * Test of getSupervisors method, of class Utils.
     */
    @Test
    public void testGetSupervisors() {
        assertArrayEquals(supervisors.toArray(), Utils.getSupervisors(assignments).toArray());
    }
    
    /**
     * Test of sortAssignmentsByDeadline method, of class Utils.
     */
    @Test
    public void testSortAssignmentsByDeadline(){
        ArrayList<Assignment> sortedAssignments = new ArrayList<>();
        ArrayList<Assignment> unsortedAssignments = new ArrayList<>();
        unsortedAssignments.addAll(assignments);
        unsortedAssignments.add(testAssignment4);
        sortedAssignments.add(testAssignment4);
        sortedAssignments.add(testAssignment3);
        sortedAssignments.add(testAssignment1);
        sortedAssignments.add(testAssignment2);
        assertArrayEquals(sortedAssignments.toArray(), Utils.sortAssignmentsByDeadline(unsortedAssignments).toArray());
    }
    
    /**
     * Test of getAssignmentsWithoutDeadline method, of class Utils
     */    
    @Test
    public void testGetAssignmentsWithoutDeadline(){
        ArrayList<Assignment> expectedResult = new ArrayList<>();
        expectedResult.add(testAssignment1);
        expectedResult.add(testAssignment2);
        assertArrayEquals(expectedResult.toArray(), Utils.getAssignmentsWithoutDeadline(assignments).toArray());
    }
    
    /**
     * Test of getAssignmentsWithDeadline method, of class Utils
     */
    @Test
    public void testGetAssignmentsWithDeadline(){
        ArrayList<Assignment> sortedAssignments = new ArrayList<>();
        ArrayList<Assignment> unsortedAssignments = new ArrayList<>();
        unsortedAssignments.addAll(assignments);
        unsortedAssignments.add(testAssignment4);
        sortedAssignments.add(testAssignment4);
        sortedAssignments.add(testAssignment3);
        assertArrayEquals(sortedAssignments.toArray(), Utils.getAssignmentsWithDeadline(unsortedAssignments).toArray());
    }
    
    /**
     * Test of getHoursGroupedByProject method, of class Utils
     */
    @Test
    public void testGetHoursGroupedByProject(){
        HashMap<String, ArrayList<TimeSlot>> groupedHours = Utils.getHoursGroupedByProject(assignments);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(groupedHours.get(project1Code)).isEmpty();
        soft.assertThat(groupedHours.get(project2Code)).isEmpty();
        soft.assertThat(groupedHours).contains(entry(assignmentProjectId2, unorderedTimeSlotList));
        soft.assertAll();
    }
    
    /**
     * Test of getUnwrittenHoursGroupedByProject method, of class Utils
     */
    @Test
    public void testGetUnwrittenHoursGroupedByProject(){
        HashMap<String, ArrayList<TimeSlot>> groupedHours = Utils.getUnwrittenHoursGroupedByProject(assignments);
        ArrayList<TimeSlot> unwrittenAssignmentProjectId2 = new ArrayList<>();
        unwrittenAssignmentProjectId2.add(testTimeSlot1);
        unwrittenAssignmentProjectId2.add(testTimeSlot2);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(groupedHours.get(project1Code)).isEmpty();
        soft.assertThat(groupedHours.get(project2Code)).isEmpty();
        soft.assertThat(groupedHours).contains(entry(assignmentProjectId2, unwrittenAssignmentProjectId2));
        soft.assertAll();
    }
}
