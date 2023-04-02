package Stock.src;

public class CatalogItem {
    int id;
    int shelves_amount;
    int back_amount;
    String name;
    String manufacturer;
    double sell_price;
    int min_capacity;
    Category category;

    int shelves_location;
    int back_location;

    public CatalogItem(int id, String name, String manufacturer, double sell_price, int min_capacity, Category category, int shelves_location, int back_location){
        this.id = id;
        this.shelves_amount = 0;
        this.back_amount = 0;
        this.name = name;
        this.manufacturer = manufacturer;
        this.sell_price = sell_price;
        this.min_capacity = min_capacity;
        this.category = category;
        this.shelves_location = shelves_location;
        this.back_location = back_location;
    }
    public void set_shelves_amount(int amount){this.shelves_amount = amount;}
    public void set_back_amount(int amount){this.back_amount = amount;}
    public void set_price(double price){this.sell_price = price;}
    public void set_min_capacity(int capacity){
        this.min_capacity = capacity;
    }
    public int get_id(){ return this.id;}
    public String get_name(){return this.name;}
    public int get_min_capacity() { return this.min_capacity;}
    public int get_shelves_amount(){ return this.shelves_amount;}
    public String toString(){
        return String.format("ID: %s, %s; %s; Manufacturer: %s; %s ₪; Amount: %s; Min Capacity: %s", id, category, name, manufacturer, sell_price, shelves_amount + back_amount, min_capacity);
    }
    public Category getCategory(){return this.category;}
    public int get_shelves_location(){return this.shelves_location;}
    public int get_back_location(){return this.back_location;}

}
