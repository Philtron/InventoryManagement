package Control;

import Model.InHouse;
import Model.Inventory;

import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * ModifyPartMenuController controls the Modify Parts Menu. It receives a part's information from the main menu
 * before the window is called. The information is then sent to the TextFields during initialization.
 */
public class ModifyPartMenuController implements Initializable {

    /**
     * Static variables to receive part information from the Main Menu
     * Used to autofill TextFields with parts current information
     */
    public static Outsourced partCatcherOutsourced; // Used if the part's subclass is Outsource
    public static InHouse partCatcherInHouse; // Used if the part's subclass is InHouse
    public static int partCatcherIndex;

    /**
     * Only toggleGroup for the radio buttons. Used to determine if a part is inHouse or Outsourced
     */
    @FXML
    private ToggleGroup partSourceRadioGroup;

    /**
     * Radio button signifying an InHouse part object
     */
    @FXML
    private RadioButton inHouseRadioBtn;

    /**
     * Radio button signifying an Outsource part object
     */
    @FXML
    private RadioButton OutSourceRadioBtn;

    /**
     * TextField for the part ID. Uneditable - ID is auto generated.
     */
    @FXML
    private TextField partIDTextField;

    /**
     * TextField for part's name.
     */
    @FXML
    private TextField partNameTextField;

    /**
     * TextField for part's stock
     */
    @FXML
    private TextField partInvTextField;

    /**
     * TextField for part's price.
     */
    @FXML
    private TextField partPriceTextField;

    /**
     * TextField for part's max stock
     */
    @FXML
    private TextField partMaxTextField;

    /**
     * TextField for part's minimum stock quantity
     */
    @FXML
    private TextField partMinTextField;

    /**
     * Label displaying part's source: InHouse or Outsource
     */
    @FXML
    private Label partSourceLbl;

    /**
     * TextField for InHouse Part's Machine ID or Outsource parts Company Name
     */
    @FXML
    private TextField partMachineIDTextField;

    /**
     * Cancel button. Displays main menu. Does not save any information entered in the TextFields..
     * Sets the partCatcher variables to null so that the next time the Modify Parts window is displayed,
     * it can choose whether the item is an InHouse or Outsource part.
     *
     * @param event Mouse click to activate the button.
     * @throws IOException If the Main Menu fxml file isn't found, will throw an IOException.
     */
    @FXML
    void onClickDisplayMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Any changed you've made will be lost. Do you" +
                " want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");

        }
        partCatcherInHouse = null;
        partCatcherOutsourced = null;
    }

    /**
     * Uses the information in the TextFields to create a new part object and replace the previous
     * modified part in the allParts list. Will throw a number exception if incorrect data type or null
     * data type is entered in any TextField. Then displays the Main Menu.
     *
     * @param event Mouseclick that activates the button.
     * @throws IOException If the Main Menu fxml file isn't found, will throw an IOException.
     */
    @FXML
    void onClickSavePart(ActionEvent event) throws IOException {
        try {
            int ID;
            // Checks to see if the part object is an InHouse or Outsource type.
            if (partCatcherOutsourced == null) {
                ID = partCatcherInHouse.getID();
            } else {
                ID = partCatcherOutsourced.getID();
            }
            Part updatedPart;
            String name = partNameTextField.getText();
            int inventory = Integer.parseInt(partInvTextField.getText());
            double price = Double.parseDouble(partPriceTextField.getText());
            int max = Integer.parseInt(partMaxTextField.getText());
            int min = Integer.parseInt(partMinTextField.getText());
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Min must be smaller than Max");
                alert.show();
            } else if ((inventory > max) || (inventory < min)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inv must be between Min and Max");
                alert.show();
            } else {
                if (inHouseRadioBtn.isSelected()) { // Saves as InHouse part and sets the machineID member.
                    int machineID = Integer.parseInt(partMachineIDTextField.getText());
                    updatedPart = new InHouse(ID, name, price, inventory, min, max, machineID);
                    Inventory.getAllParts().set(partCatcherIndex, updatedPart);
                    partCatcherInHouse = null;
                    partCatcherOutsourced = null;

                } else { // Saves as an Outsource part and sets the company name member.
                    String companyName = partMachineIDTextField.getText();
                    updatedPart = new Outsourced(ID, name, price, inventory, min, max, companyName);
                    Inventory.getAllParts().set(partCatcherIndex, updatedPart);
                    partCatcherOutsourced = null;
                    partCatcherInHouse = null;


                }

                Inventory.changeWindow(event, Inventory.mainMenuLocation, "Main Menu");
            }
            // Client entered wrong data type in TextFIeld
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter valid values in the text fields.\n " +
                    "Exception:  " + e.getMessage());
            alert.showAndWait();
        }
    }


    /**
     * First method to run. Sets the InHouse / Outsource radio button based on the part to be modified.
     * And then listens for a change in radio buttons. If the radio button is changed, will change the
     * source TextField to match.
     *
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
        if (partCatcherOutsourced == null) {
            // InHouse part object
            partIDTextField.setText(Integer.toString(partCatcherInHouse.getID()));
            partNameTextField.setText(partCatcherInHouse.getName());
            partInvTextField.setText(Integer.toString(partCatcherInHouse.getStock()));
            partPriceTextField.setText(Double.toString(partCatcherInHouse.getPrice()));
            partMinTextField.setText(Integer.toString(partCatcherInHouse.getMin()));
            partMaxTextField.setText(Integer.toString(partCatcherInHouse.getMax()));
            partMachineIDTextField.setText(Integer.toString(partCatcherInHouse.getMachineID()));
            inHouseRadioBtn.setSelected(true);
        } else {
            //Outsource part object
            partIDTextField.setText(Integer.toString(partCatcherOutsourced.getID()));
            partNameTextField.setText(partCatcherOutsourced.getName());
            partInvTextField.setText(Integer.toString(partCatcherOutsourced.getStock()));
            partPriceTextField.setText(Double.toString(partCatcherOutsourced.getPrice()));
            partMinTextField.setText(Integer.toString(partCatcherOutsourced.getMin()));
            partMaxTextField.setText(Integer.toString(partCatcherOutsourced.getMax()));
            partMachineIDTextField.setText(partCatcherOutsourced.getCompanyName());
            OutSourceRadioBtn.setSelected(true);
        }
    }
}
