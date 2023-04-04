package Stock.src;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class UserMenu {
    static Scanner input = new Scanner(System.in);
    Branch branch;

    public int input_number(){
        int num;
        try{
            num = input.nextInt();
        }
        catch(Exception e) {
            num = -1;
        }
        input.nextLine();
        return num;
    }
    public double input_double(){
        double num;
        try{
            num = input.nextDouble();
        }
        catch(Exception e){
            return -1;
        }
        input.nextLine();
        return num;
    }
    public LocalDate input_date(){
        String date = input.nextLine();
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            return null;
        }
    }
    private void print_menu(){
        System.out.println("What do you want to do?");
        System.out.println("1. Add item to the catalog");
        System.out.println("2. Remove item from the catalog");
        System.out.println("3. Add item to stock");
        System.out.println("4. Remove item from stock");
        System.out.println("5. Generate Report");
        System.out.println("6. Set price for an item");
        System.out.println("7. Set minimal capacity for an item");
        System.out.println("8. Set discount");
        System.out.println("9. Change Item location");
        System.out.println("0. Exit");
    }
    public void add_to_catalog(){
        System.out.println("Enter the item's details:");
        System.out.print("ID: "); int id = input_number();
        if(id < 0){
            System.out.println("Invalid ID. Returning to main menu...");
            return;
        }
        if(branch.contains_id(id)){
            System.out.println("ID already exist. Returning to main menu...");
            return;
        }
        System.out.print("Name: "); String name = input.nextLine();
        System.out.print("Prime category: "); String prime = input.nextLine();
        System.out.print("Sub category: "); String sub = input.nextLine();
        System.out.print("Size: "); double size = input_double();
        if(size < 0){
            System.out.println("Invalid size. Returning to main menu...");
            return;
        }
        System.out.print("Choose measure units:\n1. Units\n2. MLs\n3. Grams\n4. CMs\nMeasure unit: "); int measure_num = input_number();
        if(!(1 <= measure_num && measure_num <= 4)){
            System.out.println("Invalid Measure unit choice. Returning to main menu...");
            return;
        }
        MeasureUnit measure_unit = MeasureUnit.values()[measure_num - 1];
        System.out.print("Manufacturer: "); String manufacturer = input.nextLine();
        System.out.print("Costumer Price: "); double price = input_double();
        if(price < 0){
            System.out.println("Invalid price. Returning to main menu...");
            return;
        }
        System.out.print("Minimum Capacity: "); int min_capacity = input_number();
        if(min_capacity < 0){
            System.out.println("Invalid price. Returning to main menu...");
            return;
        }
        branch.add_catalog_item(id, name, new Category(prime, sub, new Size(size, measure_unit)), manufacturer, price, min_capacity);
        System.out.println("Adding to the catalog completed successfully");
    }

    public void remove_from_catalog(){
        System.out.print("Enter the item ID: "); int id = input_number();
        if(!branch.remove_catalog_item(id))
            System.out.println("Invalid ID. Returning to main menu...");
        else
            System.out.println("Removing item from the catalog completed successfully. Returning to the main menu...");
    }

    public void add_item_to_stock(){
        System.out.println("Enter the item's details:");
        System.out.print("ID: "); int id = input_number();
        if(!branch.contains_id(id)){
            System.out.println("Invalid ID. Returning to main menu...");
            return;
        }
        System.out.print("Cost price: "); double cost_price = input_double();
        if(cost_price < 0){
            System.out.println("Invalid price. Returning to main menu...");
            return;
        }
        System.out.println("Expiration date (Format: d/M/yy:) "); LocalDate date = input_date();
        if(date == null){
            System.out.println("Invalid date. Returning to main menu...");
            return;
        }
        System.out.print("Choose damage type:\n1. Cover\n2. PHYSICAL\n3. ELECTRICAL\n4. ROTTEN\n5. None\nDamage type: "); int damage_type = input_number();
        DamageType type = DamageType.NONE;
        if (!(damage_type >= 1 && damage_type <= 5)){
            System.out.println("Invalid Damage Type choice. Returning to main menu...");
            return;
        }
        switch (damage_type) {
            case (1) -> type = DamageType.COVER;
            case (2) -> type = DamageType.PHYSICAL;
            case (3) -> type = DamageType.ELECTRICAL;
            case (4) -> type = DamageType.ROTTEN;
            case (5) -> type = DamageType.NONE;
        }
        System.out.print("Choose where to put the item:\n1. Back storage\n2. Shelves\n3. Dont mind\nChoice: "); int item_locate = input_number();
        if (!(item_locate >= 1 && item_locate <= 3)){
            System.out.println("Invalid Item Location choice. Returning to main menu...");
            return;
        }
        int barcode = -1;
        switch (item_locate) {
            case (1) -> barcode = branch.add_item(id, cost_price, date, type, false);
            case (2) -> barcode = branch.add_item(id, cost_price, date, type, true);
            case (3) -> barcode = branch.add_item(id, cost_price, date, type);
        }
        if(barcode < 1)
            System.out.println("Error at adding to stock");
        System.out.format("Adding to the stock completed successfully. Item's barcode is %d. Returning to main menu...", barcode);
    }
    public void remove_from_stock(){
        System.out.print("Enter the barcode: "); int barcode = input_number();
        int id = branch.barcode_to_id(barcode);
        if(!branch.remove_item(barcode)) {
            System.out.println("Invalid barcode. Returning to main menu...");
            return;
        }
        CatalogItem catalog_item = branch.get_catalog_from_barcode(id);
        if(catalog_item.get_min_capacity() == catalog_item.get_total_amount() + 1)
            System.out.println("THE ITEM CROSSED THE MINIMUM GAP, CONSIDER ORDERING MORE UNITS IMMEDIATELY");
        System.out.println("Removing from the stock completed successfully. Returning to main menu...");
    }
    public void generate_report(){
        System.out.print("Choose what type of report would you want?\n1. Catalog\n2. Stock\n3. Damage\n4. Current Items\n Report type: "); int report_type = input_number();
        switch (report_type){
            case (1) -> category_report();
            case (2) -> branch.generate_stock_report();
            case (3) -> branch.generate_damaged_report();
            case (4) -> branch.generate_all_items_report();
            default -> System.out.println("Invalid choice. Returning to main menu...");
        }
    }
    public void category_report(){
        boolean run = true;
        ArrayList<String> primes = new ArrayList<>();
        ArrayList<String[]> prime_subs = new ArrayList<>();
        ArrayList<Category> fulls = new ArrayList<>();
        String prime; String sub;
        while (run){
            System.out.print("What type of category would you like to add?\n1. Prime\n2. Prime+Sub\n3. Full Category\n0. END \n"); int category_type = input_number();
            switch (category_type) {
                case (0) -> run = false;
                case (1) -> {
                    System.out.print("Prime: ");
                    primes.add(input.nextLine());
                }
                case (2) -> {
                    System.out.print("Prime: ");
                    prime = input.nextLine();
                    System.out.print("Sub: ");
                    sub = input.nextLine();
                    String[] prime_sub = {prime, sub};
                    prime_subs.add(prime_sub);
                }
                case (3) -> {
                    System.out.print("Prime: ");
                    prime = input.nextLine();
                    System.out.print("Sub: ");
                    sub = input.nextLine();
                    System.out.print("Size: ");
                    double size = input_double();
                    if (size < 0) {
                        System.out.println("Invalid size. Please enter choice again.");
                        break;
                    }
                    System.out.print("Choose measure units:\n1. Units\n2. MLs\n3. Grams\n4. CMs\nMeasure unit: ");
                    int measure_num = input_number();
                    if (!(1 <= measure_num && measure_num <= 4)) {
                        System.out.println("Invalid Measure unit choice. Please enter choice again.");
                        break;
                    }
                    MeasureUnit measure_unit = MeasureUnit.values()[measure_num - 1];
                    fulls.add(new Category(prime, sub, new Size(size, measure_unit)));
                }
            }
        }
        branch.generate_category_report(primes, prime_subs, fulls);
    }
    public void set_price(){
        System.out.print("Enter the item ID: "); int id = input_number();
        if(!branch.contains_id(id)){
            System.out.println("Invalid ID. Returning to main menu...");
            return;
        }
        System.out.print("Enter the new price: "); double price = input_double();
        if(price < 0){
            System.out.println("Invalid price. Returning to main menu...");
            return;
        }
        if (!this.branch.set_item_price(id, price)){
            System.out.print("Invalid item ID. Returning to main menu...");
        }
    }

    public void set_capacity(){
        System.out.print("Enter the item ID: "); int id = input_number();
        if(!branch.contains_id(id)){
            System.out.println("Invalid ID. Returning to main menu...");
            return;
        }
        System.out.print("Enter the new capacity: "); int amount = input_number();
        if(amount < 0){
            System.out.println("Invalid amount. Returning to main menu...");
            return;
        }
        if (!this.branch.set_item_capacity(id, amount)){
            System.out.print("Invalid item ID");
        }
    }
    public void set_discount(){
        System.out.println("Please enter discount details:");
        System.out.println("Expiration date (Format: d/M/yy): "); LocalDate date = input_date();
        if(date == null || date.isBefore(LocalDate.now())){
            System.out.println("Invalid date. Returning to main menu...");
            return;
        }
        System.out.print("Discount value: "); double value = input_double();
        if(value < 0) {
            System.out.println("Invalid value. Returning to main menu...");
            return;
        }
        System.out.print("Enter value type:\n1. amount\n2. percentage\nValue type: "); int value_type = input_number();
        if(!(1<= value_type && value_type <= 2)){
            System.out.println("Invalid value type. Returning to main menu...");
            return;
        }
        System.out.print("Enter minimum amount of products: "); int min_amount = input_number();
        if(min_amount < 0){
            System.out.println("Invalid amount. Returning to main menu...");
            return;
        }
        Discount discount = new Discount(date, value, value_type == 2, min_amount);
        System.out.print("What type of discount would you like to apply:\n1. Item discount\n2. Category discount\nDiscount Type: "); int discount_type = input_number();
        switch(discount_type){
            case(1) -> {
                System.out.print("Enter item's ID: "); int id = input_number();
                if(!branch.contains_id(id)){
                    System.out.println("Invalid ID. Returning to main menu..."); return;
                }
                branch.set_item_discount(id, discount);
            }
            case (2) -> {
                System.out.println("Please enter category details:");
                System.out.print("Prime: ");
                String prime = input.nextLine();
                System.out.print("Sub: ");
                String sub = input.nextLine();
                System.out.print("Size: ");
                double size = input_double();
                if (size < 0) {
                    System.out.println("Invalid size. Returning to main menu...");
                    break;
                }
                System.out.print("Choose measure units:\n1. Units\n2. MLs\n3. Grams\n4. CMs\nMeasure unit: ");
                int measure_num = input_number();
                if (!(1 <= measure_num && measure_num <= 4)) {
                    System.out.println("Invalid Measure unit choice. Returning to main menu...");
                    return;
                }
                MeasureUnit measure_unit = MeasureUnit.values()[measure_num - 1];
                this.branch.set_category_discount(new Category(prime, sub, new Size(size, measure_unit)), discount);
            }
        }
    }
    public void change_location(){
        System.out.print("Enter the item barcode"); int barcode = input_number();
        if (!this.branch.transfer(barcode)){
            System.out.print("Invalid item barcode. Returning to main menu...");
        }
        else
            System.out.println("Changing the item location completed successfully. Returning to main menu...");
    }
    public UserMenu(){
        System.out.print("Welcome to the system, please enter your branch's address: ");
        this.branch = new Branch(input.nextLine());
    }

    public void communicate(){
        boolean run = true;
        while(run){
            print_menu();
            System.out.print("Please enter your option: ");
            int user_digit = input_number();
            switch (user_digit){
                case(0) -> run = false;
                case(1) -> add_to_catalog();
                case(2) -> remove_from_catalog();
                case(3) -> add_item_to_stock();
                case(4) -> remove_from_stock();
                case(5) -> generate_report();
                case(6) -> set_price();
                case(7) -> set_capacity();
                case(8) -> set_discount();
                case(9) -> change_location();
                case(-1) -> System.out.println("Invalid option");
            }
        }
        System.out.format("Thank you for using %s's system", branch.get_address());
    }
}