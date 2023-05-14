package SuperLi.src.BusinessLogic;

public class OrderDiscountFactory {
    // kind = subClasses : cost/units
    // type = percentage/constant
    // size = size of discount
    // value = cost/number of units
    public OrderDiscount createOrderDiscount(String discountKind, String discountType, double size, double value)
    {
        DiscountType type = null;

        // validity check
        if ((discountKind == null) || (discountKind.equals("")))
            return null;
        if ((discountType == null) || (discountType.equals("")))
            return null;

        // finding type of discount
        if (discountType.equalsIgnoreCase("Percentage"))
            type = new PercentageDiscount();
        if (discountType.equalsIgnoreCase("Constant"))
            type = new ConstantDiscount();

        if ((discountType.equalsIgnoreCase("Percentage") && (size> 100)))
            return null;

        // creating specific discount base on given kind
        if (discountKind.equalsIgnoreCase("OrderUnitsDiscount"))
            return new OrderUnitsDiscount(size,type, (int)value);
        if (discountKind.equalsIgnoreCase("OrderCostDiscount"))
            return new OrderCostDiscount(size,type, value);

        return null;
    }
}
