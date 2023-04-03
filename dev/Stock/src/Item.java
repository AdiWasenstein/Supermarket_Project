package Stock.src;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item {
    CatalogItem catalog_item;
    int barcode;
    double cost_price;
    LocalDate expiration_date;
    DamageType damage_type;
    int location;
    Discount discount;
    static int counter = 1;


    public Item(CatalogItem catalog_item, double cost_price, LocalDate expiration_date, DamageType damaged_type, int location){
        this.catalog_item = catalog_item;
        this.barcode = counter;
        this.cost_price = cost_price;
        this.expiration_date = expiration_date;
        this.damage_type = damaged_type;
        this.location = location;
        this.discount = null;
        counter++;
    }
    public CatalogItem get_catalog_item(){
        return this.catalog_item;
    }
    public int get_barcode(){return this.barcode;}
    public long date_difference(){
        return ChronoUnit.DAYS.between( this.expiration_date, LocalDate.now());
    }
    public boolean is_expired(){ return date_difference() > 0;}
    public DamageType get_damaged(){
        return this.damage_type;
    }
    public void set_damaged(DamageType type){
        this.damage_type = type;
    }
    public int get_location(){ return this.location;}
    public void set_location(int location){this.location = location;}
    public Discount get_discount() {return discount;}
    public void set_discount(Discount discount) {this.discount = discount;}
    public boolean is_from_category(String prime_category){
        Category category = this.catalog_item.get_category();
        return prime_category.equals(category.get_prime_category());
    }
    public boolean is_from_category(String prime_category, String sub_category){
        Category category = this.catalog_item.get_category();
        return prime_category.equals(category.get_prime_category()) &&
                sub_category.equals(category.get_sub_category());
    }
    public boolean is_from_category(String prime_category, String sub_category, MeasureUnit measureunit, double amount){
        Category category = this.catalog_item.get_category();
        return prime_category.equals(category.get_prime_category()) &&
                sub_category.equals(category.get_sub_category()) &&
                category.get_measureunit() == measureunit && category.get_size_amount() == amount;
    }
}