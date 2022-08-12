package Model;

/** InHouse class file. InHouse inherits from part. InHouse items require a machineID instead of a company name.
 *
 */
public class InHouse extends Part {
    private int machineID; // Move

    /** Full Constructor.
     *
     * @param ID    Stores part ID. Used to identify the part in lists.
     * @param name  Stores part name.
     * @param price Stores part price.
     * @param stock Stores the number of parts in stock. Must be less than max, and more than min.
     * @param min   Stores the minimum number of parts that can be in stock.
     * @param max   Stores the maximum number of parts that can be in stock.
     * @param machineID Stores the machineID.
     */
    public InHouse(int ID, String name, double price, int stock, int min, int max, int machineID) {
        super(ID, name, price, stock, min, max);
        this.machineID = machineID;
    }

    /**
     * @return the machineID.
     */
    public int getMachineID() {
        return machineID;
    }

    /**
     * @param machineID the machineID to set.
     */
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }

}
