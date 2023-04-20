package SuperLi.src.Stock;

import java.util.*;

public abstract class StorageUnit {
    Map<Integer, Item> items;

    public boolean addItem(Item item, boolean forBack){
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
    public Item removeItem(int barcode){
        Item item = items.get(barcode);
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
        Item item = items.get(barcode);
        if(item == null)
            return false;
        item.setDamage(damage);
        return true;
    }
    public ArrayList<Item> getItems(){
        return new ArrayList<>(items.values());
    }
    public ArrayList<Item> getDamagedItems(){
        ArrayList<Item> damagedItems = new ArrayList<>();
        for(Item item : items.values())
            if (item.getDamage() != DamageType.NONE || item.is_expired())
                damagedItems.add(item);
        return damagedItems;
    }
    public int barcodeToId(int barcode){
        Item item = this.items.get(barcode);
        if(item == null)
            return -1;
        return item.getCatalogItem().getId();
    }
}
