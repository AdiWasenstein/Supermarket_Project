package BusinessLogic;

public interface DiscountType {
    public double CalculatePriceAfterDiscount(double initialPrice ,double discount);
    public String toString();
}
