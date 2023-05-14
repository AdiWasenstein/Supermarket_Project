package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class SupplierDataMapper extends ADataMapper<Supplier> {

    Map<Integer, Supplier> supplierIdentityMap;
    static SupplierDataMapper supplierDataMapper = null;

    private SupplierDataMapper() {
        supplierIdentityMap = new HashMap<>();
    }

    public static SupplierDataMapper getInstance() {
        if (supplierDataMapper == null)
            supplierDataMapper = new SupplierDataMapper();
        return supplierDataMapper;
    }

    //THE NEXT FUNCTIONS TAKE CARE OF SUPPLIER

    protected String insertQuery(Supplier object)
    {
        return "";
    }
    private void insertDeliverERegular(Supplier supplier) {
        SupplierDeliversERegular sup = (SupplierDeliversERegular) supplier;
        String queryFields = String.format("INSERT INTO SuppliersDeliverERegular(supplierId, numberOfDays) VALUES (%d,%d)", sup.getSupplierId(), sup.getNumberOfDaysToDeliver());
        executeVoidQuery(queryFields);
    }

    private void insertDeliverRegular(Supplier supplier) {
        SupplierDeliversRegular sup = (SupplierDeliversRegular) supplier;
        for (Day day : sup.getDeliveryDays()) {
            String queryFields = String.format("INSERT INTO SuppliersDeliverRegular(supplierId, day) VALUES (%d,'%s')", sup.getSupplierId(), day.toString());
            executeVoidQuery(queryFields);
        }
    }

    private void insertManufacturers(Supplier supplier) {
        for (String manufacturer : supplier.getSupplierManufacturers()) {
            String query = String.format("SELECT * FROM `SuppliersANDManufacturers` WHERE supplierId=%d" +
                    " AND manufacturer='%s'", supplier.getSupplierId(), manufacturer);
            ResultSet matches = executeSelectQuery(query);
            closeConnection();
            try
            {
                if (!matches.next())//need to add to SuppliersANDManufacturers table the manufacturer
                {
                    String queryFields = String.format("INSERT INTO `SuppliersANDManufacturers`(supplierId, manufacturer) VALUES (%d,'%s')", supplier.getSupplierId(), manufacturer);
                    executeVoidQuery(queryFields);
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    private void insertCategories(Supplier supplier) {
        for (String category : supplier.getSupplierCatagories()) {
            String query = String.format("SELECT * FROM `SuppliersANDCategories` WHERE supplierId=%d" +
                    " AND category='%s'", supplier.getSupplierId(), category);
            ResultSet matches = executeSelectQuery(query);
            closeConnection();
            try {
                if (!matches.next())//need to add to SuppliersANDCategories table the manufacturer
                {
                    String queryFields = String.format("INSERT INTO `SuppliersANDCategories`(supplierId, category) VALUES (%d,'%s')", supplier.getSupplierId(), category);
                    executeVoidQuery(queryFields);
                }
            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }

    //inserting new Supplier
    public void insert(Supplier supplier) {
        this.supplierIdentityMap.put(supplier.getSupplierId(), supplier);
        String queryFields = String.format("INSERT INTO Suppliers(supplierId) VALUES (%d)", supplier.getSupplierId());
        executeVoidQuery(queryFields);
        //check the type of supplier:
        if (supplier instanceof SupplierDeliversERegular) {
            this.insertDeliverERegular(supplier);
        }
        if (supplier instanceof SupplierDeliversRegular) {
            this.insertDeliverRegular(supplier);
        }
        //insert manufacturers of supplier
        this.insertManufacturers(supplier);
        //insert categories of supplier
        this.insertCategories(supplier);
        //insert supplier card
        SupplierCardDataMapper.getInstance().insert(supplier.getSupplierCard());
        //and finally - insert to Suppliers Table
    }

    //we don't allow supplier deletion in our system
    protected String deleteQuery(Supplier supplier) {
        return "";
    }
    protected String updateQuery(Supplier supplier) {
        //update supplier card
        SupplierCardDataMapper.getInstance().update(supplier.getSupplierCard());
        //insert manufacturers that don't already exist
        this.insertManufacturers(supplier);
        //insert categories that don't already exist
        this.insertCategories(supplier);
        return "";
    }

    protected String findQuery(String... key)
    {
        return "";
    }

    private LinkedList<String> findSupplierCategories(String...key)
    {
        LinkedList<String> categories = new LinkedList<>();
        String query = String.format("SELECT category FROM `SuppliersANDCategories` WHERE supplierId=%d",Integer.valueOf(key[0]));
        ResultSet matches = executeSelectQuery(query);
        if(matches == null)
        {
            closeConnection();
            return categories;
        }
        try
        {
            while (matches.next()) {
                String category = matches.getString("category");
                categories.add(category);
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return categories;
    }

    private LinkedList<String> findSupplierManufacturers(String...key)
    {
        LinkedList<String> manufacturers = new LinkedList<>();
        String query = String.format("SELECT manufacturer FROM `SuppliersANDManufacturers` WHERE supplierId=%d",Integer.valueOf(key[0]));
        ResultSet matches = executeSelectQuery(query);
        if(matches == null)
        {
            closeConnection();
            return manufacturers;
        }
        try
        {
            while (matches.next()) {
                String manufacturer = matches.getString("manufacturer");
                manufacturers.add(manufacturer);
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return manufacturers;
    }

    private Optional<SupplierDeliversRegular> createSupplierDeliversRegular(int supplierId, SupplierCard card,LinkedList<String> categories,LinkedList<String> manufacturers)
    {
        String query = String.format("SELECT * FROM SuppliersDeliverRegular WHERE supplierId=%d", supplierId);
        ResultSet results = executeSelectQuery(query);
            if (results == null)//the supplierId doesn't represent a SupplierDeliversRegular
            {
                closeConnection();
                return Optional.empty();
            }
        LinkedList<String> daysStr = new LinkedList<>();
        try
        {
            boolean flag = false;
            while (results.next())
            {
                flag = true;
                String day = results.getString("day");
                daysStr.add(day);
            }
            if(!flag)
            {
                closeConnection();
                return Optional.empty();
            }
        }
        catch (SQLException e)
        {
            System.out.println(this.getClass().toString() + e.getMessage());
            closeConnection();
            return Optional.empty();
        }
        closeConnection();
        //convert days from string to enum
        LinkedList<Day> days = new LinkedList<>();
        for(String s_day : daysStr)
        {
            if (s_day.equalsIgnoreCase("sunday")) {
                days.add(Day.sunday);
            }
            else if (s_day.equalsIgnoreCase("monday"))
                days.add(Day.monday);
            else if (s_day.equalsIgnoreCase("tuesday"))
                days.add(Day.tuesday);
            else if (s_day.equalsIgnoreCase("wednesday"))
                days.add(Day.wednesday);
            else if (s_day.equalsIgnoreCase("thursday"))
                days.add(Day.thursday);
            else if (s_day.equalsIgnoreCase("friday"))
                days.add(Day.friday);
            else
                days.add(Day.saturday);
        }
        SupplierDeliversRegular sup = new SupplierDeliversRegular(categories,manufacturers,card,null,days);
        return Optional.of(sup);
    }

    private Optional<SupplierDeliversERegular> createSupplierDeliversERegular(int supplierId, SupplierCard card,LinkedList<String> categories,LinkedList<String> manufacturers)
    {
        String query = String.format("SELECT * FROM SuppliersDeliverERegular WHERE supplierId=%d", supplierId);
        ResultSet results = executeSelectQuery(query);
        if (results == null)//the supplierId doesn't represent a SupplierDeliversERegular
        {
            closeConnection();
            return Optional.empty();
        }
        int numberOfDays = 0;
        try
        {
            if(results.next())
            {
                numberOfDays = results.getInt("numberOfDays");
            }
        }
        catch (SQLException e)
        {
            System.out.println(this.getClass().toString() + e.getMessage());
            closeConnection();
            return Optional.empty();
        }
        closeConnection();
        SupplierDeliversERegular sup = new SupplierDeliversERegular(categories,manufacturers,card,null,numberOfDays);
        return Optional.of(sup);
    }

    protected Supplier findInIdentityMap(String ...key)
    {
        return this.supplierIdentityMap.get(Integer.valueOf(key[0]));
    }

    protected Supplier insertIdentityMap(ResultSet match) throws SQLException {
        return null;
    }

    //this func creates supplier object (without contract)
    private Optional<Supplier> createSupplier(String...key)
    {
        String query = String.format("SELECT * FROM Suppliers WHERE supplierId=%d",Integer.valueOf(key[0]));
        ResultSet results = executeSelectQuery(query);
        try {
            if (!results.next())//a supplier with given id wasn't found
            {
                closeConnection();
                return Optional.empty();
            }
        }
        catch (SQLException e)
        {
            return Optional.empty();
        }
        closeConnection();
        //search for supplier card
        Optional<SupplierCard> card = SupplierCardDataMapper.getInstance().find(key);
        if(card.isEmpty())//card wasn't found
            return Optional.empty();
        //search for categories
        LinkedList<String> categories = this.findSupplierCategories(key);
        //search for manufacturers
        LinkedList<String> manufacturers = this.findSupplierManufacturers(key);
        //check for supplier type
        Supplier sup;
        Optional<SupplierDeliversRegular> deliversRegularOpt = this.createSupplierDeliversRegular(Integer.valueOf(key[0]),card.get(),categories,manufacturers);
        if(!deliversRegularOpt.isEmpty())//if it's a SupplierDeliversRegular
        {
            sup = deliversRegularOpt.get();
            return Optional.of(sup);
        }
        Optional<SupplierDeliversERegular> deliversERegularOpt = this.createSupplierDeliversERegular(Integer.valueOf(key[0]),card.get(),categories,manufacturers);
        if(!deliversERegularOpt.isEmpty())
        {
            sup = deliversERegularOpt.get();
            return Optional.of(sup);
        }
        //else, it's a not delivers supplier
        sup = new SupplierNotDelivers(categories,manufacturers,card.get(),null);
        return Optional.of(sup);
    }

    public Optional<Supplier> find(String...key)
    {
        Supplier identityMapObject = findInIdentityMap(key);
        //if fitting supplier found in identity map
        if(identityMapObject != null)
        {
            return Optional.of(identityMapObject);
        }
        //else, need to search in db :
        Optional<Supplier> supplierOpt = this.createSupplier(key);
        if(supplierOpt.isEmpty())
            return Optional.empty();
        Supplier sup = supplierOpt.get();
//        //orders
//        LinkedList<Order> orders = OrderDataMapper.getInstance().findAllBySupplier(Integer.valueOf(key[0]));
//        for( Order order : orders)
//        {
//            sup.addOrder(order);
//        }
        //create contract
        SupplierContract contract = new SupplierContract(sup.getSupplierCard().getPayment(), sup);
        sup.setSupplierContract(contract);
        //contract - supplier items
        LinkedList<SupplierItem> supplierItems = SupplierItemDataMapper.getInstance().findAllByKey(key);
        for (SupplierItem sItem : supplierItems)
        {
            contract.addItem(sItem);
        }
        //contract - discount document
        HashMap<Integer,LinkedList<ItemDiscount>> itemUnitsDiscounts = DiscountDataMapper.getInstance().findAllItemUnitsDiscountByKey(key);
        contract.getDiscountDocument().setItemsDiscounts(itemUnitsDiscounts);
        LinkedList<OrderDiscount> orderDiscounts = DiscountDataMapper.getInstance().findAllOrderDiscountByKey(key);
        contract.getDiscountDocument().setOrderDiscounts(orderDiscounts);
        this.supplierIdentityMap.put(sup.getSupplierId(),sup);
        return Optional.of(sup);
    }

    protected String findAllQuery() {
        return "SELECT supplierId FROM Suppliers";
    }


    public LinkedList<Supplier> findAll(){
        LinkedList<Integer> idSuppliers = new LinkedList<>();
        LinkedList<Supplier> objects = new LinkedList<>();
        openConnection();
        if(connection == null) {
            return objects;
        }
        ResultSet result = executeSelectQuery(findAllQuery());
        if(result == null)
        {
            closeConnection();
            return objects;
        }
        try{
            while (result.next())
            {
                int supplierId = result.getInt("supplierId");
                idSuppliers.add(supplierId);
            }
            for(Integer id : idSuppliers)
            {
                Optional<Supplier> object = find(Integer.toString(id));
                if(!object.isEmpty())
                    objects.add(object.get());
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return objects;
    }


    //THE NEXT FUNCTIONS TAKE CARE OF SUPPLIER ITEMS

    //insert new Supplier Item
    public void insertSupplierItem(SupplierItem supplierItem, int supplierId)
    {
        SupplierItemDataMapper.getInstance().insert(supplierItem,supplierId);
    }

    //this func is called by stock. whenever a catalog item is deleted, all fitting supplier items have to be deleted too.
    public void deleteMatchingCatalog(int catalogId)
    {
        HashMap<Integer,SupplierItem> supplierItemsFitToCatalogItem = SupplierItemDataMapper.getInstance().findAllFitToCatalogItemInIdMap(catalogId);
        // need to remove from supp item map
        for(Integer supplierId : supplierItemsFitToCatalogItem.keySet())
        {
            SupplierItem sItem = supplierItemsFitToCatalogItem.get(supplierId);
            Supplier sup = this.supplierIdentityMap.get(supplierId);
            if(sup != null)
            {
                //remove supplierItem from supplier
                SupplierContract contract = sup.getSupplierContract();
                try
                {
                    contract.removeItem(sItem.getCatalogNumber());
                }
                catch (Exception e)
                {
                    //do nothing
                }
            }
            this.deleteAccordingToSupplierItem(supplierId, sItem);
        }
        //remove supplier items
        SupplierItemDataMapper.getInstance().removeSupplierItemsFitToCatalogItemInIdMap(supplierItemsFitToCatalogItem);
    }

    private void deleteAccordingToSupplierItem(int supplierId, SupplierItem sItem)
    {
        //remove item units discounts that are related to supplier item
        DiscountDataMapper.getInstance().removeFromIdentityMap(supplierId,sItem.getCatalogNumber());
        //remove supplier item from periodic reports that contain it.
        PeriodicReportDataMapper.getInstance().removeSupplierItemFromPeriodicReports(sItem,supplierId);
    }
    public void deleteSupplierItem(SupplierItem sItem, int supplierId)
    {
        SupplierItemDataMapper.getInstance().delete(sItem, supplierId);
        deleteAccordingToSupplierItem(supplierId,sItem);
    }

    public void updateSupplierItem(SupplierItem sItem, int supplierId)
    {
        SupplierItemDataMapper.getInstance().update(sItem,supplierId);
    }

    public void deleteContact(Supplier sup, Contact con)
    {
        String deleteQuery = String.format("DELETE FROM `SuppliersANDContacts` WHERE supplierId=%d AND phoneNumber = '%s'", sup.getSupplierId(),con.GetPhoneNumber());
        executeVoidQuery(deleteQuery);
        //check if the contact worked only for this specific supplier:
        String selectQuery = String.format("SELECT * FROM `SuppliersANDContacts` WHERE phoneNumber = '%s'",con.GetPhoneNumber());
        ResultSet matches = executeSelectQuery(selectQuery);
        try {
            if (!matches.next())//the contact worked only for this specific supplier
            {
                closeConnection();
                ContactDataMapper.getInstance().delete(con);
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        closeConnection();
    }

    public void insertItemUnitsDiscount(Supplier sup, int catalogNumber,ItemUnitsDiscount discount)
    {
        DiscountDataMapper.getInstance().insertItemUnitsDiscount(discount,sup.getSupplierId(),catalogNumber);
    }

    public void deleteItemUnitsDiscount(Supplier sup, int catalogNumber,ItemUnitsDiscount discount)
    {
        DiscountDataMapper.getInstance().deleteItemUnitsDiscount(discount, sup.getSupplierId(),catalogNumber);
    }

    public void insertOrderUnitsDiscount(Supplier sup,OrderUnitsDiscount discount)
    {
        DiscountDataMapper.getInstance().insertOrderUnitsDiscount(discount,sup.getSupplierId());
    }

    public void deleteOrderUnitsDiscount(Supplier sup,OrderUnitsDiscount discount)
    {
        DiscountDataMapper.getInstance().deleteOrderUnitsDiscount(discount,sup.getSupplierId());
    }

    public void insertOrderCostDiscount(Supplier sup,OrderCostDiscount discount)
    {
        DiscountDataMapper.getInstance().insertOrderCostDiscount(discount, sup.getSupplierId());
    }

    public void deleteOrderCostDiscount(Supplier sup,OrderCostDiscount discount)
    {
        DiscountDataMapper.getInstance().deleteOrderCostDiscount(discount, sup.getSupplierId());
    }

    public LinkedList<Order> getOrders(int supplierId)
    {
        return OrderDataMapper.getInstance().findAllBySupplier(supplierId);
    }






}
