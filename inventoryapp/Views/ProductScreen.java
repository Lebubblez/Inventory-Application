package inventoryapp.Views;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import inventoryapp.Models.Inventory;
import inventoryapp.Models.Part;
import inventoryapp.Models.Product;

import java.util.ArrayList;
import java.util.Optional;

import static inventoryapp.Models.Inventory.getAllParts;
import static inventoryapp.FXMLDocumentController.modifyIndex;

public class ProductScreen {
    @FXML
    private TextField productIDField;
    @FXML
    private TextField productNameField;
    @FXML
    private TextField productInStockField;
    @FXML
    private TextField productPriceField;
    @FXML
    private TextField productMaxField;
    @FXML
    private TextField productMinField;
    @FXML
    private TextField productSearchField;
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
    private TableView<Part> associatedPartsTableView;
    @FXML
    private TableColumn<Part, Integer> associatedPartsIDColumn;
    @FXML
    private TableColumn<Part, String> associatedPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> associatedPartsInStockColumn;
    @FXML
    private TableColumn<Part, Double> associatedPartsPriceColumn;

    private boolean buttonClick = false;
    private int productID;
    private Product selectedProduct;
    private ObservableList<Part> currentParts = FXCollections.observableArrayList();
    public ObservableList<Part> tempPart=FXCollections.observableArrayList();
    int productIndex = modifyIndex();
    private Stage dialogStage;

    //initializes table on main screen to contain products
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
        associatedPartsIDColumn.setCellValueFactory(
                cellData -> cellData.getValue().partIDProperty().asObject());
        associatedPartsNameColumn.setCellValueFactory(
                cellData -> cellData.getValue().nameProperty());
        associatedPartsInStockColumn.setCellValueFactory(
                cellData -> cellData.getValue().inStockProperty().asObject());
        associatedPartsPriceColumn.setCellValueFactory(
                cellData -> cellData.getValue().priceProperty().asObject());
        productID = Inventory.getProductIDCount();
        productIDField.setText("Auto-Gen: " + productID);
        productInStockField.setText(Integer.toString(0));
        partsTableView.setItems(getAllParts());
    }


    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isButtonClick() {
        return buttonClick;
    }

    @FXML
    void productModifySaveHandler(ActionEvent event) {
        if (isProductValid()) {
            String id = productIDField.getText();
            String name = productNameField.getText();
            String inStock = productInStockField.getText();
            String price = productPriceField.getText();
            String min = productMinField.getText();
            String max = productMaxField.getText();

            Product modProduct = new Product();
            modProduct.setProductID(Integer.parseInt(id));
            modProduct.setName(name);
            modProduct.setInStock(Integer.parseInt(inStock));
            modProduct.setPrice(Double.parseDouble(price));
            modProduct.setMin(Integer.parseInt(min));
            modProduct.setMax(Integer.parseInt(max));
            ArrayList<Part> parts = new ArrayList<>();
            parts.addAll(associatedPartsTableView.getItems());
            modProduct.setAssociatedParts(parts);
            Inventory.updateProduct(productIndex, modProduct);

            buttonClick = true;
            Inventory.cancelProductIDCount();
            dialogStage.close();
        }
    }

    //handles cancel button functions for products
    @FXML
    void productCancelHandler(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Are you sure you want to Cancel?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            productID = Inventory.cancelProductIDCount();
            dialogStage.close();
        } else {
            alert.close();
        }
    }

    //handles delete button functions for products
    @FXML
    void productPartDeleteHandler(ActionEvent event) {
        //selects item in table
        Part part = associatedPartsTableView.getSelectionModel().getSelectedItem();
        //checks the number of items in the list
        int partSize = associatedPartsTableView.getItems().size();
        //prompts delete correclty if there is 1 or more items avaible
        if (partSize > 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete");
            alert.setHeaderText("Are you sure you want to delete " + part.getName() + " from Associated Parts?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                currentParts.remove(part);
                associatedPartsTableView.setItems(currentParts);
            } else {
                alert.close();
            }
            //if there are no items to select, an error message that says cannot delete will show
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Parts Error");
            alert.setHeaderText("Cannot delete " + part.getName());
            alert.setContentText("There are not parts in products!\n");
            alert.showAndWait();
        }
    }

    //adds parts over to the product handler in associated parts table
    @FXML
    void partsToProductHandler(ActionEvent event) {
        //selects part from table view
        Part part = partsTableView.getSelectionModel().getSelectedItem();
        //adds the selects part from table view, to current parts
        currentParts.add(part);
        //adds the current parts to the associated part table
        associatedPartsTableView.setItems(currentParts);
    }

    //handles product searching
    @FXML
    void productSearchHandler(ActionEvent event) {
        //gets the searched text and stores into searchItem
        String searchItem=productSearchField.getText();
        //if search is a string, check if product name matches search item. If so, it adds to tempPart
        //to show as a found search result
        if (searchItem.equals("")){
            partsTableView.setItems(getAllParts());
        } else{
            boolean found=false;
            //if string not found, checks if a number is given to search as an ID
            try{
                int itemNumber=Integer.parseInt(searchItem);
                Part p = Inventory.lookupPart(itemNumber);
                if(p != null){
                    found=true;
                    tempPart.clear();
                    tempPart.add(p);
                    partsTableView.setItems(tempPart);
                }
                //if ID number not found, an error alert will show
                if (found==false){
                    partsTableView.setItems(getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Part Not Found");
                    alert.setContentText("Please search by exact name or ID #");

                    alert.showAndWait();
                }
            }
            //checks the product name matching search, and adds to tempPart as a result
            catch(NumberFormatException e){
                for(Part p: Inventory.getAllParts()){
                    if(p.getName().equals(searchItem)){
                        found=true;
                        tempPart.clear();
                        tempPart.add(p);
                        partsTableView.setItems(tempPart);
                    }

                }
                //if string not found, an error message will show
                if (found==false){
                    partsTableView.setItems(getAllParts());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Error");
                    alert.setHeaderText("Part Not Found");
                    alert.setContentText("Please search by exact name or ID");

                    alert.showAndWait();
                }
            }
        }
    }
    @FXML
    void productSaveHandler(ActionEvent event) {
        if (isProductValid()) {
            String name = productNameField.getText();
            String inStock = productInStockField.getText();
            String price = productPriceField.getText();
            String min = productMinField.getText();
            String max = productMaxField.getText();

            Product newProduct = new Product();
            newProduct.setProductID(productID);
            newProduct.setName(name);
            newProduct.setInStock(Integer.parseInt(inStock));
            newProduct.setPrice(Double.parseDouble(price));
            newProduct.setMin(Integer.parseInt(min));
            newProduct.setMax(Integer.parseInt(max));
            ArrayList<Part> parts = new ArrayList<>();
            parts.addAll(associatedPartsTableView.getItems());
            newProduct.setAssociatedParts(parts);
            Inventory.addProduct(newProduct);

            buttonClick = true;
            dialogStage.close();
        }
    }

    private boolean isProductValid() {
        String name = productNameField.getText();
        String inStock = productInStockField.getText();
        String price = productPriceField.getText();
        String min = productMinField.getText();
        String max = productMaxField.getText();
        int partSize = associatedPartsTableView.getItems().size();
        ArrayList<Part> parts = new ArrayList<>();
        parts.addAll(associatedPartsTableView.getItems());

        String errorMessage = "";
        if (name == null || name.length() == 0) {
            errorMessage += "No valid product name!\n";
        }
        if (inStock == null || inStock.length() == 0) {
            productInStockField.setText(Integer.toString(0));
            errorMessage += "No valid Inventory value!\n";
        } else {
            try {
                int inStockComp = Integer.parseInt(inStock);
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (inStockComp < minComp || inStockComp > maxComp) {
                    errorMessage += "Inventory must be between the minimum or maximum value!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid Inventory value (must be an integer)!\n";
            }
        }
        if (price == null || price.length() == 0) {
            errorMessage += "No valid price!\n";
        } else {
            try {
                double productPrice = Double.parseDouble(price);
                double partsPrice = 0;
                for (Part part : parts) {
                    partsPrice = partsPrice + part.getPrice();
                }

                if (partsPrice > productPrice) {
                    errorMessage += "Product price must be higher than the sum of its parts!\n";
                }

            } catch (NumberFormatException e) {
                errorMessage += "No valid Price (must be a double)!\n";
            }
        }
        if (min == null || min.length() == 0) {
            errorMessage += "No valid Min value!\n";
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid Min value (must be an integer)!\n";
            }
        }
        if (max == null || max.length() == 0) {
            errorMessage += "No valid Max value!\n";
        } else {
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp ) {
                    errorMessage += "Maximum value must be greater than Minimum!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid Max value (must be an integer)!\n";
            }
        }
        if (partSize == 0) {
            errorMessage += "Product must have at least one Part!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public void setProduct(Product product) {
        selectedProduct = product;
        currentParts = selectedProduct.getObservableAssociatedParts();

        productIDField.setText(Integer.toString(product.getProductID()));
        productNameField.setText(product.getName());
        productInStockField.setText(Integer.toString(product.getInStock()));
        productPriceField.setText(Double.toString(product.getPrice()));
        productMinField.setText(Integer.toString(product.getMin()));
        productMaxField.setText(Integer.toString(product.getMax()));
        associatedPartsTableView.setItems(currentParts);
    }

}

