package DataAccess;

import BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;


public class DiscountDataMapper extends ADataMapper<Discount>{
    private static class MyKey {
        private int supplierId;
        private int catalogNumber;
        private int numberOfUnits;

        public MyKey(int supplierId, int catalogNumber, int numberOfUnits) {
            this.supplierId = supplierId;
            this.catalogNumber = catalogNumber;
            this.numberOfUnits = numberOfUnits;
        }

        public int getSupplierId()
        {
            return this.supplierId;
        }

        public int getCatalogNumber()
        {
            return this.catalogNumber;
        }

        public int getNumberOfUnits()
        {
            return this.numberOfUnits;
        }

        // Implement the equals() and hashCode() methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyKey)) return false;
            MyKey myKey = (MyKey) o;
            return supplierId == myKey.supplierId &&
                    catalogNumber == myKey.catalogNumber &&
                    numberOfUnits == myKey.numberOfUnits;
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplierId, catalogNumber, numberOfUnits);
        }
    }
    private static class DoubleKey {
        private int supplierId;
        private double value;

        public DoubleKey(int supplierId, double value) {
            this.supplierId = supplierId;
            this.value = value;
        }

        public int getSupplierId()
        {
            return this.supplierId;
        }

        public double getValue()
        {
            return this.value;
        }

        // Implement the equals() and hashCode() methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DoubleKey)) return false;
            DoubleKey doubleKey = (DoubleKey) o;
            return supplierId == doubleKey.supplierId &&
                    value == doubleKey.value;
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplierId, value);
        }
    }

    Map<MyKey,ItemUnitsDiscount> itemUnitsDiscountIdentityMap;
    Map<DoubleKey,OrderUnitsDiscount> orderUnitsDiscountIdentityMap;
    Map<DoubleKey,OrderCostDiscount> orderCostDiscountIdentityMap;
    static DiscountDataMapper discountDataMapper = null;
    private DiscountDataMapper()
    {
        itemUnitsDiscountIdentityMap = new HashMap<>();
        orderUnitsDiscountIdentityMap = new HashMap<>();
        orderCostDiscountIdentityMap = new HashMap<>();
    }
    public static DiscountDataMapper getInstance()
    {
        if(discountDataMapper == null)
            discountDataMapper = new DiscountDataMapper();
        return discountDataMapper;
    }

    //remove *unit items discounts* from identity map whenever deleting supplierItem.
    public void removeFromIdentityMap(int supplierId, int catalogNumber)
    {
        for(MyKey key : this.itemUnitsDiscountIdentityMap.keySet())
        {
            if(key.getSupplierId() == supplierId && key.getCatalogNumber() == catalogNumber)
                this.itemUnitsDiscountIdentityMap.remove(key);
        }
    }

    //this func returns all itemUnitsDiscount of a specific supplier.
    public HashMap<Integer,LinkedList<ItemDiscount>> findAllItemUnitsDiscountByKey(String ... key) {
        HashMap<Integer, LinkedList<ItemDiscount>> itemUnitsDiscounts = new HashMap<>();
//        openConnection();
//        if(connection == null) {
//            return itemUnitsDiscounts;
//        }
        String query = String.format("SELECT * FROM ItemUnitsDiscounts WHERE supplierId='%s'", key[0]);
        ResultSet matches = executeSelectQuery(query);
        if (matches == null)
        {
//            closeConnection();
            return itemUnitsDiscounts;
        }
        try {
            while (matches.next()) {
                Pair<Integer, ItemDiscount> discountPair = insertItemUnitsDiscountIdentityMap(matches);
                if (discountPair != null)
                {
                    LinkedList<ItemDiscount> temp;
                    if(itemUnitsDiscounts.containsKey(discountPair.getLeft()))
                    {
                        temp = itemUnitsDiscounts.get(discountPair.getLeft());
                    }
                    else
                    {
                        temp = new LinkedList<>();
                    }
                    temp.add(discountPair.getRight());
                    itemUnitsDiscounts.put(discountPair.getLeft(),temp);
                }
            }
        }
        catch(SQLException e)
        {
            System.out.println(this.getClass().toString() + e.getMessage());
        }
//            closeConnection();
        return itemUnitsDiscounts;
    }

    //this func returns all orderDiscounts of a specific supplier.
    public LinkedList<OrderDiscount> findAllOrderDiscountByKey(String ... key) {
        //orderCostDiscounts
        LinkedList<OrderDiscount> orderDiscounts = new LinkedList<>();
//        openConnection();
//        if(connection == null) {
//            return itemUnitsDiscounts;
//        }
        String query1 = String.format("SELECT * FROM OrderCostDiscounts WHERE supplierId='%s'", key[0]);
        ResultSet matches = executeSelectQuery(query1);
        if (matches != null) {
            try {
                while (matches.next()) {
                    OrderDiscount order = insertOrderCostDiscountIdentityMap(matches);
                    if (order != null) {
                        orderDiscounts.add(order);
                    }
                }
            }
            catch (SQLException e)
            {
                System.out.println(this.getClass().toString() + e.getMessage());
            }
        }
//        closeConnection();
        //orderUnitsDiscounts
        String query2 = String.format("SELECT * FROM OrderUnitsDiscounts WHERE supplierId='%s'", key[0]);
        ResultSet matches2 = executeSelectQuery(query2);
        if (matches2 != null) {
            try {
                while (matches2.next()) {
                    OrderDiscount order = insertOrderUnitsDiscountIdentityMap(matches2);
                    if (order != null) {
                        orderDiscounts.add(order);
                    }
                }
            }
            catch (SQLException e)
            {
                System.out.println(this.getClass().toString() + e.getMessage());
            }
        }
//        closeConnection();
        return orderDiscounts;
    }

    private Pair<Integer,ItemDiscount> insertItemUnitsDiscountIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        ItemUnitsDiscount itemUnitDis = this.itemUnitsDiscountIdentityMap.get(new MyKey(match.getInt("supplierId"),match.getInt("supplierCatalogNumber"), match.getInt("numberOfUnitsOfItem")));
        if(itemUnitDis != null)
            return new Pair<>(match.getInt("supplierCatalogNumber"),itemUnitDis);
        double discountSize = match.getDouble("discountSize");
        String type = match.getString("discountType");
        DiscountType discountType;
        if(type.equals("constant"))
            discountType = new ConstantDiscount();
        else
            discountType = new PercentageDiscount();
        int numberOfUnitsOfItem = match.getInt("numberOfUnitsOfItem");
        itemUnitDis = new ItemUnitsDiscount(discountSize,discountType,numberOfUnitsOfItem);
        this.itemUnitsDiscountIdentityMap.put(new MyKey(match.getInt("supplierId"),match.getInt("supplierCatalogNumber"), match.getInt("numberOfUnitsOfItem")),itemUnitDis);
        return new Pair<>(match.getInt("supplierCatalogNumber"),itemUnitDis);
    }

    private OrderDiscount insertOrderCostDiscountIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        OrderCostDiscount orderDis = this.orderCostDiscountIdentityMap.get(new DoubleKey(match.getInt("supplierId"),match.getDouble("cost")));
        if(orderDis != null)
            return orderDis;
        double discountSize = match.getDouble("discountSize");
        String type = match.getString("discountType");
        DiscountType discountType;
        if(type.equals("constant"))
            discountType = new ConstantDiscount();
        else
            discountType = new PercentageDiscount();
        double cost = match.getDouble("cost");
        orderDis = new OrderCostDiscount(discountSize,discountType,cost);
        this.orderCostDiscountIdentityMap.put(new DoubleKey(match.getInt("supplierId"),match.getDouble("cost")),orderDis);
        return orderDis;
    }

    private OrderDiscount insertOrderUnitsDiscountIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        OrderUnitsDiscount orderDis = this.orderUnitsDiscountIdentityMap.get(new DoubleKey(match.getInt("supplierId"),match.getInt("numberOfUnitsInOrder")));
        if(orderDis != null)
            return orderDis;
        double discountSize = match.getDouble("discountSize");
        String type = match.getString("discountType");
        DiscountType discountType;
        if(type.equals("constant"))
            discountType = new ConstantDiscount();
        else
            discountType = new PercentageDiscount();
        int numOfUnits = match.getInt("numberOfUnitsInOrder");
        orderDis = new OrderUnitsDiscount(discountSize,discountType,numOfUnits);
        this.orderUnitsDiscountIdentityMap.put(new DoubleKey(match.getInt("supplierId"),match.getInt("numberOfUnitsInOrder")),orderDis);
        return orderDis;
    }

    public void insertItemUnitsDiscount(ItemUnitsDiscount discount, int supplierId, int catalogNumber)
    {
        this.itemUnitsDiscountIdentityMap.put(new MyKey(supplierId,catalogNumber,discount.getNumberOfUnitsOfItem()),discount);
        String queryFields = String.format("INSERT INTO ItemUnitsDiscounts(discountSize,discountType, numberOfUnitsOfItem, supplierId, supplierCatalogNumber)" +
                "VALUES (%f,'%s',%d,%d,%d)",discount.getDiscountSize(),discount.getDiscountType().toString(),discount.getNumberOfUnitsOfItem(),supplierId,catalogNumber);
        executeVoidQuery(queryFields);
    }

    public void deleteItemUnitsDiscount(ItemUnitsDiscount discount, int supplierId, int catalogNumber)
    {
        this.itemUnitsDiscountIdentityMap.remove(new MyKey(supplierId,catalogNumber,discount.getNumberOfUnitsOfItem()));
        String query = String.format("DELETE FROM ItemUnitsDiscounts WHERE supplierId = %d AND supplierCatalogNumber = %d AND numberOfUnitsOfItem = %d", supplierId, catalogNumber, discount.getNumberOfUnitsOfItem());
        executeVoidQuery(query);
    }

    public void insertOrderUnitsDiscount(OrderUnitsDiscount discount, int supplierId)
    {
        this.orderUnitsDiscountIdentityMap.put(new DoubleKey(supplierId,discount.getNumberOfUnitsInOrder()),discount);
        String queryFields = String.format("INSERT INTO OrderUnitsDiscounts(discountSize,discountType, numberOfUnitsInOrder, supplierId)" +
                "VALUES (%f,'%s',%d,%d)",discount.getDiscountSize(),discount.getDiscountType().toString(),discount.getNumberOfUnitsInOrder(),supplierId);
        executeVoidQuery(queryFields);
    }

    public void deleteOrderUnitsDiscount(OrderUnitsDiscount discount, int supplierId)
    {
        this.orderUnitsDiscountIdentityMap.remove(new DoubleKey(supplierId,discount.getNumberOfUnitsInOrder()));
        String query = String.format("DELETE FROM OrderUnitsDiscounts WHERE supplierId = %d AND numberOfUnitsInOrder = %d", supplierId,discount.getNumberOfUnitsInOrder());
        executeVoidQuery(query);
    }

    public void insertOrderCostDiscount(OrderCostDiscount discount, int supplierId)
    {
        this.orderCostDiscountIdentityMap.put(new DoubleKey(supplierId,discount.getCost()),discount);
        String queryFields = String.format("INSERT INTO OrderCostDiscounts(discountSize,discountType, cost, supplierId)" +
                "VALUES (%f,'%s',%f,%d)",discount.getDiscountSize(),discount.getDiscountType().toString(),discount.getCost(),supplierId);
        executeVoidQuery(queryFields);
    }

    public void deleteOrderCostDiscount(OrderCostDiscount discount, int supplierId)
    {
        this.orderCostDiscountIdentityMap.remove(new DoubleKey(supplierId,discount.getCost()));
        String query = String.format("DELETE FROM OrderCostDiscounts WHERE supplierId = %d AND cost = %f", supplierId, discount.getCost());
        executeVoidQuery(query);
    }

    //empty implementations :
    protected String insertQuery(Discount discount)
    {
        return "";
    }

    protected String deleteQuery(Discount discount)
    {
        return "";
    }

    protected String updateQuery(Discount discount)
    {
        return "";
    }

    protected String findQuery(String... key)
    {
        return "";
    }

    protected String findAllQuery() {
        return "";
    }

    protected Discount findInIdentityMap(String... key) {
        return null;
    }

    protected Discount insertIdentityMap(ResultSet match) throws SQLException {
        return null;
    }


}




