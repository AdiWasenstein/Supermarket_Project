package SuperLi.src;
//import SuperLi.src.BusinessLogic.CatalogItem;
//import SuperLi.src.BusinessLogic.StockItem;
//import SuperLi.src.DataAccess.CatalogItemDataMapper;
//import SuperLi.src.DataAccess.StockItemDataMapper;
import SuperLi.src.Presentation.MainMenu;
//import SuperLi.src.Presentation.UserMenu;
//import java.util.LinkedList;
//import SuperLi.src.BusinessLogic.*;
//import java.time.LocalDate;
//
public class Main {
//    static int counter = 1;
//     static Category milk1000 = new Category(convert(new String[]{"Dairy Products", "Milk"}), new Size(1000, MeasureUnit.ML));
//    static Category milk1500 = new Category(convert(new String[]{"Dairy Products", "Milk"}), new Size(1500, MeasureUnit.ML));
//    static Category cottage250 = new Category(convert(new String[]{"Dairy Products", "Cheese"}), new Size(250, MeasureUnit.GM));
//    static Category gouda100 = new Category(convert(new String[]{"Dairy Products", "Cheese"}), new Size(100, MeasureUnit.GM));
//    static Category pudding250 = new Category(convert(new String[]{"Dairy Products", "Puddings"}), new Size(250, MeasureUnit.GM));
//    static Category shampoo750 = new Category(convert(new String[]{"Toiletries", "Shampoo"}), new Size(750, MeasureUnit.ML));
//    static  Category hand_soap500 = new Category(convert(new String[]{"Toiletries", "Hand Soap"}), new Size(500, MeasureUnit.ML));
//    static  Category pan50 = new Category(convert(new String[]{"Kitchenware", "Kitchen Tools"}), new Size(50, MeasureUnit.CM));
//    static  Category fork100 = new Category(convert(new String[]{"Kitchenware", "Cutlery"}), new Size(100, MeasureUnit.UNIT));
//    static   Category cap100 = new Category(convert(new String[]{"Kitchenware", "Caps"}), new Size(100, MeasureUnit.UNIT));
//    static Category plate50 = new Category(convert(new String[]{"Kitchenware", "Plates"}), new Size(50, MeasureUnit.UNIT));
//    static Category rice1000 = new Category(convert(new String[]{"Legumes", "Rice"}), new Size(1000, MeasureUnit.GM));
//    static  Category pasta1000 = new Category(convert(new String[]{"Legumes", "Pasta"}), new Size(1000, MeasureUnit.GM));
//    static  Category pita10 = new Category(convert(new String[]{"Breads", "Pitas"}), new Size(10, MeasureUnit.UNIT));
//    static Category rice_cake500 = new Category(convert(new String[]{"Breads", "Rice Cakes"}), new Size(500, MeasureUnit.GM));
//    static  Category white_bread500 = new Category(convert(new String[]{"Breads", "White Bread"}), new Size(500, MeasureUnit.GM));
//    static  Category red_wine750 = new Category(convert(new String[]{"Beverage", "Alcohol"}), new Size(750, MeasureUnit.ML));
//    static  Category soft_drink1500 = new Category(convert(new String[]{"Beverage", "Soft Drinks"}), new Size(1500, MeasureUnit.ML));
//    static Category cake_mold21 = new Category(convert(new String[]{"Baking", "Disposable"}), new Size(21, MeasureUnit.CM));
//
    public static void main(String[] args) {
////        OrderController.getInstance().runEveryDayToMakeOrders();//this func has to be executed in main so it will run every day automatically.
        MainMenu.getInstance().communicate();
//        UserMenu us = new UserMenu();
////        if (us.initialize_data() == 1) {
//        initialize_Catalog(us);
//            for (int i = 0; i < 15; i++)
//                initialize_Items(us, i);
//            initialize_discount(us);
////        }
////        us.communicate();// Labohen
    }
//
//    public static void initialize_Catalog(UserMenu us){
//    CatalogItemDataMapper catalogItemDataMapper = CatalogItemDataMapper.getInstance();
//    LinkedList<CatalogItem> all = new LinkedList<>();
//    all.add(new CatalogItem(10, "Milk 3%", "Tnuva", 9.9, 95, milk1500, 122, 1881));
//    all.add(new CatalogItem(11, "Milk 5%", "Tnuva", 5.9, 101, milk1500, 123, 1882));
//    all.add(new CatalogItem(12, "Milk 3%", "Tnuva", 5.9, 101, milk1000, 124, 1883));
//    all.add(new CatalogItem(13, "Milk 5%", "Tnuva", 9.9, 95, milk1000, 125, 1884));
//    all.add(new CatalogItem(14, "Milk 2%", "Tnuva", 9.9, 72, milk1500, 126, 1885));
//    all.add(new CatalogItem(15, "Soy Milk", "BIO", 15.9, 50, milk1000, 555, 1001));
//    all.add(new CatalogItem(16, "cottage 9%", "Tara", 6.4, 85, cottage250, 321, 1765));
//    all.add(new CatalogItem(17, "Yolo", "Tnuva", 5, 50, pudding250, 3, 1277));
//    all.add(new CatalogItem(18, "Milky", "Tnuva", 5.9, 60, pudding250, 44, 1657));
//    all.add(new CatalogItem(19, "Gouda", "Emek", 919, 94, gouda100, 872, 1801));
//    all.add(new CatalogItem(20, "Pita", "Angle", 11, 50, pita10, 440, 1455));
//    all.add(new CatalogItem(21, "Rice Cakes", "Fitness", 17, 50, rice_cake500, 1, 1633));
//    all.add(new CatalogItem(22, "White Bread", "Berman", 7, 60, white_bread500, 985, 1998));
//    all.add(new CatalogItem(23, "Head & Shoulders", "Procter & Gamble", 17, 50, shampoo750, 102, 1780));
//    all.add(new CatalogItem(24, "Red Wine", "Gamla", 60, 30, red_wine750, 408, 1377));
//    all.add(new CatalogItem(25, "Cake Mold", "Namal", 11.9, 40, cake_mold21, 134, 1299));
//    all.add(new CatalogItem(26, "Cottage 5%", "Tnuva", 6.4, 200, cottage250, 252, 1886));
//    all.add(new CatalogItem(27, "Cottage 12%", "Tnuva", 6.4, 200, cottage250, 253, 1887));
//    all.add(new CatalogItem(28, "Persian rice", "Sugat", 11.5, 80, rice1000, 300, 1919));
//    all.add(new CatalogItem(29, "Penne pasta", "Rummo", 5.9, 120, pasta1000, 345, 1212));
//    all.add(new CatalogItem(30, "Pnina Hand Soap", "Pnina Rosenblum",  12, 80, hand_soap500, 346, 1666));
//    all.add(new CatalogItem(31, "Pan 21cm", "Teffal",  12, 80, pan50, 349, 1776));
//    all.add(new CatalogItem(32, "Fork 100 units", "Namal", 11, 100, fork100, 277, 1090));
//    all.add(new CatalogItem(33, "Hot cups 100 units", "Namal", 21.3, 176, cap100, 762, 1332));
//    all.add(new CatalogItem(34, "Fanta", "Coca Cola", 8.7, 200, soft_drink1500, 890, 1002));
//    all.add(new CatalogItem(35, "Oval Plates", "Naaman", 80, 20, plate50, 599, 1010));
//
//    for(CatalogItem catalogItem : all)
//        catalogItemDataMapper.insert(catalogItem);
//    }
//    public static void initialize_Items(UserMenu us, int i){
//        StockItemDataMapper stockItemDataMapper = StockItemDataMapper.getInstance();
//        CatalogItemDataMapper catalogItemDataMapper = CatalogItemDataMapper.getInstance();
//        int x = i % 4;
//        switch (x){
//            case(0) -> {
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("10").get(),counter++, 3, LocalDate.of(2023, 4, 11), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("10").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("11").get(),counter++, 5.8, LocalDate.of(2024, 4, 29), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("11").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("12").get(),counter++, 3.7, LocalDate.of(2023, 4, 18), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("12").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("13").get(),counter++, 3, LocalDate.of(2023, 4, 3), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("13").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("14").get(),counter++, 4.8, LocalDate.of(2023, 4, 20), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("14").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("15").get(),counter++, 9, LocalDate.of(2024, 2, 19), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("15").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("16").get(),counter++, 3, LocalDate.of(2023, 4, 17), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("16").get().getShelvesLocation()));
//            }
//            case(1) -> {
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("17").get(),counter++, 2.15, LocalDate.of(2023,4,19), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("17").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("18").get(),counter++, 4, LocalDate.of(2023,4,8), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("18").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("19").get(),counter++, 9.66, LocalDate.of(2024,6,4), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("19").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("20").get(),counter++, 4.6, LocalDate.of(2025,12,31), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("20").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("21").get(),counter++, 7, LocalDate.of(2030,1,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("21").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("22").get(),counter++, 3.8, LocalDate.of(2023,4,25), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("22").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("23").get(),counter++, 9, LocalDate.of(2030,12,31), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("23").get().getShelvesLocation()));
//            }
//            case(2) -> {
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("24").get(),counter++, 25, LocalDate.of(2023,12,17), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("24").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("25").get(),counter++, 6, LocalDate.of(2090,1,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("25").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("26").get(),counter++, 3, LocalDate.of(2023,4,13), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("26").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("27").get(),counter++, 4.6, LocalDate.of(2025,1,30), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("27").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("28").get(),counter++, 25, LocalDate.of(2025,12,31), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("28").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("29").get(),counter++, 1.9, LocalDate.of(2050,8,27), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("29").get().getShelvesLocation()));
//            }
//            case(3) -> {
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("30").get(),counter++, 6.5, LocalDate.of(2028,6,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("30").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("31").get(),counter++, 90, LocalDate.of(2000,1,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("31").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("32").get(),counter++, 4.2, LocalDate.of(2030,1,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("32").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("33").get(),counter++, 2, LocalDate.of(2050,8,29), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("33").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("34").get(),counter++, 2, LocalDate.of(2050,8,29), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("34").get().getShelvesLocation()));
//                stockItemDataMapper.insert(new StockItem(catalogItemDataMapper.find("35").get(),counter++, 20, LocalDate.of(2050,1,1), i % 7 == 0 ? DamageType.values()[i % 5] : DamageType.NONE, 1, catalogItemDataMapper.find("35").get().getShelvesLocation()));
//            }
//        }
//    }
//    public static void initialize_discount(UserMenu us){
//        StockManagementFacade.getInstance().setCategoryDiscount(milk1000.getCategories(), milk1000.getSize().get_amount(), milk1000.getMeasureUnit().ordinal(),LocalDate.of(2023,10,1), 50, true, 2);
//        StockManagementFacade.getInstance().setCatalogItemCostumerDiscount(10, LocalDate.of(2023,8,1), 3, false, 2);
//        StockManagementFacade.getInstance().setCatalogItemCostumerDiscount(34, LocalDate.of(2023,5,1), 30, true, 0);
//        StockManagementFacade.getInstance().setCatalogItemCostumerDiscount(35, LocalDate.of(2023,4,1), 40, true, 4);
//    }
//    public static LinkedList<String> convert(String[] catagories){
//        LinkedList<String> temp = new LinkedList<>();
//        for (String catagory : catagories)
//            temp.add(catagory);
//        return temp;
//    }
}