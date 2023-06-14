package BusinessLogic;

//import org.graalvm.collections.Pair;
import BusinessLogic.Pair;


import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Map;

public class SupplierContract {

    PaymentsWays payment;
    Supplier supplier;
    DiscountDocument discountDocument;
    LinkedList<SupplierItem> supplierItems;

    public SupplierContract(PaymentsWays payment, Supplier supplier) {
        if(supplier == null)
            throw new InvalidParameterException("Need to receive a supplier");
        this.discountDocument = new DiscountDocument();
        this.supplier = supplier;
        this.payment = payment;
        this.supplierItems = new LinkedList<SupplierItem>();
    }

    public boolean checkItemIDandAmount(Pair<Integer, Integer> pairMarketIdAndQuantity)
    {
        for(int i=0;i<this.supplierItems.size();i++)
        {
            if(this.supplierItems.get(i).GetMarketId() == pairMarketIdAndQuantity.getLeft() && this.supplierItems.get(i).getNumberOfUnits() >= pairMarketIdAndQuantity.getRight())
                return true;
        }
        return false;
    }

    public boolean canSupplyMarketItem(int marketId)
    {
        for(SupplierItem sItem : this.supplierItems)
        {
            if(sItem.GetMarketId() == marketId)
                return true;
        }
        return false;
    }

    public int numberOfUnitsCanSupply(int marketId)
    {
        int units = 0;
        for(int i=0;i<this.supplierItems.size();i++)
        {
            if(this.supplierItems.get(i).GetMarketId()==marketId)
            {
                units = this.supplierItems.get(i).getNumberOfUnits();
                break;
            }
        }
        return units;
    }

    public PaymentsWays getPayment() {
        return payment;
    }

    public DiscountDocument getDiscountDocument() {
        return discountDocument;
    }

    public LinkedList<SupplierItem> getSupplierItems() {
        return supplierItems;
    }

    public void setPayment(PaymentsWays payment) {
        this.payment = payment;
    }


    //this method checks if item already exists in the supplier items' list, according to it's catalog number.
    //if exists, returns it's index in the list. else, return -1.
    public int checkIfItemExists(int catalogNumber)
    {
        for(int i=0;i<this.supplierItems.size();i++)
        {
            if(this.supplierItems.get(i).getCatalogNumber() == catalogNumber)
                return i;
        }
        return -1;
    }

    public SupplierItem getSupplierItemByCatalogNum(int catalogNumber)
    {
        int index = this.checkIfItemExists(catalogNumber);
        if(index == -1)
            return null;
        return this.supplierItems.get(index);
    }

//    public boolean isitemExists(int catalogNumber)
//    {
//        if(this.checkIfItemExists(catalogNumber) == -1)
//            return false;
//        return true;
//    }

    //this method checks if the item's manufacturer is valid.
    private boolean checkIfItemHasValidManufacturer(String manufacturer)
    {
        for(int i=0;i<supplier.getSupplierManufacturers().size();i++) {
            if (supplier.getSupplierManufacturers().get(i).equals(manufacturer)) {
                return true;
            }
        }
        return false;
    }

    //this method checks if the item's category is valid.
    private boolean checkIfItemHasValidCategory(String category)
    {
        for(int j=0;j<supplier.getSupplierCatagories().size();j++)
        {
            if(supplier.getSupplierCatagories().get(j).equals(category))
            {
                return true;
            }
        }
        return false;
    }

    //this method checks if it's possible to add an item to the supplier items' list, according to it's details.
    public boolean canAddItem(int catalogNumber,String manufacturer,String category)
    {
        if(checkIfItemExists(catalogNumber) != -1)
        {
            return false;
        }
        if(!checkIfItemHasValidManufacturer(manufacturer) || !checkIfItemHasValidCategory(category))
        {
            return false;
        }
        return true;
    }

    // add item with given parameters
//    public boolean addItem(int catalogNumber,String itemName, String manufacturer,double unitPrice,double unitWeight,int numberOfUnits, String category, int marketId)
//    {
//        if(!canAddItem(catalogNumber,manufacturer,category))
//        {
//            return false;
//        }
//        //else, if possible:
//        SupplierItem newItem = new SupplierItem(catalogNumber, itemName, manufacturer, unitPrice, unitWeight, numberOfUnits, category, marketId);
//        this.supplierItems.add(newItem);
//        return true;
//    }

    // add item with given item
    public void addItem(SupplierItem newItem)
    {
        this.supplierItems.add(newItem);
    }
    public void removeItem(int catalogNumber)throws Exception
    {
        int index = checkIfItemExists(catalogNumber);
        if(index == -1)
            throw new Exception("Supplier not supplying an item with given catalog number.");
        this.supplierItems.remove(index);
        this.getDiscountDocument().removeAllDiscountsOfItem(catalogNumber);
    }

    //this method returns null if it's impossible to add discount for item. else, returns the new discount.
    public ItemDiscount addItemDiscount(int catalogNumber, String discountKind, String discountType, double discountSize, double value)
    {
        //checking if the item we want to add discount to actually exists
        if(checkIfItemExists(catalogNumber)==-1)
        {
            return null;
        }
        return this.discountDocument.addItemDiscount(catalogNumber,discountKind,discountType,discountSize,value);
    }

    //this method throws exception if it's impossible to remove discount for item. else, removes it and returns the deleted discount.
    public ItemDiscount removeItemDiscount(int catalogNumber, String discountKind, String discountType, double discountSize, double value)throws Exception
    {
        //checking if the item we want to remove the discount from is actually exists
        if(checkIfItemExists(catalogNumber)==-1)
        {
            throw new Exception("The supplier doesn't supply an item with given catalog number.");
        }
        return this.discountDocument.removeItemDiscount(catalogNumber,discountKind,discountType,discountSize,value);
    }

    //this method returns null if it's impossible to add order discount. else, returns the new OrderDiscount.
    public OrderDiscount addOrderDiscount(String discountKind, String discountType, double discountSize, double value)
    {
        return this.discountDocument.addOrderDiscount(discountKind,discountType,discountSize,value);
    }

    //this method throws exception if it's impossible to remove discount for order. else, removes it and returns deleted discount.
    public OrderDiscount removeOrderDiscount(String discountKind, String discountType, double discountSize, double value)throws Exception
    {
        return this.discountDocument.removeOrderDiscount(discountKind,discountType,discountSize,value);
    }

    public ItemDiscount findItemDiscount(int catalogNumber, int amount)
    {
        if (catalogNumber <= 0 || amount <= 0)
            return null;
        Map<Integer,LinkedList<ItemDiscount>> itemsDiscounts = this.discountDocument.getItemsDiscounts();
        LinkedList<ItemDiscount> allDiscountsOfItem = itemsDiscounts.get(catalogNumber);
        for(ItemDiscount dis : allDiscountsOfItem)
        {
            if(dis instanceof ItemUnitsDiscount)
            {
                ItemUnitsDiscount d = (ItemUnitsDiscount)dis;
                if(d.getNumberOfUnitsOfItem() == amount)
                    return dis;
            }
        }
        return null;
    }





}

