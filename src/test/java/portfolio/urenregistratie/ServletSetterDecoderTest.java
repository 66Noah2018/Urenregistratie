/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import static java.util.UUID.randomUUID;
import org.assertj.core.api.SoftAssertions;
import org.javatuples.Pair;
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
public class ServletSetterDecoderTest {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static LocalDateTime time1;
    private static LocalDateTime time2;
    private static LocalDateTime time3;
    private static final String assignmentId1 = "TEST_ID";
    private static String timeSlotId1;
    private static final Boolean hoursWritten1 = true;
    private static final String testProjectName1 = "project1";
    private static final String testProjectCode1 = "code1";
    private static final String testProjectDisplayColor1 = "red";
    private static final String testAssignmentName1 = "assignment1";
    private static final String testAssignmentDetails1 = "lorem ipsum";
    private static final String testAssignmentSupervisor1 = "sup1";
    private static final String testAssignmentCorrespondingProjectId = testProjectCode1;
    private static LocalDateTime testDeadline1;
    
    public ServletSetterDecoderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        time1 = LocalDateTime.parse("14-07-2022 17:00:05", formatter);
        time2 = LocalDateTime.parse("14-07-2022 19:00:10", formatter);
        time3 = LocalDateTime.parse("09-02-2093 21:30:32", formatter);
        testDeadline1 = time3;
        UUID newuuid = randomUUID();
        timeSlotId1 = newuuid.toString();
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
     * Test of decodeNewTimeSlot method, of class ServletSetterDecoder.
     */
    @Test
    public void testDecodeNewTimeSlot() throws Exception {
        String encodedNewTimeSlot = "{\"assignmentId\":\"" + assignmentId1 + "\",\"startTime\":\"14-07-2022 17:00:05\",\"endTime\":\"14-07-2022 19:00:10\"}";
        Pair<String, TimeSlot> decodedTimeSlotData = ServletSetterDecoder.decodeNewTimeSlot(encodedNewTimeSlot);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(decodedTimeSlotData.getValue0()).isEqualTo(assignmentId1);
        soft.assertThat(decodedTimeSlotData.getValue1().getStartTime()).isEqualTo(time1);
        soft.assertThat(decodedTimeSlotData.getValue1().getEndTime()).isEqualTo(time2);
        soft.assertAll();
    }

    /**
     * Test of decodeUpdatedTimeSlot method, of class ServletSetterDecoder.
     */
    @Test
    public void testDecodeUpdatedTimeSlot() throws Exception {
        String encodedUpdatedTimeSlot = "{\"assignmentId\":\"" + assignmentId1 + "\",\"timeSlotId\":\"" + timeSlotId1 + "\",\"startTime\":\"14-07-2022 17:00:05\",\"endTime\":\"14-07-2022 19:00:10\",\"hoursWritten\":\"" + hoursWritten1 + "\"}";
        Pair<String, TimeSlot> decodedTimeSlotData = ServletSetterDecoder.decodeUpdatedTimeSlot(encodedUpdatedTimeSlot);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(decodedTimeSlotData.getValue0()).isEqualTo(assignmentId1);
        soft.assertThat(decodedTimeSlotData.getValue1().getTimeSlotId()).isEqualTo(timeSlotId1);
        soft.assertThat(decodedTimeSlotData.getValue1().getStartTime()).isEqualTo(time1);
        soft.assertThat(decodedTimeSlotData.getValue1().getEndTime()).isEqualTo(time2);
        soft.assertThat(decodedTimeSlotData.getValue1().getHoursWritten()).isEqualTo(hoursWritten1);
        soft.assertAll();
    }

    /**
     * Test of decodeNewProject method, of class ServletSetterDecoder.
     */
    @Test
    public void testDecodeNewProject() throws Exception {
        String encodedNewProject = "{\"projectName\":\"" + testProjectName1 + "\",\"projectCode\":\"" + testProjectCode1 + "\",\"projectDisplayColor\":\"" + testProjectDisplayColor1 + "\"}";
        Project decodedProject = ServletSetterDecoder.decodeNewProject(encodedNewProject);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(decodedProject.getProjectName()).isEqualTo(testProjectName1);
        soft.assertThat(decodedProject.getProjectCode()).isEqualTo(testProjectCode1);
        soft.assertThat(decodedProject.getProjectDisplayColor()).isEqualTo(testProjectDisplayColor1);
        soft.assertAll();
    }

    /**
     * Test of decodeNewAssignment method, of class ServletSetterDecoder.
     */
    @Test
    public void testDecodeNewAssignment() throws Exception {
        String encodedNewAssignment = "{\"assignmentName\":\"" + testAssignmentName1 + "\",\"assignmentDetails\":\"" + testAssignmentDetails1 + "\",\"supervisor\":\"" + testAssignmentSupervisor1 + "\",\"correspondingProjectId\":\"" + testAssignmentCorrespondingProjectId + "\",\"deadline\":\"" + testDeadline1.format(formatter) + "\"}";
        Assignment decodedAssignment = ServletSetterDecoder.decodeNewAssignment(encodedNewAssignment);
        final SoftAssertions soft = new SoftAssertions();
        soft.assertThat(decodedAssignment.getAssignmentName()).isEqualTo(testAssignmentName1);
        soft.assertThat(decodedAssignment.getAssignmentDetails()).isEqualTo(testAssignmentDetails1);
        soft.assertThat(decodedAssignment.getSupervisor()).isEqualTo(testAssignmentSupervisor1);
        soft.assertThat(decodedAssignment.getCorrespondingProjectId()).isEqualTo(testAssignmentCorrespondingProjectId);
        soft.assertThat(decodedAssignment.getDeadline().format(formatter)).isEqualTo(testDeadline1.format(formatter));
        soft.assertAll();
    }
    
}
