package lk.ijse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {
    private String item_Id;
    private String item_description;
    private double qty;
    private double price;
    private String sup_id;
    private String sup_name;
    private int mobile;
}
