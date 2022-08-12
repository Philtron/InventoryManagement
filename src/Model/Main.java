package Model;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

/**
 * This is the main Class for the C482 Inventory Management Performance Assessment. This program is a simple inventory
 * management program with a GUI. It utilizes observable lists to track parts and products. Products may have associated
 * parts.
 *
 * Javadocs folder is located in the /C482/doc folder.
 *
 * @author Philip Sauer
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Build and then launch the first scene and stage
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../View/MainMenu.fxml")));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root, 1000, 600));
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    /**FUTURE ENHANCEMENTS: Some future enhancements would be to provide a total cost for parts and products.
     * Possibly an expense report, or receipt window. Products total cost would be a running total of the associated
     * parts plus the products base cost. Also, the ability to export and store part/product data in a database would
     * be useful.
     *
     * Main method creates some parts and products to provide sample data. It then launches the application.
     *
     * @param args array variable to store command line arguments.
     */
    public static void main(String[] args) {
        Part part1 = new Outsourced(Inventory.generatePartID(), "Flux Capacitor", 2.99, 10, 1, 15, "Doc Brown Inc.");
        Part part2 = new InHouse(Inventory.generatePartID(), "Sprocket", 12.99, 10, 1, 15, 4321);
        Part part3 = new Outsourced(Inventory.generatePartID(), "Samoflange", 26.99, 10, 1, 15, "LexCorp");
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        Product prod1 = new Product(Inventory.generateProductID(), "Bicycle", 4.99, 10, 1, 15);
        Product prod2 = new Product(Inventory.generateProductID(), "Ladder", 5.99, 11, 2, 16);
        Product prod3 = new Product(Inventory.generateProductID(), "Dolorean", 6.99, 12, 3, 17);
        prod3.addAssociatedPart(part1);
        Inventory.addProduct(prod1);
        Inventory.addProduct(prod2);
        Inventory.addProduct(prod3);


        launch(args);
    }
}
