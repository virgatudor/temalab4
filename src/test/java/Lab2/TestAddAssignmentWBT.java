package Lab2;

import Lab2.domain.Student;
import Lab2.domain.Tema;
import Lab2.repository.NotaXMLRepository;
import Lab2.repository.StudentXMLRepository;
import Lab2.repository.TemaXMLRepository;
import Lab2.service.Service;
import Lab2.validation.NotaValidator;
import Lab2.validation.StudentValidator;
import Lab2.validation.TemaValidator;
import Lab2.validation.ValidationException;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestAddAssignmentWBT {
    private Service service;
    private StudentXMLRepository studentRepository;
    private TemaXMLRepository assignmentRepository;
    private NotaXMLRepository gradeRepository;

    @Before
    public void init(){
        studentRepository = new StudentXMLRepository(new StudentValidator(), "studenti.xml");
        assignmentRepository = new TemaXMLRepository(new TemaValidator(), "teme.xml");
        gradeRepository = new NotaXMLRepository(new NotaValidator(), "note.xml");
        service = new Service(studentRepository, assignmentRepository, gradeRepository);
    }

    @Test
    public void testAddAssignment() {
        String idAssignment = "22";
        String description = "description";
        int deadline = 2;
        int startline = 1;
        Tema assignment = new Tema(idAssignment, description, deadline, startline);

        service.deleteTema(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));
        assignmentRepository.save(assignment);
        assertNotNull(assignmentRepository.findOne(idAssignment));
        assertEquals(assignmentRepository.findOne(idAssignment).getDescriere(),description);
    }

    @Test
    public void testAddAssignmentInvalid() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String id = null;
        String description = "description";
        int deadline = 1;
        int startline = 12;
        Tema assigment = new Tema(id, description, deadline, startline);

        try {
            assignmentRepository.save(assigment);
            assert(true);
        } catch (ValidationException ve) {
            assertEquals(ve.getMessage(), "ID invalid! ");
        }

        try {
            assertNull(assignmentRepository.findOne(id));
            assert(false);
        } catch (IllegalArgumentException e){
            assertEquals("ID-ul nu poate fi null! \n", e.getMessage());
        }
    }

    @Test
    public void testAddAssignmentService() {

        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        service.deleteTema(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));
        assertEquals(service.saveTema(idAssignment, description, deadline, startline), 1);
        assertEquals(assignmentRepository.findOne(idAssignment).getDescriere(),description);
    }

    @Test
    public void testAddAssignmentServiceDuplicate() {

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "101";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        service.deleteTema(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));
        assertEquals(service.saveTema(idAssignment, description, deadline, startline), 1);
        assertEquals(service.saveTema(idAssignment, description, deadline, startline), 0);

        assertNotEquals("Duplicate ID! \n", outContent.toString());

        assertEquals(assignmentRepository.findOne(idAssignment).getDescriere(),description);
    }

    @Test
    public void testAddAssignmentRepository() {
        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));
        assertNull(assignmentRepository.save(assigment));
        assertEquals(assignmentRepository.findOne(idAssignment).getDescriere(), description);
        assertEquals(assignmentRepository.findOne(idAssignment).getStartline(), startline);
        assertEquals(assignmentRepository.findOne(idAssignment).getDeadline(), deadline);
        assertEquals(assignmentRepository.findOne(idAssignment).getID(), idAssignment);
    }

    @Test
    public void testAddAssignmentRepositoryDuplicate() {
        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        long count = service.findAllTeme().spliterator().getExactSizeIfKnown();

        assignmentRepository.save(assigment);

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve){
            assertEquals(ve.getMessage(), "Duplicate ID! ");
        }

        long newCount = service.findAllTeme().spliterator().getExactSizeIfKnown();

        assertEquals(count + 1, newCount);

        assertEquals(assignmentRepository.findOne(idAssignment).getDescriere(), description);
        assertEquals(assignmentRepository.findOne(idAssignment).getStartline(), startline);
        assertEquals(assignmentRepository.findOne(idAssignment).getDeadline(), deadline);
        assertEquals(assignmentRepository.findOne(idAssignment).getID(), idAssignment);
    }

    @Test
    public void testAddAssignmentValidatorDeadline1() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 15;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));
    }

    @Test
    public void testAddAssignmentValidatorDeadline2() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 1;
        int startline = 3;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));
    }


    @Test
    public void testAddAssignmentValidatorDeadline3() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = -1;
        int startline = 3;
        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Deadline invalid! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));
    }

    @Test
    public void testAddAssignmentValidatorStartLine() {

        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "description";
        int deadline = 2;
        int startline = -1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Data de primire invalida! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));

        service.saveTema(idAssignment, description, deadline, startline);
        assertNotEquals("Data de primire invalida! \n", outContent.toString());
        assertNull(assignmentRepository.findOne(idAssignment));
        outContent.reset();

    }

    @Test
    public void testAddAssignmentValidatorDescriptionEmpty() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Descriere invalida! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));

    }

    @Test
    public void testAddAssignmentValidatorDescriptionNull() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        try {
            assignmentRepository.save(assigment);
        } catch (ValidationException ve) {
            assertEquals("Descriere invalida! ", ve.getMessage());
        }
        assertNull(assignmentRepository.findOne(idAssignment));

    }

    @Test
    public void testAddAssignmentValidatorDescriptionValid() {
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        String idAssignment = "100";
        String description = "it should pass";
        int deadline = 2;
        int startline = 1;

        Tema assigment = new Tema(idAssignment, description, deadline, startline);

        assignmentRepository.delete(idAssignment);
        assertNull(assignmentRepository.findOne(idAssignment));

        assignmentRepository.save(assigment);

        assertNotNull(assignmentRepository.findOne(idAssignment));

    }

}
