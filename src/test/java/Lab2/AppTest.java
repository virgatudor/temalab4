package Lab2;

import Lab2.domain.Nota;
import Lab2.domain.Student;
import Lab2.domain.Tema;
import Lab2.repository.NotaXMLRepository;
import Lab2.repository.StudentXMLRepository;
import Lab2.repository.TemaXMLRepository;
import Lab2.service.Service;
import Lab2.validation.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */

    Service service;
    StudentXMLRepository studentXMLRepository;
    TemaXMLRepository assignmentXMLRepository;
    NotaXMLRepository gradeXMLRepository;
    private static final String STUDENT_NAME = "Mihai";
    private static final String STUDENT_ID = "11";

    @Before
    public void initData(){
        Validator<Tema> temaValidator = new TemaValidator();
        Validator<Nota> notaValidator = new NotaValidator();
        Validator<Student> studentValidator = new StudentValidator();

        studentXMLRepository = new StudentXMLRepository(studentValidator, "studenti.xml");
        assignmentXMLRepository = new TemaXMLRepository(temaValidator, "teme.xml");
        gradeXMLRepository = new NotaXMLRepository(notaValidator, "note.xml");
        service = new Service(studentXMLRepository, assignmentXMLRepository, gradeXMLRepository);
    }

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void testAddStudentDuplicateId() {

        service.deleteStudent("10");
        assertNull(studentXMLRepository.findOne("10"));
        assertTrue(service.saveStudent("10", STUDENT_NAME, 932) == 1);
        assertFalse(service.saveStudent("10", "Ale", 932) == 1);

        assertEquals(studentXMLRepository.findOne("10").getNume(), STUDENT_NAME);
    }

    @Test
    public void testAddStudentToRepository(){

        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, STUDENT_NAME, 932) == 1);
        assertEquals(studentXMLRepository.findOne(STUDENT_ID).getNume(), STUDENT_NAME);
    }

    @Test
    public void tc1AddStudentToRepository(){

        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, STUDENT_NAME, 932) == 1);
        assertEquals(studentXMLRepository.findOne(STUDENT_ID).getNume(), STUDENT_NAME);
    }

    @Test
    public void tc2AddStudentIdNull() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(null, STUDENT_NAME, 932) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }

    @Test
    public void tc3AddStudentIdEmpty() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent("", STUDENT_NAME, 932) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }

    @Test
    public void tc4AddStudentNameNull() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, null, 932) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }

    @Test
    public void tc5AddStudentNameEmpty() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, "", 932) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }


    @Test
    public void tc6AddStudentGroup110Invalid() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, "", 932) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }

    @Test
    public void tc7AddStudentGroup938Invalid() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, STUDENT_NAME, 938) == 1);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
    }


    @Test
    public void tc8AddStudentGroup111Valid() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, STUDENT_NAME, 111) == 1);
        assertEquals(studentXMLRepository.findOne(STUDENT_ID).getGrupa(), 111);
    }

    @Test
    public void tc9AddStudentGroup937Valid() {
        service.deleteStudent(STUDENT_ID);
        assertNull(studentXMLRepository.findOne(STUDENT_ID));
        assertTrue(service.saveStudent(STUDENT_ID, STUDENT_NAME, 937) == 1);
        assertEquals(studentXMLRepository.findOne(STUDENT_ID).getGrupa(), 937);
    }
}
