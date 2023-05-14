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
        executeVoidQuery(String.format("INSERT INTO `Orders`(orderNumber, supplierId, orderDate, costOfOrder, branchNumber) " +
                        "VALUES (%d, %d, '%s', %.2f, %d)", order.getOrderNumber(), order.getSupplierId(),
                order.getOrderDate().toString(), order.getCostOfOrder(), order.getBranchNumber()));
        for (OrderItem item : order.getOrderItems())
        {
            OrderItemDataMapper.getInstance().insert(item, order.getOrderNumber(), order.getSupplierId());
        }
//        executeVoidQuery(String.format("INSERT INTO `Orders`(orderNumber, supplierId, orderDate, costOfOrder, branchNumber) " +
//                        "VALUES (%d, %d, '%s', %.2f, %d)", order.getOrderNumber(), order.getSupplierId(),
//                order.getOrderDate().toString(), order.getCostOfOrder(), order.getBranchNumber()));

    }

    protected String deleteQuery(Order order){
        return "";
    }


    protected String updateQuery(Order order)
    {
        return "";
    }


    public String findQuery(String ...key){
        return String.format("SELECT * FROM `Orders` WHERE orderNumber = %d", Integer.valueOf(key[0]));
    }

    public LinkedList<Order> findAllBySupplier(int suppId) {
        LinkedList<Integer> idOrders = new LinkedList<>();
        LinkedList<Order> objects = new LinkedList<>();
        openConnection();
        if (connection == null) {
            return objects;
        }
        String query = String.format("SELECT orderNumber FROM Orders WHERE supplierId = %d", suppId);
        ResultSet result = executeSelectQuery(query);
        if (result == null) {
            closeConnection();
            return objects;
        }
        try {
            while (result.next()) {
                int orderId = result.getInt("orderNumber");
                idOrders.add(orderId);
            }
            for (Integer id : idOrders) {
                Optional<Order> object = find(Integer.toString(suppId));
                if (!object.isEmpty())
                    objects.add(object.get());
            }
        } catch (SQLException e) {
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return objects;
    }
//        LinkedList<Order> ordersForSupp = new LinkedList<>();
//        for (Order order: findAll())
//        {
//            if (order.getSupplierId() == suppId)
//                ordersForSupp.add(order);
//        }
//        return ordersForSupp;

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
        return "SELECT * FROM `Orders`";
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


        int orderNumber = match.getInt("orderNumber");
        int branchNumber= match.getInt("branchNumber");
        Optional<Supplier> suppOpt = SupplierDataMapper.getInstance().find(String.valueOf(match.getInt("supplierId")));
        if (suppOpt.isEmpty())
            return null;
        Supplier supp = suppOpt.get();
        LinkedList<OrderItem> items = OrderItemDataMapper.getInstance().findAllByKey((Integer.toString(orderNumber)), Integer.toString(orderNumber));

        order = new Order(orderNumber, supp, items, branchNumber);
        ordersIdentityMap.put(orderNumber, order);
        return order;
    }




}
