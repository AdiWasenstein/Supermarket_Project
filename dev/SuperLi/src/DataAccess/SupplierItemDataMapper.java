package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

public class SupplierItemDataMapper extends ADataMapper<SupplierItem>{
    private static class MyKey {
        private int supplierId;
        private int catalogNumber;

        public MyKey(int supplierId, int catalogNumber) {
            this.supplierId = supplierId;
            this.catalogNumber = catalogNumber;
        }

        public int getSupplierId() {
            return this.supplierId;
        }

        public int getCatalogNumber() {
            return this.catalogNumber;
        }


        // Implement the equals() and hashCode() methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyKey)) return false;
            MyKey myKey = (MyKey) o;
            return supplierId == myKey.supplierId &&
                    catalogNumber == myKey.catalogNumber;
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplierId, catalogNumber);
        }
    }
    Map<MyKey, SupplierItem> supplierItemIdentityMap;//key: supplierId + catalog number, value: supplierItem
    static SupplierItemDataMapper supplierItemDataMapper = null;
    private SupplierItemDataMapper()
    {
        supplierItemIdentityMap = new HashMap<>();
    }
    public static SupplierItemDataMapper getInstance()
    {
        if(supplierItemDataMapper == null)
            supplierItemDataMapper = new SupplierItemDataMapper();
        return supplierItemDataMapper;
    }

    protected String insertQuery(SupplierItem supplierItem)
    {
        return "";
    }
    public void insert(SupplierItem supplierItem, int supplierId)
    {
        executeVoidQuery(insertQuery(supplierItem,supplierId));
    }

    protected String insertQuery(SupplierItem supplierItem, int supplierId) {
        this.supplierItemIdentityMap.put(new MyKey(supplierId,supplierItem.getCatalogNumber()), supplierItem);
        int catalogNum = supplierItem.getCatalogNumber();
        String itemName = supplierItem.getItemName();
        String manufacturer = supplierItem.getManufacturer();
        String category = supplierItem.getCatagory();
        double unitPrice = supplierItem.getUnitPrice();
        double unitWeight = supplierItem.getUnitWeight();
        int numOfUnits = supplierItem.getNumberOfUnits();
        int marketId = supplierItem.GetMarketId();
        String queryFields = String.format("INSERT INTO `SuppliersItems`(catalogNumber,itemName, manufacturer, catagory, unitPrice, unitWeight, numberOfUnits, marketId, supplierId)" +
                "VALUES (%d,'%s','%s','%s',%f,%f,%d,%d,%d)",catalogNum,itemName,manufacturer,category,unitPrice,unitWeight,numOfUnits,marketId,supplierId);
        return queryFields;
    }


    //this func returns all supplier items *in identity map* that fit to catalog item it's id is given.
    public HashMap<Integer,SupplierItem> findAllFitToCatalogItemInIdMap(int catalogId)
    {
        HashMap<Integer,SupplierItem> resultList = new HashMap<>();
        for(MyKey key : this.supplierItemIdentityMap.keySet())
        {
            SupplierItem currentSupplierItem = supplierItemIdentityMap.get(key);
            if(currentSupplierItem.GetMarketId() == catalogId)
                resultList.put(key.getSupplierId(),currentSupplierItem);
        }
        return resultList;
    }

    //this func deletes all supplier items *in identity map* (that fit to catalog item it's id is given.).
    public void removeSupplierItemsFitToCatalogItemInIdMap(HashMap<Integer,SupplierItem> itemsToRemove)
    {
        for(Integer supplierId : itemsToRemove.keySet())
        {
            SupplierItem sItem = itemsToRemove.get(supplierId);
            MyKey temp = new MyKey(supplierId,sItem.getCatalogNumber());
            this.supplierItemIdentityMap.remove(temp);
        }
    }

    protected String deleteQuery(SupplierItem supplierItem)
    {
        return "";
    }

    protected String deleteQuery(SupplierItem supplierItem, int supplierId)
    {
        int catalogNumber = supplierItem.getCatalogNumber();
        MyKey temp = new MyKey(supplierId,catalogNumber);
        this.supplierItemIdentityMap.remove(temp);
        return String.format("DELETE FROM `SuppliersItems` WHERE supplierId = %d AND catalogNumber = %d", supplierId, catalogNumber);
    }

    public void delete(SupplierItem sItem, int supplierId)
    {
        executeVoidQuery(deleteQuery(sItem,supplierId));
    }

    public void update(SupplierItem sItem, int supplierId)
    {
        executeVoidQuery(updateQuery(sItem,supplierId));
    }

    protected String updateQuery(SupplierItem sItem, int supplierId) {
        String queryFields = String.format("UPDATE `SuppliersItems` SET itemName = '%s', manufacturer = '%s'," +
                "catagory = '%s', unitPrice = %f, unitWeight = %f, numberOfUnits = %d, marketId = %d " +
                "WHERE supplierId = %d AND catalogNumber = %d",sItem.getItemName(),
                sItem.getManufacturer(),sItem.getCatagory(),sItem.getUnitPrice(),sItem.getUnitWeight(),sItem.getNumberOfUnits(),
                sItem.GetMarketId(),supplierId,sItem.getCatalogNumber());
        return queryFields;
    }

    protected String updateQuery(SupplierItem supplierItem)
    {
        return "";
    }

    protected String findQuery(String ... key)
    {
        return String.format("SELECT * FROM `SuppliersItems` WHERE catalogNumber = '%s' AND supplierId = '%s'" ,key[1],key[0]);
    }

    protected String findAllQuery(){return "SELECT * FROM `SuppliersItems`";}
    protected SupplierItem findInIdentityMap(String ...key)
    {
        return this.supplierItemIdentityMap.get(new MyKey(Integer.valueOf(key[0]),Integer.valueOf(key[1])));}
    protected SupplierItem insertIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        SupplierItem sItem = this.supplierItemIdentityMap.get(new MyKey(match.getInt("supplierId"),match.getInt("catalogNumber")));
        if(sItem != null)
            return sItem;
        int catalogNumber = match.getInt("catalogNumber");
        String itemName = match.getString("itemName");
        String manufacturer = match.getString("manufacturer");
        String catagory = match.getString("catagory");
        double unitPrice = match.getDouble("unitPrice");
        double unitWeight = match.getDouble("unitWeight");
        int numberOfUnits = match.getInt("numberOfUnits");
        int marketId = match.getInt("marketId");
        int supplierId = match.getInt("supplierId");
        sItem = new SupplierItem(catalogNumber,itemName,manufacturer,unitPrice,unitWeight,numberOfUnits,catagory,marketId);
        this.supplierItemIdentityMap.put(new MyKey(supplierId, catalogNumber),sItem);
        return sItem;
    }

    //find all suppliers items of given supplier
    protected String findAllQueryByKey(String ... key)
    {
        return String.format("SELECT * FROM `SuppliersItems` WHERE supplierId = '%s'", key[0]);
    }


}
