/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class TimeSlotTest {
    
    public TimeSlotTest() {
    }
    
    private static TimeSlot testTimeSlot1;
    private static TimeSlot testTimeSlot2;
    private static TimeSlot testTimeSlot3;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static String idTest2;
    private static String idTest3;
    private static LocalDateTime time1;
    private static LocalDateTime time2;
    private static LocalDateTime time3;
    private static LocalDateTime time4;
    private static LocalDateTime time5;
    private static LocalDateTime time6;
    
    @BeforeClass
    public static void setUpClass() {
        time1 = LocalDateTime.parse("14-07-2022 17:00:05", formatter);
        time2 = LocalDateTime.parse("14-07-2022 19:00:10", formatter);
        time3 = LocalDateTime.parse("09-02-2093 21:30:32", formatter);
        time4 = LocalDateTime.parse("09-02-2093 23:00:00", formatter);
        time5 = LocalDateTime.parse("03-08-1983 11:55:30", formatter);
        time6 = LocalDateTime.parse("03-08-1983 14:00:00", formatter);
        
        UUID newUuid = randomUUID();
        idTest2 = newUuid.toString();
        UUID newUuid2 = randomUUID();
        idTest3 = newUuid.toString();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testTimeSlot1 = new TimeSlot(time1, time2);
        testTimeSlot2 = new TimeSlot(idTest2, time3, time4);
        testTimeSlot3 = new TimeSlot(idTest3, time5, time6, true);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getTimeSlotId method, of class TimeSlot.
     */
    @Test
    public void testGetTimeSlotId2() {
        assertEquals(idTest2, testTimeSlot2.getTimeSlotId());
    }
    @Test
    public void testGetTimeSlotId3() {
        assertEquals(idTest3, testTimeSlot3.getTimeSlotId());
    }

    /**
     * Test of getStartTime method, of class TimeSlot.
     */
    @Test
    public void testGetStartTime1() {
        assertEquals(time1, testTimeSlot1.getStartTime());
    }
    @Test
    public void testGetStartTime2() {
        assertEquals(time3, testTimeSlot2.getStartTime());
    }
    @Test
    public void testGetStartTime3() {
        assertEquals(time5, testTimeSlot3.getStartTime());
    }

    /**
     * Test of getEndTime method, of class TimeSlot.
     */
    @Test
    public void testGetEndTime1() {
        assertEquals(time2, testTimeSlot1.getEndTime());
    }
    @Test
    public void testGetEndTime2() {
        assertEquals(time4, testTimeSlot2.getEndTime());
    }
    @Test
    public void testGetEndTime3() {
        assertEquals(time6, testTimeSlot3.getEndTime());
    }

    /**
     * Test of getHoursWritten method, of class TimeSlot.
     */
    @Test
    public void testGetHoursWritten1() {
        assertEquals(false, testTimeSlot1.getHoursWritten());
    }
    @Test
    public void testGetHoursWritten2() {
        assertEquals(false, testTimeSlot2.getHoursWritten());
    }
    @Test
    public void testGetHoursWritten3() {
        assertEquals(true, testTimeSlot3.getHoursWritten());
    }

    /**
     * Test of setStartTime method, of class TimeSlot.
     */
    @Test
    public void testSetStartTime1() {
        testTimeSlot1.setStartTime(time3);
        assertEquals(time3, testTimeSlot1.getStartTime());
    }
    @Test
    public void testSetStartTime2() {
        testTimeSlot2.setStartTime(time5);
        assertEquals(time5, testTimeSlot2.getStartTime());
    }
    @Test
    public void testSetStartTime3() {
        testTimeSlot3.setStartTime(time1);
        assertEquals(time1, testTimeSlot3.getStartTime());
    }

    /**
     * Test of setEndTime method, of class TimeSlot.
     */
    @Test
    public void testSetEndTime1() {
        testTimeSlot1.setEndTime(time4);
        assertEquals(time4, testTimeSlot1.getEndTime());
    }
    @Test
    public void testSetEndTime2() {
        testTimeSlot2.setEndTime(time6);
        assertEquals(time6, testTimeSlot2.getEndTime());
    }
    @Test
    public void testSetEndTime3() {
        testTimeSlot3.setEndTime(time2);
        assertEquals(time2, testTimeSlot3.getEndTime());
    }

    /**
     * Test of setHoursWritten method, of class TimeSlot.
     */
    @Test
    public void testSetHoursWritten1() {
        testTimeSlot1.setHoursWritten(Boolean.TRUE);
        assertEquals(Boolean.TRUE, testTimeSlot1.getHoursWritten());
    }
    @Test
    public void testSetHoursWritten2() {
        testTimeSlot2.setHoursWritten(Boolean.TRUE);
        assertEquals(Boolean.TRUE, testTimeSlot2.getHoursWritten());
    }
    @Test
    public void testSetHoursWritten3() {
        testTimeSlot3.setHoursWritten(Boolean.FALSE);
        assertEquals(Boolean.FALSE, testTimeSlot3.getHoursWritten());
    }
    
}
