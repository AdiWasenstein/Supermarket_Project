package Stock.src;
import java.util.*;

public abstract class StorageUnit {
    Map<Integer, Item> items;

    public boolean add_item(Item item){
        if(items.containsKey(item.get_barcode()))
            return false;
        items.put(item.get_barcode(), item);
        return true;
    }
    public Item remove_item(int barcode){
        return items.get(barcode);
    }
    public void remove_catalog_item(int id){
        for(Integer item_id: items.keySet())
            if(items.get(item_id).get_catalog_item().get_id() == id)
                items.remove(id);
    }
    public ArrayList<Item> damaged_items(){
        ArrayList<Item> damaged_report = new ArrayList<>();
        for(Item item : items.values())
            if (item.get_damaged() != DamageType.NONE || item.is_expired())
                damaged_report.add(item);
        return damaged_report;
    }
    public boolean contain(int barcode){return this.items.containsKey(barcode);}
    public int barcode_to_id(int barcode){
        Item item = this.items.get(barcode);
        if(item == null)
            return -1;
        return item.get_catalog_item().get_id();
    }
}
