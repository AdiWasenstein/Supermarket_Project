package Stock.src;

public class CatalogItem {
    int id;
    String name;
    Category category;
    String manufacturer;
    double sell_price;
    int min_capacity;
    int shelves_amount;
    int back_amount;
    int shelves_location;
    int back_location;
    Discount discount;
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
        this.discount = null;
    }
    public int get_id(){ return this.id;}
    public String get_name(){return this.name;}
    public String get_manufacturer(){return this.manufacturer;}
    public Category get_category(){return this.category;}
    public double get_price(){return this.sell_price;}
    public void set_price(double price){this.sell_price = price;}
    public int get_min_capacity() { return this.min_capacity;}
    public void set_min_capacity(int capacity){this.min_capacity = capacity;}
    public int get_total_amount(){return this.shelves_amount + this.back_amount;}
    public int get_shelves_amount(){ return this.shelves_amount;}
    public void set_shelves_amount(int amount){this.shelves_amount = amount;}
    public void inc_shelves(){this.shelves_amount++;}
    public void dec_shelves(){this.shelves_amount--;}
    public int get_back_amount(){ return this.back_amount;}
    public void set_back_amount(int amount){this.back_amount = amount;}
    public void inc_back(){this.back_amount++;}
    public void dec_back(){this.back_amount--;}
    public int get_shelves_location(){return this.shelves_location;}
    public int get_back_location(){return this.back_location;}
    public String toString(){
        return String.format("ID: %d; %s; %s; Manufacturer: %s; %.1f₪,%s; Amount: %d; Min Capacity: %d",
                id, category, name, manufacturer, sell_price, (discount == null ? "" : discount.toString()), shelves_amount + back_amount, min_capacity);
    }
    public Discount get_discount(){return this.discount;}
    public void set_discount(Discount discount){this.discount = discount;}
    public double get_discounted_price() {
        if(this.discount == null)
            return this.sell_price;
        return this.discount.generate_discount(this.sell_price, this.discount.get_min_capacity());}
    public double get_discounted_price(int amount) {return this.discount.generate_discount(this.sell_price, amount);}
    public boolean is_from_category(Category category){
        return category.compareTo(this.get_category()) == 0;
    }
}
