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
}
