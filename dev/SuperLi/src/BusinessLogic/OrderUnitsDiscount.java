package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;

public class OrderUnitsDiscount extends OrderDiscount {
    private int numberOfUnitsInOrder;

    public OrderUnitsDiscount(double discountSize, DiscountType type, int numberOfUnitsInOrder)
    {
        super(discountSize,type);
        if (numberOfUnitsInOrder <= 0)
        {
            throw new InvalidParameterException("number of units in order must be positive.");
        }
        this.numberOfUnitsInOrder = numberOfUnitsInOrder;
    }

    public boolean canUseTheDiscount(double value) {
        return value >= this.numberOfUnitsInOrder;
    }

    public int getNumberOfUnitsInOrder() {
        return this.numberOfUnitsInOrder;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!hasSameCondition(obj))
        {
            return false;
        }
        OrderUnitsDiscount newO = (OrderUnitsDiscount)obj;
//        return this.numberOfUnitsInOrder == newO.numberOfUnitsInOrder && this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
//        return this.numberOfUnitsInOrder == newO.numberOfUnitsInOrder;
        return this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
    }

    //this method returns true if given object is an SuperLi.src.BusinessLogic.OrderUnitsDiscount object and has the same numberOfUnitsInOrder
    public boolean hasSameCondition(Object obj)
    {
        if(this==obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderUnitsDiscount newO = (OrderUnitsDiscount)obj;
        return this.numberOfUnitsInOrder == newO.numberOfUnitsInOrder;
    }

    public String toString()
    {
        return super.toString() + "per " + getNumberOfUnitsInOrder() + " units in order";
    }
}
