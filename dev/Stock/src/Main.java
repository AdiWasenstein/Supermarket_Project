package Stock.src;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        UserMenu us = new UserMenu();
        initialize_Categories(us);
        for (int i = 0; i < 80; i++)
            initialize_Items(us);
        initialize_discount(us);
        us.communicate();// Labohen
    }

    public static void initialize_Categories(UserMenu us){
    us.branch.add_catalog_item(10, "Milk 3%", new Category("Dairy", "Milk", new Size(1500, MeasureUnit.ML)), "Tnuva", 5.9, 101);
    us.branch.add_catalog_item(11, "Soy Milk", new Category("Health", "Milk", new Size(1000, MeasureUnit.ML)), "BIO", 13.9, 50);
    us.branch.add_catalog_item(12, "Kotetz 9%", new Category("Dairy", "Maadan", new Size(250, MeasureUnit.ML)), "Tara", 6.4, 85);
    us.branch.add_catalog_item(13, "Yolo", new Category("Dairy", "Maadan", new Size(250, MeasureUnit.ML)), "Tnuva", 5, 50);
    us.branch.add_catalog_item(14, "Milki", new Category("Dairy", "Maadan", new Size(100, MeasureUnit.ML)), "Tnuva", 5.9, 60);
    us.branch.add_catalog_item(15, "Cheese", new Category("Dairy", "Cheese", new Size(200, MeasureUnit.GM)), "Emek", 19, 94);
    us.branch.add_catalog_item(16, "Pita", new Category("Baking", "Bread", new Size(10, MeasureUnit.UNIT)), "Angle", 11, 50);
    us.branch.add_catalog_item(17, "Rice Cakes", new Category("Baking", "Bread", new Size(500, MeasureUnit.GM)), "Fitness", 17, 50);
    us.branch.add_catalog_item(18, "White Bread", new Category("Baking", "Bread", new Size(500, MeasureUnit.GM)), "Berman", 7, 60);
    us.branch.add_catalog_item(19, "Head & Shoulders", new Category("Cosmetics", "Shower", new Size(750, MeasureUnit.ML)), "Procter & Gamble", 17, 50);
    us.branch.add_catalog_item(20, "Red Wine", new Category("Beverage", "Alcohol", new Size(750, MeasureUnit.ML)), "Gamla", 60, 30);
    us.branch.add_catalog_item(21, "Cake Mold", new Category("Baking", "Disposable", new Size(21, MeasureUnit.CM)), "Namal", 11.9, 40);
    }
    public static void initialize_Items(UserMenu us){
        us.branch.add_item(10, 3, LocalDate.of(2023,4,11), DamageType.NONE);
        us.branch.add_item(11, 9, LocalDate.of(2024,2,19), DamageType.NONE);
        us.branch.add_item(12, 3.7, LocalDate.of(2023,4,18), DamageType.NONE);
        us.branch.add_item(13, 3, LocalDate.of(2023,4,3), DamageType.NONE);
        us.branch.add_item(14, 3.8, LocalDate.of(2023,4,20), DamageType.ROTTEN);
        us.branch.add_item(15, 10, LocalDate.of(2023,5,10), DamageType.NONE);
        us.branch.add_item(16, 6, LocalDate.of(2023,4,17), DamageType.NONE);
        us.branch.add_item(17, 8, LocalDate.of(2023,10,1), DamageType.NONE);
        us.branch.add_item(18, 4, LocalDate.of(2023,4,8), DamageType.NONE);
        us.branch.add_item(19, 9.66, LocalDate.of(2024,6,4), DamageType.COVER);
        us.branch.add_item(20, 25, LocalDate.of(2025,12,31), DamageType.NONE);
        us.branch.add_item(21, 2, LocalDate.of(2030,1,1), DamageType.NONE);
    }
    public static void initialize_discount(UserMenu us){
        us.branch.set_category_discount(new Category("Baking", "Bread", new Size(500, MeasureUnit.GM)), new Discount(LocalDate.of(2023,10,1), 50, true, 0 ));
        us.branch.set_item_discount(13, new Discount(LocalDate.of(2023,10,1),1.5, false, 0 ));
    }
}