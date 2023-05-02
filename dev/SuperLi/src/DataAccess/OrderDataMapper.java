package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.Order;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class OrderDataMapper implements IDataMapper<Order> {
    private HashMap<Integer, Order> ordersIdentityMap;
    private static OrderDataMapper instance = new OrderDataMapper();

    private OrderDataMapper()
    {
        this.ordersIdentityMap = new HashMap<>();
    }

    public static OrderDataMapper getInstance()
    {
        return instance;
    }

    public Optional<Order> find(String phoneNumber)
    {
        return null;//REMOVE
    }
    public LinkedList<Order> findAll(String phoneNumber)//NOT SURE WE NEED THIS HERE
    {
        return null;//REMOVE
    }
    public void insert(Order order)
    {

    }
    public void update(Order order)
    {

    }
    public void delete(Order order)
    {

    }
}
