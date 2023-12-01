package lk.ijse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.db.DbConnection;
import lk.ijse.dto.ProductDto;
import lk.ijse.controller.RegExPatterns.RegExPatterns;
import lk.ijse.dto.tm.ProductTm;
import lk.ijse.model.ProductModel;
import lk.ijse.model.SupplierModel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;


public class ProductCategoryFormController {
    @FXML
    private TableColumn<?, ?> colId;

    @FXML
    private TableColumn<?, ?> colNameOfTheProduct;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuantity;

    @FXML
    private TableColumn<?, ?> colType;

    @FXML
    private Label lblProductId;

    @FXML
    private AnchorPane rootNode;

    @FXML
    private TableView<ProductTm> tblProducts;

    @FXML
    private TextField txtPrice;

    @FXML
    private TextField txtProductName;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtSearch;

    @FXML
    private TextField txtType;

    private ProductModel productModel= new ProductModel();

    private SupplierModel supplierModel= new SupplierModel();
    private Object RegEx;

    @FXML
    void btnClearOnAction(ActionEvent event) {
        clearFields();
        generateNextProductID();
    }

    public void initialize(){
        generateNextProductID();
        loadAllProducts();
        setValueFactory();
    }
    private void generateNextProductID() {
        try {
            String previousComplainID = lblProductId.getText();
            String complainID = ProductModel.generateNextProductID();
            lblProductId.setText(complainID);
            clearFields();
            if (btnClearPressed){
                lblProductId.setText(previousComplainID);
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    private boolean btnClearPressed=false;

    private void clearFields(){
        txtProductName.setText("");
        txtType.setText("");
        txtQuantity.setText("");
        txtPrice.setText("");
    }
    private void setValueFactory() {
        colId.setCellValueFactory(new PropertyValueFactory<>("p_Id"));
        colNameOfTheProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colQuantity.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private void loadAllProducts() {
        ObservableList<ProductTm> obList = FXCollections.observableArrayList();
        try {
            List<ProductDto> dtoList = productModel.getAllProducts();
            for (ProductDto dto : dtoList){
                obList.add(
                        new ProductTm(
                                dto.getP_Id(),
                                dto.getName(),
                                dto.getType(),
                                dto.getQty(),
                                dto.getPrice()
                        )
                );
            }
            tblProducts.setItems(obList);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    @FXML
    void btnDeleteOnAction(ActionEvent event) {
        String id = lblProductId.getText();
        String name = txtProductName.getText();
        String type= txtType.getText();
        String qtyText = txtQuantity.getText();
        String priceText = txtPrice.getText();

        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidType = RegExPatterns.getValidName().matcher(type).matches();
        boolean isValidQty = RegExPatterns.getValidDouble().matcher(qtyText).matches();
        boolean isValidPrice = RegExPatterns.getValidDouble().matcher(priceText).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR, "Can not Delete Product.Name is Empty").showAndWait();
            return;
        }if (!isValidType){
            new Alert(Alert.AlertType.ERROR, "Can not Delete Product.Type is Empty").showAndWait();
            return;
        }if (!isValidQty){
            new Alert(Alert.AlertType.ERROR, "Can not Delete Product.Quantity is Empty").showAndWait();
            return;
        }if (!isValidPrice){
            new Alert(Alert.AlertType.ERROR, "Can not Delete Product.Price is Empty").showAndWait();
        }else {
            try {
                boolean isDeleted = productModel.deleteProduct(id);
                if (isDeleted){
                    new Alert(Alert.AlertType.CONFIRMATION,"Product is Deleted").show();
                    clearFields();
                    generateNextProductID();
                    loadAllProducts();
                }else {
                    new Alert(Alert.AlertType.ERROR,"Product is Not Deleted").show();
                }
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnSaveOnAction(ActionEvent event) {
        String id = lblProductId.getText();
        String name = txtProductName.getText();
        String type= txtType.getText();
        String qtyText = txtQuantity.getText();
        String priceText=txtPrice.getText();


        boolean isValidName = RegExPatterns.getValidName().matcher(name).matches();
        boolean isValidQty = RegExPatterns.getValidDouble().matcher(qtyText).matches();
        boolean isValidPrice = RegExPatterns.getValidDouble().matcher(priceText).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR,"Not a Valid Product Name").showAndWait();
            return;
        }if (!isValidQty){
            new Alert(Alert.AlertType.ERROR,"Not a Valid Quantity").showAndWait();
            return;
        }if (!isValidPrice){
            new Alert(Alert.AlertType.ERROR,"Can not Save Product.Price is Empty").showAndWait();
        }
        else {
            try {
                double qty = Double.parseDouble(qtyText);
                double price = Double.parseDouble(priceText);


                var dto = new ProductDto(id,name,type,qty,price);
                try {
                    boolean isSaved = ProductModel.saveProduct(dto);
                    if (isSaved){
                        new Alert(Alert.AlertType.CONFIRMATION,"Item is Saved").show();
                        clearFields();
                        generateNextProductID();
                        loadAllProducts();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"Item is not Saved").show();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            }catch (NumberFormatException e) {
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void btnUpdateOnAction(ActionEvent event) {
        String id = lblProductId.getText();
        String productName = txtProductName.getText();
        String type=txtType.getText();
        String qtyText = txtQuantity.getText();
        String priceText = txtPrice.getText();

        boolean isValidName = RegExPatterns.getValidName().matcher(productName).matches();
        boolean isValidType = RegExPatterns.getValidName().matcher(type).matches();
        boolean isValidQty = RegExPatterns.getValidDouble().matcher(qtyText).matches();
        boolean isValidPrice = RegExPatterns.getValidDouble().matcher(priceText).matches();

        if (!isValidName){
            new Alert(Alert.AlertType.ERROR, "Can not Update Product.Name is Empty").showAndWait();
            return;
        }if (!isValidType){
            new Alert(Alert.AlertType.ERROR, "Can not Update Product.Type is Empty").showAndWait();
            return;
        }if (!isValidQty){
            new Alert(Alert.AlertType.ERROR, "Can not Update Product.Quantity is Empty").showAndWait();
            return;
        }if (!isValidPrice){
            new Alert(Alert.AlertType.ERROR, "Can not Update Product.Price is Empty").showAndWait();
        }else {
            try {
                double qty = Double.parseDouble(qtyText);
                double price = Double.parseDouble(priceText);

                var dto = new ProductDto(id,productName,type,qty,price);
                try {
                    boolean isUpdated = productModel.updateProducts(dto);
                    if (isUpdated){
                        new Alert(Alert.AlertType.CONFIRMATION,"Product Is Updated").show();
                        clearFields();
                        generateNextProductID();
                    }
                } catch (SQLException e) {
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            }catch (NumberFormatException e){
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
        }
    }

    @FXML
    void txtSearchOnAction(ActionEvent event) {
        String searchInput = txtSearch.getText();

        try {
            ProductDto productDto;
                productDto = ProductModel.searchProductById(searchInput);
            if (productDto != null ){
                lblProductId.setText(productDto.getP_Id());
                txtProductName.setText(productDto.getName());
                txtType.setText(productDto.getType());
                txtQuantity.setText(String.valueOf(productDto.getQty()));
                txtPrice.setText(String.valueOf(productDto.getPrice()));
            }else {
                lblProductId.setText("");
                generateNextProductID();
                new Alert(Alert.AlertType.CONFIRMATION,"Product Not Found").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }
    @FXML
    void btnReportOnAction(ActionEvent event) throws SQLException, JRException {
        InputStream resourceAsStream = getClass().getResourceAsStream("/report/product.jrxml");
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
    void txtPriceOnAction(ActionEvent event) {

    }

    @FXML
    void txtProductNameOnAction(ActionEvent event) {

    }

    @FXML
    void txtQuantityOnAction(ActionEvent event) {

    }
    @FXML
    void txtTypeOnAction(ActionEvent event) {

    }
}
