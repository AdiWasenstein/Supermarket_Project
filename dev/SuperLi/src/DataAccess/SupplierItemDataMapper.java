package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SupplierItemDataMapper extends ADataMapper<SupplierItem>{
    Map<Integer, SupplierItem> supplierItemIdentityMap;
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



    public LinkedList<Supplier> findAll()
    {
        return new LinkedList<>();
    }

    //PASTING FROM STOCK

    protected String insertQuery(SupplierItem supplierItem)
    {
        return "";
    }
    public void insert(SupplierItem supplierItem, int supplierId)
    {
        executeVoidQuery(insertQuery(supplierItem,supplierId));
    }

    protected String insertQuery(SupplierItem supplierItem, int supplierId) {
        this.supplierItemIdentityMap.put(supplierItem.getCatalogNumber(), supplierItem);
        int catalogNum = supplierItem.getCatalogNumber();
        String itemName = supplierItem.getItemName();
        String manufacturer = supplierItem.getManufacturer();
        String category = supplierItem.getCatagory();
        double unitPrice = supplierItem.getUnitPrice();
        double unitWeight = supplierItem.getUnitWeight();
        int numOfUnits = supplierItem.getNumberOfUnits();
        int marketId = supplierItem.GetMarketId();
        String queryFields = String.format("INSERT INTO SuppliersItems(catalogNumber,itemName, manufacturer, catagory, unitPrice, unitWeight, numberOfUnits, marketId, supplierId)" +
                "VALUES (%d,%s,%s,%s,%f,%f,%d,%d,%d)",catalogNum,itemName,manufacturer,category,unitPrice,unitWeight,numOfUnits,marketId,supplierId);
        return queryFields;
    }

    public void deleteMatchingCatalog(int catalogId)
    {
        for (SupplierItem supplierItem : supplierItemIdentityMap.values())
            if (supplierItem.GetMarketId()== catalogId)
                supplierItemIdentityMap.remove(supplierItem.getCatalogNumber());
    }
    protected String deleteQuery(SupplierItem supplierItem) {
        int catalogNumber = supplierItem.getCatalogNumber();
        StockItemDataMapper.getInstance().deleteMatchingCatalog(id);
        catalogItemsIdentitiyMap.remove(id);
        return String.format("DELETE FROM CatalogItems WHERE Id = %d", id);
    }

    protected String updateQuery(CatalogItem catalogItem) {
        String queryFields = String.format("UPDATE CatalogItems SET SellPrice = %.1f,MinCapacity = %d", catalogItem.getSellPrice(), catalogItem.getMinCapacity());
        CostumerDiscount discount = catalogItem.getCostumerDiscount();
        return queryFields +
                (discount == null ? "" : String.format(", DiscountValue=%.1f, DiscountPercentage=%d, DiscountCapacity=%d, DiscountExpiration='%s'",
                        discount.getValue(), discount.isPercentage() ? 1 : 0, discount.getMinCapacity(), discount.getExpirationDate().toString())) +
                String.format("WHERE Id=%d", catalogItem.getId());
    }
    protected String findQuery(String... key) {return String.format("SELECT * FROM CatalogItems WHERE Id=%s" ,key[0]);}
    protected String findAllQuery(){return "SELECT * FROM CatalogItems";}
    protected CatalogItem findInIdentityMap(String ...key){return catalogItemsIdentitiyMap.get(Integer.valueOf(key[0]));}
    protected CatalogItem insertIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        CatalogItem catalogItem = catalogItemsIdentitiyMap.get(match.getInt("Id"));
        if(catalogItem != null)
            return catalogItem;
        int id = match.getInt("Id");
        LinkedList<String> categories = getCatalogItemCategories(id);
        Category category = new Category(categories, new Size(match.getDouble("SizeAmount"), MeasureUnit.values()[match.getInt("MeasureUnit")]));
        String name = match.getString("Name");
        String manufacturer = match.getString("Manufacturer");
        double sellPrice = match.getDouble("SellPrice");
        int minCapacity = match.getInt("MinCapacity");
        int shelfLife = match.getInt("ShelfLife");
        int shelvesLocation = match.getInt("ShelvesLocation");
        int backLocation = match.getInt("BackLocation");
        catalogItem = new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation, shelfLife);
        String expirationDate = match.getString("DiscountExpiration");
        if(expirationDate != null) {
            double value = match.getDouble("DiscountValue");
            boolean isPercentage = match.getInt("DiscountPercentage") == 1;
            int capacity = match.getInt("DiscountCapacity");
            catalogItem.setCostumerDiscount(new CostumerDiscount(LocalDate.parse(expirationDate, DateTimeFormatter.ofPattern("d/M/yy")), value, isPercentage, capacity));
        }
        catalogItemsIdentitiyMap.put(catalogItem.getId(), catalogItem);
        return catalogItem;
    }
    public LinkedList<String> getCatalogItemCategories(int id) throws SQLException{
        ResultSet matches = executeSelectQuery(String.format("SELECT Category FROM CatalogItemsCategories WHERE CatalogItemId=%d", id));
        LinkedList<String> categories = new LinkedList<>();
        while(matches.next())
            categories.add(matches.getString("Category"));
        return categories;
    }
    public LinkedList<CatalogItem> findAllFromCategory(Category category){
        LinkedList<CatalogItem> catalogItems = new LinkedList<>();
        for(CatalogItem catalogItem : findAll())
            if(catalogItem.isFromCategory(category))
                catalogItems.add(catalogItem);
        return catalogItems;
    }
}
