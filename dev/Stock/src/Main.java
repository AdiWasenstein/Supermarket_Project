package Stock.src;

import java.time.LocalDate;

public class Main {
    static Category milk1000 = new Category("Dairy", "Milk", new Size(1000, MeasureUnit.ML));
    static Category milk1500 = new Category("Dairy", "Milk", new Size(1500, MeasureUnit.ML));
    static Category cottage250 = new Category("Dairy", "Cheese", new Size(250, MeasureUnit.GM));
    static Category gouda100 = new Category("Dairy", "Cheese", new Size(100, MeasureUnit.GM));
    static Category pudding250 = new Category("Dairy", "Puddings", new Size(250, MeasureUnit.GM));
    static Category shampoo750 = new Category("Toiletries", "Shampoo", new Size(750, MeasureUnit.ML));
    static Category hand_soap500 = new Category("Toiletries", "Hand Soap", new Size(500, MeasureUnit.ML));
    static Category pan50 = new Category("Kitchen", "Kitchen Tools", new Size(50, MeasureUnit.CM));
    static Category fork100 = new Category("Kitchen", "Cutlery", new Size(100, MeasureUnit.UNIT));
    static Category cap100 = new Category("Kitchenware", "Caps", new Size(100, MeasureUnit.UNIT));
    static Category plate50 = new Category("Kitchenware", "Plates", new Size(50, MeasureUnit.UNIT));
    static Category rice1000 = new Category("Legumes", "Rice", new Size(1000, MeasureUnit.GM));
    static Category pasta1000 = new Category("Legumes", "Pasta", new Size(1000, MeasureUnit.GM));
    static Category pita10 = new Category("Breads", "Pitas", new Size(10, MeasureUnit.UNIT));
    static Category rice_cake500 = new Category("Breads", "Rice Cakes", new Size(500, MeasureUnit.GM));
    static Category white_bread500 = new Category("Breads", "White Bread", new Size(500, MeasureUnit.GM));
    static Category red_wine750 = new Category("Beverage", "Alcohol", new Size(750, MeasureUnit.ML));
    static Category soft_drink1500 = new Category("Beverage", "Soft Drinks", new Size(1500, MeasureUnit.ML));
    static Category cake_mold21 = new Category("Baking", "Disposable", new Size(21, MeasureUnit.CM));

    public static void main(String[] args) {
        UserMenu us = new UserMenu();
        if (us.initialize_data() == 1) {
            initialize_Catalog(us);
            for (int i = 0; i < 15; i++)
                initialize_Items(us, i);
            initialize_discount(us);
        }
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
    us.branch.add_catalog_item(30, "Pnina Hand Soap", hand_soap500, "Pnina Rosenblum", 12, 80);
    us.branch.add_catalog_item(31, "Pan 21cm", pan50, "Teffal", 200, 30);
    us.branch.add_catalog_item(32, "Fork 100 units", fork100, "Namal", 11, 100);
    us.branch.add_catalog_item(33, "Hot cups 100 units", cap100, "Namal", 21.3, 176);
    us.branch.add_catalog_item(34, "Fanta", soft_drink1500, "Coca Cola", 8.7, 200);
    us.branch.add_catalog_item(35, "Oval Plates", plate50, "Naaman", 80, 20);
    }
    public static void initialize_Items(UserMenu us, int i){
        switch (i % 4){
            case(0) -> {
                us.branch.add_item(10, 3, LocalDate.of(2023, 4, 11), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(11, 5.8, LocalDate.of(2024, 4, 29), i % 7 == 1 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(12, 3.7, LocalDate.of(2023, 4, 18), i % 7 == 2 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(13, 3, LocalDate.of(2023, 4, 3), i % 7 == 3 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(14, 4.8, LocalDate.of(2023, 4, 20), i % 7 == 4 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(15, 9, LocalDate.of(2024, 2, 19), i % 7 == 5 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(16, 3, LocalDate.of(2023, 4, 17), i % 7 == 6 ? DamageType.values()[i % 5] : DamageType.NONE);
            }
            case(1) -> {
                us.branch.add_item(17, 2.15, LocalDate.of(2023,4,19), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(18, 4, LocalDate.of(2023,4,8), i % 7 == 1 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(19, 9.66, LocalDate.of(2024,6,4), i % 7 == 2 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(20, 4.6, LocalDate.of(2025,12,31), i % 7 == 3 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(21, 7, LocalDate.of(2030,1,1), i % 7 == 4 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(22, 3.8, LocalDate.of(2023,4,25), i % 7 == 5 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(23, 9, LocalDate.of(2030,12,31), i % 7 == 6 ? DamageType.values()[i % 5] : DamageType.NONE);
            }
            case(2) -> {
                us.branch.add_item(24, 25, LocalDate.of(2023,12,17), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(25, 6, LocalDate.of(2090,1,1), i % 7 == 1 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(26, 3, LocalDate.of(2023,4,13), i % 7 == 2 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(27, 4.6, LocalDate.of(2025,1,30), i % 7 == 3 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(28, 25, LocalDate.of(2025,12,31), i % 7 == 4 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(29, 1.9, LocalDate.of(2050,8,27), i % 7 == 5 ? DamageType.values()[i % 5] : DamageType.NONE);
            }
            case(3) -> {
                us.branch.add_item(30, 6.5, LocalDate.of(2028,6,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(31, 90, LocalDate.of(2000,1,1), i % 7 == 1 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(32, 4.2, LocalDate.of(2030,1,1), i % 7 == 2 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(33, 2, LocalDate.of(2050,8,29), i % 7 == 3 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(34, 3.4, LocalDate.of(2025,1,1), i % 7 == 4 ? DamageType.values()[i % 5] : DamageType.NONE);
                us.branch.add_item(35, 20, LocalDate.of(2050,1,1), i % 7 == 5 ? DamageType.values()[i % 5] : DamageType.NONE);
            }
        }
    }
    public static void initialize_discount(UserMenu us){
        us.branch.set_category_discount(white_bread500, new Discount(LocalDate.of(2023,10,1), 50, true, 2));
        us.branch.set_item_discount(10, new Discount(LocalDate.of(2023,8,1), 3, false, 2));
        us.branch.set_item_discount(34, new Discount(LocalDate.of(2023,5,1), 30, true, 0));
        us.branch.set_item_discount(35, new Discount(LocalDate.of(2023,4,1), 40, true, 4));
    }
}