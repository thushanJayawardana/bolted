package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.CustomerDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerModel {
    private String splitCustomerID(String currentCustomerID){
        if (currentCustomerID != null){
            String [] split = currentCustomerID.split("00");

            int id = Integer.parseInt(split[1]);
            id++;
            return "C00" + id;
        }else {
            return "C001";
        }
    }

    public String generateNextCustomer() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT cust_Id FROM customer ORDER BY cust_Id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitCustomerID(resultSet.getString(1));
        }
        return splitCustomerID(null);
    }

    public boolean save(CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO customer VALUES (?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);

        ptsm.setString(1,dto.getCust_Id());
        ptsm.setString(2,dto.getName());
        ptsm.setString(3,dto.getAddress());
        ptsm.setString(4,dto.getMobile());

        return ptsm.executeUpdate() > 0;
    }
    public List<CustomerDto> getAllCustomer() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql ="SELECT * FROM customer";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<CustomerDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new CustomerDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4)
                    )
            );
        }
        return dtoList;
    }
    public boolean updateCustomer(CustomerDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE customer SET name = ?,address = ?,mobile = ? WHERE cust_Id =?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getName());
        ptsm.setString(2, dto.getAddress());
        ptsm.setString(3, String.valueOf(dto.getMobile()));
        ptsm.setString(4, dto.getCust_Id());

        return ptsm.executeUpdate() > 0;
    }
    public CustomerDto searchCustomer(String searchId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer WHERE cust_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchId);
        ResultSet resultSet = ptsm.executeQuery();

        CustomerDto dto = null;
        if (resultSet.next()){
            String customer_id = resultSet.getString(1);
            String customer_name = resultSet.getString(2);
            String customer_address = resultSet.getString(3);
            String customer_mobile = resultSet.getString(4);

            dto = new CustomerDto(customer_id,customer_name,customer_address,customer_mobile);
        }
        return dto;
    }

    public boolean deleteCustomers(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM customer WHERE cust_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);
        return ptsm.executeUpdate() > 0;
    }
    public boolean isValidCustomer(CustomerDto customerDto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM customer WHERE cust_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,customerDto.getCust_Id());

        ResultSet resultSet = ptsm.executeQuery();
        return resultSet.next();
    }
}
