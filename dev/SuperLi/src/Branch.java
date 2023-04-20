package SuperLi.src;
import SuperLi.src.Stock.BackStorage;
import SuperLi.src.Stock.CostumerDiscount;
import SuperLi.src.Stock.Reports.*;
import SuperLi.src.Stock.StoreShelves;

import java.time.LocalDate;
import java.util.*;

public class Branch {
    String address;
    StoreShelves shelves;
    BackStorage back;
    Map<Integer, CatalogItem> catalog;
    public final static int BACKSTART = 1000;
    public final static int BACKEND = 2000;
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
    public CatalogItem get_catalog_from_barcode(int id){return this.catalog.get(id);}
    public boolean set_item_price(int id, double price) {
        if (!contains_id(id) || price < 0)
            return false;
        catalog.get(id).setPrice(price);
        return true;
    }
    public boolean set_damage(int barcode, DamageType damage){
        return this.shelves.set_damage(barcode, damage) || this.back.set_damage(barcode, damage);
    }
    public boolean set_item_capacity(int id, int amount){
        if (!contains_id(id) || amount < 0)
            return false;
        catalog.get(id).setMinCapacity(amount);
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
    public int add_item(int id, double cost_price, LocalDate expiration_date, DamageType damage, boolean for_front){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return -1;
        Item item = new Item(catalog_item, cost_price, expiration_date, damage, catalog_item.getBackLocation());
        if(for_front) {
            item.setLocation(catalog_item.getShelvesLocation());
            if(!this.shelves.add_item(item, false))
                return -1;
            catalog_item.incShelves();
            return item.getBarcode();
        }
        if(!this.back.add_item(item, true))
            return -1;
        catalog_item.incBack();
        return item.getBarcode();
    }
    public int add_item(int id, double cost_price, LocalDate expiration_date, DamageType damage){
        CatalogItem catalog_item = catalog.get(id);
        if(catalog_item == null)
            return -1;
        boolean for_front = catalog_item.getShelvesAmount() < catalog_item.getMinCapacity();
        return add_item(id, cost_price, expiration_date, damage, for_front);
    }
    public boolean remove_item(int barcode){
        Item front_item = this.shelves.remove_item(barcode);
        Item back_item = this.back.remove_item(barcode);
        if(front_item != null) {
            int id = front_item.getCatalogItem().getId();
            CatalogItem catalog_item = catalog.get(id);
            catalog_item.decShelves();
            return true;
        }
        if(back_item != null){
            int id = back_item.getCatalogItem().getId();
        CatalogItem catalog_item = catalog.get(id);
            catalog_item.decBack();
        return true;
        }
        return false;
    }
    public boolean transfer_back_to_front(int barcode){
        Item item = this.back.remove_item(barcode);
        if (item == null)
            return false;
        this.catalog.get(item.getCatalogItem().getId()).decBack();
        this.catalog.get(item.getCatalogItem().getId()).incShelves();
        return this.shelves.add_item(item, false);
    }
    public boolean transfer_front_to_back(int barcode){
        Item item = this.shelves.remove_item(barcode);
        if (item == null)
            return false;
        this.catalog.get(item.getCatalogItem().getId()).decShelves();
        this.catalog.get(item.getCatalogItem().getId()).incBack();
        return this.back.add_item(item, true);
    }
    public boolean transfer(int barcode){
        if (this.shelves.contains(barcode))
            return transfer_front_to_back(barcode);
        else if (this.back.contains(barcode)){
            return transfer_back_to_front(barcode);
        }
        return false;
    }
    public boolean contains_barcode(int barcode){
        return this.shelves.contains(barcode) || this.back.contains(barcode);
    }
    public void generate_stock_report(){
        RequiredStockReport stock_report = new RequiredStockReport();
        for(CatalogItem catalog_item : this.catalog.values())
            if(catalog_item.getShelvesAmount() + catalog_item.getBackAmount() < catalog_item.getMinCapacity())
                stock_report.add_to_report(catalog_item);
        stock_report.generate_report();
    }
    public void generate_category_report(ArrayList<String> primes, ArrayList<String[]> prime_subs, ArrayList<Category> full){
        CategoryReport category_report = new CategoryReport();
        for(CatalogItem catalog_item :  this.catalog.values()){
            Category item_category = catalog_item.getCategory();
            boolean to_add = primes.contains(item_category.get_prime_category());
            if(!to_add) {
                for (String[] prime_sub : prime_subs)
                    if (catalog_item.isFromCategory(prime_sub[0], prime_sub[1])) {
                        to_add = true;
                        break;
                    }
            }
            to_add = to_add || full.contains(item_category);
            if(to_add)
                category_report.add_to_report(catalog_item);
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

    public void generate_all_items_report(){
        StockItemsReport rep = new StockItemsReport();
        for (Item item : shelves.all_items_report())
            rep.add_to_report(item);
        for (Item item : back.all_items_report())
            rep.add_to_report(item);
        rep.generate_report();
    }
    public void generate_catalog_report(){
        AllCatalogReport rep = new AllCatalogReport();
        for(CatalogItem catalog_item : catalog.values())
            rep.add_to_report(catalog_item);
        rep.generate_report();
    }
    public boolean set_item_discount(int id, CostumerDiscount costumerDiscount){
        CatalogItem catalog_item = this.catalog.get(id);
        if(catalog_item == null)
            return false;
        catalog_item.setDiscount(costumerDiscount);
        return true;
    }
    public void set_category_discount(Category category, CostumerDiscount costumerDiscount){
        for(CatalogItem catalog_item : this.catalog.values())
            if(catalog_item.isFromCategory(category))
                catalog_item.setDiscount(costumerDiscount);
    }
    public int get_item_location(int barcode){
        int id = shelves.barcode_to_id(barcode);
        if(id >= 0)
            return catalog.get(id).getShelvesLocation();
        id = back.barcode_to_id(barcode);
        if(id < 0)
            return -1;
        return catalog.get(id).getBackLocation();
    }
}