package inventoryapp.Models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Product {

    //Instance variables
    private ArrayList<Part> associatedParts;
    private final IntegerProperty productID;
    private final StringProperty productName;
    private final DoubleProperty productPrice;
    private final IntegerProperty productInStock;
    private final IntegerProperty productMin;
    private final IntegerProperty productMax;

    //constructor for products
    public Product(int productID, String name, double price, int inStock, int min, int max, ArrayList<Part> associatedParts) {
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(name);
        this.productPrice = new SimpleDoubleProperty(price);
        this.productInStock = new SimpleIntegerProperty(inStock);
        this.productMin = new SimpleIntegerProperty(min);
        this.productMax = new SimpleIntegerProperty(max);
        this.associatedParts = new ArrayList<>(associatedParts);
    }

    //default products constructor
    public Product() {
        this.productID = new SimpleIntegerProperty(0);
        this.productName = new SimpleStringProperty("");
        this.productPrice = new SimpleDoubleProperty(0);
        this.productInStock = new SimpleIntegerProperty(0);
        this.productMin = new SimpleIntegerProperty(0);
        this.productMax = new SimpleIntegerProperty(0);
        this.associatedParts = new ArrayList<>();

    }

    //productID
    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public int getProductID() {
        return this.productID.get();
    }

    public IntegerProperty productIDProperty() {
        return productID;
    }

    //productName
    public void setName(String name) {
        this.productName.set(name);
    }

    public String getName() {
        return this.productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    //productPrice
    public void setPrice(double price) {
        this.productPrice.set(price);
    }

    public double getPrice() {
        return this.productPrice.get();
    }

    public DoubleProperty productPriceProperty() {
        return productPrice;
    }

    //productInStock
    public void setInStock(int inStock) {
        this.productInStock.set(inStock);
    }

    public int getInStock() {
        return this.productInStock.get();
    }

    public IntegerProperty productInStockProperty() {
        return productInStock;
    }

    //productMin
    public void setMin(int min) {
        this.productMin.set(min);
    }

    public int getMin() {
        return this.productMin.get();
    }

    public IntegerProperty productMinProperty() {
        return productMin;
    }

    //productMax
    public void setMax(int max) {
        this.productMax.set(max);
    }

    public int getMax() {
        return this.productMax.get();
    }

    public IntegerProperty productMaxProperty() {
        return productMax;
    }

    //associatedParts
    public void setAssociatedParts(ArrayList<Part> associatedParts) {
        this.associatedParts = associatedParts;
    }

    public ArrayList<Part> getAssociatedParts() {
        return this.associatedParts;
    }

    public boolean removeAssociatedPart() {
        return false;
    }

    public ObservableList<Part> getObservableAssociatedParts() {
        ObservableList<Part> parts = FXCollections.observableArrayList(this.associatedParts);
        return parts;
    }

}

