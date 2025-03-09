package uniandes.dse.examen1.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class StudentDetailDTO extends StudentDTO {
    private List<CourseDTO> courses = new ArrayList<>();
    private List<RecordDTO> records = new ArrayList<>();

}
