package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.RegisterDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterModel {
    public static boolean registerUser(RegisterDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO register VALUES (?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getUser_name());
        ptsm.setString(2, dto.getPassword());

        return ptsm.executeUpdate() > 0;
    }
    public static boolean isValidUser(String userName, String password) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM register WHERE user_name = ? AND password = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, userName);
        ptsm.setString(2,password);

        ResultSet resultSet = ptsm.executeQuery();

        return resultSet.next();
    }
    public static RegisterDto getUserInfo(String userName) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM register WHERE user_name = ?";
        try (PreparedStatement ptsm = connection.prepareStatement(sql)) {
            ptsm.setString(1, userName);

            try (ResultSet resultSet = ptsm.executeQuery()) {
                if (resultSet.next()) {
                    String retrievedUserName = resultSet.getString("user_name");
                    String retrievedPassword = resultSet.getString("password");

                    return new RegisterDto(retrievedUserName, retrievedPassword);
                }
            }
        }
        return null; // User isn't found
    }
    public static boolean check(String userName, String password) throws SQLException {
        return isValidUser(userName,password);
    }
}
