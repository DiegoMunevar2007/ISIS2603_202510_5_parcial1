package uniandes.dse.examen1.dto;

import lombok.Data;

@Data
public class RecordDTO {

    private Long id;
    private String name;
    private String grade;
    private String semester;
    private StudentDTO student; 
    private CourseDTO course;

}
