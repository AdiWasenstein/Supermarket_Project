package Stock.src;

public class Category{
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
    @Override
    public boolean equals(Object category){
        Category other = (Category)category;
        String other_prime = other.get_prime_category();
        String other_sub = other.get_sub_category();
        MeasureUnit other_unit = other.get_measureunit();
        double other_size = other.get_size_amount();
        return prime_category.equals(other_prime) && sub_category.equals(other_sub) &&
                size.get_measureunit() == other_unit && size.get_amount() == other_size;
    }
}
