package BusinessLogic;

import java.security.InvalidParameterException;

public class OrderCostDiscount extends OrderDiscount {
    private double cost;
    public OrderCostDiscount(double discountSize, DiscountType type, double cost)
    {
        super(discountSize,type);
        if (cost <= 0)
        {
            throw new InvalidParameterException("total coat in order must be positive.");
        }
        this.cost = cost;
    }

    public boolean canUseTheDiscount(double value) {
        return value >= this.cost;
    }
    public double getCost()
    {
        return this.cost;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!hasSameCondition(obj))
        {
            return false;
        }
        OrderCostDiscount newO = (OrderCostDiscount)obj;
//        return this.cost == newO.cost && this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
//        return this.cost == newO.cost;
        return this.getDiscountType().equals(newO.getDiscountType()) && this.getDiscountSize() == newO.getDiscountSize();
    }

    //this method returns true if given object is an SuperLi.src.BusinessLogic.OrderCostDiscount object and has the same cost
    public boolean hasSameCondition(Object obj)
    {
        if(this==obj)
        {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        OrderCostDiscount newO = (OrderCostDiscount)obj;
        return this.cost == newO.cost;
    }



    public String toString()
    {
        return super.toString() + "for buying in total cost of more than " + getCost() + " shekels";
    }
}
