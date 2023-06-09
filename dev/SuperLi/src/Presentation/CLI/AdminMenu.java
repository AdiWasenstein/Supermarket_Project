package SuperLi.src.Presentation.CLI;
import SuperLi.src.BusinessLogic.*;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.time.LocalDate;

public class AdminMenu extends AMenu {

    private static AdminMenu adminMenu = null;
    StockManagementFacade stockManagementFacade;
    AdminController adminController;
    private AdminMenu() {
        stockManagementFacade = StockManagementFacade.getInstance();
        adminController = AdminController.getInstance();
    }
    public static AdminMenu getInstance() {
        if (adminMenu == null)
            adminMenu = new AdminMenu();
        return adminMenu;
    }
    public void printMenu() {
        System.out.println("Dear admin, what would you like to do?");
        System.out.println("1. Add new branch to system.");
        System.out.println("2. Add new catalog item to system.");
        System.out.println("3. Add new supplier to system.");
        System.out.println("4. Remove catalog item from the system.");
        System.out.println("5. Change catalog item's details.");
        System.out.println("6. Print report.");
        System.out.println("7. Print all suppliers in the system.");
        System.out.println("8. Print all orders were made.");
        System.out.println("0. Actually, I would like to exit menu.");
    }
    public void communicate() {
        boolean run = true;
        while (run) {
            printMenu();
            int option = inputNumber();
            switch (option) {
                case 1 -> addNewBranchToSystem();
                case 2 -> addCatalogItem();
                case 3 -> addNewSupplierToSystem();
                case 4 -> removeCatalogItem();
                case 5 -> changeCatalogItemDetails();
                case 6 -> generateReport();
                case 7 -> printAllSuppliersInSystem();
                case 8 -> printAllOrdersInSystem();
                case 0 -> run = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    public void changeCatalogItemDetails() {
        System.out.println("What would you like to change about the catalog item?");
        System.out.println("1. Sell price.");
        System.out.println("2. Minimum capacity required in a branch.");
        System.out.println("3. Set costumer discount (For specific item / for category).");
        System.out.println("0. Actually, I would like to exit this menu.");
        System.out.print("Please enter your choice: ");
        int choice = inputNumber();
        if (choice == 1 || choice == 2) {
            System.out.print("Enter the item ID: ");
            int id = inputNumber();
            if (id < 0) {
                System.out.println("Invalid ID. Returning to menu...");
                return;
            }
            if (choice == 1)
                changeCostumerPrice(id);
            else
                changeCatalogItemMinCapacity(id);
        } else if (choice == 3)
            setCostumerDiscount();
        else
            System.out.println((choice == 0 ? "" : "Invalid option. ") + "Returning to menu...");
    }
    private int getSupplierId() {
        while (true) {
            try {
                System.out.println("Enter supplier id");
                int id = input.nextInt();
                input.nextLine();
                if (id < 0)
                    throw new InvalidParameterException("Supplier id must be a number");
                if (adminController.isSupplierIdExists(id))
                    throw new InvalidParameterException("Supplier id already exists in system, please try again.");
                return id;
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("Supplier id must be a positive number");
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getSupplierBankAccount() {
        while (true) {
            try {
                System.out.println("Enter bank account");
                String account = input.nextLine();
                if (!(account.matches("[0-9]+")))
                    throw new InvalidParameterException();
                return account;
            } catch (InvalidParameterException e) {
                System.out.println("Bank account number is not valid");
            }
        }
    }
    private String getSupplierName() {
        while (true) {
            try {
                System.out.println("Enter name");
                String name = input.nextLine();
                if (!(name.matches("[a-zA-Z]+")))
                    throw new InvalidParameterException();
                return name;
            } catch (InvalidParameterException e) {
                System.out.println("Name is not valid");
            }
        }
    }
    public String getSupplierAddress() {
        while (true) {
            try {
                System.out.println("Enter address");
                String address = input.nextLine();
                if (address.equals(""))
                    throw new InvalidParameterException();
                return address;
            } catch (InvalidParameterException e) {
                System.out.println("supplier must have an address");
            }
        }
    }
    public PaymentsWays getSupplierPaymentWays() {
        while (true) {
            try {
                System.out.println("Please choose Payment way:");
                System.out.println("1. direct");
                System.out.println("2. plus30");
                System.out.println("3. plus60");
                int pay = input.nextInt();
                input.nextLine();
                if (pay == 1) {
                    return PaymentsWays.direct;
                } else if (pay == 2) {
                    return PaymentsWays.plus30;
                } else if (pay == 3) {
                    return PaymentsWays.plus60;
                } else {
                    System.out.println("please enter a number between 1-3");
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("please enter a number between 1-3");
            }
        }
    }
    public LinkedList<String> getSupplierCategory() {
        LinkedList<String> categories = new LinkedList<>();
        while (true) {
            try {
                System.out.println("Enter category");
                String category = input.nextLine();
                if ((category.matches("[0-9]+")))
                    throw new InvalidParameterException("category is not valid");
                if (categories.contains(category))
                    throw new InvalidParameterException("impossible to add the same category twice.");
                categories.add(category);
                System.out.println("if you want to stop entering categories, please enter 1. else, press anything to continue");
                String choice = input.nextLine();
                if (choice.equals("1"))
                    break;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
        return categories;
    }
    public LinkedList<String> getSupplierManufacturers() {
        LinkedList<String> manufacturers = new LinkedList<>();
        while (true) {
            try {
                System.out.println("Enter manufacturer");
                String manufacturer = input.nextLine();
                if ((manufacturer.matches("[0-9]+")))
                    throw new InvalidParameterException("manufacturer is not valid");
                if (manufacturers.contains(manufacturer))
                    throw new InvalidParameterException("impossible to add the same manufacturer twice.");
                manufacturers.add(manufacturer);
                System.out.println("if you want to stop entering manufacturers, please enter 1. else, press anything to continue");
                String choice = input.nextLine();
                if (choice.equals("1"))
                    break;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
        return manufacturers;
    }
    private boolean isSupplierDelivering() {
        System.out.println("Are you delivering or not?");
        System.out.println("1 - yes, i'm delivering");
        System.out.println("2 - no, i'm not delivering");
        while (true) {
            try {
                int choice = input.nextInt();
                input.nextLine();
                if (choice == 1) {
                    return true;
                } else if (choice == 2) {
                    return false;
                } else {
                    System.out.println("please choose a number between 1-2");
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("please enter a number between 1-2");
            }
        }
    }
    private boolean isSupplierRegularDelivering() {
        System.out.println("Do you supply on regular days?");
        System.out.println("1 - yes");
        System.out.println("2 - no");
        while (true) {
            try {
                int choice = input.nextInt();
                input.nextLine();
                if (choice == 1) {
                    return true;
                } else if (choice == 2) {
                    return false;
                } else {
                    System.out.println("please choose a number between 1-2");
                }
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("please enter a number between 1-2");
            }
        }
    }
    private Day matchStringToDay(String s_day) {
        if (s_day.equalsIgnoreCase("sunday"))
            return Day.sunday;
        else if (s_day.equalsIgnoreCase("monday"))
            return Day.monday;
        else if (s_day.equalsIgnoreCase("tuesday"))
            return Day.tuesday;
        else if (s_day.equalsIgnoreCase("wednesday"))
            return Day.wednesday;
        else if (s_day.equalsIgnoreCase("thursday"))
            return Day.thursday;
        else if (s_day.equalsIgnoreCase("friday"))
            return Day.friday;
        else
            return Day.saturday;
    }
    private LinkedList<Day> chooseDeliveryDays() {
        LinkedList<Day> days = new LinkedList<>();
        LinkedList<String> daysOfTheWeek = new LinkedList<>(Arrays.asList("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"));
        while (true) {
            System.out.println("Please enter day for delivering:");
            String chosenDay = input.nextLine();
            int index = -1;
            for (int i = 0; i < daysOfTheWeek.size(); i++) {
                if (daysOfTheWeek.get(i).equalsIgnoreCase(chosenDay)) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                if (daysOfTheWeek.size() > 0) {
                    System.out.println("Please enter valid day");
                } else {
                    System.out.println("There are no more possible days to enter.");
                    break;
                }
            } else {
                Day day = matchStringToDay(daysOfTheWeek.get(index));
                days.add(day);
                daysOfTheWeek.remove(index);
                System.out.println("if you want to stop entering days of delivering, please enter 1. else, press anything to continue");
                String choice = input.nextLine();
                if (choice.equals("1"))
                    break;
            }
        }
        return days;
    }

    private int getNumberOfDaysToDeliver() {
        while (true) {
            try {
                System.out.println("Enter how many days after ordering you will deliver");
                int numDaysToDeliver = input.nextInt();
                input.nextLine();
                if (numDaysToDeliver < 0)
                    throw new InvalidParameterException();
                return numDaysToDeliver;
            } catch (InputMismatchException e) {
                input.nextLine();
                System.out.println("number of days must be a number");
            } catch (InvalidParameterException e) {
                System.out.println("number of days until supplier delivers must be positive");
            }
        }
    }

    private String getSupplierContactName() {
        System.out.println("Please enter contact's details:");
        System.out.println("Enter contact's name");
        return input.nextLine();
    }

    private String getSupplierContactPhone() {
        System.out.println("Enter contact's phone number");
        return input.nextLine();
    }

    private String getSupplierContactEmail() {
        System.out.println("Enter contact's email address");
        return input.nextLine();
    }

    public void addNewSupplierToSystem() {
        System.out.println("Please enter the next details:");
        int id = getSupplierId();
        String bankAcc = getSupplierBankAccount();
        String name = getSupplierName();
        String address = getSupplierAddress();
        PaymentsWays payment = getSupplierPaymentWays();
        String contactName = getSupplierContactName();
        String contactPhone = getSupplierContactPhone();
        String contactEmail = getSupplierContactEmail();
        LinkedList<String> categories = getSupplierCategory();
        LinkedList<String> manufacturers = getSupplierManufacturers();
        LinkedList<Day> days = null;
        int numDaysToDeliver = -1;
        if (isSupplierDelivering())//delivering
        {
            if (isSupplierRegularDelivering())//delivers regularly
            {
                days = chooseDeliveryDays();
            } else //doesn't deliver regularly
            {
                numDaysToDeliver = getNumberOfDaysToDeliver();
            }
        }
        try
        {
            adminController.addNewSupplier(name, address, id, bankAcc, payment, contactName, contactPhone, contactEmail, categories, manufacturers, days, numDaysToDeliver);
        }
        catch (InvalidParameterException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Details are incorrect, no supplier was added. please try again next time.");
        }
    }

    public void addNewBranchToSystem() {
        System.out.println("Please enter the new branch's address:");
        String address = input.nextLine();
        if(address.equals("")) {
            System.out.println("Invalid Address");
            return;
        }
        if(stockManagementFacade.addBranch(address))
            System.out.format("%s branch was added successfully. Returning to menu...\n", address);
        else
            System.out.println("Failed to add the new branch to the system. Returning to main menu...");
    }
    public void printAllOrdersInSystem() {
        LinkedList<Order> allOrders = adminController.getAllOrdersInSystem();
        for (Order order : allOrders) {
            System.out.println(order.toString());
        }
    }
    public void printAllSuppliersInSystem() {
        LinkedList<Supplier> allSuppliers = adminController.getAllSuppliersInSystem();
        for (Supplier supplier : allSuppliers)
            System.out.println(supplier.allDataOfSupplier());
    }
    public void printAllBranchOrders(int branchNumber) {
        try {
            LinkedList<Order> allBranchOrders = adminController.getAllOrdersOfBranch(branchNumber);
            for (Order order : allBranchOrders)
                System.out.println(order.toString());
        } catch (InvalidParameterException e) {
            System.out.println(e.getMessage());
        }
    }
    public void addCatalogItem() {
        System.out.println("Enter the item's details:");
        System.out.print("ID: ");
        int id = inputNumber();
        if (id < 0) {
            System.out.println("Invalid ID. Returning to menu...");
            return;
        }
        System.out.print("Name: ");
        String name = input.nextLine();
        LinkedList<String> categories = getCatalogItemCategories();
        if (categories.size() == 0) {
            System.out.println("Item required to have at least one category. Returning to menu...");
            return;
        }
        System.out.print("Size: ");
        double size = inputDouble();
        if (size < 0) {
            System.out.println("Invalid size. Returning to menu...");
            return;
        }
        System.out.print("Choose measure units:\n1. Units\n2. MLs\n3. Grams\n4. CMs\nMeasure unit: ");
        int measureNum = inputNumber();
        if (!(1 <= measureNum && measureNum <= 4)) {
            System.out.println("Invalid Measure unit choice. Returning to menu...");
            return;
        }
        measureNum--;
        System.out.print("Manufacturer: ");
        String manufacturer = input.nextLine();
        System.out.print("Costumer Price: ");
        double price = inputDouble();
        if (price < 0) {
            System.out.println("Invalid price. Returning to menu...");
            return;
        }
        System.out.print("Minimum Capacity: ");
        int minCapacity = inputNumber();
        if (minCapacity < 0) {
            System.out.println("Invalid price. Returning to menu...");
            return;
        }
        System.out.print("Shelf life: (0 if want's default)");
        int shelfLife = inputNumber();
        if (shelfLife < 0) {
            System.out.println("Invalid shelf life. Returning to menu...");
            return;
        }
        if (!(shelfLife == 0 ? stockManagementFacade.addCatalogItem(id, name, manufacturer, price, minCapacity, categories, size, measureNum) :
                stockManagementFacade.addCatalogItem(id, name, manufacturer, price, minCapacity, categories, size, measureNum, shelfLife)))
            System.out.println("Failed to add item to catalog. Returning to menu...");
        else
            System.out.println("Adding to the catalog completed successfully. Returning to menu...");
    }
    public void removeCatalogItem() {
        System.out.print("Enter the item ID: ");
        int id = inputNumber();
        if (id < 0 || !stockManagementFacade.removeCatalogItem(id))
            System.out.println("Invalid ID. Returning to menu...");
        else
            System.out.println("Removing item from the catalog completed successfully. Returning to the menu...");
    }
    public void changeCostumerPrice(int catalogId) {
        System.out.print("Enter the new sell price: ");
        double costumerPrice = inputDouble();
        if (costumerPrice < 0) {
            System.out.println("Invalid sell price. Returning to menu...");
            return;
        }
        if (!stockManagementFacade.setSellPrice(catalogId, costumerPrice)) {
            System.out.print("Invalid item ID. Returning to menu...");
        }
        System.out.format("Catalog item %s's sell price changed to %.1fILS successfully. Returning to menu...\n", stockManagementFacade.getCatalogIdName(catalogId), costumerPrice);
    }
    public void changeCatalogItemMinCapacity(int catalogId) {
        System.out.print("Enter the new minimum capacity: ");
        int minCapacity = inputNumber();
        if (minCapacity < 0) {
            System.out.println("Invalid capacity. Returning to menu...");
            return;
        }
        if (!stockManagementFacade.setMinCapacity(catalogId, minCapacity)) {
            System.out.print("Invalid item ID. Returning to menu...");
        }
        System.out.format("Catalog item %s's minimum capacity changed to %d successfully. Returning to menu...\n", stockManagementFacade.getCatalogIdName(catalogId), minCapacity);
    }

    public void setCostumerDiscount() {
        System.out.println("Please enter costumer discount details:");
        System.out.println("Expiration date (Format: d/M/yy): ");
        LocalDate expirationDate = inputDate();
        if (expirationDate == null || expirationDate.isBefore(LocalDate.now())) {
            System.out.println("Invalid date. Returning to menu...");
            return;
        }
        System.out.print("Discount value: ");
        double value = inputDouble();
        if (value < 0) {
            System.out.println("Invalid value. Returning to menu...");
            return;
        }
        System.out.print("Enter value type:\n1. amount\n2. percentage\nValue type: ");
        int valueType = inputNumber();
        if (!(1 <= valueType && valueType <= 2)) {
            System.out.println("Invalid value type. Returning to main menu...");
            return;
        }
        boolean isPercentage = valueType == 2;
        System.out.print("Enter minimum amount of products: ");
        int minAmount = inputNumber();
        if (minAmount < 0) {
            System.out.println("Invalid amount. Returning to main menu...");
            return;
        }
        System.out.print("What type of discount would you like to apply:\n1. Catalog Item Discount\n2. Category Discount\nDiscount Type: ");
        int discountType = inputNumber();
        switch (discountType) {
            case (1) -> {
                System.out.print("Enter item's ID: ");
                int id = inputNumber();
                if (stockManagementFacade.setCatalogItemCostumerDiscount(id, expirationDate, value, isPercentage, minAmount))
                    System.out.format("Catalog item %s's costumer discount added successfully. Returning to menu...\n", stockManagementFacade.getCatalogIdName(id));
                else
                    System.out.println("Invalid ID. returning to menu...");
            }
            case (2) -> {
                System.out.println("Please enter category details:");
                LinkedList<String> categories = getCatalogItemCategories();
                if (categories.size() == 0) {
                    System.out.println("Item required to have at least one category. Returning to menu...");
                    return;
                }
                System.out.print("Size: ");
                double size = inputDouble();
                if (size < 0) {
                    System.out.println("Invalid size. Returning to main menu...");
                    break;
                }
                System.out.print("Choose measure units:\n1. Units\n2. MLs\n3. Grams\n4. CMs\nMeasure unit: ");
                int measureNum = inputNumber();
                if (!(1 <= measureNum && measureNum <= 4)) {
                    System.out.println("Invalid Measure unit choice. Returning to main menu...");
                    return;
                }
                measureNum--;
                stockManagementFacade.setCategoryDiscount(categories, size, measureNum, expirationDate, value, isPercentage, minAmount);
                System.out.println("The discount was applied to all the items from the category");
            }
            default -> System.out.println("Invalid option. Returning to main menu...");
        }
    }
    public LinkedList<String> getCatalogItemCategories() {
        String category;
        LinkedList<String> categories = new LinkedList<>();
        while (true) {
            System.out.print("Please enter item's category name (0 to stop): ");
            category = input.nextLine();
            if (category.equals("0"))
                break;
            categories.add(category);
        }
        return categories;
    }
    public int getCatalogReport() {
        System.out.println("1. Category Report");
        System.out.println("2. Catalog Report");
        System.out.println("3. Specific Branch Reports");
        System.out.print("Please enter your option: ");
        return inputNumber();
    }
    public void generateReport() {
        ReportViewer reportViewer = ReportViewer.getInstance();
        switch (getCatalogReport()) {
            case 1 -> {
                LinkedList<LinkedList<String>> categoriesList = new LinkedList<>();
                while (true){
                    System.out.format("Category %d's details:\n", categoriesList.size() + 1);
                    LinkedList<String> categories = getCatalogItemCategories();
                    if (categories.size() == 0)
                        break;
                    categoriesList.add(categories);
                }
                reportViewer.generateCategoryReport(categoriesList);
            }
            case 2 -> reportViewer.generateAllCatalogReport();
            case 3 -> generateSpecificBranchReports(reportViewer);
            default -> System.out.println("Invalid option. Returning to main...");
        }
    }
    public int getBranchReport(){
        System.out.println("1. Stock Item Report");
        System.out.println("2. Required Stock Report");
        System.out.println("3. Damaged Stock Report");
        System.out.println("4. All orders were made for specific branch.");
        System.out.print("Please enter your option: "); return inputNumber();
    }
    public void generateSpecificBranchReports(ReportViewer reportViewer){
        int branchId = getBranchID();
        if(branchId < 0)
            return;
        switch (getBranchReport()){
            case 1 -> reportViewer.generateStockItemReport(branchId);
            case 2 -> reportViewer.generateRequiredStockReport(branchId);
            case 3 -> reportViewer.generateDamagedReport(branchId);
            case 4 -> printAllBranchOrders(branchId);
            default -> System.out.println("Invalid option. Returning to main...");
        }
    }
    public static void main(String[] args){
        AdminMenu.getInstance().communicate();
    }
}
