package lk.ijse.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderTm {
    private String order_Id;
    private String product_Id;
    private String description;
    private double unit_Price;
    private int qty;
    private double total;

}
