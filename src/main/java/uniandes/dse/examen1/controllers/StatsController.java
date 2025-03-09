package uniandes.dse.examen1.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import uniandes.dse.examen1.services.StatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;


@RestController
@RequestMapping("/stats")
@Import({StatsService.class, ModelMapper.class})
public class StatsController {

    @Autowired
    private StatsService statsService;

    @Autowired
    private ModelMapper modelMapper;
    
    @GetMapping(value="/cursos/{codCurso}")
    @ResponseStatus(code=HttpStatus.OK)
    public Double getCourseAverage(@PathVariable String codCurso) {
        return statsService.calculateCourseAverage(codCurso);
    }

    @GetMapping(value = "/estudiantes/{login}")
    @ResponseStatus(code=HttpStatus.OK)
    public Double getStudentAverage(@PathVariable String login) {
        return statsService.calculateStudentAverage(login);
    }

    
}
