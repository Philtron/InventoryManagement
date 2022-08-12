package Control;

import Model.*;
import Model.Inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *This class controls the add part window. Allows the creation of new parts.
 */
public class AddPartMenuController implements Initializable {

    /** Toggle group for inhouse and outsource radio buttons.*/
    @FXML
    private ToggleGroup partSourceRadioGroup;

    /** Denotes an inhouse part object. */
    @FXML
    private RadioButton inHouseRadioBtn;

    /** Denotes an Outsource part object. */
    @FXML
    private RadioButton OutSourceRadioBtn;

    /** Uneditable textfield. Displays "Auto-Gen". Part IDs are automatically generated. */
    @FXML
    private TextField partIDTextField;

    /** TextField for part's name. */
    @FXML
    private TextField partNameTextField;

    /** TextField for parts stock */
    @FXML
    private TextField partInvTextField;

    /** TextField for parts cost. */
    @FXML
    private TextField partPriceTextField;

    /** TextField for part's max stock. */
    @FXML
    private TextField partMaxTextField;

    /** TextField for part's minimum stock. */
    @FXML
    private TextField partMinTextField;


    /** TextField for inhouse part's machine ID or outsource part's company name. */
    @FXML
    private TextField partMachineIDTextField;

    /** Label denoting whether part is inhouse or outsource.*/
    @FXML
    private Label partSourceLbl;


    /**
     * Cancel button. Throws out data entered in TextFields and displays the main menu.
     * @param event mouseclick event that activates the button.
     * @throws IOException If the main menu fxml file is not found, will throw IOException.
     */
    @FXML
    void onClickDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text fields. Do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");

        }
    }

    /**
     * Saves TextFields and uses the information to create a new part object and adds it to the all parts list. It then
     * displays the main menu.
     * @param event mouseclick that activates the button.
     * @throws IOException If the main menu fxml file is not found, will throw IOException.
     */
    @FXML
    void onClickSavePart(ActionEvent event) throws IOException {

        /** Try statement to make sure all the TextFields have data and that the data entered is of the correct
         * datatype.
         */
        try {
            String name = partNameTextField.getText();
            int inventory = Integer.parseInt(partInvTextField.getText());
            double price = Double.parseDouble(partPriceTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            // Minimum must be less than maximum
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min must be smaller than Max");
                alert.show();
                // Stock must be greater than minimum and less than maximum.
            } else if ((inventory > max) || (inventory < min)) {
                Alert invAlert = new Alert(Alert.AlertType.ERROR, "Inv must be between min and max.");
                invAlert.show();
            } else {
                if (inHouseRadioBtn.isSelected()) {    // part object created will be an inhouse part with a machine ID
                    int machineID = Integer.parseInt(partMachineIDTextField.getText());
                    Inventory.addPart(new InHouse(Inventory.generatePartID(), name, price, inventory, min, max,
                            machineID));
                } else { // part object created will be an outsourced part with a company name.
                    String companyName = partMachineIDTextField.getText();
                    Inventory.addPart(new Outsourced(Inventory.generatePartID(), name, price, inventory, min, max,
                            companyName));
                }

                Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
            }
            // Displays an error if incorrect or null data has been entered in the TextFields.
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid values in the text fields.\n" +
                    "Exception:  " + e.getMessage());
            alert.showAndWait();
        }


    }


    /**
     * First method run when window is created. Listens and sets the partsource label based off which radio button is selected.
     * Will display "Company Name is the outsource button is selected, and will display "Machine ID" if the inhouse button is selected.
     * @param url            The location used to resolve relative paths for the root object, or null if the location
     *                       is not known.
     * @param resourceBundle The resources used to localize the root object, or null if the root
     *                       object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        partSourceRadioGroup.selectedToggleProperty().addListener((ov, old_toggle, new_toggle) -> {
            if (partSourceRadioGroup.getSelectedToggle() != null) {
                if (OutSourceRadioBtn.isSelected()) {
                    partSourceLbl.setText("Company Name");
                } else {
                    partSourceLbl.setText("Machine ID");
                }
            }
        });

    }
}
