package Model;

/** Outsourced class file. Inherits from Part. Outsourced items require a company name instead of a machine ID.
 *
 */
public class Outsourced extends Part {
    private String companyName;

    /** Full constructor
     *
     * @param ID    Stores part ID. Used to identify the part in lists.
     * @param name  Stores part name.
     * @param price Stores part price.
     * @param stock Stores the number of parts in stock. Must be less than max, and more than min.
     * @param min   Stores the minimum number of parts that can be in stock.
     * @param max   Stores the maximum number of parts that can be in stock.
     * @param companyName Stores the Company Name.
     */
    public Outsourced(int ID, String name, double price, int stock, int min, int max, String companyName) {
        super(ID, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * @return the companyName.
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to be set.
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
