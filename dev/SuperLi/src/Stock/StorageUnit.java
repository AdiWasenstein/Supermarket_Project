package SuperLi.src.Stock;

import java.util.*;

public abstract class StorageUnit {
    Map<Integer, StockItem> items;

    public boolean addItem(StockItem item, boolean forBack){
        if(items.containsKey(item.getBarcode()))
            return false;
        if (forBack) {
            item.setLocation(item.getCatalogItem().getBackLocation());
            items.put(item.getBarcode(), item);
            return true;
        }
        item.setLocation(item.getCatalogItem().getShelvesLocation());
        items.put(item.getBarcode(), item);
        return true;
    }
    public StockItem removeItem(int barcode){
        StockItem item = items.get(barcode);
        items.remove(barcode);
        return item;
    }
    public void removeCatalogItem(int id){
        Set<Integer> barcodes = Set.copyOf(items.keySet());
        for(Integer barcode: barcodes)
            if(items.get(barcode).getCatalogItem().getId() == id)
                items.remove(barcode);
    }
    public boolean contains(int barcode){return this.items.containsKey(barcode);}
    public boolean setDamage(int barcode, DamageType damage){
        StockItem item = items.get(barcode);
        if(item == null)
            return false;
        item.setDamage(damage);
        return true;
    }
    public ArrayList<StockItem> getItems(){
        return new ArrayList<>(items.values());
    }
    public ArrayList<StockItem> getDamagedItems(){
        ArrayList<StockItem> damagedItems = new ArrayList<>();
        for(StockItem item : items.values())
            if (item.getDamage() != DamageType.NONE || item.is_expired())
                damagedItems.add(item);
        return damagedItems;
    }
    public int barcodeToId(int barcode){
        StockItem item = this.items.get(barcode);
        if(item == null)
            return -1;
        return item.getCatalogItem().getId();
    }
}
