package Stock.src;

public class Category {
    String prime_category;
    String sub_category;
    Size size;
    Discount discount;

    public Category(String prime_category, String sub_category, Size size){
        this.prime_category = prime_category;
        this.sub_category = sub_category;
        this.size = size;
        discount = null;
    }

    public void add_discount(int day, int month, int year, double value, boolean is_percentage, int min_capacity) {
        this.discount = new Discount(day, month, year, value, is_percentage, min_capacity);
    }

    public String ToString(){
        return String.format("Category: %s, Sub Category: %s, Size: %s %ss", this.prime_category, this.sub_category, this.size.get_amount(), this.size.get_measureunit().name());
    }
    public String get_prime_category(){
        return this.prime_category;
    }
    public String get_sub_category(){
        return this.sub_category;
    }
    public Discount get_discount(){
        return this.discount;
    }
    public MeasureUnit get_measureunit(){
        return this.size.get_measureunit();
    }
    public double get_size_amount(){
        return this.size.get_amount();
    }
}
