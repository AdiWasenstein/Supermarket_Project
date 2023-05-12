package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.*;
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
    protected String insertQuery(StockItem stockItem) {
        stockItemsIdentitiyMap.put(stockItem.getBarcode(), stockItem);
        return String.format("INSERT INTO StockItems (Barcode, CatalogItemId, CostPrice, Expiration, DamageType, BranchId, Location) VALUES (%d, %d, %f, '%s', %d, %d, %d)",
                stockItem.getBarcode(), stockItem.getCatalogItem().getId(), stockItem.getCostPrice(), stockItem.getExpiration(), stockItem.getDamage().ordinal(),
                stockItem.getBranchId(), stockItem.getLocation());
    }
    protected String deleteQuery(StockItem object) {
        stockItemsIdentitiyMap.remove(object.getBarcode());
        return String.format("DELETE FROM StockItems WHERE Barcode = %d", object.getBarcode());
    }
    protected String updateQuery(StockItem object) {
        return String.format("UPDATE StockItems SET DamageType = %d, Location = %d WHERE Barcode = %d", object.getDamage().ordinal(), object.getLocation(), object.getBarcode());
    }
    protected String findQuery(String...key) {return String.format("SELECT * FROM StockItems WHERE Barcode = %s",key[0]);}
    protected String findAllQuery() {return "SELECT * FROM StockItems";}
    public StockItem findInIdentityMap(String... key) {return stockItemsIdentitiyMap.get(Integer.parseInt(key[0]));}
    protected StockItem insertIdentityMap(ResultSet match) throws SQLException{
        if (match == null)
            return null;
        StockItem stockItem = stockItemsIdentitiyMap.get(match.getInt("Barcode"));
        if (stockItem != null)
            return stockItem;
        LocalDate expiration = LocalDate.parse(match.getString("Expiration"));
        int barcode = match.getInt("Barcode");
        double costPrice = match.getDouble("CostPrice");
        DamageType damageType = DamageType.values()[match.getInt("DamageType")];
        int branchId = match.getInt("BranchId");
        int location = match.getInt("Location");
        Optional<CatalogItem> catalogItem = CatalogItemDataMapper.getInstance().find(String.valueOf(match.getInt("CatalogItemId")));
        if (catalogItem.isEmpty())
            return null;
        stockItem = new StockItem(catalogItem.get(), barcode, costPrice, expiration, damageType, branchId, location);
        stockItemsIdentitiyMap.put(barcode, stockItem);
        return stockItem;
    }
    public LinkedList<StockItem> findAllFromBranch(int branchId){
        LinkedList<StockItem> stockItems = new LinkedList<>();
        for (StockItem stock : findAll())
            if (stock.getBranchId() == branchId)
                stockItems.add(stock);
        return stockItems;
    }
    public void deleteMatchingCatalog(int catalogId){
        for (StockItem stockItem : stockItemsIdentitiyMap.values())
            if (stockItem.getCatalogItem().getId() == catalogId)
                stockItemsIdentitiyMap.remove(stockItem.getBarcode());
    }
    private int getIdAmount(int branchId, int catalogId, boolean needsFront){
        LinkedList<StockItem> stockItems = findAllFromBranch(branchId);
        int count = 0;
        for(StockItem stockItem : stockItems)
            if(stockItem.getCatalogItem().getId() == catalogId){
                boolean inFront = stockItem.getLocation() < Branch.BACKSTART;
                if((needsFront && inFront) || (!needsFront && !inFront))
                    count++;
            }
        return count;
    }
    public int getShelvesIdAmount(int branchId, int catalogId){
        return getIdAmount(branchId, catalogId, true);
    }

    public int getBackIdAmount(int branchId, int catalogId){
        return getIdAmount(branchId, catalogId, false);
    }
}