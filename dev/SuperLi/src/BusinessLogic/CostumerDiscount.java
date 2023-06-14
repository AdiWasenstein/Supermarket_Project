package BusinessLogic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CostumerDiscount {
    LocalDate expirationDate;
    double value;
    boolean isPercentage;
    int minCapacity;

    public CostumerDiscount(LocalDate expirationDate, double value, boolean isPercentage, int minCapacity){
        this.expirationDate = expirationDate;
        this.value = value;
        this.isPercentage = isPercentage;
        this.minCapacity = minCapacity;
    }
    public double getValue(){return this.value;}
    public boolean isPercentage(){return this.isPercentage;}
    public int getMinCapacity(){return this.minCapacity;}
    public LocalDate getExpirationDate(){return this.expirationDate;}
    public double generateDiscount(double originPrice, int amount){
        if(discountValid(amount)) {
            if (isPercentage)
                return originPrice * (1 - value / 100);
            return Math.max(originPrice - value, 0); // Positive validation
        }
        return originPrice;
    }
    public int amountToAdd(int currentCapacity){
        return Math.max(this.minCapacity - currentCapacity, 0);
    }

    public boolean isDateValid(){
        return this.expirationDate.isAfter(LocalDate.now());
    }

    public boolean discountValid(int currentCapacity){
        return isDateValid() && amountToAdd(currentCapacity) == 0;
    }
    public String toString(){
        return String.format("%.1f%s, %d+ end %s", value, (isPercentage ? "%" : "ILS"), minCapacity,
                expirationDate.format(DateTimeFormatter.ofPattern("d/M/yy")));
    }
}