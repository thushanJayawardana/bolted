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
    private String sup_Id;
    private String sup_name;
    private String sup_mobile;
}
