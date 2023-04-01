package Stock.src;

public class CatalogItem {
    int id;
    int amount;
    String name;
    String manufacturer;
    double sell_price;
    int min_capacity;
    Category category;

    public CatalogItem(int id, String name, String manufacturer, double sell_price, int min_capacity, Category category){
        this.id = id;
        this.amount = 0;
        this.name = name;
        this.manufacturer = manufacturer;
        this.sell_price = sell_price;
        this.min_capacity = min_capacity;
        this.category = category;
    }
    public void set_amount(int amount){
        this.amount = amount;
    }
    public void set_price(double price){
        this.sell_price = price;
    }
    public void set_min_capacity(int capacity){
        this.min_capacity = capacity;
    }
    public int get_id(){
        return this.id;
    }
    public String get_name(){
        return this.name;
    }
}
