package Control;

import Model.Inventory;

import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * AddProductMenuController controls the AddProductMenu window. Allows for the creating of new products and allows for
 * parts to be added to the new products associated parts list.
 */
public class AddProductMenuController implements Initializable {

    /**
     * List to display the associated parts in the bottom tableview.
     */
    ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();


    /**
     * TextField for Product ID. Disabled as the ID is auto generated.
     */
    @FXML
    private TextField productIDTextField;

    /**
     * TextField for product Name.
     */
    @FXML
    private TextField productNameTextField;

    /**
     * TextField for product's stock. Should be greater than min and less than max
     */
    @FXML
    private TextField productInvTextField;

    /**
     * TextField for product's price
     */
    @FXML
    private TextField productPriceTextField;

    /**
     * Textfield for products maximum allowed stock.
     */
    @FXML
    private TextField productMaxTextField;

    /**
     * TextField for products minimum allowed stock.
     */
    @FXML
    private TextField productMinTextField;

    /**
     * TextField to hold search strings, substrings for product names, or ints for product ID searches.
     */
    @FXML
    private TextField allPartsSearchTextField;

    /**
     * TableView that holds and displays the all products list.
     */
    @FXML
    private TableView<Part> allPartsTableView;

    /**
     * TableView column that will hold the product's ID
     */
    @FXML
    private TableColumn<Part, Integer> allPartsIDCol;

    /**
     * TableView column that will hold the product's's name.
     */
    @FXML
    private TableColumn<Part, String> allPartsNameCol;

    /**
     * TableView column that will hold the product's stock (inventory)
     */
    @FXML
    private TableColumn<Part, Integer> allPartsInvCol;

    /**
     * TableView column that will hold product's's price
     */
    @FXML
    private TableColumn<Part, Double> allPartsPriceCol;

    /**
     * TableView that will hold the list of associated parts
     */
    @FXML
    private TableView<Part> associatedPartsTableView;

    /**
     * TableView column that will hold the associated part's ID
     */
    @FXML
    private TableColumn<Part, Integer> associatedPartsIDCol;

    /**
     * TableView column that will hold associated part's name
     */
    @FXML
    private TableColumn<Part, String> associatedPartsNameCol;

    /**
     * TableView column that will hold the associated part's stock
     */
    @FXML
    private TableColumn<Part, Integer> associatedPartsInvCol;

    /**
     * TableView column that will hold the associated part's price
     */
    @FXML
    private TableColumn<Part, Double> associatedPartsPriceCol;

    /**
     * Saves TextFields and uses the information to create a new product object and adds it to the all product list.
     * It then displays the main menu.
     *
     * @param event mouseclick that activates the button.
     * @throws IOException If the main menu fxml file is not found, will throw IOException.
     */
    @FXML
    void onClickAddProduct(ActionEvent event) throws IOException {

        /** Try statement to make sure all the TextFields have data and that the data entered is of the correct
         * datatype.
         */
        try {

            String name = productNameTextField.getText();
            int inventory = Integer.parseInt(productInvTextField.getText());
            double price = Double.parseDouble(productPriceTextField.getText());
            int max = Integer.parseInt(productMaxTextField.getText());
            int min = Integer.parseInt(productMinTextField.getText());
            if (min > max) {
                // Minimum must be less than maximum
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min must be smaller than Max");
                alert.show();
            } else if ((inventory < min) || (inventory > max)) {
                // Stock must be greater than minimum and less than maximum.
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between Min and Max");
                alert.show();
            } else {
                Product newProduct = new Product(Inventory.generateProductID(), name, price, inventory, min, max);

                for (Part part : associatedPartsList) {
                    newProduct.addAssociatedPart(part);
                }
                // Product is added to list of all products
                Inventory.addProduct(newProduct);


                Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
            }


        } catch (NumberFormatException e) {
            // Displays an error if incorrect or null data has been entered in the TextFields.
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid values in the text fields.\n" +
                    " Exception:  " + e.getMessage());
            alert.showAndWait();
        }

    }

    /**
     * Cancel button. Throws out data entered in TextFields and displays the main menu.
     *
     * @param event mouseclick event that activates the button.
     * @throws IOException If the main menu fxml file is not found, will throw IOException.
     */
    @FXML
    void onClickDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text fields. Do you want to " +
                "continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
        }

    }

    /**
     * Adds the selected part to the product's associated parts list
     *
     * @param actionEvent mouseclick to activate button.
     */
    public void onClickAddToAssociatedParts(ActionEvent actionEvent) {

        try {
            Part part = allPartsTableView.getSelectionModel().getSelectedItem();
            associatedPartsList.add(Objects.requireNonNull(part));

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to add.");
            alert.showAndWait();
        }
    }

    /**
     * Removes the selected part from the associated parts list.
     *
     * @param actionEvent mouseclick to activate button
     */
    public void onClickRemoveFromAssociatedParts(ActionEvent actionEvent) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to remove this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
                associatedPartsTableView.getItems().remove(associatedPartsTableView.getSelectionModel()
                        .getSelectedIndex());
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to remove.");
            alert.showAndWait();
        } catch (IndexOutOfBoundsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to remove.");
            alert.showAndWait();
        }

    }

    /**
     * First method that runs when the window is created. Sets up and formats the two Tableviews.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                       is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     *                       object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Sets the top tableview to display all parts list.
        allPartsTableView.setItems(Inventory.getAllParts());
        allPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Sets the bottom tableview to display associated parts when they are added.
        associatedPartsTableView.setItems(associatedPartsList);
        associatedPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));


    }

    public void onActionSearchPartsTableView(ActionEvent actionEvent) {
        if (allPartsSearchTextField.getText() == null) {
            allPartsTableView.setItems(Inventory.getAllParts());
        } else {
            boolean isNumber;
            try {
                Integer.parseInt(allPartsSearchTextField.getText());
                isNumber = true;
            } catch (Exception e) {
                isNumber = false;
            }

            if (!isNumber) {
                Inventory.lookupPart(allPartsSearchTextField.getText());
                allPartsTableView.setItems(Inventory.getAllFilteredParts());
            } else {
                allPartsTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt
                        (allPartsSearchTextField.getText())));
            }
        }
    }
}
