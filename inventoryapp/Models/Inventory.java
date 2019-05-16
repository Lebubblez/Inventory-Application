package inventoryapp.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {

    //instance variables
    private static ObservableList<Product> products = FXCollections.observableArrayList();
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    
    private static int partIDCount = 0;
    private static int productIDCount = 0;
    public static boolean alreadyExecuted = false;

    //Parts
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void deletePart(Part part) {
        allParts.remove(part);
    }

    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    public static int getPartIDCount() {
        partIDCount++;
        return partIDCount;
    }

    public static int cancelPartIDCount() {
        partIDCount--;
        return partIDCount;
    }

    public static Part lookupPart(int itemNumber) {
        for(Part p: getAllParts()){
            if(p.getPartID()==itemNumber){
                return p;
            }
        }
        return null;
    }


    //Products
    public static ObservableList<Product> getProducts() {
        return products;
    }

    public static void addProduct(Product product) {
        products.add(product);
    }

    public static void removeProduct(Product product) {
        products.remove(product);
    }

    public static int getProductIDCount() {
        productIDCount++;
        return productIDCount;
    }


    public static int cancelProductIDCount() {
        productIDCount--;
        return productIDCount;
    }

    public static Product lookupProduct(int itemNumber) {
        for(Product p: getProducts()){
            if(p.getProductID()==itemNumber){
                return p;
            }
        }
        return null;
    }

    public static void updateProduct(int index, Product product) {
        products.set(index, product);
    }
}

