package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.controller.RegExPatterns.RegExPatterns;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.CustomerDto;
import lk.ijse.dto.tm.CustomerTm;
import lk.ijse.model.CustomerModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class CustomerFormController {

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colCustomerId;

    @FXML
    private TableColumn<?, ?> colMobile;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private Label lblCustomerID;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<CustomerTm> tblCustomer;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCustomerName;

    @FXML
    private TextField txtMobile;

    @FXML
    private TextField txtSearch;

    private CustomerModel customerModel=new CustomerModel();

    public void initialize(){//abstraction method
        setCellValueFactory();
        generateNextCustomerID();
        loadAllCustomers();
    }
    private void  generateNextCustomerID(){
        try {
            String previousCustomerID = lblCustomerID.getText();
            String customerID = customerModel.generateNextCustomer();
            lblCustomerID.setText(customerID);
            clearFields();
            if (btnClearPressed){
                lblCustomerID.setText(previousCustomerID);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
    private boolean btnClearPressed  = false;
    private void clearFields(){
        txtCustomerName.setText("");
        txtAddress.setText("");
        txtMobile.setText("");
        txtSearch.setText("");
    }

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        generateNextCustomerID();
    }

    private void setCellValueFactory() {
        colCustomerId.setCellValueFactory(new PropertyValueFactory<>("cust_Id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
    }
    private void loadAllCustomers() {
        ObservableList<CustomerTm> obList = FXCollections.observableArrayList();
        try {
            List<CustomerDto> dtoList = customerModel.getAllCustomer();
            for (CustomerDto dto : dtoList){
                obList.add(
                        new CustomerTm(
                                dto.getCust_Id(),
                                dto.getName(),
                                dto.getAddress(),
                                dto.getMobile()
                        )
                );
            }
            tblCustomer.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblCustomerID.getText();
        String name = txtCustomerName.getText();
        String address = txtAddress.getText();
        String mobile = txtMobile.getText();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidAddress = RegExPatterns.getValidAddress().matcher(address).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete.Name is Empty").showAndWait();
            return;
        }if (!isValidAddress){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete.Address is Empty").showAndWait();
            return;
        }if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Cannot Delete.Mobile Number is Empty").showAndWait();
        }else {
            try {
                boolean isDeleted = customerModel.deleteCustomers(id);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer is Deleted").show();
                    loadAllCustomers();
                    clearFields();
                    generateNextCustomerID();
                }else {
                    new Alert(Alert.AlertType.INFORMATION,"Customer Not Deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }


    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblCustomerID.getText();
        String name = txtCustomerName.getText();
        String address = txtAddress.getText();
        String mobile = txtMobile.getText();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidAddress = RegExPatterns.getValidAddress().matcher(address).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot save.Name Empty").showAndWait();
            return;
        }if (!isValidAddress){
            new Alert(Alert.AlertType.ERROR,"Invalid Address").showAndWait();
            return;
        }if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Invalid Phone Number").showAndWait();
        }else {

            var dto = new CustomerDto(id,name,address,mobile);
            try {
                boolean isSaved = customerModel.save(dto);
                if (isSaved){
                    new Alert(Alert.AlertType.CONFIRMATION,"Customer is Saved").show();
                    clearFields();
                    generateNextCustomerID();
                    loadAllCustomers();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Customer is Not Saved").show();
                }
            }catch (SQLException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblCustomerID.getText();
        String name = txtCustomerName.getText();
        String address = txtAddress.getText();
        String mobile = txtMobile.getText();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidAddress = RegExPatterns.getValidAddress().matcher(address).matches();
        boolean isValidMobile = RegExPatterns.getValidMobile().matcher(mobile).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Customers.Name is Empty").showAndWait();
            return;
        }if (!isValidAddress){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Customer.Address is Empty").showAndWait();
            return;
        }if (!isValidMobile){
            new Alert(Alert.AlertType.ERROR,"Cannot Update Customer.Phone Number is Empty").showAndWait();
        }else {
            try {
                var dto = new CustomerDto(id,name,address,mobile);
                try {
                    boolean isUpdated = customerModel.updateCustomer(dto);
                    if (isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Customer is Updated").show();
                        loadAllCustomers();
                        clearFields();
                        generateNextCustomerID();
                    }
                }catch (SQLException e){
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,"Invalid Mobile Number Format").showAndWait();
            }
        }
    }
    @FXML
    void btnReportOnAction(ActionEvent event) throws JRException, SQLException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/CustomerList1.jrxml");
        JasperDesign load = JRXmlLoader.load(resourceAsStream);
        JasperReport jasperReport = JasperCompileManager.compileReport(load);
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                null,
                DbConnection.getInstance().getConnection()
        );
        JasperViewer.viewReport(jasperPrint,false);
    }

    @FXML
    void txtAddressOnAction(ActionEvent event) {
        txtAddress.requestFocus();
    }

    @FXML
    void txtCustomerNameOnAction(ActionEvent event) {
        txtCustomerName.requestFocus();
    }

    @FXML
    void txtMobileOnAction(ActionEvent event) {
        txtMobile.requestFocus();
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String searchInput = txtSearch.getText();

        try {
            CustomerDto customerDto;
                customerDto = customerModel.searchCustomer(searchInput);

            if (customerDto != null) {
                lblCustomerID.setText(customerDto.getCust_Id());
                txtCustomerName.setText(customerDto.getName());
                txtAddress.setText(customerDto.getAddress());
                txtMobile.setText(customerDto.getMobile());
                txtSearch.setText("");
            } else {
                lblCustomerID.setText("");
                generateNextCustomerID();
                new Alert(Alert.AlertType.INFORMATION, "Customer not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }
}
