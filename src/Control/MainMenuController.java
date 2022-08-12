package Control;

import Model.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * MainMenuController controls the MainMenu Window. The main menu is the first window displayed and the starting point
 * for adding or modifying parts or products. It also has two TableViews, which display all parts and all products.
 */
public class MainMenuController implements Initializable {

    /**
     * TableView to display all parts
     */
    @FXML
    private TableView<Part> partsTableView;

    /**
     * Column to display part ID's
     */
    @FXML
    private TableColumn<Part, Integer> partIDCol;

    /**
     * Column to display part names
     */
    @FXML
    private TableColumn<Part, String> partNameCol;

    /**
     * Column to display part stock
     */
    @FXML
    private TableColumn<Part, Integer> partInvCol;

    /**
     * Column to display part prices
     */
    @FXML
    private TableColumn<Part, Double> partPriceCostCol;

    /**
     * TableView to display all Products
     */
    @FXML
    private TableView<Product> productsTableView;

    /**
     * Column to display product IDs
     */
    @FXML
    private TableColumn<Product, Integer> productIDCol;

    /**
     * Column to display product names
     */
    @FXML
    private TableColumn<Product, String> productNameCol;

    /**
     * Column to display product stock
     */
    @FXML
    private TableColumn<Product, Integer> productInvCol;

    /**
     * Column to display price costs
     */
    @FXML
    private TableColumn<Product, Double> productPriceCostCol;

    /**
     * TextField to receive user search parameters. Int will select products by ID Strings
     * and substrings will filter by product name
     */
    @FXML
    private TextField productSearchTextField;

    /**
     * TextField to receive user search parameters. Int will select parts by ID. Strings and
     * substrings will filter by product name
     */
    @FXML
    private TextField partSearchTextField;

    /**
     * Activated by pressing enter after entering int or string search parameters. Will select a part if
     * it's ID matches the Integer entered. Or will filter parts by names matching string entered or parts that
     * contain substrings entered. Will display a message if the item was not found.
     */
    @FXML
    void onActionSearchPartsTableView() {
        if (partSearchTextField.getText() == null) {
            partsTableView.setItems(Inventory.getAllParts());
        } else {
            boolean isNumber;
            try {
                Integer.parseInt(partSearchTextField.getText());
                isNumber = true;
            } catch (Exception e) {
                isNumber = false;
            }

            if (!isNumber) {
                Inventory.lookupPart(partSearchTextField.getText());
                partsTableView.setItems(Inventory.getAllFilteredParts());
            } else {
                partsTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt
                        (partSearchTextField.getText())));
            }
        }
    }

    /**
     * Activated by pressing enter after entering int or string search parameters. Will select a product if
     * it's ID matches the Integer entered. Or will filter products by names matching string entered or product whose
     * names contain substrings entered. Will display a message if the item was not found.
     */
    @FXML
    void onActionSearchProductsTableView() {
        boolean isNumber;
        try {
            Integer.parseInt(productSearchTextField.getText());
            isNumber = true;
        } catch (Exception e) {
            isNumber = false;
        }

        if (!isNumber) {
            Inventory.lookupProduct(productSearchTextField.getText());
            productsTableView.setItems(Inventory.getAllFilteredProducts());
        } else {

            productsTableView.getSelectionModel().select(Inventory.lookupProduct(Integer.
                    parseInt(productSearchTextField.getText())));
        }
    }

    /**
     * Changes the window to the add parts window.
     *
     * @param event mouseclick that activates the button
     * @throws IOException if the AddPartMenu.fxml file isn't found will throw an IOException.
     */
    @FXML
    void onClickAddPartsMenu(ActionEvent event) throws IOException {

        Inventory.changeWindow(event, Inventory.addPartMenu, "Add Parts");

    }

    /**
     * Will change the window to the Add Products window.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException if the AddProducts.fxml file isn't found will throw an IOException
     */
    @FXML
    void onClickAddProductsMenu(ActionEvent event) throws IOException {
        Inventory.changeWindow(event, Inventory.addProductsMenu, "Add Products");


    }

    /**
     * Deletes the part selected in the all parts TableView. Will display an error message if
     * no part is selected when the delete button is pressed.
     */
    @FXML
    void onClickDeletePart() {
        try {
            if (Inventory.deletePart(partsTableView.getSelectionModel().getSelectedItem())) {
                JOptionPane.showMessageDialog(null, "Part deleted successfully");
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item to Delete");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the product selected in the all products TableView. Will display an error message if
     * no product is selected when the delete button is pressed. Will also display an error message if
     * the selected product has associated parts.
     */
    @FXML
    void onClickDeleteProduct() {
        try {
            Product product = productsTableView.getSelectionModel().getSelectedItem();
            if (Inventory.deleteProduct(product)) {
                JOptionPane.showMessageDialog(null, "Product deleted successfully");
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item to Delete.");
            alert.showAndWait();
        }
    }

    /**
     * LOGICAL ERROR: Initially I discovered that when choosing to modify a part after having filtered the TableView
     * the part would occasionally be duplicated in the allParts list instead of being updated. If the filtered list
     * contained all the parts of the allParts list, up to the index of the part to be modified, it worked correctly.
     * What I learned was that when selecting a part to modify, it was calculating the index from the filtered list
     * instead of the allParts list. I solved this issue by explicitly finding the part in the allParts list, instead
     * of just the list the part was chosen from, and calculating the index from that.
     *
     * This method Sends selected part information to the ModifyPartMenu Controller. Then changes the window to the
     * modify parts window. Will display an error message if no part is selected when the button is clicked.
     *
     * @param event mouseclick which activates the button.
     * @throws IOException If the modify parts fxml file isn't found, will throw an IOException
     */
    @FXML
    void onClickModifyPartsMenu(ActionEvent event) throws IOException {
        try {
            Part part = partsTableView.getSelectionModel().getSelectedItem();

            /* Gets an index from the allPartsList instead of the TableView.
              Otherwise it could be attempting to get an index from a possibly filtered
              TableView. */
            int index = 0;
            int count = -1;
            for (Part searchPart : Inventory.getAllParts()) {
                ++count;
                if (part.getID() == searchPart.getID()) {
                    index = count;
                }
            }
            Inventory.updatePart(index, part);
            Inventory.changeWindow(event, Inventory.modifyPartsLocation, "Modify Parts");
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item to modify");
            alert.showAndWait();
        }
    }

    /**
     * Sends selected product information to the ModifyProductMenu Controller. Then changes the window to the modify
     * products window. Will display an error message if no product is selected when the button is clicked.
     *
     * @param event mouseclick which activates the button.
     * @throws IOException If the modify products menu fxml file isn't found, will throw an IOException
     */
    @FXML
    void onClickModifyProductsMenu(ActionEvent event) throws IOException {
        try {
            Product product = productsTableView.getSelectionModel().getSelectedItem();
            int index = 0;
            int count = -1;
            for (Product searchProduct : Inventory.getAllProducts()) {
                ++count;
                if (product.getID() == searchProduct.getID()) {
                    index = count;
                }
            }
            Inventory.updateProduct(index, product);
            Inventory.changeWindow(event, Inventory.modifyProductsLocation, "Modify Products");
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select an item to modify");
            alert.showAndWait();
        }
    }

    /**
     * Displays a confirmation box, then exits the program with a status code of 0.
     */
    @FXML
    void onClickShutdown() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you wish to exit?");
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            System.exit(0);
        }

    }

    /**
     * First method run when the window is created. Sets up the parts TableVIew and the Products
     * TableView to display the allParts and allProducts lists respectively.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root object
     *                       was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Sets up the parts TableView using the allParts list.
        partsTableView.setItems(Inventory.getAllParts());
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Sets up the products TableView using the allProducts list.
        productsTableView.setItems(Inventory.getAllProducts());
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}
