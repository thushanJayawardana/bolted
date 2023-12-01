package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.controller.RegExPatterns.RegExPatterns;
import lk.ijse.dto.SupplierDto;
import lk.ijse.dto.tm.SupplierTm;
import lk.ijse.model.SupplierModel;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class SupplierFormController {
    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colMobile;

    @FXML
    private TableColumn<?, ?> colSupplierName;

    @FXML
    private TableColumn<?, ?> colSupplyingProducts;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private Label lblID;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<SupplierTm> tblSuppliers;

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtSupplierName;

    @FXML
    private TextField txtSupplyingProducts;

    @FXML
    private DatePicker txtDate;


    private SupplierModel supplierModel=new SupplierModel();

    public void initialize(){
        setValueFactory();
        generateNextSupplierID();
        loadAllSuppliers();
    }
    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        generateNextSupplierID();
    }

    public void clearFields(){
        txtSupplierName.setText("");
        txtSupplyingProducts.setText("");
        txtMobile.setText("");
        txtSearch.setText("");
        txtDate.setValue(null);
    }

    public void generateNextSupplierID(){
        try {
            String previousSupplierID = lblID.getText();
            String supplierID = supplierModel.generateNextSupplierID();
            lblID.setText(supplierID);
            clearFields();
            if (btnClearIsPressed){
                lblID.setText(previousSupplierID);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    private boolean btnClearIsPressed= false;

    private void setValueFactory(){
        colId.setCellValueFactory(new PropertyValueFactory<>("sup_Id"));
        colSupplierName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSupplyingProducts.setCellValueFactory(new PropertyValueFactory<>("products"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
    }

    private void loadAllSuppliers(){
        var model = new SupplierModel();
        ObservableList<SupplierTm> obList = FXCollections.observableArrayList();
        try {
            List<SupplierDto> dtoList = model.getAllSuppliers();
            for (SupplierDto dto : dtoList){
                obList.add(
                        new SupplierTm(
                                dto.getSup_Id(),
                                dto.getName(),
                                dto.getProducts(),
                                dto.getMobile(),
                                dto.getDate()
                        )
                );
            }
            tblSuppliers.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblID.getText();
        String name = txtSupplierName.getText();
        String products=txtSupplyingProducts.getText();
        String mobile = txtMobile.getText();
        LocalDate date = txtDate.getValue();



        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidProduct = RegExPatterns.getValidProduct().matcher(products).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete Supplier.Name is empty").showAndWait();
            return;
        }if (date == null){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete Supplier.Date is empty").showAndWait();
            return;
        }if (!isValidProduct){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete Supplier.Product is empty").showAndWait();
            return;
        }if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete Supplier.Mobile Number is empty").showAndWait();
        }else {
            try {
                boolean isDeleted = supplierModel.deleteSuppliers(id);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier id Deleted").show();
                    clearFields();
                    generateNextSupplierID();
                    loadAllSuppliers();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Supplier is not Deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblID.getText();
        String name = txtSupplierName.getText();
        String products = txtSupplyingProducts.getText();
        String mobile = txtMobile.getText();
        LocalDate date = txtDate.getValue();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidProduct = RegExPatterns.getValidProduct().matcher(products).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot Save Supplier.Name is empty").showAndWait();
            return;
        }if (date == null){
            new Alert(Alert.AlertType.ERROR,"Cannot Save Supplier.Date is empty").showAndWait();
            return;
        }if (!isValidProduct){
            new Alert(Alert.AlertType.ERROR,"Cannot Save Supplier.Product is empty").showAndWait();
            return;
        }
        if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Cannot Save Supplier.Mobile Number is empty").showAndWait();
        }else {
            var dto = new SupplierDto(id, name, products, mobile,date);
            try {
                boolean isSaved = supplierModel.saveSupplier(dto);
                if (isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier is saved").show();
                    clearFields();
                    generateNextSupplierID();
                    loadAllSuppliers();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Supplier is not saved").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblID.getText();
        String name = txtSupplierName.getText();
        String products = txtSupplyingProducts.getText();
        String mobile = txtMobile.getText();
        LocalDate date = txtDate.getValue();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidProduct = RegExPatterns.getValidProduct().matcher(products).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();


        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Supplier.Name is empty").showAndWait();
            return;
        }if (date == null){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Supplier.Date is empty").showAndWait();
            return;
        }if (!isValidProduct){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Supplier.Product is empty").showAndWait();
            return;
        }if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Can not Update Supplier.Phone Number is empty").showAndWait();
        } else {
            var dto = new SupplierDto(id, name ,products ,mobile,date);
            try {
                boolean isUpdated = supplierModel.updateSuppliers(dto);
                if (isUpdated){
                    new Alert(Alert.AlertType.CONFIRMATION,"Supplier is Updated").show();
                    clearFields();
                    generateNextSupplierID();
                    loadAllSuppliers();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Supplier is Not Updated").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtMobileOnAction(ActionEvent event) {

    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String searchInput = txtSearch.getText();

        try {
            SupplierDto supplierDto;
                supplierDto = supplierModel.searchSupplierById(searchInput);
            if (supplierDto != null){
                lblID.setText(supplierDto.getSup_Id());
                txtSupplierName.setText(supplierDto.getName());
                txtSupplyingProducts.setText(supplierDto.getProducts());
                txtMobile.setText(supplierDto.getMobile());
                txtDate.setValue(supplierDto.getDate());
            } else {
                lblID.setText("");
                generateNextSupplierID();
                new Alert(Alert.AlertType.INFORMATION,"Supplier Not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void txtSupplierOnAction(ActionEvent event) {

    }

    @FXML
    void txtSupplyingProductsOnAction(ActionEvent event) {

    }
}
