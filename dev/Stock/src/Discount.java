package Stock.src;

import java.time.LocalDate;

public class Discount {
    LocalDate expiration_date;
    double value;
    boolean is_percentage;
    int min_capacity;

    public Discount(LocalDate expiration_date, double value, boolean is_percentage, int min_capacity){
        this.expiration_date = expiration_date;
        this.value = value;
        this.is_percentage = is_percentage;
        this.min_capacity = min_capacity;
    }

    public double generate_discount(double origin_price, int amount){
        if(discount_valid(amount)) {
            if (is_percentage)
                return origin_price * value;
            return Math.max(origin_price - value, 0); // Positive validation
        }
        return origin_price;
    }

    public int amount_to_add(int current_capacity){
        return Math.max(this.min_capacity - current_capacity, 0);
    }

    public boolean is_date_valid(){
        return this.expiration_date.isAfter(LocalDate.now());
    }

    public boolean discount_valid(int current_capacity){
        return is_date_valid() && amount_to_add(current_capacity) == 0;
    }
}