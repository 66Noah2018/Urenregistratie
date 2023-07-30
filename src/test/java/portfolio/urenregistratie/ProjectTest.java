/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package portfolio.urenregistratie;

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
public class ProjectTest {
    
    public ProjectTest() {
    }
    private static Project testProject1;
    private static Project testProject2;
    private static String project2Id;
    private static final String project1Name = "CAPABLE";
    private static final String project1Code = "C1234";
    private static final String project2Name = "testP";
    private static final String project2Code = "test5678";
    
    @BeforeClass
    public static void setUpClass() {
        UUID newUuid = randomUUID();
        project2Id = newUuid.toString();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        testProject1 = new Project(project1Name, project1Code);
        testProject2 = new Project(project2Id, project2Name, project2Code);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getProjectId method, of class Project.
     */
    @Test
    public void testGetProjectId2() {
        assertEquals(project2Id, testProject2.getProjectId());
    }

    /**
     * Test of getProjectName method, of class Project.
     */
    @Test
    public void testGetProjectName1() {
        assertEquals(project1Name, testProject1.getProjectName());
    }
    @Test
    public void testGetProjectName2() {
        assertEquals(project2Name, testProject2.getProjectName());
    }

    /**
     * Test of getProjectCode method, of class Project.
     */
    @Test
    public void testGetProjectCode1() {
        assertEquals(project1Code, testProject1.getProjectCode());
    }
    @Test
    public void testGetProjectCode2() {
        assertEquals(project2Code, testProject2.getProjectCode());
    }

    /**
     * Test of setProjectName method, of class Project.
     */
    @Test
    public void testSetProjectName1() {
        String altName1 = "CaPaBlE";
        testProject1.setProjectName(altName1);
        assertEquals(altName1, testProject1.getProjectName());
    }
    @Test
    public void testSetProjectName2() {
        String altName2 = "1234TEST";
        testProject2.setProjectName(altName2);
        assertEquals(altName2, testProject2.getProjectName());
    }

    /**
     * Test of setProjectCode method, of class Project.
     */
    @Test
    public void testSetProjectCode1() {
        String altCode1 = "321654987";
        testProject1.setProjectCode(altCode1);
        assertEquals(altCode1, testProject1.getProjectCode());
    }
    @Test
    public void testSetProjectCode2() {
        String altCode2 = "0987654321";
        testProject2.setProjectCode(altCode2);
        assertEquals(altCode2, testProject2.getProjectCode());
    }    
}
