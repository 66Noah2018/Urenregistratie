/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

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

/**
 *
 * @author RLvan
 */
public class AssignmentTest {
    
    public AssignmentTest() {
    }
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
    private static LocalDateTime deadline2;
    
    @BeforeClass
    public static void setUpClass() {
        UUID newUuid = randomUUID();
        assignmentId2 = newUuid.toString();
        UUID newUuid2 = randomUUID();
        assignmentId3 = newUuid2.toString();
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
        deadline2 = time5;
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testAssignment1 = new Assignment(assignmentName1, assignmentDetails1, assignmentSupervisor1, assignmentProjectId1);
        testAssignment2 = new Assignment(assignmentId2, assignmentName2, assignmentDetails2, assignmentSupervisor2, assignmentProjectId2, unorderedTimeSlotList, assignmentState2);
        testAssignment3 = new Assignment(assignmentId3, assignmentName3, assignmentDetails3, assignmentSupervisor3, assignmentProjectId3, assignmentState3, deadline1);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getAssignmentId method, of class Assignment.
     */
    @Test
    public void testGetAssignmentId2() {
        assertEquals(assignmentId2, testAssignment2.getAssignmentId());
    }
    @Test
    public void testGetAssignmentId3() {
        assertEquals(assignmentId3, testAssignment3.getAssignmentId());
    }

    /**
     * Test of getAssignmentName method, of class Assignment.
     */
    @Test
    public void testGetAssignmentName1() {
        assertEquals(assignmentName1, testAssignment1.getAssignmentName());
    }
    @Test
    public void testGetAssignmentName2() {
        assertEquals(assignmentName2, testAssignment2.getAssignmentName());
    }
    @Test
    public void testGetAssignmentName3() {
        assertEquals(assignmentName3, testAssignment3.getAssignmentName());
    }

    /**
     * Test of getAssignmentDetails method, of class Assignment.
     */
    @Test
    public void testGetAssignmentDetails1() {
        assertEquals(assignmentDetails1, testAssignment1.getAssignmentDetails());
    }
    @Test
    public void testGetAssignmentDetails2() {
        assertEquals(assignmentDetails2, testAssignment2.getAssignmentDetails());
    }
    @Test
    public void testGetAssignmentDetails3() {
        assertEquals(assignmentDetails3, testAssignment3.getAssignmentDetails());
    }

    /**
     * Test of getSupervisor method, of class Assignment.
     */
    @Test
    public void testGetSupervisor1() {
        assertEquals(assignmentSupervisor1, testAssignment1.getSupervisor());
    }
    @Test
    public void testGetSupervisor2() {
        assertEquals(assignmentSupervisor2, testAssignment2.getSupervisor());
    }
    @Test
    public void testGetSupervisor3() {
        assertEquals(assignmentSupervisor3, testAssignment3.getSupervisor());
    }

    /**
     * Test of getCorrespondingProjectId method, of class Assignment.
     */
    @Test
    public void testGetCorrespondingProjectId1() {
        assertEquals(assignmentProjectId1, testAssignment1.getCorrespondingProjectId());
    }
    @Test
    public void testGetCorrespondingProjectId2() {
        assertEquals(assignmentProjectId2, testAssignment2.getCorrespondingProjectId());
    }
    @Test
    public void testGetCorrespondingProjectId3() {
        assertEquals(assignmentProjectId3, testAssignment3.getCorrespondingProjectId());
    }

    /**
     * Test of getHoursWorked method, of class Assignment.
     */
    @Test
    public void testGetHoursWorked2() {
        assertEquals(unorderedTimeSlotList, testAssignment2.getHoursWorked());
    }

    /**
     * Test of getAssignmentState method, of class Assignment.
     */
    @Test
    public void testGetAssignmentState2() {
        assertEquals(assignmentState2.name(), testAssignment2.getAssignmentState());
    }
    @Test
    public void testGetAssignmentState3() {
        assertEquals(assignmentState3.name(), testAssignment3.getAssignmentState());
    }
    
    /**
     * Test of getDeadline method, of class Assignment.
     */
    @Test
    public void testGetDeadline(){
        assertEquals(deadline1, testAssignment3.getDeadline());
    }

    /**
     * Test of setAssignmentName method, of class Assignment.
     */
    @Test
    public void testSetAssignmentName1() {
        testAssignment1.setAssignmentName(assignmentName2);
        assertEquals(assignmentName2, testAssignment1.getAssignmentName());
    }
    @Test
    public void testSetAssignmentName2() {
        testAssignment2.setAssignmentName(assignmentName3);
        assertEquals(assignmentName3, testAssignment2.getAssignmentName());
    }
    @Test
    public void testSetAssignmentName3() {
        testAssignment3.setAssignmentName(assignmentName1);
        assertEquals(assignmentName1, testAssignment3.getAssignmentName());
    }

    /**
     * Test of setAssignmentDetails method, of class Assignment.
     */
    @Test
    public void testSetAssignmentDetails1() {
        testAssignment1.setAssignmentDetails(assignmentDetails2);
        assertEquals(assignmentDetails2, testAssignment1.getAssignmentDetails());
    }
    @Test
    public void testSetAssignmentDetails2() {
        testAssignment2.setAssignmentDetails(assignmentDetails3);
        assertEquals(assignmentDetails3, testAssignment2.getAssignmentDetails());
    }
    @Test
    public void testSetAssignmentDetails3() {
        testAssignment3.setAssignmentDetails(assignmentDetails1);
        assertEquals(assignmentDetails1, testAssignment3.getAssignmentDetails());
    }

    /**
     * Test of setSupervisor method, of class Assignment.
     */
    @Test
    public void testSetSupervisor1() {
        testAssignment1.setSupervisor(assignmentSupervisor2);
        assertEquals(assignmentSupervisor2, testAssignment1.getSupervisor());
    }
    @Test
    public void testSetSupervisor2() {
        testAssignment2.setSupervisor(assignmentSupervisor3);
        assertEquals(assignmentSupervisor3, testAssignment2.getSupervisor());
    }
    @Test
    public void testSetSupervisor3() {
        testAssignment3.setSupervisor(assignmentSupervisor1);
        assertEquals(assignmentSupervisor1, testAssignment3.getSupervisor());
    }

    /**
     * Test of setCorrespondingProjectId method, of class Assignment.
     */
    @Test
    public void testSetCorrespondingProjectId1() {
        testAssignment1.setCorrespondingProjectId(assignmentProjectId2);
        assertEquals(assignmentProjectId2, testAssignment1.getCorrespondingProjectId());
    }
    @Test
    public void testSetCorrespondingProjectId2() {
        testAssignment2.setCorrespondingProjectId(assignmentProjectId3);
        assertEquals(assignmentProjectId3, testAssignment2.getCorrespondingProjectId());
    }
    @Test
    public void testSetCorrespondingProjectId3() {
        testAssignment3.setCorrespondingProjectId(assignmentProjectId1);
        assertEquals(assignmentProjectId1, testAssignment3.getCorrespondingProjectId());
    }

    /**
     * Test of setAssignmentState method, of class Assignment.
     */
    @Test
    public void testSetAssignmentState1() {
        testAssignment1.setAssignmentState("STARTED");
        assertEquals(Utils.AssignmentState.STARTED.name(), testAssignment1.getAssignmentState());
    }
    @Test
    public void testSetAssignmentState2() {
        testAssignment2.setAssignmentState("INSUFFICIENT_INFORMATION");
        assertEquals(Utils.AssignmentState.INSUFFICIENT_INFORMATION.name(), testAssignment2.getAssignmentState());
    }
    @Test
    public void testSetAssignmentState3() {
        testAssignment3.setAssignmentState("NOT_STARTED");
        assertEquals(Utils.AssignmentState.NOT_STARTED.name(), testAssignment3.getAssignmentState());
    }
    @Test
    public void testSetAssignmentState4() {
        testAssignment3.setAssignmentState("FINISHED");
        assertEquals(Utils.AssignmentState.FINISHED.name(), testAssignment3.getAssignmentState());
    }
    
    /**
     * Test of setDeadline method, of class Assignment.
     */
    
    @Test
    public void testSetDeadline(){
        testAssignment3.setDeadline(deadline2);
        assertEquals(deadline2, testAssignment3.getDeadline());
    }

    /**
     * Test of addHoursWorked method, of class Assignment.
     */
    @Test
    public void testAddHoursWorked() {
        testAssignment1.addHoursWorked(testTimeSlot1);
        assertEquals(singleTimeSlot, testAssignment1.getHoursWorked());
    }

    /**
     * Test of removeHoursWorked method, of class Assignment.
     */
    @Test
    public void testRemoveHoursWorked() {
        testAssignment2.removeHoursWorked(testTimeSlot3.getTimeSlotId());
        assertEquals(shorterTimeSlotList, testAssignment2.getHoursWorked());
        testAssignment2.addHoursWorked(testTimeSlot3);
    }

    /**
     * Test of sortTimeSlots method, of class Assignment.
     */
    @Test
    public void testSortTimeSlots() {
        testAssignment2.sortTimeSlots();
        assertArrayEquals(orderedTimeSlotList.toArray(), testAssignment2.getHoursWorked().toArray());
    }
    
    /**
     * Test of updateHoursWorked method, of class Assignment
     */
    @Test
    public void testUpdateHoursWorked(){
        testTimeSlot2.setEndTime(time5);
        testAssignment2.updateHoursWorked(testTimeSlot2);
        assertArrayEquals(orderedTimeSlotList.toArray(), testAssignment2.getHoursWorked().toArray());
    }
    
}
