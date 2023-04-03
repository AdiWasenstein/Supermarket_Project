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
    public String get_address(){return this.address;}
    public boolean contains_id(int id){
        return catalog.containsKey(id);
    }
    public int barcode_to_id(int barcode){
        return Math.max(this.shelves.barcode_to_id(barcode), this.back.barcode_to_id(barcode));
    }
    public CatalogItem get_catalog_from_barcode(int id){
        return this.catalog.get(id);
    }
    public boolean set_item_price(int id, double price) {
        if (!contains_id(id) || price < 0)
            return false;
        catalog.get(id).set_price(price);
        return true;
    }

    public boolean set_item_capacity(int id, int amount){
        if (!contains_id(id) || amount < 0)
            return false;
        catalog.get(id).set_min_capacity(amount);
        return true;
    }
    public boolean add_catalog_item(int id, String name, Category category, String manufacturer, double sell_price, int min_capacity){
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
    public boolean add_item(int id, double cost_price, LocalDate expiration_date, DamageType damage, boolean for_front){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return false;
        Item item = new Item(catalog_item, cost_price, expiration_date, damage, catalog_item.get_back_location());
        if(for_front) {
            item.set_location(catalog_item.get_shelves_location());
            if(!this.shelves.add_item(item))
                return false;
            catalog_item.inc_shelves();
            return true;
        }
        if(!this.back.add_item(item))
            return false;
        catalog_item.inc_back();
        return true;
    }
    public boolean add_item(int id, double cost_price, LocalDate expiration_date, DamageType damage){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return false;
        boolean for_front = catalog_item.get_shelves_amount() < catalog_item.get_min_capacity();
        return add_item(id, cost_price, expiration_date, damage, for_front);
    }
    public boolean remove_item(int barcode){
        Item front_item = this.shelves.remove_item(barcode);
        Item back_item = this.back.remove_item(barcode);
        if(front_item != null) {
            int id = front_item.get_catalog_item().get_id();
            CatalogItem catalog_item = catalog.get(id);
            catalog_item.dec_shelves();
            return true;
        }
        if(back_item != null){
            int id = back_item.get_catalog_item().get_id();
        CatalogItem catalog_item = catalog.get(id);
            catalog_item.dec_back();
        return true;
        }
        return false;
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
    public boolean transfer(int barcode){
        if (this.shelves.contain(barcode))
            return transfer_front_to_back(barcode);
        else if (this.back.contain(barcode)){
            return transfer_back_to_front(barcode);
        }
        return false;
    }
    public void generate_stock_report(){
        StockReport stock_report = new StockReport();
        for(CatalogItem catalog_item : this.catalog.values())
            if(catalog_item.get_shelves_amount() + catalog_item.get_back_amount() < catalog_item.get_min_capacity())
                stock_report.add_to_report(catalog_item);
        stock_report.generate_report();
    }
    public void generate_category_report(ArrayList<String> primes, ArrayList<String[]> prime_subs, ArrayList<Category> full){
        CategoryReport category_report = new CategoryReport();
        for(CatalogItem catalog_item :  this.catalog.values()){
            Category item_category = catalog_item.get_category();
            String prime = item_category.get_prime_category();
            String sub = item_category.get_sub_category();
            if(primes.contains(prime)){
                category_report.add_to_report(catalog_item);
                continue;
            }
            String[] prime_sub = {prime, sub};
            if(prime_subs.contains(prime_sub)){
                category_report.add_to_report(catalog_item);
                continue;
            }
            if(full.contains(catalog_item.get_category())){
                category_report.add_to_report(catalog_item);
            }
        }
        category_report.generate_report();
    }

    public void generate_damaged_report(){
        DamagedReport damaged_report = new DamagedReport();
        for(Item item : shelves.damaged_items())
            damaged_report.add_to_report(item);
        for(Item item : back.damaged_items())
            damaged_report.add_to_report(item);
        damaged_report.generate_report();
    }
    public boolean set_item_discount(int id, Discount discount){
        CatalogItem catalog_item = this.catalog.get(id);
        if(catalog_item == null)
            return false;
        catalog_item.set_discount(discount);
        return true;
    }
    public void set_category_discount(Category category, Discount discount){
        for(CatalogItem catalog_item : this.catalog.values())
            if(catalog_item.is_from_category(category))
                catalog_item.set_discount(discount);
    }
}