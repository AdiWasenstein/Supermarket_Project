package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class OrderDataMapper extends ADataMapper<Order> {
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


    public void insert(Order order)
    {
        this.ordersIdentityMap.put(order.getOrderNumber(), order);
        insertOrder(order);
    }

    protected String insertQuery(Order order)
    {
        return "";
    }


    public void insertOrder(Order order)
    {
        for (OrderItem item : order.getOrderItems())
        {
            OrderItemDataMapper.getInstance().insert(item, order.getOrderNumber());
        }
        executeVoidQuery(String.format("INSERT INTO Orders(orderNumber, supplierId, orderDate, costOfOrder, branchNumber) " +
                        "VALUES (%d, %d, %s, %.2f, %d)", order.getOrderNumber(), order.getSupplierId(),
                order.getOrderDate().toString(), order.getCostOfOrder(), order.getBranchNumber()));

    }

    protected String deleteQuery(Order order){
        return "";
    }


    protected String updateQuery(Order order)
    {
        return "";
    }


    public String findQuery(String ...key){
        return String.format("SELECT * FROM Orders WHERE orderNumber = %d", Integer.valueOf(key[0]));
    }

    public LinkedList<Order> findAllBySupplier(int suppId)
    {
        LinkedList<Order> ordersForSupp = new LinkedList<>();
        for (Order order: findAll())
        {
            if (order.getSupplierId() == suppId)
                ordersForSupp.add(order);
        }
        return ordersForSupp;
    }

    public LinkedList<Order> findAllByBranch(int branchNumber)
    {
        LinkedList<Order> ordersForSupp = new LinkedList<>();
        for (Order order: findAll())
        {
            if (order.getBranchNumber() == branchNumber)
                ordersForSupp.add(order);
        }
        return ordersForSupp;
    }

    public String findAllQueryByKey(String ...key){
        return "";
    }

    public String findAllQuery(){
        return "SELECT * FROM Orders";
    }

    public Order findInIdentityMap(String ...key){
        return ordersIdentityMap.get(Integer.valueOf(key[0]));
    }


    public Order insertIdentityMap(ResultSet match) throws SQLException {
        if (match == null)
            return null;
        Order order = ordersIdentityMap.get(match.getInt("orderNumber"));
        if(order != null)
            return order;

        Optional<Supplier> suppOpt = SupplierDataMapper.getInstance().find(String.valueOf(match.getInt("supplierId")));
        if (suppOpt.isEmpty())
            return null;
        Supplier supp = suppOpt.get();
        int orderNumber = match.getInt("orderNumber");
        LinkedList<OrderItem> items = OrderItemDataMapper.getInstance().findAllByKey(Integer.toString(orderNumber));
        int branchNumber= match.getInt("branchNumber");
        order = new Order(supp, items, branchNumber, orderNumber);
        ordersIdentityMap.put(orderNumber, order);
        return order;
    }




}
