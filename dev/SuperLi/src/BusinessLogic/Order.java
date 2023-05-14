package SuperLi.src.BusinessLogic;

import SuperLi.src.BusinessLogic.Supplier;

import java.security.InvalidParameterException;
import java.util.Date;
import java.util.LinkedList;
import java.text.*;

public class Order {

//    static int orderNum = 1;
    Supplier orderSupplier;
    int orderNumber;
    Date OrderDate;
    LinkedList<OrderItem> orderItems;
    double costOfOrder;
    int branchNumber;



    public Order(int orderNumber, Supplier orderSupplier, LinkedList<OrderItem> orderItems, int branchNumber)
    {
        if (orderSupplier == null)
            throw new InvalidParameterException("order must have supplier");
        if (orderItems == null || orderItems.isEmpty())
            throw new InvalidParameterException("order must have a least one item to order");
        if (branchNumber <= 0)
            throw new InvalidParameterException("Branch number must be a positive number");

        this.orderSupplier = orderSupplier;
        this.orderItems = orderItems;
        this.OrderDate = new Date();
        for (OrderItem curr : orderItems)
        {
            this.costOfOrder+=curr.getFinalPrice();
        }
        this.branchNumber = branchNumber;
        this.orderNumber = orderNumber;
    }


    public String getSupplierName()
    {
        return this.orderSupplier.getSupplierCard().getSupplierName();
    }

    public String getAddress()
    {
        return this.orderSupplier.getSupplierCard().getSupplierAddress();
    }

    public int getOrderNumber()
    {
        return this.orderNumber;
    }

    public int getSupplierId()
    {
        return this.orderSupplier.getSupplierId();
    }

    public Date getOrderDate()
    {
        return this.OrderDate;
    }

    public int getBranchNumber() {return this.branchNumber;}

    public String getContactNumber()
    {
        return this.orderSupplier.getContacts().getFirst().GetPhoneNumber();
    }

    public LinkedList<OrderItem> getOrderItems()
    {
        return this.orderItems;
    }

    public Supplier getOrderSupplier()
    {
        return this.orderSupplier;
    }

    public double getCostOfOrder()
    {
        return this.costOfOrder;
    }
    public void setCostOfOrder(double newCost)
    {
        if (newCost <= 0 || newCost > this.costOfOrder)
            return;
        this.costOfOrder = newCost;
    }
    @Override
    public String toString()
    {
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        String dateAsString = df.format(OrderDate);
        String str =  "Branch number: " + branchNumber + "\nOrder number: " + orderNumber + "\nSupplier: " + orderSupplier +  " ,supply time: " + orderSupplier.printSupplyTimeData() +"\nOrder date: " + dateAsString + "\nOrdered items:";
        for(OrderItem item: orderItems)
        {
            str += "\n" + item.toString();
        }
        return str;
    }

//    // check if really needed
//    public int daysTillArrives(Day dayOfOrder, Supplier supplier)
//    {
//        return 0;
//    }
}
