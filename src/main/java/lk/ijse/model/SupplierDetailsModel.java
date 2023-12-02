package lk.ijse.model;

import lk.ijse.db.DbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SupplierDetailsModel {
    public boolean save(String itemId, String supId) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO supplier_details VALUES (?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);

        ptsm.setString(1,itemId);
        ptsm.setString(2,supId);
        return ptsm.executeUpdate() > 0;
    }
}
