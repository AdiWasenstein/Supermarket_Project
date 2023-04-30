package SuperLi.src;
import SuperLi.src.Stock.*;
import SuperLi.src.Stock.Reports.*;

import java.time.LocalDate;
import java.util.*;

public class Branch {
    String address;
    StoreShelves shelves;
    BackStorage back;
    Map<Integer, CatalogItem> catalogItemsMap;
    public final static int BACKSTART = 1000;
    public final static int BACKEND = 2000;
    public Branch(String address){
        this.address = address;
        this.shelves = new StoreShelves();
        this.back = new BackStorage();
        this.catalogItemsMap = new HashMap<>();
    }
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
    public int addItem(int id, double costPrice, LocalDate expirationDate, DamageType damage, boolean forFront){
        CatalogItem catalogItem = catalogItemsMap.get(id);
        if(catalogItem == null)
            return -1;
        StockItem item = new StockItem(catalogItem, costPrice, expirationDate, damage, catalogItem.getBackLocation());
        if(forFront) {
            item.setLocation(catalogItem.getShelvesLocation());
            if(!this.shelves.addItem(item, false))
                return -1;
            catalogItem.incShelves();
            return item.getBarcode();
        }
        if(!this.back.addItem(item, true))
            return -1;
        catalogItem.incBack();
        return item.getBarcode();
    }
    public int addItem(int id, double costPrice, LocalDate expirationDate, DamageType damage){
        CatalogItem catalogItem = catalogItemsMap.get(id);
        if(catalogItem == null)
            return -1;
        boolean forFront = catalogItem.getShelvesAmount() < catalogItem.getMinCapacity();
        return addItem(id, costPrice, expirationDate, damage, forFront);
    }
    public boolean removeItem(int barcode){
        StockItem frontItem = this.shelves.removeItem(barcode);
        StockItem backItem = this.back.removeItem(barcode);
        if(frontItem != null) {
            int id = frontItem.getCatalogItem().getId();
            CatalogItem catalogItem = catalogItemsMap.get(id);
            catalogItem.decShelves();
            return true;
        }
        if(backItem != null){
            int id = backItem.getCatalogItem().getId();
            CatalogItem catalogItem = catalogItemsMap.get(id);
            catalogItem.decBack();
            return true;
        }
        return false;
    }
    public boolean containsBarcode(int barcode){
        return this.shelves.contains(barcode) || this.back.contains(barcode);
    }
    public boolean setDamage(int barcode, DamageType damage){
        return this.shelves.setDamage(barcode, damage) || this.back.setDamage(barcode, damage);
    }
    public int getItemLocation(int barcode){
        int id = shelves.barcodeToId(barcode);
        if(id >= 0)
            return catalogItemsMap.get(id).getShelvesLocation();
        id = back.barcodeToId(barcode);
        if(id < 0)
            return -1;
        return catalogItemsMap.get(id).getBackLocation();
    }
    public boolean transferItem(int barcode){
        if (this.shelves.contains(barcode))
            return transferFrontToBack(barcode);
        else if (this.back.contains(barcode)){
            return transferBackToFront(barcode);
        }
        return false;
    }
    public boolean transferFrontToBack(int barcode){
        StockItem item = this.shelves.removeItem(barcode);
        if (item == null)
            return false;
        this.catalogItemsMap.get(item.getCatalogItem().getId()).decShelves();
        this.catalogItemsMap.get(item.getCatalogItem().getId()).incBack();
        return this.back.addItem(item, true);
    }
    public boolean transferBackToFront(int barcode){
        StockItem item = this.back.removeItem(barcode);
        if (item == null)
            return false;
        this.catalogItemsMap.get(item.getCatalogItem().getId()).decBack();
        this.catalogItemsMap.get(item.getCatalogItem().getId()).incShelves();
        return this.shelves.addItem(item, false);
    }
    public int barcodeToId(int barcode){
        return Math.max(this.shelves.barcodeToId(barcode), this.back.barcodeToId(barcode));
    }
    public CatalogItem getCatalogItemFromBarcode(int id){return this.catalogItemsMap.get(id);}
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