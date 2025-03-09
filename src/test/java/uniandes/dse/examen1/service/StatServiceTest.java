package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.RecordService;
import uniandes.dse.examen1.services.StatsService;
import uniandes.dse.examen1.services.StudentService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class, StatsService.class })
public class StatServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StatsService statService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private TestEntityManager entityManager;

    private String loginEstudiante;
    private String codCurso;

    private PodamFactory factory = new PodamFactoryImpl();

    @BeforeEach
    void setUp()  {
        StudentEntity estudiante = factory.manufacturePojo(StudentEntity.class);
        loginEstudiante=estudiante.getLogin();
        entityManager.persist(estudiante);
        CourseEntity courseEntity = factory.manufacturePojo(CourseEntity.class);
        codCurso=courseEntity.getCourseCode();
        entityManager.persist(courseEntity);
    }

    // Hice dos pruebas para obtener buen coverage porque testFailure no era muy descriptiva, 
    @Test 
    void testCalculateStudentAverage() throws InvalidRecordException, RepeatedCourseException{
        RecordEntity recordCreado =recordService.createRecord(loginEstudiante, codCurso, 4.0,"2");
        entityManager.persist(recordCreado);
        assertEquals(4.0,statService.calculateStudentAverage(loginEstudiante));
        
        CourseEntity cursoNuevo = courseService.createCourse(factory.manufacturePojo(CourseEntity.class));
        entityManager.persist(cursoNuevo);
        recordService.createRecord(loginEstudiante,cursoNuevo.getCourseCode(),5.0,"3");
        assertEquals(4.5, statService.calculateStudentAverage(loginEstudiante));
    }

    @Test
    void testCalculateStudentAverageSinRegistros() {
        assertEquals(0.0,statService.calculateStudentAverage(loginEstudiante));
    }

    @Test
    void testCalculateCourseAverage() throws InvalidRecordException{
        RecordEntity recordCreado =recordService.createRecord(loginEstudiante, codCurso, 4.0,"2");
        entityManager.persist(recordCreado);
        assertEquals(4.0,statService.calculateCourseAverage(codCurso));
        
        StudentEntity estudiante = factory.manufacturePojo(StudentEntity.class);
        entityManager.persist(estudiante);
        recordService.createRecord(estudiante.getLogin(),codCurso,5.0,"3");
        assertEquals(4.5, statService.calculateCourseAverage(codCurso));

    }

    @Test
    void testCalculateCourseAverageSinRegistros() {
        assertEquals(0.0,statService.calculateCourseAverage(codCurso));
    }

    @Test
    void testCalculateCourseAverageCursosDiferentes() throws InvalidRecordException, RepeatedCourseException{
        RecordEntity recordCreado =recordService.createRecord(loginEstudiante, codCurso, 4.0,"2");
        entityManager.persist(recordCreado);
        CourseEntity cursoNuevo = courseService.createCourse(factory.manufacturePojo(CourseEntity.class));
        entityManager.persist(cursoNuevo);
        recordService.createRecord(loginEstudiante,cursoNuevo.getCourseCode(),5.0,"3");
        assertEquals(4.0,statService.calculateCourseAverage(codCurso));
    }
}
