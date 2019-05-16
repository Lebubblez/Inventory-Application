package inventoryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Modality;
import inventoryapp.Models.*;
import javafx.scene.control.Alert;
import inventoryapp.Models.Product;
import inventoryapp.Models.OutsourcedPart;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static inventoryapp.Models.Inventory.*;

public class FXMLDocumentController {

    @FXML
    private TextField searchPartsField;
    @FXML
    private TextField searchProductsField;
    @FXML
    private TableView<Part> partsTableView;
    @FXML
    private TableColumn<Part, Integer> partsIDColumn;
    @FXML
    private TableColumn<Part, String> partsNameColumn;
    @FXML
    private TableColumn<Part, Integer> partsInStockColumn;
    @FXML
    private TableColumn<Part, Double> partsPriceColumn;
    @FXML
    private TableView<Product> productsTableView;
    @FXML
    private TableColumn<Product, Integer> productsIDColumn;
    @FXML
    private TableColumn<Product, String> productsNameColumn;
    @FXML
    private TableColumn<Product, Integer> productsInStockColumn;
    @FXML
    private TableColumn<Product, Double> productsPriceColumn;
    @FXML
    public ObservableList<Part> tempPart = FXCollections.observableArrayList();
    public ObservableList<Product> tempProduct = FXCollections.observableArrayList();
    private Part newPart;
    private Product newProduct;
    private InventoryApp inventoryApp;
    private static int index;

    public FXMLDocumentController() {
    }

    //method to grab selected part or product index from other controllers
    public static int modifyIndex() {
        return index;
    }

    //Parts Section
    //add part button
    @FXML
    void partsAddHandler(ActionEvent event) throws IOException {
        boolean okClicked = inventoryApp.showPartScreen();
    }

    //delete part button
    @FXML
    void partsDeleteHandler(ActionEvent event) {
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        if (part != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete " + part.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                deletePart(part);
            } else {
                alert.close();
            }
        }
    }

    //parts modify button
    @FXML
    void partsModifyHandler(ActionEvent event) {
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        index = getAllParts().indexOf(selectedPart);
        if (selectedPart != null) {
            boolean saveClicked = InventoryApp.showModifyPartScreen(selectedPart);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No part selected");
            alert.setContentText("Please select a part in the Table");
            alert.showAndWait();
        }
    }

    //parts search handler
    @FXML
    void partsSearchHandler(ActionEvent event) {
        //retrieves text, & if searchItem is string, it retrieves the part searched for
        String searchItem = searchPartsField.getText();
        if (searchItem.equals("")) {
            partsTableView.setItems(getAllParts());
        } else {
            //if not found, it searches by item number, then adds and retrives to the tempProduct
            boolean found = false;
            try {
                int itemNumber = Integer.parseInt(searchItem);
                Part p = Inventory.lookupPart(itemNumber);
                if (p != null) {
                    found = true;
                    tempPart.clear();
                    tempPart.add(p);
                    //shows temp table for the searched products
                    partsTableView.setItems(tempPart);
                }
                //if not found by text or item number, an error alert will show
                if (found == false) {
                    partsTableView.setItems(getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error Message");
                    alert.setHeaderText("Part Not Found");
                    alert.setContentText("Please search by exact name or ID #");

                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                for (Part p : getAllParts()) {
                    if (p.getName().equals(searchItem)) {
                        System.out.println("This is part " + p.getPartID());
                        found = true;
                        tempPart.clear();
                        tempPart.add(p);
                        //shows temp table for the searched products
                        partsTableView.setItems(tempPart);
                    }
                }
                //if not found by text or item number, an error alert will show
                if (found == false) {
                    partsTableView.setItems(getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Part Not Found");
                    alert.setContentText("Please search by exact name or ID #");
                    alert.showAndWait();
                }
            }
        }

    }

    //Products Section
    //products add button
    @FXML
    void productsAddHandler(ActionEvent event) {
        inventoryApp.showProductScreen();
    }

    //products delete button
    @FXML
    void productsDeleteHandler() {
        Product product = productsTableView.getSelectionModel().getSelectedItem();
        if (product != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion of Product");
            alert.setHeaderText("Are you sure you want to delete " + product.getName() + "?");
            alert.setContentText("Product has associated Parts");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                System.out.println("Product Deleted");
                removeProduct(product);
            } else {
                alert.close();
            }
        }
    }

    //products modify button
    @FXML
    void productsModifyHandler(ActionEvent event) {
        Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
        index = getProducts().indexOf(selectedProduct);
        if (selectedProduct != null) {
            InventoryApp.showModifyProductScreen(selectedProduct);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No product selected");
            alert.setContentText("Please select a product in the Table");
            alert.showAndWait();
        }
    }

    //products search handler
    @FXML
    void productsSearchHandler(ActionEvent event) {
        //retrieves text, & if searchItem is string, it retrieves the product searched for
        String searchItem = searchProductsField.getText();
        if (searchItem.equals("")) {
            productsTableView.setItems(getProducts());
        } else {
            //if not found, it searches by item number, then adds and retrives to the tempProduct
            boolean found = false;
            try {
                int itemNumber = Integer.parseInt(searchItem);
                Product p = Inventory.lookupProduct(itemNumber);
                if (p != null) {
                    found = true;
                    tempProduct.clear();
                    tempProduct.add(p);
                    //shows temp table for the searched products
                    productsTableView.setItems(tempProduct);
                }
                //if not found by text or item number, an error alert will show
                if (found == false) {
                    productsTableView.setItems(getProducts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Product not found");
                    alert.setContentText("Please search by exact Product Name or ID #");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                for (Product p : Inventory.getProducts()) {
                    if (p.getName().equals(searchItem)) {
                        found = true;
                        tempProduct.clear();
                        tempProduct.add(p);
                        //shows temp table for the searched products
                        productsTableView.setItems(tempProduct);
                    }

                }
                if (found == false) {
                    productsTableView.setItems(getProducts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Product not found");
                    alert.setContentText("Please search by ProductID or exact Product Name");
                    alert.showAndWait();
                }
            }
        }
    }


    //exit buttons
    @FXML
    void exitHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm Exit");
        alert.setHeaderText("Do you want to exit?");
        alert.setContentText("Any unsaved changes will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            System.exit(0);
        } else {
            System.out.println("Cancel was clicked.");
        }
    }

    //Input for all existing parts as an example for the table
    void existingParts() {
        int partID = Inventory.getPartIDCount();
        InHousePart camPart1 = new InHousePart(partID, "Part", 25.00, 80, 0, 999, 7000);
        Inventory.addPart(camPart1);
        OutsourcedPart camPart2 = new OutsourcedPart(getPartIDCount(), "PartAgain", 249.99, 20, 0, 999, "Best Company");
        Inventory.addPart(camPart2);
        OutsourcedPart camPart3 = new OutsourcedPart(getPartIDCount(), "PartAgainX2", 999.99, 15, 0, 999, "Best Company");
        Inventory.addPart(camPart3);
    }
//input for all existing products as an example for the table
    void existingProducts() {
        int productID = Inventory.getProductIDCount();
        ArrayList<Part> sysParts1 = new ArrayList<>();
        sysParts1.add(lookupPart(1));
        Product sysProduct1 = new Product(productID, "BestProduct", 249.99, 18, 1, 999, sysParts1);
        Inventory.addProduct(sysProduct1);
        ArrayList<Part> sysParts2 = new ArrayList<>();
        sysParts2.add(lookupPart(3));
        Product sysProduct2 = new Product(Inventory.getProductIDCount(), "BetterProduct", 999.99, 10, 1, 999, sysParts2);
        Inventory.addProduct(sysProduct2);
    }

    /* Visually sets up the columns for the Part and Product table on mainscreen by
    retreiving cell data for table*/
    @FXML
    private void initialize() {
        partsIDColumn.setCellValueFactory(
                cellData -> cellData.getValue().partIDProperty().asObject());
        partsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        partsInStockColumn.setCellValueFactory(
                cellData -> cellData.getValue().inStockProperty().asObject());
        partsPriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().priceProperty().asObject());
        productsIDColumn.setCellValueFactory(
                cellData -> cellData.getValue().productIDProperty().asObject());
        productsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().productNameProperty());
        productsInStockColumn.setCellValueFactory(
                cellData -> cellData.getValue().productInStockProperty().asObject());
        productsPriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().productPriceProperty().asObject());
    }

    //Sets the existing products and parts on mainscreen tables to view
    public void setMainApp(InventoryApp inventoryApp) {
        this.inventoryApp = inventoryApp;
        existingParts();
        existingProducts();
        partsTableView.setItems(getAllParts());
        productsTableView.setItems(getProducts());
    }
}
