package Stock.src;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item {
    CatalogItem catalog_item;
    int barcode;
    Category category;
    double cost_price;
    LocalDate expiration_date;
    DamagedType damaged;
    int place;
    Discount discount;
    static int counter = 0;


    public Item(CatalogItem catalog_item, int barcode, Category category, double cost_price, int year, int month, int day,
                DamagedType damaged, int item_place){
        this.catalog_item = catalog_item;
        this.barcode = counter;
        this.category = category;
        this.cost_price = cost_price;
        this.expiration_date = LocalDate.of(year, month, day);
        this.damaged = DamagedType.NONE;
        this.place = -1;
        this.discount = null;
        counter++;
    }
    public long date_difference(){
        return ChronoUnit.DAYS.between(LocalDate.now(), this.expiration_date);
    }
    public void set_damaged(DamagedType type){
        this.damaged = type;
    }
    public void set_place(int place){
        this.place = place;
    }
    public void add_discount(int day, int month, int year, double value, boolean is_percentage, int min_capacity) {
        this.discount = new Discount(day, month, year, value, is_percentage, min_capacity);
    }
    public String get_category(){
        return this.category.ToString();
    }
    public String get_damaged(){
        return this.damaged.name();
    }
    public boolean is_from_category(String prime_category){
        return this.category.get_prime_category().equals(prime_category);
    }
    public boolean is_from_category(String prime_category, String sub_category){
        return this.category.get_prime_category().equals(prime_category) && this.category.get_sub_category().equals(sub_category);
    }
    public boolean is_from_category(String prime_category, String sub_category, MeasureUnit measureunit, double amount){
        return this.category.get_prime_category().equals(prime_category) && this.category.get_sub_category().equals(sub_category) && this.category.get_measureunit() == measureunit && this.category.get_size_amount() == amount;
    }
    public CatalogItem get_catalog_item(){
        return this.catalog_item;
    }
}