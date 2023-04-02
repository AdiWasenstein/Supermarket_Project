package Stock.src;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Item {
    CatalogItem catalog_item;
    int barcode;
    double cost_price;
    LocalDate expiration_date;
    DamagedType damaged;
    int place;
    Discount discount;
    static int counter = 0;


    public Item(CatalogItem catalog_item, double cost_price, LocalDate date, DamagedType damaged, int item_place){
        this.catalog_item = catalog_item;
        this.barcode = counter;
        this.cost_price = cost_price;
        this.expiration_date = date;
        this.damaged = damaged;
        this.place = item_place;
        this.discount = null;
        counter++;
    }
    public boolean is_expired(){ return date_difference() > 0;}
    public int get_place(){ return this.place;}
    public int get_barcode(){return this.barcode;}
    public long date_difference(){
        return ChronoUnit.DAYS.between( this.expiration_date, LocalDate.now());
    }
    public void set_damaged(DamagedType type){
        this.damaged = type;
    }
    public void set_place(int place){
        this.place = place;
    }
    public void add_discount(LocalDate date, double value, boolean is_percentage, int min_capacity) {
        this.discount = new Discount(date, value, is_percentage, min_capacity);
    }
    public String get_category(){
        return this.catalog_item.getCategory().toString();
    }
    public DamagedType get_damaged(){
        return this.damaged;
    }
    public boolean is_from_category(String prime_category){
        return this.catalog_item.getCategory().get_prime_category().equals(prime_category);
    }
    public boolean is_from_category(String prime_category, String sub_category){
        return this.catalog_item.getCategory().get_prime_category().equals(prime_category) && this.catalog_item.getCategory().get_sub_category().equals(sub_category);
    }
    public boolean is_from_category(String prime_category, String sub_category, MeasureUnit measureunit, double amount){
        return this.catalog_item.getCategory().get_prime_category().equals(prime_category) &&
                this.catalog_item.getCategory().get_sub_category().equals(sub_category) &&
                this.catalog_item.getCategory().get_measureunit() == measureunit && this.catalog_item.getCategory().get_size_amount() == amount;
    }
    public CatalogItem get_catalog_item(){
        return this.catalog_item;
    }
}