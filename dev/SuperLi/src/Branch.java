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
//    public void generateCatalogReport(){
//        AllCatalogReport rep = new AllCatalogReport(this.id);
//        for(CatalogItem catalogItem : catalogItemsMap.values())
//            rep.addToReport(catalogItem);
//        rep.generate_report();
//    }
//    public void generateCategoryReport(ArrayList<Category> categories, ArrayList<ArrayList<String>> categoriesStrList) {
//        CategoryReport categoryReport = new CategoryReport();
//        for (CatalogItem catalogItem : this.catalogItemsMap.values()) {
//            Category itemCategory = catalogItem.getCategory();
//            if (categories.contains(itemCategory)) {
//                categoryReport.addToReport(catalogItem);
//                continue;
//            }
//            for (ArrayList<String> categoriesStr : categoriesStrList) {
//                if (itemCategory.getCategories().containsAll(categoriesStr)) {
//                    categoryReport.addToReport(catalogItem);
//                    break;
//                }
//            }
//            categoryReport.generate_report();
//        }
//    }

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
//    public void generateRequiredStockReport(){
//        RequiredStockReport requiredStockReport = new RequiredStockReport();
//        for(CatalogItem catalogItem : this.catalogItemsMap.values())
//            if(catalogItem.getShelvesAmount() + catalogItem.getBackAmount() < catalogItem.getMinCapacity())
//                requiredStockReport.addToReport(catalogItem);
//        requiredStockReport.generate_report();
//    }
//    public void generateStockItemsReport(){
//        StockItemsReport rep = new StockItemsReport();
//        for (StockItem item : shelves.getItems())
//            rep.addToReport(item);
//        for (StockItem item : back.getItems())
//            rep.addToReport(item);
//        rep.generate_report();
//    }
//    public void generateDamagedReport(){
//        DamagedReport damagedReport = new DamagedReport();
//        for(StockItem item : shelves.getDamagedItems())
//            damagedReport.addToReport(item);
//        for(StockItem item : back.getDamagedItems())
//            damagedReport.addToReport(item);
//        damagedReport.generate_report();
//    }
}