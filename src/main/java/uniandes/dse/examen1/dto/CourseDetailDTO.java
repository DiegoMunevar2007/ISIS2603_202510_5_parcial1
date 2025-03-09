package uniandes.dse.examen1.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CourseDetailDTO extends CourseDTO {
    private List<StudentDTO> students= new ArrayList<>();

}
