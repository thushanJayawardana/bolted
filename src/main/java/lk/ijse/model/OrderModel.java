package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.OrderDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class OrderModel {
    private ProductModel productModel = new ProductModel();
    private OrderDetailModel orderDetailModel = new OrderDetailModel();
    private String splitOrderID(String currentOrderID){
        if (currentOrderID != null){
            String [] split = currentOrderID.split("[O]");

            int id = Integer.parseInt(split[1]);
            id++;
            return String.format("O%03d",id);
        }else {
            return "O001";
        }
    }
    public String generateNextOrderID() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT order_Id FROM orders ORDER BY order_Id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitOrderID(resultSet.getString(1));
        }
        return splitOrderID(null);
    }
    public boolean placeOrder(OrderDto orderDto) throws SQLException {
        String orderId = orderDto.getOrder_Id();
        LocalDate date = orderDto.getDate();
        String customerId = orderDto.getCustomer_Id();

        Connection connection = null;
        try {
            connection = DbConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved = saveOrder(orderId,date,customerId);
            if (isOrderSaved){
                boolean isUpdated = productModel.updateProduct(orderDto.getOrderTmList());
                if (isUpdated){
                    boolean isOrderDetailSaved = orderDetailModel.saveOrderDetails(orderDto.getOrder_Id(),orderDto.getOrderTmList());
                    if (isOrderDetailSaved){
                        connection.commit();
                    }
                }
            }
            connection.rollback();
        } finally {
            connection.setAutoCommit(true);
        }
        return true;
    }
    private boolean saveOrder(String orderId, LocalDate date, String customerId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO orders VALUES (?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,orderId);
        ptsm.setString(2, String.valueOf(date));
        ptsm.setString(3,customerId);
        return ptsm.executeUpdate() > 0;
    }
}
