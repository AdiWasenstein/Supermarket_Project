package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.DamageType;
import SuperLi.src.BusinessLogic.StockItem;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class StockItemDataMapper extends ADataMapper<StockItem> {
    Map<Integer, StockItem> stockItemsIdentitiyMap;
    static StockItemDataMapper stockItemDataMapper = null;
    private StockItemDataMapper(){
        stockItemsIdentitiyMap = new HashMap<>();
    }
    public static StockItemDataMapper getInstance(){
        if(stockItemDataMapper == null)
            stockItemDataMapper = new StockItemDataMapper();
        return stockItemDataMapper;
    }
    public String insertQuery(StockItem object) {
        stockItemsIdentitiyMap.put(object.getBarcode(), object);
        return String.format("INSERT INTO StockItems (Barcode, CatalogItemId, CostPrice, Expiration, DamageType, BranchId, Location) VALUES (%d, %d, %.1f, '%s', %d, %d, %d)",
                object.getBarcode(), object.getCatalogItem().getId(), object.getCostPrice(), object.getExpirationString(), object.getDamage().ordinal(),
                object.getBranchId(), object.getLocation());
    }
    public String deleteQuery(StockItem object) {
        stockItemsIdentitiyMap.remove(object.getBarcode());
        return String.format("DELETE FROM StockItems WHERE Barcode = %d", object.getBarcode());
    }
    public String updateQuery(StockItem object) {
        return String.format("UPDATE StockItems SET DamageType = %d, Location = %d WHERE Barcode = %d", object.getDamage().ordinal(), object.getLocation(), object.getBarcode());
    }
    public String findQuery(String...key) {return String.format("SELECT * FROM StockItems WHERE Barcode = '%s'",key[0]);}
    public String findAllQuery() {return "SELECT * FROM StockItems";}
    public StockItem findInIdentityMap(String ...key) {return stockItemsIdentitiyMap.get(Integer.valueOf(key[0]));}
    public StockItem insertIdentityMap(ResultSet matches) throws SQLException{
        if (matches == null)
            return null;
        StockItem stockItem = stockItemsIdentitiyMap.get(matches.getInt("Barcode"));
        if (stockItem != null)
            return stockItem;
        Optional<CatalogItem> catalogItem = CatalogItemDataMapper.getInstance().find(String.valueOf(matches.getInt("CatalogItemId")));
        if (catalogItem.isEmpty())
            return null;
        LocalDate expiration = LocalDate.parse(matches.getString("Expiration"));
        stockItem = new StockItem(catalogItem.get(), matches.getInt("Barcode"), matches.getDouble("CostPrice"), expiration, DamageType.valueOf(String.valueOf(matches.getInt("DamageType"))),matches.getInt("BranchId"), matches.getInt("Location"));
        stockItemsIdentitiyMap.put(stockItem.getBarcode(), stockItem);
        return stockItem;
    }
    public LinkedList<StockItem> findAllFromBranch(int branchId){
        LinkedList<StockItem> objects = findAll();
        objects.removeIf(stockItem -> stockItem.getBranchId() != branchId);
        return objects;
    }

    public int getShelvesIdAmount(int branchId, int catalogId){
        return branchId + catalogId;
    }
    public int getBackIdAmount(int branchId, int catalogId){
        return branchId + catalogId;
    }
}