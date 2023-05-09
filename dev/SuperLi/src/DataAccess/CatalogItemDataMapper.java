package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class CatalogItemDataMapper extends ADataMapper<CatalogItem> {
    Map<Integer, CatalogItem> catalogItemsIdentitiyMap;
    static CatalogItemDataMapper catalogItemDataMapper = null;

    private CatalogItemDataMapper() {
        catalogItemsIdentitiyMap = new HashMap<>();
    }

    public static CatalogItemDataMapper getInstance() {
        if (catalogItemDataMapper == null)
            catalogItemDataMapper = new CatalogItemDataMapper();
        return catalogItemDataMapper;
    }

    public void insert(CatalogItem catalogItem) {
        super.insert(catalogItem);
        int id = catalogItem.getId();
        // Insert Categories
        String categoryQuery = "INSERT INTO CatalogItemsCategories(CatalogItemId, Category) VALUES(%d, '%s')";
        for (String category : catalogItem.getCategory().getCategories())
            executeVoidQuery(String.format(categoryQuery, id, category));
    }

    protected String insertQuery(CatalogItem catalogItem) {
        catalogItemsIdentitiyMap.put(catalogItem.getId(), catalogItem);
        int id = catalogItem.getId();
        String name = catalogItem.getName();
        String manufacturer = catalogItem.getManufacturer();
        double sellPrice = catalogItem.getSellPrice();
        int minCapacity = catalogItem.getMinCapacity();
        int shelfLife = catalogItem.getShelfLife();
        double sizeAmount = catalogItem.getCategory().getSizeAmount();
        int measureUnit = catalogItem.getCategory().getMeasureUnit().ordinal();
        int shelvesLocation = catalogItem.getShelvesLocation();
        int backLocation = catalogItem.getBackLocation();
        CostumerDiscount costumerDiscount = catalogItem.getCostumerDiscount();
        String queryFields = String.format("INSERT INTO CatalogItems(Id, Name, Manufacturer, SellPrice, MinCapacity, ShelfLife, SizeAmount, MeasureUnit, ShelvesLocation, BackLocation%s)",
                costumerDiscount == null ? "" : ", DiscountValue, DiscountPercentage, DiscountCapacity, DiscountExpiration");
        String discountValues = costumerDiscount == null ? "" : String.format(", %.1f, %d, %d, '%s'",
                costumerDiscount.getValue(),
                costumerDiscount.isPercentage() ? 1 : 0,
                costumerDiscount.getMinCapacity(),
                costumerDiscount.getExpirationDate().toString());
        return queryFields + String.format("VALUES(%d, '%s', '%s', %.1f, %d, %d, %.1f, %d, %d, %d%s)",
                id, name, manufacturer, sellPrice, minCapacity, shelfLife, sizeAmount, measureUnit, shelvesLocation, backLocation, discountValues);
    }

    protected String deleteQuery(CatalogItem catalogItem) {
        int id = catalogItem.getId();
        catalogItemsIdentitiyMap.remove(id);
        StockItemDataMapper.getInstance().deleteMatchingCatalog(id);
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
    protected CatalogItem insertIdentityMap(ResultSet match) throws SQLException{
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
        int backLocation = match.getInt("Location");
        catalogItem = new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation, shelfLife);
        String expirationDate = match.getString("DiscountExpiration");
        if(expirationDate != null) {
            double value = match.getDouble("DiscountValue");
            boolean isPercentage = match.getInt("DiscountPercentage") == 1;
            int capacity = match.getInt("DiscountCapacity");
            catalogItem.setCostumerDiscount(new CostumerDiscount(LocalDate.parse(expirationDate), value, isPercentage, capacity));
        }
        catalogItemsIdentitiyMap.put(catalogItem.getId(), catalogItem);
        return catalogItem;
    }
    public LinkedList<String> getCatalogItemCategories(int id) throws SQLException{
        ResultSet matches = executeSelectQuery(String.format("SELECT Category FROM CatalogItemsCategories WHERE Id=%d", id));
        LinkedList<String> categories = new LinkedList<>();
        while(matches.next())
            categories.add(matches.getString("Category"));
        return categories;
    }
}