package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.OrderItem;
import SuperLi.src.BusinessLogic.SupplierItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;


public class OrderItemDataMapper extends ADataMapper<OrderItem>{
    private static class MyKey {
        private int orderNumber;
        private int catalogNumber;

        public MyKey(int orderNumber, int catalogNumber) {
            this.orderNumber = orderNumber;
            this.catalogNumber = catalogNumber;
        }

        public int getOrderNumber() {
            return this.orderNumber;
        }

        public int getCatalogNumber() {
            return this.catalogNumber;
        }

        // Implement the equals() and hashCode() methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof OrderItemDataMapper.MyKey)) return false;
            OrderItemDataMapper.MyKey myKey = (OrderItemDataMapper.MyKey) o;
            return orderNumber == myKey.orderNumber &&
                    catalogNumber == myKey.catalogNumber;
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderNumber, catalogNumber);
        }
    }

    Map<OrderItemDataMapper.MyKey, OrderItem> orderItemIdentityMap;//key: orderNumber + catalog number, value: orderItem
    static OrderItemDataMapper orderItemDataMapper = null;
    private OrderItemDataMapper()
    {
        orderItemIdentityMap = new HashMap<>();
    }
    public static OrderItemDataMapper getInstance()
    {
        if(orderItemDataMapper == null)
            orderItemDataMapper = new OrderItemDataMapper();
        return orderItemDataMapper;
    }



    protected String insertQuery(OrderItem orderItem)
    {
        return "";
    }

    public void insert(OrderItem orderItem, int orderId, int supplierId)
    {
        executeVoidQuery(insertQuery(orderItem, orderId, supplierId));
    }

    protected String insertQuery(OrderItem item, int orderNum, int supplierId) {
        this.orderItemIdentityMap.put(new MyKey(orderNum,item.getItemNumber()), item);
        int amount = item.getItemAmount();
        double itemDiscount = item.getItemDiscount();
        double finalPrice = item.getFinalPrice();
        int supplierCatalogNumber = item.getItemNumber();
        int orderId = orderNum;
        String queryFields = String.format("INSERT INTO OrdersItems (amount, itemDiscount, finalPrice, supplierCatalogNumber, orderId, supplierId)" +
                "VALUES (%d,%.2f,%.2f,%d,%d, %d)",amount, itemDiscount, finalPrice, supplierCatalogNumber, orderId, supplierId);
        return queryFields;
    }


    protected String deleteQuery(OrderItem orderItem)
    {
        return "";
    }

    protected String updateQuery(OrderItem item) {
       return "";
    }

    protected String findQuery(int orderNumber, int catalogNumber)
    {
        return String.format("SELECT * FROM OrdersItems WHERE orderId = %d AND `supplierCatalogNumber` = %d " ,orderNumber ,catalogNumber);
    }

    protected String findQuery(String ... key)
    {
        return "";
    }

    protected String findAllQuery(){return "SELECT * FROM OrdersItems";}



    protected OrderItem findInIdentityMap(String ...key)
    {
        return this.orderItemIdentityMap.get(new MyKey(Integer.parseInt(key[0]), Integer.parseInt(key[1])));}

    protected OrderItem insertIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
//        SupplierItem sItem = this.supplierItemIdentityMap.get(new SupplierItemDataMapper.MyKey(match.getInt("supplierId"),match.getInt("catalogNumber")));
        OrderItem orderItem = this.orderItemIdentityMap.get(new MyKey(match.getInt("orderId"), match.getInt("supplierCatalogNumber")));
        if(orderItem != null)
            return orderItem;
        Optional<SupplierItem> supplierItemOptional = SupplierItemDataMapper.getInstance().find(match.getString("supplierCatalogNumber"));
        if (supplierItemOptional.isEmpty())
            return null;
        SupplierItem supplierItem = supplierItemOptional.get();
        int amount = match.getInt("amount");
        double discount = match.getDouble("itemDiscount");
        double finalPrice = match.getDouble("finalPrice");
        orderItem = new OrderItem(supplierItem, amount, discount, finalPrice);
        int orderId = match.getInt("orderId");
        int catalogNumber = match.getInt("supplierCatalogNumber");
        this.orderItemIdentityMap.put(new MyKey(orderId, catalogNumber), orderItem);
        return orderItem;

    }

    //find all order items of given order number
    protected String findAllQueryByKey(String ... key)
    {
        return String.format("SELECT * FROM `OrdersItems` WHERE orderId = '%s'", key[0]);
    }




}
