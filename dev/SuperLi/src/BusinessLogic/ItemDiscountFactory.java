package BusinessLogic;

public class ItemDiscountFactory {
    // kind = subClasses : cost/units
    // type = percentage/constant
    // size = size of discount
    // value = cost/number of units
    public ItemDiscount createItemDiscount(String discountKind, String discountType, double size, double value)
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
        //checking validity of prercentage size if neccessery
        if ((discountType.equalsIgnoreCase("Percentage") && (size> 100)))
            return null;
        // finding kind of discount
        if (discountKind.equalsIgnoreCase("ItemUnitDiscount"))
            return new ItemUnitsDiscount(size, type, (int)value);
        return null;
    }
}
