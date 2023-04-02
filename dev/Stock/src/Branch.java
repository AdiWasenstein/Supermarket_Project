package Stock.src;
import java.time.LocalDate;
import java.util.*;

public class Branch {
    String address;
    StoreShelves shelves;
    BackStorage back;
    Map<Integer, CatalogItem> catalog;

    final static int BACKSTART = 1000;
    final static int BACKEND = 2000;

    public Branch(String address){
        this.address = address;
        this.shelves = new StoreShelves();
        this.back = new BackStorage();
        this.catalog = new HashMap<>();
    }

    public boolean add_catalog_item(int id, String name, String manufacturer, double sell_price, int min_capacity, Category category){
        if(catalog.containsKey(id))
            return false;
        Random rnd = new Random();
        int shelves_location = rnd.nextInt(BACKSTART);
        int back_location = rnd.nextInt(BACKSTART, BACKEND + 1);
        catalog.put(id, new CatalogItem(id, name, manufacturer, sell_price, min_capacity, category, shelves_location, back_location));
        return true;
    }

    public boolean remove_catalog_item(int id){
        if(!catalog.containsKey(id))
            return false;
        this.shelves.remove_catalog_item(id);
        this.back.remove_catalog_item(id);
        this.catalog.remove(id);
        return true;
    }

    public boolean add_item(int id, double cost_price, LocalDate expiration_date, DamagedType damage, boolean for_front){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return false;
        Item item = new Item(catalog_item, cost_price, expiration_date, damage, catalog_item.get_back_location());
        if(for_front) {
            item.set_place(catalog_item.get_shelves_location());
            return this.shelves.add_item(item);
        }
        return this.back.add_item(item);
    }

    public boolean add_item(int id, double cost_price, LocalDate expiration_date, DamagedType damage){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return false;
        boolean for_front = catalog_item.get_shelves_amount() < catalog_item.get_min_capacity();
        return add_item(id, cost_price, expiration_date, damage, for_front);
    }

    public boolean remove_item(int barcode){
        return !(this.shelves.remove_item(barcode) == null && this.back.remove_item(barcode) == null);
    }

    public boolean transfer_back_to_front(int barcode){
        Item item = this.back.remove_item(barcode);
        if (item == null)
            return false;
        return this.shelves.add_item(item);
    }

    public boolean transfer_front_to_back(int barcode){
        Item item = this.shelves.remove_item(barcode);
        if (item == null)
            return false;
        return this.back.add_item(item);
    }
}