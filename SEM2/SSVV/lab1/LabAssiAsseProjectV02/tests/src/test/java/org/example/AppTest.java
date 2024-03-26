package org.example;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.example.domain.Student;
import org.example.repository.NotaXMLRepo;
import org.example.repository.StudentFileRepository;
import org.example.repository.StudentXMLRepo;
import org.example.repository.TemaXMLRepo;
import org.example.service.Service;
import org.example.validation.NotaValidator;
import org.example.validation.StudentValidator;
import org.example.validation.TemaValidator;
import org.example.validation.ValidationException;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }


    public void testAddStudent_correctInput_shouldReturnAddedStudent() {
        var addedStudent = new Student(String.valueOf(Math.random() * 100000000), "John", 933, "david@gmail.com");
        String filenameStudent = "../fisiere/Studenti.xml";
        String filenameTema = "../fisiere/Teme.xml";
        String filenameNota = "../fisiere/Note.xml";

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        var returned = service.addStudent(addedStudent);

        assertTrue(returned == null);
    }

    public void testAddStudent_invalidInput_shouldThrowValidationException() {
        var addedStudent = new Student(String.valueOf(Math.random() * 100000000), "John", 933, "david@gmail.com");
        String filenameStudent = "../fisiere/Studenti.xml";
        String filenameTema = "../fisiere/Teme.xml";
        String filenameNota = "../fisiere/Note.xml";

        StudentValidator studentValidator = new StudentValidator();
        TemaValidator temaValidator = new TemaValidator();
        StudentXMLRepo studentXMLRepository = new StudentXMLRepo(filenameStudent);
        TemaXMLRepo temaXMLRepository = new TemaXMLRepo(filenameTema);
        NotaValidator notaValidator = new NotaValidator(studentXMLRepository, temaXMLRepository);
        NotaXMLRepo notaXMLRepository = new NotaXMLRepo(filenameNota);
        Service service = new Service(studentXMLRepository, studentValidator, temaXMLRepository, temaValidator, notaXMLRepository, notaValidator);

        var returned = service.addStudent(addedStudent);

        assertTrue(returned == null);

        try {
            service.addStudent(new Student("1", "John", 933, ""));
            fail();
        } catch (ValidationException e) {
            assertTrue(true);
        } catch (Exception e) {
            fail();
        }
    }
}
