package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.ProductDto;
import lk.ijse.dto.tm.OrderTm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductModel {

    private static String splitProductID(String currentItemID) {
        if (currentItemID != null) {
            String[] split = currentItemID.split("00");

            int id = Integer.parseInt(split[1]);
            id++;
            return "P00" + id;
        } else {
            return "P001";
        }
    }

        public static String generateNextProductID() throws SQLException {
            Connection connection = DbConnection.getInstance().getConnection();

            String sql = "SELECT p_Id FROM product ORDER BY p_Id DESC LIMIT 1";
            PreparedStatement ptsm = connection.prepareStatement(sql);
            ResultSet resultSet = ptsm.executeQuery();
            if (resultSet.next()){
                return splitProductID(resultSet.getString(1));
            }
            return splitProductID(null);
        }

    public static boolean saveProduct(ProductDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO product VALUES (?,?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,dto.getP_Id());
        ptsm.setString(2,dto.getName());
        ptsm.setString(3,dto.getType());
        ptsm.setDouble(4,dto.getQty());
        ptsm.setDouble(5,dto.getPrice());

        return ptsm.executeUpdate() > 0;
    }
    public List<ProductDto> getAllProducts() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM product";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<ProductDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new ProductDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getDouble(4),
                            resultSet.getDouble(5)
                    )
            );
        }
        return dtoList;
    }
    public static ProductDto searchProductById(String searchId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM product WHERE p_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchId);
        ResultSet resultSet = ptsm.executeQuery();

        ProductDto dto = null;
        if (resultSet.next()){
            String Product_id = resultSet.getString(1);
            String Product_name = resultSet.getString(2);
            String Product_type = resultSet.getString(3);
            double Product_qty = Double.parseDouble(resultSet.getString(4));
            double Product_price = Double.parseDouble(resultSet.getString(5));

            dto = new ProductDto(Product_id, Product_name,Product_type, Product_qty ,Product_price);
        }
        return dto;
    }
    public boolean updateProducts(ProductDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE product SET name = ?, type = ?, qty = ?, price = ? WHERE p_id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,dto.getName());
        ptsm.setString(2,dto.getType());
        ptsm.setString(3, String.valueOf(dto.getQty()));
        ptsm.setString(4, String.valueOf(dto.getPrice()));
        ptsm.setString(5,dto.getP_Id());

        return ptsm.executeUpdate() > 0;
    }
    public boolean updateProduct(List<OrderTm> orderTmList) throws SQLException {
        for (OrderTm tm: orderTmList){
            if (!updateQty(tm.getProduct_Id(),tm.getQty())){
                return false;
            }
        }
        return true;
    }
    private boolean updateQty(String productId, int qty) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE products SET qty = qty - ? WHERE p_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setDouble(1,qty);
        ptsm.setString(2,productId);

        return ptsm.executeUpdate() > 0;
    }
    public boolean deleteProduct(String p_Id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM product WHERE p_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,p_Id);
        return ptsm.executeUpdate() > 0;
    }
}
