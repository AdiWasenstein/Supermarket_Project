package SuperLi.src.BusinessLogic;
import java.security.InvalidParameterException;

public class SupplierItem{
    private int catalogNumber;
    private String itemName;
    private String manufacturer;
    private double unitPrice;
    private double unitWeight;
    private int numberOfUnits;
    private String catagory;
    private int marketId;

    public SupplierItem(int catalogNumber,String itemName, String manufacturer,double unitPrice,double unitWeight,int numberOfUnits,String catagory,int marketId)throws InvalidParameterException
    {
        if(catalogNumber <= 0 )
            throw new InvalidParameterException("catalog number must be a positive number");
        if(itemName.equals(""))
            throw new InvalidParameterException("item must have a name");
        if(manufacturer.equals(""))
            throw new InvalidParameterException("manufacturer must have a name");
        if(unitPrice <= 0)
            throw new InvalidParameterException("unit price must be a positive number");
        if(unitWeight <= 0)
            throw new InvalidParameterException("unit weight must be a positive number");
        if(numberOfUnits <= 0)
            throw new InvalidParameterException("number of units must be a positive number");
        if(catagory.equals(""))
            throw new InvalidParameterException("catagory must have a name");
        this.catalogNumber = catalogNumber;
        this.itemName  =itemName;
        this.manufacturer = manufacturer;
        this.unitPrice = unitPrice;
        this.unitWeight = unitWeight;
        this.numberOfUnits = numberOfUnits;
        this.catagory = catagory;
        this.marketId = marketId;
    }

    public int GetMarketId()
    {
        return this.marketId;
    }
    public void SetUnitPrice(double price)
    {
        if(price > 0)
            this.unitPrice = price;
    }

    public void SetNumberOfUnits(int numOfUnits)
    {
        if (numOfUnits > 0)
            this.numberOfUnits = numOfUnits;
    }

    public int getCatalogNumber() {
        return catalogNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double getUnitWeight() {
        return unitWeight;
    }

    public int getNumberOfUnits() {return numberOfUnits;}

    public String getCatagory() {return catagory;}
    public String toString()
    {
        return "Catalog number: " + catalogNumber + ", Market Id: " + marketId + ", Item name: " + itemName;
    }

}
