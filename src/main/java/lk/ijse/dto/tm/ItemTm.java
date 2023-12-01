package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemTm {
    private String item_Id;
    private String sup_Id;
    private String sup_name;
    private String item_description;
    private double qty;
    private double price;
}
