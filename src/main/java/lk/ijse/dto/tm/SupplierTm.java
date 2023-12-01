package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class SupplierTm {
    private String sup_Id;
    private String name;
    private String products;
    private String mobile;
    private LocalDate date;
}
