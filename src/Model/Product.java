package Model;

import Control.ModifyProductMenuController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/** Product class file.
 *
 * @author Philip Sauer
 */
public class Product {

    /** Observable list contains any associated parts. It is not included in the constructor */
    private final ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    private int ID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /** Full constructor minus setting the associated parts
     *
     * @param id    Stores product ID. Used to identify the product in lists.
     * @param name  Stores product name.
     * @param price Stores product price.
     * @param stock Stores the number of product in stock. Must be less than max, and more than min.
     * @param min   Stores the minimum number of product that can be in stock.
     * @param max   Stores the maximum number of product that can be in stock.
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        this.ID = id;
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.max = max;
        this.min = min;

    }


    /** Adds a part to the associated parts list.
     *
     * @param part Part object to be added.
     */
    public void addAssociatedPart(Part part) {
        associatedPartsList.add(part);
    }

    /** returns associated parts list.
     *
     * @return Observable list of associatedProducts.
     */
    public ObservableList<Part> getAllAssociatedParts() {
        return associatedPartsList;
    }

    /** Finds and removes a part from the associated parts list. Displays a confirmation alert before beginning.
     *
     * @param selectedAssociatedPart Part object to be removed.
     * @return boolean true if the part was found and removed, false otherwise.
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this associated part?");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            for (Part part : ModifyProductMenuController.productCatcher.getAllAssociatedParts()) {
                if (part.getID() == selectedAssociatedPart.getID()) {
                    ModifyProductMenuController.productCatcher.getAllAssociatedParts().remove(part);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @return the ID.
     */
    public int getID() {
        return ID;
    }

    /**
     * @param ID the id to set
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name String name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the stock.
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the price.
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * @return the max.
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the min.
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

}
