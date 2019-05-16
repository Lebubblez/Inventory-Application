package inventoryapp.Views;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import inventoryapp.Models.InHousePart;
import inventoryapp.Models.Inventory;
import inventoryapp.Models.OutsourcedPart;
import inventoryapp.Models.Part;

import java.io.IOException;
import java.util.Optional;

import static inventoryapp.FXMLDocumentController.modifyIndex;

public class PartScreen {

    private int partID;
    private Part part;
    private Part selectedPart;
    private Stage dialogStage;
    private boolean okClicked = false;
    int partIndex = modifyIndex();
    private InHousePart selectedInPart;
    private OutsourcedPart selectedOutPart;

    @FXML
    private TextField partIDField;
    @FXML
    private TextField partNameField;
    @FXML
    private TextField partInStockField;
    @FXML
    private TextField partPriceField;
    @FXML
    private TextField companyMachineField;
    @FXML
    private TextField partMaxField;
    @FXML
    private TextField partMinField;
    @FXML
    private RadioButton inhouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    @FXML
    private Label companyMachineLabel;
    @FXML
    private ToggleGroup partToggleGroup;

    private boolean isOutsourced;

    //checks if a button is clicked or not
    public boolean isOkClicked() {
        return okClicked;
    }

    //used as the partID number autoGenerator
    @FXML
    private void initialize() {
        //references partID from Modules.Inventory
        this.inhouseRadioButton.setToggleGroup(partToggleGroup);
        this.outsourcedRadioButton.setToggleGroup(partToggleGroup);
        partID = Inventory.getPartIDCount();
        partIDField.setText("Auto-Generated: " + partID);
    }

    //uses the boolean to determine which radio button is selected
    //inHouse radio button prompt text and label
    
    public void radioHandler()
    {
         if (this.partToggleGroup.getSelectedToggle().equals(this.inhouseRadioButton)){
            companyMachineLabel.setText("Machine ID");
            companyMachineField.setPromptText("Machine ID");
            outsourcedRadioButton.selectedProperty().set(false);
         }
         if (this.partToggleGroup.getSelectedToggle().equals(this.outsourcedRadioButton)){
            companyMachineLabel.setText("Company Name");
            companyMachineField.setPromptText("Company Name");
            
         }
    }

    //TODO: try different approach to stage and Part part
    //References to private class Stage dialogeStage
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    //References to private class Part part, using the setter from Modules.Part
    //sets the functionality for the add-part screen text-boxes and radio buttons
    public void setPart(Part part) {
        selectedPart = part;

        //Reference to Modules.Part for getters
        partIDField.setText(Integer.toString(part.getPartID()));
        partNameField.setText(part.getName());
        partInStockField.setText(Integer.toString(part.getInStock()));
        partPriceField.setText(Double.toString(part.getPrice()));
        partMinField.setText(Integer.toString(part.getMin()));
        partMaxField.setText(Integer.toString(part.getMax()));

        if (part instanceof InHousePart) {
            selectedInPart = (InHousePart) part;
            companyMachineLabel.setText("Machine ID");
            inhouseRadioButton.selectedProperty().set(true);
            companyMachineField.setText(Integer.toString(selectedInPart.getMachineID()));
        } else {
            selectedOutPart = (OutsourcedPart) part;
            companyMachineLabel.setText("Company Name");
            outsourcedRadioButton.selectedProperty().set(true);
            companyMachineField.setText(selectedOutPart.getCompanyName());
        }
    }

    //functionality for the saving button within new Part Screen
    @FXML
    private void handleNewSave() {
        //checks if inputs are valid, then retrieve inputs
        if (isPartValid()) {
            String name = partNameField.getText();
            String inStock = partInStockField.getText();
            String price = partPriceField.getText();
            String min = partMinField.getText();
            String max = partMaxField.getText();
            String companyMachine = companyMachineField.getText();
            if (isOutsourced == false) {
                InHousePart inPart = new InHousePart();
                inPart.setPartID(partID);
                inPart.setName(name);
                inPart.setPrice(Double.parseDouble(price));
                inPart.setInStock(Integer.parseInt(inStock));
                inPart.setMin(Integer.parseInt(min));
                inPart.setMax(Integer.parseInt(max));
                inPart.setMachineID(Integer.parseInt(companyMachine));
                Inventory.addPart(inPart);
            } else {
                OutsourcedPart outPart = new OutsourcedPart();
                outPart.setPartID(partID);
                outPart.setName(name);
                outPart.setPrice(Double.parseDouble(price));
                outPart.setInStock(Integer.parseInt(inStock));
                outPart.setMax(Integer.parseInt(max));
                outPart.setMin(Integer.parseInt(min));
                outPart.setCompanyName(companyMachine);
                Inventory.addPart(outPart);
            }
            dialogStage.close();
        }
    }

//functionality for cancel button handling
    @FXML
    void handleCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Cancel");
        alert.setHeaderText("Do you want to Cancel?");
        alert.setContentText("Are you sure you want to cancel adding a new part?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.OK) {
            partID = Inventory.cancelPartIDCount();
            dialogStage.close();
        } else {
            alert.close();
        }
    }

    //checks if all inputed fields are appropriate, and prompts if not
    private boolean isPartValid() {
        String name = partNameField.getText();
        String inStock = partInStockField.getText();
        String price = partPriceField.getText();
        String min = partMinField.getText();
        String max = partMaxField.getText();
        String companyMachine = companyMachineField.getText();
        String errorMessage = "";

        //Checks for null inputs for Name
        if (name == null || name.length() == 0) {
            errorMessage += "Not a valid name!\n";
        }
        //Checks for null inputs for Stock
        if (inStock == null || inStock.length() == 0) {
            errorMessage += "Not a valid stock value!\n";
        } else {
            //parses the string into an integer to allows an integer comparison for Stock
            try {
                int inStockComp = Integer.parseInt(inStock);
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (inStockComp < minComp || inStockComp > maxComp) {
                    errorMessage += "Stock is too low! Must be between min & max value!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid input value!\n";
            }
        }
        //Checks for null input for Price
        if (price == null || price.length() == 0) {
            errorMessage += "Not a valid price!\n";
        } else {
            //parses the string into a double for Price
            try {
            Double.parseDouble(price);
        } catch (NumberFormatException e) {
                errorMessage += "Not a valid price! Must contain decimal value!\n";
            }
    }
        //Checks for null input for Min Value
        if (min == null || min.length() == 0) {
            errorMessage += "Not a valid min value!\n";
        } else {
            //parses max, min string into an integer to check Min Value
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp) {
                    errorMessage += "Max value must be greater than min value!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a valid min value!\n";
            }
        }
        //Checks if null input for Max Value
        if (max == null || max.length() == 0) {
            errorMessage += "Not a valid max value!\n";
        } else {
            //parses max, min string into an integer to check Max Value
            try {
                int minComp = Integer.parseInt(min);
                int maxComp = Integer.parseInt(max);
                if (maxComp < minComp || minComp >= maxComp) {
                    errorMessage += "Max value must be greater than min value!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Not a vaid Max value!\n";
            }
        }
        //Checks if null input for Company name or ID
        if (companyMachine == null || companyMachine.length() == 0 ) {
            errorMessage += "Not a valid Company name or ID!\n";
        }
//        //checks if outsourced company name is a string
//        if (isOutsourced == true && companyMachine.matches(".*\\d.*") == true) {
//            errorMessage += "Not a valid Company name!\n";
//        }
//        //checks if inhouse ID is a number
//        if (isOutsourced == false && companyMachine.matches(".*\\d.*") == false) {
//            errorMessage += "Not a valid Company ID number!\n";
//        }

        //if input not null, return true
        //TODO: handle this for the mod saving with inhouse/outsourced
        if (errorMessage.length() == 0) {
            return true;
        } else {
            //error message if incorrect company name or ID format
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Inputs");
            alert.setHeaderText("Input errors were found. Please correct these!\n");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        }

        //functionality for modify-save button
    @FXML
    private void handleModifySave() {
        //checks if inputs are valid, then retrieves inputs
        if (isPartValid()) {
            String ID = partIDField.getText();
            String name = partNameField.getText();
            String inStock = partInStockField.getText();
            String price = partPriceField.getText();
            String min = partMinField.getText();
            String max = partMaxField.getText();
            String companyMachine = companyMachineField.getText();
            //if inhouse is selected
            if ((this.partToggleGroup.getSelectedToggle().equals(this.inhouseRadioButton))) {
                InHousePart inPart = new InHousePart();
                inPart.setPartID(Integer.parseInt(ID));
                inPart.setName(name);
                inPart.setPrice(Double.parseDouble(price));
                inPart.setInStock(Integer.parseInt(inStock));
                inPart.setMin(Integer.parseInt(min));
                inPart.setMax(Integer.parseInt(max));
                inPart.setMachineID(Integer.parseInt(companyMachine));
                Inventory.updatePart(partIndex, inPart);
            } else {
                //if outsourced is selected
                OutsourcedPart outPart = new OutsourcedPart();
                outPart.setPartID(Integer.parseInt(ID));
                outPart.setName(name);
                outPart.setPrice(Double.parseDouble(price));
                outPart.setInStock(Integer.parseInt(inStock));
                outPart.setMin(Integer.parseInt(min));
                outPart.setMax(Integer.parseInt(max));
                outPart.setCompanyName(companyMachine);
                Inventory.updatePart(partIndex, outPart);
            }
            okClicked = true;
            Inventory.cancelPartIDCount();
            dialogStage.close();
        }

    }

}
