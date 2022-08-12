package Model;

/**
 * Supplied class Part.java
 */


/**
 * Class file for the Part Abstract Object. Inherited from by Outsource and InHouse classes.
 *
 * @author Philip Sauer
 */
public abstract class Part {

    private int ID;
    private String name;
    private double price;
    private int stock;
    private int min;
    private int max;

    /**
     * Full constructor for a part object.
     *
     * @param ID    Stores part ID. Used to identify the part in lists.
     * @param name  Stores part name.
     * @param price Stores part price.
     * @param stock Stores the number of parts in stock. Must be less than max, and more than min.
     * @param min   Stores the minimum number of parts that can be in stock.
     * @param max   Stores the maximum number of parts that can be in stock.
     */
    public Part(int ID, String name, double price, int stock, int min, int max) {
        this.ID = ID;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;

    }


    /**
     * @return the id
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
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
     * @return the stock
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
     * @return the min
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

    /**
     * @return the max
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


}