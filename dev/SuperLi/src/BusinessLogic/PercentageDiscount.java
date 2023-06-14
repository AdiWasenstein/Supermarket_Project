package BusinessLogic;

public class PercentageDiscount implements DiscountType {
    public double CalculatePriceAfterDiscount(double initialPrice ,double discount)
    {
        double percentageD = (100 - discount) / 100;
        return initialPrice * percentageD;
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
        return "percentage";
    }
}

