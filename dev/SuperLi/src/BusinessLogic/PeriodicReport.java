package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.LinkedList;

public class PeriodicReport extends AReport{
    private static int reportCounter = 1;
    private int reportId;
    private int branchNumber;
    private Day dayToOrder;
    private Supplier supplier;
    private HashMap<SupplierItem,Integer> items;

    public PeriodicReport(int branchNumber, Day dayToOrder, Supplier supplier, HashMap<SupplierItem,Integer> items)
    {
        if(branchNumber<=0)
            throw new InvalidParameterException("branch number must be positive number.");
        if(dayToOrder == null)
            throw new InvalidParameterException("periodic report must have a day to make an order.");
        if(supplier == null)
            throw new InvalidParameterException("periodic report must have a supplier.");
        if(items == null)
            throw new InvalidParameterException("periodic report must have items to order.");
        this.reportId = reportCounter++;
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
                if(newQuantity > item.getNumberOfUnits()) //if requested amount is bigger than max number of units supplier can supply, update the new amount to the max amount possible.
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

    public Day oneDayBeforeOrderDay()
    {
        //getting the index of day to order
        int indexDayToOrder = this.dayToOrder.ordinal();
        if(indexDayToOrder==0)//if day is sunday
            return Day.values()[6];//then previous day is Saturday.
        return Day.values()[indexDayToOrder-1];
    }

    public String toString()
    {
        return "";
    }

    public String[] getRecordData(SupplierItem supplierItem)
    {
        String catalogNumber = Integer.toString(supplierItem.getCatalogNumber());
        String MarketId = Integer.toString(supplierItem.GetMarketId());
        String name = supplierItem.getItemName();
        String manufacturer = supplierItem.getManufacturer();
        String category = supplierItem.getCatagory();
        String numberOfUnits = Integer.toString(supplierItem.getNumberOfUnits());
        String unitPrice = Double.toString(supplierItem.getUnitPrice());
        String unitWeight = Double.toString(supplierItem.getUnitWeight());
        return new String[]{catalogNumber, MarketId, name, manufacturer, category,numberOfUnits,unitPrice,unitWeight};
    }

    public LinkedList<String[]> initializeRecords()
    {
        LinkedList<String[]> records = new LinkedList<>();
        for(SupplierItem supplierItem : this.items.keySet())
            records.add(getRecordData(supplierItem));
        return records;
    }


    public String[] getHeaders()
    {
        return new String[]{"Catalog Number", "Market Id", "Name", "Manufacturer", "Category","Number Of Units","Unit Price","Unit Weight"};

    }

    public void removeItem(SupplierItem sItem)throws Exception
    {
        if(this.items.containsKey(sItem))
        {
            this.items.remove(sItem);
        }
        else
            throw new Exception("supplier item doesn't exist in report.");
    }




}
