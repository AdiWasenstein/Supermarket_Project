package Stock.src;

import java.time.LocalDate;

public class Main {
    static Category milk1000 = new Category("Dairy", "Milk", new Size(1000, MeasureUnit.ML));
    static Category milk1500 = new Category("Dairy", "Milk", new Size(1500, MeasureUnit.ML));
    static Category cottage250 = new Category("Dairy", "Cheese", new Size(250, MeasureUnit.GM));
    static Category gouda100 = new Category("Dairy", "Cheese", new Size(100, MeasureUnit.GM));
    static Category pudding250 = new Category("Dairy", "Puddings", new Size(250, MeasureUnit.GM));
    static Category shampoo750 = new Category("Dairy", "Milk", new Size(750, MeasureUnit.ML));
    static Category soap500 = new Category("Dairy", "Milk", new Size(500, MeasureUnit.ML));
    static Category pan50 = new Category("Kitchen", "Kitchen Tools", new Size(50, MeasureUnit.CM));
    static Category fork100 = new Category("Kitchen", "Cutlery", new Size(100, MeasureUnit.UNIT));
    static Category cap100 = new Category("Food Accessories", "Caps", new Size(100, MeasureUnit.UNIT));
    static Category plate50 = new Category("Food Accessories", "Plates", new Size(50, MeasureUnit.UNIT));
    static Category rice1000 = new Category("Legumes", "Rice", new Size(1000, MeasureUnit.GM));
    static Category pasta1000 = new Category("Legumes", "Milk", new Size(1000, MeasureUnit.GM));
    static Category pita10 = new Category("Breads", "Pitas", new Size(10, MeasureUnit.UNIT));
    static Category rice_cake500 = new Category("Breads", "Rice Cakes", new Size(500, MeasureUnit.GM));
    static Category white_bread500 = new Category("Breads", "White Bread", new Size(500, MeasureUnit.GM));
    static Category red_wine750 = new Category("Beverage", "Alcohol", new Size(750, MeasureUnit.ML));
    static Category soft_drink1500 = new Category("Beverage", "Soft Drinks", new Size(1500, MeasureUnit.ML));
    static Category cake_mold21 = new Category("Baking", "Disposable", new Size(21, MeasureUnit.CM));

    public static void main(String[] args) {
        UserMenu us = new UserMenu();
        initialize_Catalog(us);
        for (int i = 0; i < 1; i++)
            initialize_Items(us);
        initialize_discount(us);
        us.communicate();// Labohen
    }

    public static void initialize_Catalog(UserMenu us){
    us.branch.add_catalog_item(10, "Milk 3%", milk1500, "Tnuva", 9.9, 95);
    us.branch.add_catalog_item(11, "Milk 5%", milk1500, "Tnuva", 9.9, 95);
    us.branch.add_catalog_item(12, "Milk 3%", milk1000, "Tnuva", 5.9, 101);
    us.branch.add_catalog_item(13, "Milk 5%", milk1000, "Tnuva", 5.9, 101);
    us.branch.add_catalog_item(14, "Milk 2%", milk1500, "Tnuva", 9.9, 70);
    us.branch.add_catalog_item(15, "Soy Milk", milk1000, "BIO", 15.9, 50);
    us.branch.add_catalog_item(16, "cottage 9%", cottage250, "Tara", 6.4, 85);
    us.branch.add_catalog_item(17, "Yolo", pudding250, "Tnuva", 5, 50);
    us.branch.add_catalog_item(18, "Milky", pudding250, "Tnuva", 5.9, 60);
    us.branch.add_catalog_item(19, "Gouda", gouda100, "Emek", 19, 94);
    us.branch.add_catalog_item(20, "Pita", pita10, "Angle", 11, 50);
    us.branch.add_catalog_item(21, "Rice Cakes", rice_cake500, "Fitness", 17, 50);
    us.branch.add_catalog_item(22, "White Bread", white_bread500, "Berman", 7, 60);
    us.branch.add_catalog_item(23, "Head & Shoulders", shampoo750, "Procter & Gamble", 17, 50);
    us.branch.add_catalog_item(24, "Red Wine",red_wine750 , "Gamla", 60, 30);
    us.branch.add_catalog_item(25, "Cake Mold", cake_mold21, "Namal", 11.9, 40);
    us.branch.add_catalog_item(26, "Cottage 5%", cottage250, "Tnuva", 6.4, 200);
    us.branch.add_catalog_item(27, "Cottage 12%", cottage250, "Tnuva", 6.4, 500);
    us.branch.add_catalog_item(28, "Persian rice", rice1000, "Sugat", 11.5, 80);
    us.branch.add_catalog_item(29, "Penne pasta", pasta1000, "Rummo", 5.9, 120);
    us.branch.add_catalog_item(30, "Pnina Hand Soap", soap500, "Pnina Rosenblum", 12, 80);
    us.branch.add_catalog_item(31, "Pan 21cm", pan50, "Teffal", 200, 30);
    us.branch.add_catalog_item(32, "Fork 100 units", fork100, "Namal", 11, 100);
    us.branch.add_catalog_item(33, "Hot cups 100 units", cap100, "Namal", 21.3, 176);
    us.branch.add_catalog_item(34, "Fanta", soft_drink1500, "Coca Cola", 8.7, 200);
    us.branch.add_catalog_item(35, "Oval Plates", plate50, "Naaman", 80, 20);
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