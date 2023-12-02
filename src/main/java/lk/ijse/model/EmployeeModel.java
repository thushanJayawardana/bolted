package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.EmployeeDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeModel {
    private String splitEmployeeID(String currentEmployeeID){
        if (currentEmployeeID != null){
            String [] split = currentEmployeeID.split("00");

            int id = Integer.parseInt(split[1]);
            id++;
            return "E00" + id;
        }else {
            return "E001";
        }
    }
    public String generateNextEmployeeID() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT e_Id FROM employee ORDER BY e_Id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitEmployeeID(resultSet.getString(1));
        }
        return splitEmployeeID(null);
    }

    public boolean saveEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO employee VALUES (?,?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getE_Id());
        ptsm.setString(2, dto.getName());
        ptsm.setString(3,dto.getEmail());
        ptsm.setInt(4,dto.getMobile());
        ptsm.setString(5, dto.getPosition());

        return ptsm.executeUpdate() > 0;
    }

    public EmployeeDto searchEmployeeByID(String searchID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE e_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchID);
        ResultSet resultSet = ptsm.executeQuery();

        EmployeeDto dto = null;
        if (resultSet.next()){
            String Employee_id = resultSet.getString(1);
            String Employee_name = resultSet.getString(2);
            String Employee_email = resultSet.getString(3);
            int Employee_mobile = resultSet.getInt(4);
            String Employee_position = resultSet.getString(5);

            dto = new EmployeeDto(Employee_id, Employee_name, Employee_email, Employee_mobile, Employee_position);
        }
        return dto;
    }


    public boolean updateEmployee(EmployeeDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql ="UPDATE employee SET name = ?,email = ?,mobile = ?,position = ? WHERE e_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getName());
        ptsm.setString(2, dto.getEmail());
        ptsm.setInt(3, dto.getMobile());
        ptsm.setString(4,dto.getPosition());
        ptsm.setString(5, dto.getE_Id());

        return ptsm.executeUpdate() > 0;
    }

    public boolean deleteEmployee(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM employee WHERE e_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);
        return ptsm.executeUpdate() > 0;
    }

    public List<EmployeeDto> getAllEmployees() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<EmployeeDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new EmployeeDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getString(3),
                            resultSet.getInt(4),
                            resultSet.getString(5)
                    )
            );
        }
        return dtoList;
    }

    public EmployeeDto searchEmployeeByPhoneNumber(String searchInput) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM employee WHERE mobile = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchInput);

        ResultSet resultSet = ptsm.executeQuery();

        EmployeeDto dto = null;
        if (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            int tele = Integer.parseInt(resultSet.getString(4));
            String po = resultSet.getString(5);

            dto = new EmployeeDto(id,name,email,tele,po);
        }
        return dto;
    }
}
