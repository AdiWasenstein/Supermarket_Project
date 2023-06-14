package BusinessLogic;

import BusinessLogic.OrderCostDiscount;
import BusinessLogic.OrderDiscount;
import BusinessLogic.OrderDiscountFactory;
import BusinessLogic.OrderUnitsDiscount;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class DiscountDocument {
    LinkedList<OrderDiscount> orderDiscounts;
    Map<Integer,LinkedList<ItemDiscount>> itemsDiscounts;

    public DiscountDocument()
    {
        orderDiscounts =  new LinkedList<OrderDiscount>();
        itemsDiscounts = new HashMap<Integer,LinkedList<ItemDiscount>>();
    }

    public LinkedList<OrderDiscount> getOrderDiscounts()
    {
        return this.orderDiscounts;
    }

    public Map<Integer,LinkedList<ItemDiscount>> getItemsDiscounts()
    {
        return this.itemsDiscounts;
    }

    public void setItemsDiscounts(Map<Integer,LinkedList<ItemDiscount>> itemsDiscounts)
    {
        this.itemsDiscounts = itemsDiscounts;
    }

    public void setOrderDiscounts(LinkedList<OrderDiscount> orderDiscounts)
    {
        this.orderDiscounts = orderDiscounts;
    }

    //this method returns null if it's impossible to add discount for item. else, adds it and returns it.
    public ItemDiscount addItemDiscount(int catalogNumber, String discountKind, String discountType, double discountSize, double value)
    {
        //creating new item discount
        ItemDiscountFactory itemFactoy = new ItemDiscountFactory();
        ItemDiscount newItem = itemFactoy.createItemDiscount(discountKind,discountType,discountSize,value);
        if(newItem==null)//meaning couldn't create new SuperLi.src.BusinessLogic.ItemDiscount object for some reason
            return null;
        //checking if there are no discounts for this item yet
        if(!(this.itemsDiscounts.containsKey(catalogNumber)))
        {
            LinkedList<ItemDiscount> tempList = new LinkedList<ItemDiscount>();
            tempList.add(newItem);
            this.itemsDiscounts.put(catalogNumber,tempList);
            return newItem;
        }
        //if there are already discounts for this item, check if we don't already have the given discount
        LinkedList<ItemDiscount> discountsOfItem = this.itemsDiscounts.get(catalogNumber);
        for(int i=0;i<discountsOfItem.size();i++)
        {
            if(discountsOfItem.get(i).hasSameCondition(newItem))
            {
                return null;//item already has the given discount
            }
        }
        //adding the new discount
        discountsOfItem.add(newItem);
        return newItem;
    }

    //this method throws exception if it's impossible to remove discount for item. else, removes it and returns the deleted discount.
    public ItemDiscount removeItemDiscount(int catalogNumber, String discountKind, String discountType, double discountSize, double value)throws Exception
    {
        //checking if there are no discounts for this item yet
        if(!(this.itemsDiscounts.containsKey(catalogNumber)))
        {
            throw new Exception("There are no discounts for this item, and therefore the given discount doesn't exist");
        }
        //else, we need to check if item has this discount
        ItemDiscountFactory itemFactoy = new ItemDiscountFactory();
        ItemDiscount tempDiscount = itemFactoy.createItemDiscount(discountKind,discountType,discountSize,value);
        if(tempDiscount==null)//meaning couldn't create new ItemDiscount object for some reason
            throw new Exception("discount details are incorrect, item discount was not removed.");
        LinkedList<ItemDiscount> discountsOfItem = this.itemsDiscounts.get(catalogNumber);
        int index = -1;
        for(int i=0;i<discountsOfItem.size();i++)
        {
            if(discountsOfItem.get(i).equals(tempDiscount))
            {
                index = i;
                break;
            }
        }
        if (index==-1)//meaning the discount was not found
        {
            throw new Exception("given discount doesn't exist for this item.");
        }
        ItemDiscount removedDiscount = discountsOfItem.remove(index);
        if(discountsOfItem.isEmpty())
        {
            this.itemsDiscounts.remove(catalogNumber);
        }
        return removedDiscount;
    }

    //this method returns null if it's impossible to add order discount. else, adds it and returns it.
    public OrderDiscount addOrderDiscount(String discountKind, String discountType, double discountSize, double value)
    {
        OrderDiscountFactory orderFactory = new OrderDiscountFactory();
        OrderDiscount newItem = orderFactory.createOrderDiscount(discountKind,discountType,discountSize,value);
        if(newItem==null)//meaning couldn't create new SuperLi.src.BusinessLogic.OrderDiscount object for some reason
            return null;
        //check if we don't already have the given discount
        for(int i=0;i<this.orderDiscounts.size();i++)
        {
            if(this.orderDiscounts.get(i).hasSameCondition(newItem))
            {
                return null;//already has the given discount
            }
        }
        //adding the new discount
        this.orderDiscounts.add(newItem);
        return newItem;
    }

    //this method throws exception if it's impossible to remove order discount. else, removes it and returns deleted discount.
    public OrderDiscount removeOrderDiscount(String discountKind, String discountType, double discountSize, double value)throws Exception
    {
        OrderDiscountFactory orderFactory = new OrderDiscountFactory();
        OrderDiscount tempDiscount = orderFactory.createOrderDiscount(discountKind,discountType,discountSize,value);
        if(tempDiscount==null)//meaning couldn't create new SuperLi.src.BusinessLogic.OrderDiscount object for some reason
            throw new Exception("discount details are incorrect, order discount was not removed.");
        //check if we have the given discount
        int index = -1;
        for(int i=0;i<this.orderDiscounts.size();i++)
        {
            if(this.orderDiscounts.get(i).equals(tempDiscount))
            {
                index = i;
                break;
            }
        }
        if(index==-1)//meaning the discount was not found
        {
            throw new Exception("given order discount doesn't exist for this item.");
        }
        OrderDiscount removedDiscount = this.orderDiscounts.remove(index);
        return removedDiscount;
    }


    private LinkedList<ItemDiscount> allPossibleDiscountItem(int catalogNumber, double value)
    {
        LinkedList<ItemDiscount> allItemDiscounts = this.itemsDiscounts.get(catalogNumber);
        if (allItemDiscounts == null) {
            return null;
        }
        LinkedList<ItemDiscount> result = new LinkedList<>();

        for (ItemDiscount currDis : allItemDiscounts)
        {
            if (currDis.canUseTheDiscount(value))
                result.add(currDis);
        }
        return result;

    }

    public ItemDiscount findBestDiscount(int catalogNumber, int value, double initialCost)
    {
        LinkedList<ItemDiscount> allPossibleDiscountItem = allPossibleDiscountItem(catalogNumber, value);
        if (allPossibleDiscountItem == null || allPossibleDiscountItem.isEmpty()) {
            return null;
        }
        ItemDiscount bestDiscount = allPossibleDiscountItem.getFirst();
        if (bestDiscount == null) {
            return null;
        }
        for (ItemDiscount currDis : allPossibleDiscountItem)
        {
            if (currDis.GetPriceAfterDiscount(initialCost) < bestDiscount.GetPriceAfterDiscount(initialCost))
                bestDiscount = currDis;
        }
        return bestDiscount;
    }

    private LinkedList<OrderDiscount> allPossibleDiscountOrder(int Units, double cost)
    {
        LinkedList<OrderDiscount> allOrderDiscount = this.getOrderDiscounts();
        LinkedList<OrderDiscount> result = new LinkedList<>();

        for (OrderDiscount dis : allOrderDiscount)
        {
            if (dis instanceof OrderUnitsDiscount)
            {
                if (((OrderUnitsDiscount) dis).getNumberOfUnitsInOrder() <= Units)
                    result.add(dis);
            }

            if (dis instanceof OrderCostDiscount)
                if (((OrderCostDiscount) dis).getCost() <= cost)
                    result.add(dis);
        }

        return result;
    }

    public OrderDiscount bestOrderDiscount(int units, double cost)
    {
        LinkedList<OrderDiscount> allPosiible = allPossibleDiscountOrder(units, cost);
        if (allPosiible == null || allPosiible.isEmpty())
            return null;
        OrderDiscount bestOrderDis = allPosiible.getFirst();
        for (OrderDiscount dis : allPosiible)
        {
            if (dis.GetPriceAfterDiscount(cost) < bestOrderDis.GetPriceAfterDiscount(cost))
                bestOrderDis = dis;
        }
        return bestOrderDis;
    }

    public void removeAllDiscountsOfItem(int catalogNumber)
    {
        if(this.itemsDiscounts.containsKey(catalogNumber))
        {
            this.itemsDiscounts.remove(catalogNumber);
        }
    }

    public boolean supplierHasItemDiscount()
    {
        if(this.itemsDiscounts.isEmpty())
            return false;
        return true;
    }

    public boolean supplierHasOrderDiscount()
    {
        if(this.orderDiscounts.isEmpty())
            return false;
        return true;
    }
}
