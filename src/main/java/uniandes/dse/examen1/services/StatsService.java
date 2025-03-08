package uniandes.dse.examen1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    @Autowired
    RecordRepository inscripcionRepository;

    public Double calculateStudentAverage(String login) {
        double average=0;
        List<RecordEntity> records = estudianteRepository.findByLogin(login).get().getRecords();
        for (RecordEntity record : records ){
            average+=record.getFinalGrade();
        }
        if (!records.isEmpty()){
            average=average/records.size();
        }
        return average;
    }

    public Double calculateCourseAverage(String courseCode) {
        int cantidadContada =0;
        double promedio =0;
        List<StudentEntity> estudiantes = cursoRepository.findByCourseCode(courseCode).get().getStudents();
        for (StudentEntity estudiante : estudiantes){
            for (RecordEntity record : estudiante.getRecords()){
                if (record.getCourse().getCourseCode().equals(courseCode)){
                    promedio+=record.getFinalGrade();
                    cantidadContada++;
                }
            }
        }
        if (cantidadContada>0){
            promedio=promedio/cantidadContada;
        }
        return promedio;
    }

}
