package BusinessLogic;

public abstract class OrderDiscount extends Discount {
    public OrderDiscount(double discountSize, DiscountType type)
    {
        super(discountSize,type);
    }

    public String toString()
    {
        return "SuperLi.src.BusinessLogic.Order discount- " + super.toString();
    }

    @Override
    public double GetPriceAfterDiscount(double initialPrice) {
        return this.getDiscountType().CalculatePriceAfterDiscount(initialPrice, this.getDiscountSize());    }
}
