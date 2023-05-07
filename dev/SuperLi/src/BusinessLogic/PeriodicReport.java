package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;
import java.util.HashMap;

public class PeriodicReport {
    private int reportId;
    private int branchNumber;
    private Day dayToOrder;
    private Supplier supplier;
    private HashMap<SupplierItem,Integer> items;

    public PeriodicReport(int reportId, int branchNumber, Day dayToOrder, Supplier supplier, HashMap<SupplierItem,Integer> items)
    {
        if(reportId<=0)
            throw new InvalidParameterException("report id must be positive number.");
        if(branchNumber<=0)
            throw new InvalidParameterException("branch number must be positive number.");
        if(dayToOrder == null)
            throw new InvalidParameterException("periodic report must have a day to make an order.");
        if(supplier == null)
            throw new InvalidParameterException("periodic report must have a supplier.");
        if(items == null)
            throw new InvalidParameterException("periodic report must have items to order.");
        this.reportId = reportId;
        this.branchNumber = branchNumber;
        this.dayToOrder = dayToOrder;
        this.supplier = supplier;
        this.items = items;
    }

    public int getReportId() {
        return reportId;
    }

    public int getBranchNumber() {
        return branchNumber;
    }

    public Day getDayToOrder() {
        return dayToOrder;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public HashMap<SupplierItem, Integer> getItems() {
        return items;
    }

    //this func updates the quantity of an item.
    public void setQuantityOfItem(int itemCatalogNumber, int newQuantity)throws InvalidParameterException
    {
        if(newQuantity <= 0)
            throw new InvalidParameterException("quantity of item must be a positive number.");
        boolean itemFound = false;
        for(SupplierItem item : items.keySet())
        {
            if(item.getCatalogNumber() == itemCatalogNumber)
            {
                if(newQuantity > item.getNumberOfUnits())
                    throw new InvalidParameterException("quantity of item must be smaller or equal to number of units supplier can supply from item.");
                items.put(item,newQuantity);
                itemFound = true;
                break;
            }
        }
        if(!itemFound)
            throw new InvalidParameterException("an item with given catalog number does not exist in periodic report.");
    }




}
