package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDto {
    private String sup_Id;
    private String name;
    private String products;
    private String mobile;
    private LocalDate date;

}
