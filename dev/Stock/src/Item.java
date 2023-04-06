package Stock.src;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Item {
    CatalogItem catalog_item;
    int barcode;
    double cost_price;
    LocalDate expiration_date;
    DamageType damage_type;
    int location;
    static int counter = 1;


    public Item(CatalogItem catalog_item, double cost_price, LocalDate expiration_date, DamageType damaged_type, int location){
        this.catalog_item = catalog_item;
        this.barcode = counter;
        this.cost_price = cost_price;
        this.expiration_date = expiration_date;
        this.damage_type = damaged_type;
        this.location = location;
        counter++;
    }
    public CatalogItem get_catalog_item(){
        return this.catalog_item;
    }
    public int get_barcode(){return this.barcode;}
    public double get_cost_price(){return this.cost_price;}
    public long date_difference(){
        return ChronoUnit.DAYS.between( this.expiration_date, LocalDate.now());
    }
    public boolean is_expired(){ return date_difference() > 0;}
    public String get_expiration_str(){return this.expiration_date.format(DateTimeFormatter.ofPattern("d/M/yy"));}
    public DamageType get_damage(){return this.damage_type;}
    public void set_damage(DamageType type){
        this.damage_type = type;
    }
    public int get_location(){ return this.location;}
    public void set_location(int location){this.location = location;}
    public String toString(){
        return String.format("ID: %d; Barcode: %d; Price: %.1f₪; Location: %d; Manufacturer: %s; Amount: %d; Shelves Amount: %d; Back Amount: %d",
                get_catalog_item().get_id(), get_barcode(), get_catalog_item().get_discounted_price(), get_location(), get_catalog_item().get_manufacturer(),
                get_catalog_item().get_total_amount(), get_catalog_item().get_shelves_amount(), get_catalog_item().get_back_amount());
    }
}