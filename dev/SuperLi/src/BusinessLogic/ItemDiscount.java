package BusinessLogic;

public abstract class ItemDiscount extends Discount {
    public ItemDiscount(double discountSize, DiscountType type)
    {
        super(discountSize,type);
    }

    public String toString()
    {
        return "Item discount- " + super.toString();
    }
}
