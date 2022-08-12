package Model;


import Control.ModifyPartMenuController;
import Control.ModifyProductMenuController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;


/**
 * Inventory class contains the parts and products Observable lists, as well as the filtered parts and products lists.
 * It also provides many useful static methods, and holds the final classpath strings.
 */
public class Inventory {
    /**
     * Observable list to contain all the parts
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /**
     * Observable list to contain all the products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Observable list to contain filtered parts. Used when searching from the Main Menu
     */
    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    /**
     * Observable list to contain filtered products. Used when searching from the Main Menu and
     * Modify Products windows.
     */
    private static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();

    /**
     * Variables to hold windows file paths
     */
    public static final String mainMenuLocation = "/View/MainMenu.fxml";
    public static final String modifyPartsLocation = "/View/ModifyPartMenu.fxml";
    public static final String modifyProductsLocation = "/View/ModifyProductMenu.fxml";
    public static final String addPartMenu = "/View/AddPartMenu.fxml";
    public static final String addProductsMenu = "/View/AddProductMenu.fxml";


    /**
     * Static method to hold part IDs, generatePartID() method increments the variable
     * when it creates a new ID.
     */
    private static int partIDList = 0;
    /**
     * Static method to hold product IDs, generateProductID() method increments the variable
     * when it creates a new ID.
     */
    private static int productIDList = 0;

    /**
     * Adds part to allParts list.
     *
     * @param part part object to be added.
     */
    public static void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Returns allParts list.
     *
     * @return Observable list of all parts.
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Adds product to allProducts list
     *
     * @param product Product object to be added.
     */
    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    /**
     * Returns allProducts list
     *
     * @return Observable List of all products.
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Adds part to filteredParts list.
     *
     * @param part Part object to be added.
     */
    public static void addFilteredPart(Part part) {
        filteredParts.add(part);
    }

    /**
     * Returns filteredParts list.
     *
     * @return Observable list of filtered parts.
     */
    public static ObservableList<Part> getAllFilteredParts() {
        return filteredParts;
    }

    /**
     * Adds product to filteredProducts list.
     *
     * @param product Product object to be added.
     */
    public static void addFilteredProduct(Product product) {
        filteredProducts.add(product);
    }

    /**
     * Returns filteredProducts list.
     *
     * @return Observable list of filtered products.
     */
    public static ObservableList<Product> getAllFilteredProducts() {
        return filteredProducts;
    }

    /**
     * Iterates through allParts list and returns a part object whose ID matches
     * the ID searched for.
     *
     * @param partID int partID number to search for.
     * @return part Object whose ID matched the ID in the parameter. Returns null
     * if a part with matching ID cannot be found.
     */
    public static Part lookupPart(int partID) {
        for (Part part : getAllParts()) {
            if (part.getID() == partID) {
                return part;
            }
        }
        return null;
    }


    /**
     * Iterates through allProducts list and returns a product object whose ID matches
     * the ID searched for.
     *
     * @param productID int productID number to search for.
     * @return product Object whose ID matched the ID in the parameter. Returns null
     * if a product with matching ID cannot be found.
     */
    public static Product lookupProduct(int productID) {

        for (Product product : getAllProducts()) {
            if (product.getID() == productID) {

                return product;
            }
        }
        return null;
    }

    /**
     * Method to set the catch variables for the modify part window.
     * Will set the selectedPart to InHouse or Outsource subclass, and
     * sets the index variable.
     *
     * @param index        int index of part to modify.
     * @param selectedPart Part object to send to Modify Part Menu.
     */
    public static void updatePart(int index, Part selectedPart) {
        if (selectedPart instanceof InHouse) {
            ModifyPartMenuController.partCatcherInHouse = (InHouse) selectedPart;
        } else if (selectedPart instanceof Outsourced) {
            ModifyPartMenuController.partCatcherOutsourced = (Outsourced) selectedPart;

        }
        ModifyPartMenuController.partCatcherIndex = index;
    }

    /**
     * Sets the modify product menu's static variables for initialization.
     *
     * @param index      int index of product to modify.
     * @param newProduct Product object to send to Modify Product Menu.
     */
    public static void updateProduct(int index, Product newProduct) {
        ModifyProductMenuController.productCatcher = newProduct;
        ModifyProductMenuController.productCatcherIndex = index;
    }

    /**
     * Removes part from allParts list. Displays a confirmation Alert first.
     *
     * @param selectedPart Part object to be removed from list.
     * @return boolean True if part was found and removed, false otherwise.
     */
    public static boolean deletePart(Part selectedPart) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Part?");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            for (Part part : allParts) {
                if (selectedPart.getID() == part.getID()) {
                    allParts.remove(part);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes product from allProducts list. Displays an ERROR alert if the product
     * has associated parts. Otherwise, will display a confirmation alert before removing
     * the product.
     *
     * @param selectedProduct Product object to be removed from the list.
     * @return boolean True if product was found and removed, false otherwise.
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (selectedProduct.getAllAssociatedParts().size() > 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Product has associated parts. Please remove the parts before deleting.");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this Product?");
            Optional<ButtonType> result = alert.showAndWait();

            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                for (Product product : allProducts) {
                    if (selectedProduct.getID() == product.getID()) {
                        allProducts.remove(product);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Method to display a new window.
     *
     * @param event    mouseclick to activate button.
     * @param fileName String path to new window to be displayed
     * @param title    String title to set the .ew window.
     * @throws IOException if the file is not found, throws an IOException.
     */
    public static void changeWindow(ActionEvent event, String fileName, String title) throws IOException {

        Parent scene = FXMLLoader.load(Objects.requireNonNull(Inventory.class.getResource(fileName)));
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(scene));
        stage.setTitle(title);
        stage.show();
    }

    /**
     * Method to search allParts list for a part whose name matches
     * or contains a substring of the name searched for. If it finds a
     * part who matches, it adds it to the filteredParts list.
     *
     * @param name String to search for.
     * @return Observable list of filtered parts.
     */
    public static ObservableList<Part> lookupPart(String name) {
        boolean found = false;
        if (!(getAllFilteredParts().isEmpty())) {
            getAllFilteredParts().clear();
        }
        for (Part part : getAllParts()) {
            if (part.getName().contains(name)) {
                addFilteredPart(part);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(null, name + " not found.");
        }
        return getAllFilteredParts();
    }

    /**
     * Method to search allProducts list for a product whose name matches
     * or contains a substring of the name searched for. If it finds a
     * product who matches, it adds it to the filteredProducts list.
     *
     * @param name String to search for.
     * @return Observable list of filtered products.
     */
    public static ObservableList<Product> lookupProduct(String name) {
        boolean found = false;
        if (!(getAllFilteredProducts().isEmpty())) {
            getAllFilteredProducts().clear();
        }
        for (Product product : getAllProducts()) {
            if (product.getName().contains(name)) {
                addFilteredProduct(product);
                found = true;
            }
        }
        if (!found) {
            JOptionPane.showMessageDialog(null, name + " not found.");
        }
        return getAllFilteredProducts();
    }

    /**
     * Increments the static partIDList variable and then returns it.
     * Used to create unique part IDs.
     *
     * @return integer newly incremented number suitable to be used for a part's ID.
     */
    public static int generatePartID() {
        partIDList++;
        return partIDList;
    }


    /**
     * Increments the static productIDList variable and then returns it.
     * Used to create unique product IDs.
     *
     * @return integer newly incremented number suitable to be used for a product's ID.
     */
    public static int generateProductID() {
        productIDList++;
        return productIDList;
    }
}

