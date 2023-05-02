package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;

public class ItemUnitsDiscount extends ItemDiscount {
    private int numberOfUnitsOfItem;
    public ItemUnitsDiscount(double discountSize, DiscountType type, int numberOfUnitsOfItem)
    {
        super(discountSize,type);
        if (numberOfUnitsOfItem <= 0)
        {
            throw new InvalidParameterException("number of item's units must be positive.");
        }
        this.numberOfUnitsOfItem = numberOfUnitsOfItem;
    }
    public boolean canUseTheDiscount(double value) {
        return value >= this.numberOfUnitsOfItem;
    }
    public int getNumberOfUnitsOfItem()
    {
        return this.numberOfUnitsOfItem;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!hasSameCondition(obj))
        {
            return false;
        }
        ItemUnitsDiscount newO = (ItemUnitsDiscount)obj;
//        return this.numberOfUnitsOfItem == newO.numberOfUnitsOfItem && this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
//        return this.numberOfUnitsOfItem == newO.numberOfUnitsOfItem;
       return this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
    }

    //this method returns true if given object is an SuperLi.src.BusinessLogic.ItemUnitsDiscount object and has the same numberOfUnitsOfItem
    public boolean hasSameCondition(Object obj)
    {
        if(this==obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ItemUnitsDiscount newO = (ItemUnitsDiscount)obj;
        return this.numberOfUnitsOfItem == newO.numberOfUnitsOfItem;
    }

    @Override
    public double GetPriceAfterDiscount(double initialPrice) {
        return this.getDiscountType().CalculatePriceAfterDiscount(initialPrice, this.getDiscountSize());
    }

    public String toString()
    {
        return super.toString() + "per " + getNumberOfUnitsOfItem() + " units of item";
    }
}
