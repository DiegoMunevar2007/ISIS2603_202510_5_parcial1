package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    /**
     * Tests the normal creation of a record for a student in a course
     */
    @Test
    void testCreateRecord() {
        assertDoesNotThrow(()->{
            recordService.createRecord(login, courseCode, 5.0, "4");
        });
        RecordEntity record = studentRepository.findByLogin(login).get().getRecords().get(0);
        assertNotEquals(0, studentRepository.findByLogin(login).get().getRecords().size());
        assertEquals(courseCode, record.getCourse().getCourseCode());   
        assertEquals(5.0, record.getFinalGrade());
        assertEquals("4", record.getSemester());
    }

    /**
     * Tests the creation of a record when the login of the student is wrong
     */
    @Test
    void testCreateRecordMissingStudent() {
        try{
            recordService.createRecord("", courseCode, 5.0, courseCode);
            fail("Debio haber lanzado una excepción porque el estudiante no existe");
        }
        catch (InvalidRecordException e){

        }
    }
    /**
     * Tests the creation of a record when the course code is wrong
     */
    @Test
    void testCreateInscripcionMissingCourse() {
        try{
            recordService.createRecord(login, null, 5.0, courseCode);
            fail("Debio haber lanzado una excepción porque el curso no existe");
            }
            catch (InvalidRecordException e){

            }
        }
    
    /**
     * Tests the creation of a record when the grade is not valid
     */
    @Test
    void testCreateInscripcionWrongGrade() {
        try{
            recordService.createRecord(login, courseCode, 0.0, courseCode);
            fail("Debio haber lanzado una excepción porque no es una nota válida");
            }
        catch (InvalidRecordException e){
        }

        try{
            recordService.createRecord(login, courseCode, 5.1, courseCode);
            fail("Debio haber lanzado una excepción porque no es una nota válida");
            }
        catch (InvalidRecordException e){
        }
    }

    /**
     * Tests the creation of a record when the student already has a passing grade
     * for the course
     */
    @Test
    void testCreateInscripcionRepetida1() {
        try{
            recordService.createRecord(login, courseCode, 5.0, courseCode);
            recordService.createRecord(login, courseCode, 4.0, courseCode);
            fail("Debio haber lanzado una excepción porque el estudiante ya pasó el curso");
        }
        catch (InvalidRecordException e){
        
        }
    }

    /**
     * Tests the creation of a record when the student already has a record for the
     * course, but he has not passed the course yet.
     */
    @Test
    void testCreateInscripcionRepetida2() {
        try{
            recordService.createRecord(login, courseCode, 2.0, courseCode);
            recordService.createRecord(login, courseCode, 4.0, courseCode);
            }
             catch (InvalidRecordException e){
                fail("No debio haber lanzado una excepción");
             }
    }
}
