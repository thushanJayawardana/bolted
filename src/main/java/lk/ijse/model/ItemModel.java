package lk.ijse.model;

import lk.ijse.db.DbConnection;
import lk.ijse.dto.ItemDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemModel {
    private String splitItemID(String currentItemID){
        if (currentItemID != null){
            String [] split = currentItemID.split("[I]");

            int id = Integer.parseInt(split[1]);
            id++;
            return String.format("I%03d",id);
        }else {
            return "I001";
        }
    }
    public String generateNextItemID() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT item_Id FROM item ORDER BY item_Id DESC LIMIT 1";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();
        if (resultSet.next()){
            return splitItemID(resultSet.getString(1));
        }
        return splitItemID(null);
    }
    public boolean saveItems(ItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "INSERT INTO item VALUES (?,?,?,?,?,?,?)";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,dto.getItem_Id());
        ptsm.setString(2,dto.getItem_description());
        ptsm.setString(3, String.valueOf(dto.getQty()));
        ptsm.setString(4, String.valueOf(dto.getPrice()));
        ptsm.setString(5,dto.getSup_Id());
        ptsm.setString(6,dto.getSup_name());
        ptsm.setString(7,String.valueOf(dto.getSup_mobile()));

        return ptsm.executeUpdate() > 0;
    }
    public boolean deleteItems(String id) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "DELETE FROM item WHERE item_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,id);

        return ptsm.executeUpdate() > 0;
    }
    public boolean updateItems(ItemDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "UPDATE item SET item_description = ?, qty = ?, price = ?, sup_Id = ?, sup_name = ?, sup_mobile = ? WHERE item_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1, dto.getItem_description());
        ptsm.setString(2, String.valueOf(dto.getQty()));
        ptsm.setString(3, String.valueOf(dto.getPrice()));
        ptsm.setString(4,dto.getSup_Id());
        ptsm.setString(5,dto.getSup_name());
        ptsm.setString(6, String.valueOf(dto.getSup_mobile()));
        ptsm.setString(7,dto.getItem_Id());

        return ptsm.executeUpdate() > 0;
    }
    public ItemDto searchProductById(String searchID) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM item WHERE item_Id = ?";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ptsm.setString(1,searchID);
        ResultSet resultSet = ptsm.executeQuery();

        ItemDto dto = null;
        if (resultSet.next()){
            String Item_id = resultSet.getString(1);
            String Item_desc = resultSet.getString(2);
            double Item_qty = Double.parseDouble(resultSet.getString(3));
            double Item_price = Double.parseDouble(resultSet.getString(4));
            String Item_su_id = resultSet.getString(5);
            String Item_su_name = resultSet.getString(6);
            String Item_su_mob = resultSet.getString(7);

            dto = new ItemDto(Item_id, Item_desc ,Item_qty ,Item_price, Item_su_id,Item_su_name,Item_su_mob);
        }
        return dto;
    }
    public List<ItemDto> getAllItems() throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();

        String sql = "SELECT * FROM item";
        PreparedStatement ptsm = connection.prepareStatement(sql);
        ResultSet resultSet = ptsm.executeQuery();

        ArrayList<ItemDto> dtoList = new ArrayList<>();

        while (resultSet.next()){
            dtoList.add(
                    new ItemDto(
                            resultSet.getString(1),
                            resultSet.getString(2),
                            resultSet.getDouble(3),
                            resultSet.getDouble(4),
                            resultSet.getString(5),
                            resultSet.getString(6),
                            resultSet.getString(7)
                    )
            );
        }
        return dtoList;
    }
}
