package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;

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

    //this func returns all the items and quantities.
    public HashMap<SupplierItem, Integer> getItems() {
        return items;
    }

    //this func returns only supplier items (without quantities)
    public LinkedList<SupplierItem> getSupplierItems()
    {
        LinkedList<SupplierItem> supplierItems = new LinkedList<>();
        for(SupplierItem s: this.items.keySet())
        {
            supplierItems.add(s);
        }
        return supplierItems;
    }
    //this func updates the quantity of an item.
    public void setQuantityOfItem(int itemCatalogNumber, int newQuantity)throws InvalidParameterException
    {
        if(newQuantity <= 0)
            throw new InvalidParameterException("quantity of item must be a positive number.");
        boolean itemFound = false;
        for(SupplierItem item : items.keySet())
        {
            if(item.GetMarketId() == itemCatalogNumber)
            {
                if(newQuantity > item.getNumberOfUnits())//if requested amount is bigger than max number of units supplier can supply, update the new amount to the max amount possible.
                    items.put(item,item.getNumberOfUnits());
                else
                {
                    items.put(item, newQuantity);
                }
                itemFound = true;
                break;
            }
        }
        if(!itemFound)
            throw new InvalidParameterException("an item with given catalog number does not exist in periodic report.");
    }




}
