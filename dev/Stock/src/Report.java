package Stock.src;
import java.util.*;

public class Report {
    ArrayList<Item> items;

    public void add_item(Item item){
        items.add(item);
    }
    public void generate_report(){
        Map<Integer, CatalogItem> catalog_items = aggregate_items();
        for(Integer id : catalog_items.keySet()){
            System.out.println(catalog_items.get(id)); //need to do catalog_item ToString
        }
    }

    public Map<Integer, CatalogItem> aggregate_items(){
        Map<Integer, CatalogItem> catalog_items= new HashMap<>();
        for (Item item : items) {
            CatalogItem catalog_item = item.get_catalog_item();
            Integer id = catalog_item.get_id();
            catalog_items.put(id, catalog_item);
        }
        return catalog_items;
    }
}