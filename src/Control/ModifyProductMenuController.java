package Control;

import Model.Inventory;
import Model.Part;
import Model.Product;
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
 * ModifyProductMenuController controls the Modify Products Menu. It receives a Product's information from the main menu
 * before the window is called. The information is then sent to the TextFields during initialization.
 */
public class ModifyProductMenuController implements Initializable {

    /**
     * Static variables to receive product information from the Main Menu
     * Used to autofill TextFields with product's current information
     */
    public static Product productCatcher;
    public static int productCatcherIndex;

    /** TextField for the product's ID. */
    @FXML
    private TextField productIDTextField;

    /** TextField for the product's name */
    @FXML
    private TextField productNameTextField;

    /** TextField for the product's Stock */
    @FXML
    private TextField productInvTextField;

    /** TextField for the product's price */
    @FXML
    private TextField productPriceTextField;

    /** TextField for the product's maximum allowed stock */
    @FXML
    private TextField productMaxTextField;

    /** TextField for the product's minimum allowed stock */
    @FXML
    private TextField productMinTextField;

    /** TextField for searching allParts TableView */
    @FXML
    private TextField allPartsSearchTextField;

    /** TableView displaying all parts. Parts can be added to associated parts from this TableView */
    @FXML
    private TableView<Part> allPartsTableView;

    /** TableColumn displaying parts' IDs */
    @FXML
    private TableColumn<Part, Integer> allPartsIDCol;

    /** TableColumn displaying parts' names */
    @FXML
    private TableColumn<Part, String> allPartsNameCol;

    /** TableColumn displaying parts' stock*/
    @FXML
    private TableColumn<Part, Integer> allPartsInvCol;

    /** TableColumn displaying parts' prices */
    @FXML
    private TableColumn<Part, Double> allPartsPriceCol;

    /**TableView displaying associated Parts. Parts can be added form the allParts TableView */
    @FXML
    private TableView<Part> associatedPartsTableView;

    /** TableColumn displaying parts' ID */
    @FXML
    private TableColumn<Part, Integer> associatedPartsIDCol;

    /** TableColumn displaying parts' names */
    @FXML
    private TableColumn<Part, String> associatedPartsNameCol;

    /** TableColumn displaying parts' stock*/
    @FXML
    private TableColumn<Part, Integer> associatedPartsInvCol;

    /** TableColumn displaying parts' prices */
    @FXML
    private TableColumn<Part, Double> associatedPartsPriceCol;

    /**
     * Uses the information in the TextFields to create a new product object and replace the previous
     * product to be modified in the allProducts list. Will throw a number exception if incorrect data type or null
     * data type is entered in any TextField. Then displays the Main Menu.
     *
     * @param event Mouseclick that activates the button.
     * @throws IOException If the Main Menu fxml file isn't found, will throw an IOException.
     */
    @FXML
    void onClickAddProduct(ActionEvent event) throws IOException {

        try {
            int ID = productCatcher.getID();
            String name = productNameTextField.getText();
            int inventory = Integer.parseInt(productInvTextField.getText());
            double price = Double.parseDouble(productPriceTextField.getText());
            int max = Integer.parseInt(productMaxTextField.getText());
            int min = Integer.parseInt(productMinTextField.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min must be smaller than Max");
                alert.show();
            } else if ((inventory > max) || (inventory < min)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between Min and Max");
                alert.show();
            } else {

                Product product = new Product(ID, name, price, inventory, min, max);
                for (Part part : productCatcher.getAllAssociatedParts()) {
                    product.addAssociatedPart(part);
                }
                Inventory.getAllProducts().set(productCatcherIndex, product);
                Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid values in the text fields.\n " +
                    "Exception:  " + e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Cancel button. Displays main menu. Does not save any information entered in the TextFields..
     *
     * @param event Mouse click to activate the button.
     * @throws IOException If the Main Menu fxml file isn't found, will throw an IOException.
     */
    @FXML
    void onClickDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any TextField changes you've made will not be saved.\nDo you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
        }

    }

    /**
     * Places a copy, of the selected part in the allParts TableView, in the product's associatedParts list.
     * Will display an error alert if a part isn't selected in the allParts TableView when the button is clicked.
     * @param actionEvent mouseclick to activate the button.
     */
    public void onClickAddToAssociatedParts(ActionEvent actionEvent) {

            try {
                Part part = allPartsTableView.getSelectionModel().getSelectedItem();
                productCatcher.getAllAssociatedParts().add(Objects.requireNonNull(part));
            } catch (NullPointerException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to add.");
                alert.showAndWait();
            }

    }

    /**
     * Removes the selected part from the product's associatedParts list.
     * Will display an alert confirmation before working. Will display an error alert if a part isn't selected when
     * the button is clicked.
     *
     * @param actionEvent mouseclick to activate the button.
     */
    public void onClickRemoveFromAssociatedParts(ActionEvent actionEvent) {
        try {
            Part part = associatedPartsTableView.getSelectionModel().getSelectedItem();
            if (productCatcher.deleteAssociatedPart(part)) {
                JOptionPane.showMessageDialog(null, "Associated part removed");
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a part to remove.");
            alert.showAndWait();
        }

    }

    /**
     * Activated by pressing enter after entering int or string search parameters. Will select a product if
     * it's ID matches the Integer entered. Or will filter products by names matching string entered or product whose
     * names contain substrings entered. Will display a message if the item was not found. If enter is pressed when
     * the TextField is empty, will display all parts.
     *
     * @param actionEvent pressing enter with the textField selected.
     */
    public void onActionSearchParts(ActionEvent actionEvent) {
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
                allPartsTableView.getSelectionModel().select(Inventory.lookupPart(Integer.parseInt(allPartsSearchTextField.getText())));
            }
        }
    }

    /**
     * First method ran when the window is created. Sets up the allParts TableView and the associatedParts Tableview
     * with information passed from the Main Menu when it was called. Also fills out all TextFields with information
     * passed from the Main Menu.
     *
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                       is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     *                       object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        allPartsTableView.setItems(Inventory.getAllParts());
        allPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        allPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartsTableView.setItems(productCatcher.getAllAssociatedParts());
        associatedPartsIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productIDTextField.setText(Integer.toString(productCatcher.getID()));
        productNameTextField.setText(productCatcher.getName());
        productInvTextField.setText(Integer.toString(productCatcher.getStock()));
        productPriceTextField.setText(Double.toString(productCatcher.getPrice()));
        productMinTextField.setText(Integer.toString(productCatcher.getMin()));
        productMaxTextField.setText(Integer.toString(productCatcher.getMax()));


    }
}


