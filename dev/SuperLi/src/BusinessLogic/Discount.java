package BusinessLogic;

import java.security.InvalidParameterException;

public abstract class Discount {
    private double discountSize;
    private DiscountType type;//we use the Bridge Design Pattern

    public Discount(double discountSize, DiscountType type) {
        if (discountSize <= 0)
            throw new InvalidParameterException("discount size must be positive");
        if (type == null)
            throw new InvalidParameterException("discount must have a type");
        this.discountSize = discountSize;
        this.type = type;
    }

    public DiscountType getDiscountType()
    {
        return this.type;
    }
    public double getDiscountSize()
    {
        return this.discountSize;
    }

    public void SetDiscountSize(double size)
    {
        if (size <= 0)
            throw new InvalidParameterException("discount size must be positive");
        this.discountSize = size;
    }

    public String toString()
    {
        return "discount size: " + getDiscountSize() + " discount type: " + getDiscountType().toString() +" ";
    }

    //this method returns true if given object is the same type of current discount, and has the same value (condition)
    public abstract boolean hasSameCondition(Object obj);

    public abstract double GetPriceAfterDiscount(double initialPrice);

    public abstract boolean canUseTheDiscount(double value);

//    {
//        if (initialPrice < 0)
//            throw new InvalidParameterException("initial price can't be negative");
//        return this.type.CalculatePriceAfterDiscount(initialPrice,this.discountSize);
//    }
}
