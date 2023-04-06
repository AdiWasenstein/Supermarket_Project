package Stock.src;
import java.util.*;

public abstract class StorageUnit {
    Map<Integer, Item> items;

    public boolean add_item(Item item, boolean for_back){
        if(items.containsKey(item.get_barcode()))
            return false;
        if (for_back) {
            item.set_location(item.get_catalog_item().get_back_location());
            items.put(item.get_barcode(), item);
            return true;
        }
        item.set_location(item.get_catalog_item().get_shelves_location());
        items.put(item.get_barcode(), item);
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
            if(items.get(barcode).get_catalog_item().get_id() == id)
                items.remove(barcode);
    }
    public ArrayList<Item> damaged_items(){
        ArrayList<Item> damaged_report = new ArrayList<>();
        for(Item item : items.values())
            if (item.get_damage() != DamageType.NONE || item.is_expired())
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
        return item.get_catalog_item().get_id();
    }
    public boolean set_damage(int barcode, DamageType damage){
        Item item = items.get(barcode);
        if(item == null)
            return false;
        item.set_damage(damage);
        return true;
    }
}
