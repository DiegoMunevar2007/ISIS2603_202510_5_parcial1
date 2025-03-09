package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
        Optional<StudentEntity> estudianteEncontrado = studentRepository.findByLogin(loginStudent);
        Optional<CourseEntity> cursoEncontrado = courseRepository.findByCourseCode(courseCode);
        //Verificar que el estudiante y el curso existan
        if (estudianteEncontrado.isEmpty()){
            throw new InvalidRecordException("No existe un estudiante con el login "+loginStudent);
        }
        if (cursoEncontrado.isEmpty()){
            throw new InvalidRecordException("No existe un curso con el codigo "+courseCode);
        }
        if (grade<1.5 || grade>5){
            throw new InvalidRecordException("La nota para este curso no es valida");
        }

        //Crear el registro
        RecordEntity recordCreado = new RecordEntity();
        recordCreado.setCourse(cursoEncontrado.get());
        recordCreado.setFinalGrade(grade);
        recordCreado.setSemester(semester);
        recordCreado.setStudent(estudianteEncontrado.get());

        //Verificar que el estudiante no haya tomado el curso
        boolean cursoYaTomado=false;
        for (RecordEntity entidad : estudianteEncontrado.get().getRecords()){
            if (entidad.getCourse().equals(cursoEncontrado.get()) && entidad.getFinalGrade()>=3.0){
                throw new InvalidRecordException("No se puede generar un nuevo registro para este estudiante.");
            }
            else if (entidad.getCourse().equals(cursoEncontrado.get())){
                cursoYaTomado=true;
            }
        }
        //Si el curso ya fue tomado, se agrega el registro al estudiante
        //Si no, se agrega el registro al estudiante y el estudiante al curso
        if (cursoYaTomado){
            estudianteEncontrado.get().getRecords().add(recordCreado);
            return recordRepository.save(recordCreado);
        }
        cursoEncontrado.get().getStudents().add(estudianteEncontrado.get());
        estudianteEncontrado.get().getRecords().add(recordCreado);
        return recordRepository.save(recordCreado);    
       

    }
}
