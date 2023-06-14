package BusinessLogic;

import BusinessLogic.Supplier;

import java.security.InvalidParameterException;

public class OrderItem{
    SupplierItem supplierItem;
    int amount;
    double itemDiscount;
    double finalPrice;

    public OrderItem(SupplierItem supplierItem, int amount, double itemDiscount, double finalPrice)
    {
        if (supplierItem == null)
            throw new InvalidParameterException("Item in order must be connected to supplier item");
        if (amount <= 0)
            throw new InvalidParameterException("Need to order at least one unit from the item");
        if (itemDiscount < 0)
            throw new InvalidParameterException("Discount can't be negative");
        // if not holds the original price itself!
        if (finalPrice > supplierItem.getUnitPrice()*amount)
            throw new InvalidParameterException("Final price can't be bigger than original price");
        this.supplierItem = supplierItem;
        this.amount = amount;
        this.itemDiscount = itemDiscount;
        this.finalPrice = finalPrice;
    }

    public int getItemNumber()
    {
        return this.supplierItem.getCatalogNumber();
    }

    public String getItemName()
    {
        return this.supplierItem.getItemName();
    }

    public int getItemAmount()
    {
        return this.amount;
    }

    public double getInitPrice()
    {
        return this.supplierItem.getUnitPrice();
    }

    public double getItemDiscount()
    {
        return this.itemDiscount;
    }

    public double getFinalPrice()
    {
        return this.finalPrice;
    }


    @Override
    public String toString()
    {
        return "Item name: " + supplierItem.getItemName() + " ,Number of units: " + amount + " ,Discount in shekels: " + itemDiscount + " ,Final price: " + finalPrice + " shekels.";
    }

}
