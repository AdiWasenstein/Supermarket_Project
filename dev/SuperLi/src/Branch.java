package SuperLi.src;
import SuperLi.src.Stock.*;
import SuperLi.src.Stock.Reports.*;
import java.time.LocalDate;
import java.util.*;

public class Branch {
    final String address;
    final int id;
    StoreShelves shelves;
    BackStorage back;
    static int counter = 0;
    public Branch(String address){
        this.address = address;
        this.id = counter++;
        this.shelves = new StoreShelves();
        this.back = new BackStorage();
    }
    public String getAddress(){return address;}
    public int getId(){return id;}
    // Stock Items
    public int addItem(int id, double costPrice, LocalDate expirationDate, DamageType damage, boolean forFront){
        CatalogItem catalogItem = catalogItemsMap.get(id);
        if(catalogItem == null)
            return -1;
        Item item = new Item(catalogItem, costPrice, expirationDate, damage, catalogItem.getBackLocation());
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
        Item frontItem = this.shelves.removeItem(barcode);
        Item backItem = this.back.removeItem(barcode);
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
        Item item = this.shelves.removeItem(barcode);
        if (item == null)
            return false;
        this.catalogItemsMap.get(item.getCatalogItem().getId()).decShelves();
        this.catalogItemsMap.get(item.getCatalogItem().getId()).incBack();
        return this.back.addItem(item, true);
    }
    public boolean transferBackToFront(int barcode){
        Item item = this.back.removeItem(barcode);
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
        for (Item item : shelves.getItems())
            rep.add_to_report(item);
        for (Item item : back.getItems())
            rep.add_to_report(item);
        rep.generate_report();
    }
    public void generateDamagedReport(){
        DamagedReport damagedReport = new DamagedReport();
        for(Item item : shelves.getDamagedItems())
            damagedReport.add_to_report(item);
        for(Item item : back.getDamagedItems())
            damagedReport.add_to_report(item);
        damagedReport.generate_report();
    }
}