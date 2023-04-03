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
        return String.format("ID: %d, %s; %s; Manufacturer: %s; %f ₪; Amount: %d; Min Capacity: %d",
                id, category, name, manufacturer, sell_price, shelves_amount + back_amount, min_capacity);
    }
    public void set_discount(Discount discount){this.discount = discount;}
    public double get_discounted_price() {return this.discount.generate_discount(this.sell_price, 0);}
    public double get_discounted_price(int amount) {return this.discount.generate_discount(this.sell_price, amount);}
    public boolean is_from_category(Category category){
        Category item_category = this.category;
        String item_prime = item_category.get_prime_category(); String other_prime = category.get_prime_category();
        String item_sub = item_category.get_prime_category(); String other_sub = category.get_sub_category();
        MeasureUnit item_unit = item_category.get_measureunit(); MeasureUnit other_unit = category.get_measureunit();
        double item_size = item_category.get_size_amount(); double other_size = category.get_size_amount();
        return item_prime.equals(other_prime) && item_sub.equals(other_sub) && item_unit == other_unit && item_size == other_size;
    }
}
