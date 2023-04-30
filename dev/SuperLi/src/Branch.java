package SuperLi.src;
import SuperLi.src.Stock.*;
import SuperLi.src.Stock.Reports.*;

import java.time.LocalDate;
import java.util.*;

public class Branch {
    final int id;
    final String address;
    public final static int BACKSTART = 1000;
    public final static int BACKEND = 2000;
    StockItemDataMapper stockItemDataMapper;
    public Branch(String address, int id){
        this.address = address;
        this.id = id;
        stockItemDataMapper = StockItemDataMapper.getInstance();
    }
    public int getId(){return this.id;}
    public String getAddress(){return this.address;}
    //Catalog Items
    public boolean addCatalogItem(int id, String name, Category category, String manufacturer, double sellPrice, int minCapacity){
        if(catalogItemsMap.containsKey(id))
            return false;
        Random rnd = new Random();
        int shelvesLocation = rnd.nextInt(BACKSTART);
        int backLocation = rnd.nextInt(BACKSTART, BACKEND + 1);
        catalogItemsMap.put(id, new CatalogItem(id, name, manufacturer, sellPrice, minCapacity, category, shelvesLocation, backLocation));
        return true;
    }
    public boolean removeCatalogItem(int id){
        if(!catalogItemsMap.containsKey(id))
            return false;
        this.shelves.removeCatalogItem(id);
        this.back.removeCatalogItem(id);
        this.catalogItemsMap.remove(id);
        return true;
    }
    public boolean containsId(int id){
        return catalogItemsMap.containsKey(id);
    }
    public boolean setPrice(int id, double price) {
        if (!containsId(id) || price < 0)
            return false;
        catalogItemsMap.get(id).setPrice(price);
        return true;
    }
    public boolean setMinCapacity(int id, int amount){
        if (!containsId(id) || amount < 0)
            return false;
        catalogItemsMap.get(id).setMinCapacity(amount);
        return true;
    }
    public boolean setItemDiscount(int id, CostumerDiscount costumerDiscount){
        CatalogItem catalogItem = this.catalogItemsMap.get(id);
        if(catalogItem == null)
            return false;
        catalogItem.setCostumerDiscount(costumerDiscount);
        return true;
    }
    public void setCategoryDiscount(Category category, CostumerDiscount costumerDiscount){
        for(CatalogItem catalogItem : this.catalogItemsMap.values())
            if(catalogItem.isFromCategory(category))
                catalogItem.setCostumerDiscount(costumerDiscount);
    }
    public void generateCatalogReport(){
        AllCatalogReport rep = new AllCatalogReport();
        for(CatalogItem catalogItem : catalogItemsMap.values())
            rep.add_to_report(catalogItem);
        rep.generate_report();
    }
    public void generateCategoryReport(ArrayList<Category> categories, ArrayList<ArrayList<String>> categoriesStrList) {
        CategoryReport categoryReport = new CategoryReport();
        for (CatalogItem catalogItem : this.catalogItemsMap.values()) {
            Category itemCategory = catalogItem.getCategory();
            if (categories.contains(itemCategory)) {
                categoryReport.add_to_report(catalogItem);
                continue;
            }
            for (ArrayList<String> categoriesStr : categoriesStrList) {
                if (itemCategory.getCategories().containsAll(categoriesStr)) {
                    categoryReport.add_to_report(catalogItem);
                    break;
                }
            }
            categoryReport.generate_report();
        }
    }

    // Stock Items
    public StockItem getStockItem(int barcode) {
        Optional<StockItem> stockItem = StockItemDataMapper.getInstance().find(Integer.toString(barcode));
        if(stockItem.isEmpty() || stockItem.get().getBranchId() != this.id)
            return null;
        return stockItem.get();
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, LocalDate expirationDate, DamageType damage, boolean forFront) {
        if(getStockItem(barcode) != null)
            return false;
        int location = forFront ? catalogItem.getShelvesLocation() : catalogItem.getBackLocation();
        StockItem stockItem = new StockItem(catalogItem, barcode, costPrice, expirationDate, damage, this.id, location);
        stockItemDataMapper.insert(stockItem);
        return true;
    }
    public boolean addStockItem(CatalogItem catalogItem, int barcode, double costPrice, LocalDate expirationDate, DamageType damage){
        boolean forFront = catalogItem.getShelvesAmount(id) < catalogItem.getMinCapacity();
        return addStockItem(catalogItem, barcode, costPrice, expirationDate, damage, forFront);
    }
    public boolean removeItem(int barcode){
        StockItem stockItem =  getStockItem(barcode);
        if(stockItem == null || stockItem.getBranchId() != this.id)
            return false;
        stockItemDataMapper.delete(stockItem);
        return true;
    }
    public boolean containsBarcode(int barcode){
        return getStockItem(barcode) != null;
    }
    public boolean setDamage(int barcode, DamageType damage){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setDamage(damage);
        return true;
    }
    public int getItemLocation(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return -1;
        return stockItem.getLocation();
    }
    public boolean transferItem(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        int currentLocation = stockItem.getLocation();
        if(currentLocation < Branch.BACKSTART)
            transferFrontToBack(barcode);
        else
            transferBackToFront(barcode);
        return true;
    }
    public boolean transferFrontToBack(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setLocation(stockItem.getCatalogItem().getBackLocation());
        return true;
    }
    public boolean transferBackToFront(int barcode){
        StockItem stockItem = getStockItem(barcode);
        if(stockItem == null)
            return false;
        stockItem.setLocation(stockItem.getCatalogItem().getShelvesLocation());
        return true;
    }
    public int barcodeToId(int barcode){
        StockItem stockItem = getStockItem(barcode);
        return stockItem == null ? -1 : stockItem.getCatalogItem().getId();
    }
    public void generateRequiredStockReport(){
        RequiredStockReport requiredStockReport = new RequiredStockReport();
        for(CatalogItem catalogItem : this.catalogItemsMap.values())
            if(catalogItem.getShelvesAmount() + catalogItem.getBackAmount() < catalogItem.getMinCapacity())
                requiredStockReport.add_to_report(catalogItem);
        requiredStockReport.generate_report();
    }
    public void generateStockItemsReport(){
        StockItemsReport rep = new StockItemsReport();
        for (StockItem item : shelves.getItems())
            rep.add_to_report(item);
        for (StockItem item : back.getItems())
            rep.add_to_report(item);
        rep.generate_report();
    }
    public void generateDamagedReport(){
        DamagedReport damagedReport = new DamagedReport();
        for(StockItem item : shelves.getDamagedItems())
            damagedReport.add_to_report(item);
        for(StockItem item : back.getDamagedItems())
            damagedReport.add_to_report(item);
        damagedReport.generate_report();
    }
}