package inventoryapp.Models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class InHousePart extends Part {

    private final IntegerProperty machineID;

    //InHousePart constructor
    public InHousePart(int partID, String name, double price, int inStock, int min, int max, int machineID) {
        this.partID = new SimpleIntegerProperty(partID);
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.inStock = new SimpleIntegerProperty(inStock);
        this.min = new SimpleIntegerProperty(min);
        this.max = new SimpleIntegerProperty(max);
        this.machineID = new SimpleIntegerProperty(machineID);
    }

    //sets default constructor
    public InHousePart() {
        this.partID = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.price = new SimpleDoubleProperty(0);
        this.inStock = new SimpleIntegerProperty(0);
        this.min = new SimpleIntegerProperty(0);
        this.max = new SimpleIntegerProperty(0);
        this.machineID = new SimpleIntegerProperty(0);
    }


    //getters and setters for inhouse machineID
    public int getMachineID() {
        return this.machineID.get();
    }

    public void setMachineID(int machineID) {
        this.machineID.set(machineID);
    }

    public IntegerProperty machineIDProperty(){
        return machineID;
    }
}
