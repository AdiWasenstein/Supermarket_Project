package SuperLi.src.Stock;
import SuperLi.src.DamageType;
import SuperLi.src.Item;

import java.util.*;

public abstract class StorageUnit {
    Map<Integer, Item> items;

    public boolean add_item(Item item, boolean for_back){
        if(items.containsKey(item.getBarcode()))
            return false;
        if (for_back) {
            item.setLocation(item.getCatalogItem().getBackLocation());
            items.put(item.getBarcode(), item);
            return true;
        }
        item.setLocation(item.getCatalogItem().getShelvesLocation());
        items.put(item.getBarcode(), item);
        return true;
    }
    public Item remove_item(int barcode){
        Item item = items.get(barcode);
        items.remove(barcode);
        return item;
    }
    public void remove_catalog_item(int id){
        Set<Integer> barcodes = Set.copyOf(items.keySet());
        for(Integer barcode: barcodes)
            if(items.get(barcode).getCatalogItem().getId() == id)
                items.remove(barcode);
    }
    public ArrayList<Item> damaged_items(){
        ArrayList<Item> damaged_report = new ArrayList<>();
        for(Item item : items.values())
            if (item.getDamage() != DamageType.NONE || item.is_expired())
                damaged_report.add(item);
        return damaged_report;
    }
    public ArrayList<Item> all_items_report(){
        return new ArrayList<>(items.values());
    }

    public boolean contains(int barcode){return this.items.containsKey(barcode);}
    public int barcode_to_id(int barcode){
        Item item = this.items.get(barcode);
        if(item == null)
            return -1;
        return item.getCatalogItem().getId();
    }
    public boolean set_damage(int barcode, DamageType damage){
        Item item = items.get(barcode);
        if(item == null)
            return false;
        item.setDamage(damage);
        return true;
    }
}
