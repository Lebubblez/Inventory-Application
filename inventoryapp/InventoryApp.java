package inventoryapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import inventoryapp.Models.Part;
import inventoryapp.Models.Product;
import inventoryapp.Views.PartScreen;
import inventoryapp.Views.ProductScreen;
import static javafx.application.Application.launch;
import java.io.IOException;

public class InventoryApp extends Application {

    //TODO:constructors (true?)
    private static Stage primaryStage;
    private AnchorPane mainScreen;

    //ignored unless application doesnt run, and is needed as a fallback
    public static void main(String[] args) {
        launch(args);
    }

    //getter for primary stage
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    //Main screen stage
    @Override
    public void start(Stage primaryStage) {
        InventoryApp.primaryStage = primaryStage;
        InventoryApp.primaryStage.setTitle("Inventory System");

        showMainScreen();
    }

    //Visuals and functions for the main screen
    private void showMainScreen() {
        try {

            //Loads the visuals for main screen
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApp.class.getResource("/inventoryapp/Views/MainScreen.fxml"));
            mainScreen = loader.load();

            //Loads the controller for main screen
            FXMLDocumentController controller = loader.getController();
            controller.setMainApp(this);

            //Shows main screen Root Layout in scene
            Scene scene = new Scene(mainScreen);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
        }
    }

    //Visuals and functions for the part screen
    public boolean showPartScreen() {
        try {
            //Loads the part screen & dialog popup.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApp.class.getResource("/inventoryapp/Views/PartScreen.fxml"));
            GridPane partScreen = loader.load();

            //dialog stage for 'add part' screen
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Part");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(partScreen);
            dialogStage.setScene(scene);

            //dialoge stage referenced from PartScreenController. Set person in controller.
            PartScreen controller = loader.getController();
            controller.setDialogStage(dialogStage);

            //Show dialog and wait for action from user
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            return false;
        }

    }

    //Visuals and functions for the product screen
    public boolean showProductScreen() {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApp.class.getResource("/inventoryapp/Views/ProductScreen.fxml"));
            AnchorPane partScreen = loader.load();

            // Create the dialog Stage for 'add product' screen
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(partScreen);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ProductScreen controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isButtonClick();
        } catch (IOException e) {
            return false;
        }
    }

    //Visuals and functions for the modify-part screen
    public static boolean showModifyPartScreen(Part part) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApp.class.getResource("/inventoryapp/Views/ModifyPartScreen.fxml"));
            GridPane partScreen = loader.load();

            // Create the dialog Stage for 'modify-part' screen.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Part");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(partScreen);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            PartScreen controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setPart(part);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
        } catch (IOException e) {
            return false;
        }
    }

    //Visuals and functions for the modify-product screen
    public static boolean showModifyProductScreen(Product product) {
        try {
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(InventoryApp.class.getResource("/inventoryapp/Views/ModifyProductScreen.fxml"));
            AnchorPane partScreen = loader.load();

            // Create the dialog Stage for 'modify product' screen.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Product");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(partScreen);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            ProductScreen controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setProduct(product);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isButtonClick();
        } catch (IOException e) {
            return false;
        }
    }
}