package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.SupplierDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SupplierModel {
    private String splitSupplierID(String currentSupplierID) {
        if (currentSupplierID != null){
            String [] split = currentSupplierID.split("00");

            int id = Integer.parseInt(split[1]);
            id++;
            return "S00" + id;
        }else {
            return "S001";
        }
    }
    public String generateNextSupplierID() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT sup_Id FROM supplier ORDER BY sup_Id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitSupplierID(resultSet.getString(1));
        }
        return splitSupplierID(null);
    }
    public boolean saveSupplier(SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO supplier VALUES (?,?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getSup_Id());
        ptsm.setString(2, dto.getName());
        ptsm.setString(3, dto.getProducts());
        ptsm.setString(4,dto.getMobile());
        ptsm.setString(5, String.valueOf(dto.getDate()));

        return ptsm.executeUpdate() > 0;
    }
    public List<SupplierDto> getAllSuppliers() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT  * FROM supplier";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<SupplierDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new SupplierDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getString(4),
                            resultSet.getDate(5).toLocalDate()
                    )
            );
        }
        return dtoList;
    }
    public SupplierDto searchSupplierById(String searchId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM supplier WHERE sup_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchId);
        ResultSet resultSet = ptsm.executeQuery();

        SupplierDto dto = null;
        if (resultSet.next()) {
            String supplier_id = resultSet.getString(1);
            String supplier_name = resultSet.getString(2);
            String supplier_products = resultSet.getString(3);
            String supplier_mobile = resultSet.getString(4);
            LocalDate supplier_date = LocalDate.parse(resultSet.getString(5));

            dto = new SupplierDto(supplier_id ,supplier_name, supplier_products ,supplier_mobile,supplier_date);
        }
        return dto;
    }
    public boolean deleteSuppliers(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM supplier WHERE sup_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);

        return ptsm.executeUpdate() > 0;
    }

    public boolean updateSuppliers(SupplierDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE supplier SET name = ?,products = ?,mobile = ?,date = ? WHERE sup_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getName());
        ptsm.setString(2, dto.getProducts());
        ptsm.setString(3, dto.getMobile());
        ptsm.setString(4, String.valueOf(dto.getDate()));
        ptsm.setString(5, dto.getSup_Id());

        return ptsm.executeUpdate() > 0;
    }
}
