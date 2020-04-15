package Lab2;

import Lab2.domain.Nota;
import Lab2.domain.Pair;
import Lab2.domain.Tema;
import Lab2.repository.NotaXMLRepository;
import Lab2.repository.StudentXMLRepository;
import Lab2.repository.TemaXMLRepository;
import Lab2.service.Service;
import Lab2.validation.NotaValidator;
import Lab2.validation.StudentValidator;
import Lab2.validation.TemaValidator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class BigBangIntegration {

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
    public void testAddStudent(){

        service.deleteStudent("1");
        assertNull(studentRepository.findOne("1"));
        assertTrue(service.saveStudent("1", "Ale", 932) == 1);
        assertEquals(studentRepository.findOne("1").getNume(), "Ale");
    }

    @Test
    public void testAddAssignmentToRepository() {
        String idAssignment = "1";
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
    public void testAddGrade(){

        Pair<String, String> idGrade = new Pair<>("1", "1");
        double gr = 10;
        int week = 7;
        String feedback = "Great assignment";

        Nota grade = new Nota(idGrade, gr, week, feedback);

        gradeRepository.delete(idGrade);
        gradeRepository.save(grade);
        assert (gradeRepository.findOne(idGrade).getNota() == gr);
        assertEquals(gradeRepository.findOne(idGrade).getSaptamanaPredare(), week);
        assertEquals(gradeRepository.findOne(idGrade).getFeedback(), feedback);
    }

    @Test
    public void runAllTests(){
        testAddStudent();
        testAddAssignmentToRepository();
        testAddGrade();
    }


}