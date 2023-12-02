package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeDto {
    private String e_Id;
    private String name;
    private String email;
    private int mobile;
    private String position;
}
