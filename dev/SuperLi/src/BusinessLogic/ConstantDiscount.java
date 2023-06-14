package BusinessLogic;

public class ConstantDiscount implements DiscountType {
    public double CalculatePriceAfterDiscount(double initialPrice ,double discount)
    {
        return initialPrice - discount;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass())
            return false;
        return true;
    }

    public String toString()
    {
        return "constant";
    }
}
