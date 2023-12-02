package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeTm {
    private String e_Id;
    private String name;
    private String email;
    private int mobile;
    private String position;

}
