package Stock.src;

import java.time.LocalDate;

public class Category {
    String prime_category;
    String sub_category;
    Size size;

    public Category(String prime_category, String sub_category, Size size){
        this.prime_category = prime_category;
        this.sub_category = sub_category;
        this.size = size;
    }
    public String toString(){
        return String.format("%s, %s, %.1f %ss", this.prime_category, this.sub_category, this.size.get_amount(), this.size.get_measureunit().name());
    }
    public String get_prime_category(){
        return this.prime_category;
    }
    public String get_sub_category(){
        return this.sub_category;
    }
    public MeasureUnit get_measureunit(){
        return this.size.get_measureunit();
    }
    public double get_size_amount(){
        return this.size.get_amount();
    }
}
