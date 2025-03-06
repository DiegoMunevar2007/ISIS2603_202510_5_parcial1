package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.StudentRepository;

@Slf4j
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    public StudentEntity createStudent(StudentEntity newStudent) throws RepeatedStudentException {
       log.info("Se ha empezado la creación del estudiante con login"+newStudent.getLogin());
        Optional<StudentEntity> cursoEncontrado = studentRepository.findByLogin(newStudent.getLogin());
        if (cursoEncontrado.isPresent()){
            log.info("Ya existe un estudiante con el login "+cursoEncontrado.get().getLogin());
            throw new RepeatedStudentException("Ya existe un estudiante con el login "+cursoEncontrado.get().getLogin());
        }
        return studentRepository.save(newStudent);
    }
}
