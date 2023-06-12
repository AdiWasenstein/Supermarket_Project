package SuperLi.src.Presentation.CLI;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

import SuperLi.src.BusinessLogic.*;

public class StockKeeperMenu extends AMenu{
    StockManagementFacade stockManagementFacade;
    OrderController orderController;
    int branchId;
    static StockKeeperMenu stockKeeperMenu = null;
    private StockKeeperMenu(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
        orderController = OrderController.getInstance();
        this.branchId = branchId;
    }
    public static StockKeeperMenu getInstance(int branchId){
        if(stockKeeperMenu == null)
            stockKeeperMenu = new StockKeeperMenu(branchId);
        else
            stockKeeperMenu.branchId = branchId;
        return stockKeeperMenu;
    }
    public void printMenu(){
        System.out.format("Welcome %s's stock-keeper!\n", stockManagementFacade.getBranchAddress(branchId));
        System.out.println("What would you like to do?");
        System.out.println("1. Add Stock Item");
        System.out.println("2. Remove Stock Item.");
        System.out.println("3. Adding Damage to Item.");
        System.out.println("4. Move stock item.");
        System.out.println("5. Create / Update order according to stock.");
        System.out.println("0. Actually, I would like to exit menu.");
    }
    public void communicate(){
        boolean run = true;
        while (run){
            printMenu();
            System.out.print("Please enter your option: ");
            int choice = inputNumber();
            switch (choice) {
                case 1 -> addStockItem();
                case 2 -> removeStockItem();
                case 3 -> updateDamage();
                case 4 -> moveStockItem();
                case 5 -> manageOrder();
                case 0 -> run = false;
                default -> System.out.println("Invalid option");
            }
        }
    }
    public void addStockItem() {
        System.out.println("Enter the item's details:");
        System.out.print("ID: "); int id = inputNumber();
        if(id < 0){
            System.out.println("Invalid ID. Returning to menu...");
            return;
        }
        System.out.print("Barcode: "); int barcode = inputNumber();
        if(barcode < 0){
            System.out.println("Invalid barcode. Returning to menu...");
            return;
        }
        System.out.print("Cost price: "); double costPrice = inputDouble();
        if(costPrice < 0){
            System.out.println("Invalid price. Returning to menu...");
            return;
        }
        System.out.println("Do you want to add an expiration date?");
        System.out.print("Please enter your chose (0 = no, 1 = yes): "); int addExpiration = inputNumber();
        if(addExpiration > 1) {
            System.out.println("Invalid option. Returning to menu...");
            return;
        }
        LocalDate date = null;
        if(addExpiration == 1) {
            System.out.println("Expiration date (Format: d/M/yy:) ");
            date = inputDate();
            if (date == null) {
                System.out.println("Invalid date. Returning to menu...");
                return;
            }
        }
        System.out.print("Choose damage type:\n1. Cover\n2. PHYSICAL\n3. ELECTRICAL\n4. ROTTEN\n5. None\nDamage type: "); int type = inputNumber();
        if (!(type >= 1 && type <= 5)){
            System.out.println("Invalid Damage Type choice. Returning to main menu...");
            return;
        }
        type--;
        System.out.print("Choose where to put the item:\n1. Back storage\n2. Shelves\n3. Dont mind\nChoice: "); int itemLocation = inputNumber();
        if (!(itemLocation >= 1 && itemLocation <= 3)){
            System.out.println("Invalid StockItem Location choice. Returning to main menu...");
            return;
        }
        boolean succeeded = false;
        switch (itemLocation) {
            case (1) -> succeeded = stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, date, type, false);
            case (2) -> succeeded = stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, date, type, true);
            case (3) -> succeeded = stockManagementFacade.addStockItem(id, branchId, barcode, costPrice, date, type);
        }
        if(succeeded)
            System.out.println("Adding to the stock completed successfully. Returning to menu...");
        else
            System.out.println("Failed to add the item to the branch. Returning to menu...");
    }
    public void removeStockItem() {
        System.out.print("Enter the barcode: "); int barcode = inputNumber();
        int catalogId = stockManagementFacade.barcodeToId(barcode, branchId);
        if(!stockManagementFacade.removeStockItem(barcode, branchId)) {
            System.out.println("Invalid barcode. Returning to menu");
            return;
        }
        if(stockManagementFacade.isRequired(catalogId, branchId))
            System.out.println("THE ITEM CROSSED THE MINIMUM GAP, CONSIDER ORDERING MORE UNITS IMMEDIATELY");
        String fill_shelves = stockManagementFacade.needsShelvesIncrement(catalogId, branchId) ?
                String.format(" Consider filling the shelves with %s (ID %d).", stockManagementFacade.getCatalogIdName(catalogId), catalogId) : "";
        System.out.format("Removing from the stock completed successfully.%s Returning to main menu...\n", fill_shelves);
    }
    public void updateDamage() {
        System.out.print("Enter the barcode: "); int barcode = inputNumber();
        System.out.print("Choose damage type:\n1. Cover\n2. PHYSICAL\n3. ELECTRICAL\n4. ROTTEN\nDamage type: "); int damageType = inputNumber();
        if(damageType < 1 || damageType > 4){
            System.out.println("Invalid choice. Returning to main menu...");
            return;
        }
        damageType--;
        if(!stockManagementFacade.setDamage(barcode, damageType, branchId)){
            System.out.println("Error setting new damage. Returning to main menu...");
            return;
        }
        System.out.format("Adding damage %s to barcode %d completed successfully. Returning to main menu...\n",
                stockManagementFacade.getStockItemDamage(barcode, branchId), barcode);
    }
    public void moveStockItem() {
        System.out.print("Enter the barcode: "); int barcode = inputNumber();
        if (!stockManagementFacade.moveStockItem(barcode, branchId)){
            System.out.print("Error at changing location. Returning to main menu...");
        }
        System.out.format("Changing the item location to location %d completed successfully. Returning to main menu...\n",
                stockManagementFacade.getLocationOfBarcode(barcode, branchId));
    }
    public void manageOrder() {
        System.out.println("1. Create Shortage Order");
        System.out.println("2. Create New Periodic Report");
        System.out.println("3. Update Periodic Report");
        System.out.print("Please enter your option: ");
        switch (inputNumber()) {
            case 1 -> createShortageStockOrder();
            case 2 -> makeNewPeriodicOrder();
            case 3 -> updatePeriodicOrder();
            default -> System.out.println("Invalid option");
        }
    }
    public void createShortageStockOrder() {
        if (!stockManagementFacade.createShortageOrder(branchId))
            System.out.print("No items required for the branch. ");
        else
            System.out.print("Required stock ordered sent to the suppliers successfully. ");
        System.out.println("Returning to menu...");
    }
    public void updatePeriodicOrder() {
        System.out.println("Please choose a report to update:");
        if(!ReportViewer.getInstance().generatePeriodicReportsAccordingToBrunch(branchId)) {
            System.out.println("There are no periodic reports of branch with given number. Returning to menu...");
            return;
        }
        System.out.print("Please insert the report's ID that you want ot update: "); int reportId = inputNumber();
        if(reportId < 0) {
            System.out.println("Invalid report ID. Returning to menu...");
            return;
        }
        try{
            if(!stockManagementFacade.updatePeriodReport(reportId, branchId)) {
                System.out.println("Cannot update the requested reportId");
                return;
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return;
        }
        System.out.format("Report %d was updated successfully. Returning to menu...\n", reportId);
    }

    public boolean makeNewPeriodicOrder() {
        System.out.println("\nCreating new periodic report. Please enter details.");
        Scanner scan = new Scanner(System.in);
        while (true) {
            // receive branch number from user and find the branch
            System.out.println("Please enter report id or -1 to cancel.");
            int reportId;
            try {
                reportId = scan.nextInt();
                scan.nextLine();
                if (reportId == -1)
                    return false;
                if (reportId < 0)
                    throw new InvalidParameterException("Report id must be a positive number, please try again.\n");
                if(this.orderController.isReportIdAlreadyExist(reportId))
                    throw new InvalidParameterException("There is already report with givan id, please try again.\n");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Report id must be a positive number, please try again.\n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }
            // receive branch number from user and find the branch
            System.out.println("Please enter branch number or -1 to cancel.");
            int branchNumber;
            try {
                branchNumber = scan.nextInt();
                scan.nextLine();
                if (branchNumber == -1)
                    return false;
                if (branchNumber < 0)
                    throw new InvalidParameterException("Branch number must be a positive number, please try again.\n");
                Branch branch = this.orderController.getBranchByID(branchNumber);
                if (branch == null)
                    throw new InvalidParameterException("Branch number must be number of existing branch.");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Branch number must be a positive number, please try again.\n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // receive day of the periodic order from the user
            System.out.println("Please enter the day you want the order will be sent each week (number between 1-7) " +
                    "or -1 to cancel.");
            int dayInt;
            try {
                dayInt = scan.nextInt();
                scan.nextLine();
                if (dayInt == -1)
                    return false;
                if (dayInt < 0 || dayInt > 7)
                    throw new InvalidParameterException("Day number must be positive number in range of 1-7, please try again.\n");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Day number must be a positive number, please try again. \n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }
            Day[] allDays = Day.values();
            Day day = allDays[dayInt-1];

            // print all suppliers for user to choose
            LinkedList<Supplier> allSupp = this.orderController.getAllSuppliers();
            System.out.println("Here is a list of all suppliers in the system.");
            for(Supplier sup : allSupp)
            {
                System.out.println(sup.toString());
            }
            System.out.println("Please enter wanted supplier's id or -1 to exit.");
            int id;
            Supplier supp;
            try {
                id = scan.nextInt();
                scan.nextLine();
                if (id == -1)
                    return false;
                if (id < 0)
                    throw new InvalidParameterException("id must be a positive number, please try again.\n");
                supp = this.orderController.getSuppByID(id);
                if (supp == null)
                    throw new InvalidParameterException("Supplier id must be id of supplier in the system, please try again.\n");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("id must be a positive number, please try again.\n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // print all the items of supplier and choosing items for the report
            LinkedList<SupplierItem> supplierItems = this.orderController.getAllItemsOfSupplier(id);
            if (supplierItems.isEmpty())
            {
                System.out.println("Supplier has no items. please choose another supplier.\n");
                continue;
            }
            System.out.println("Here is all the items that the chosen supplier supply : ");
            for(SupplierItem sItem : supplierItems)
            {
                System.out.println(sItem.toString());
            }
            int marketID = 0, amount = 0;
            HashMap<Integer,Integer> items = new HashMap<>();

            while (marketID != -1)
            {
                System.out.println("Please enter market id of item you want to add to the report, or -1 for exit.");

                try {
                    marketID = scan.nextInt();
                    scan.nextLine();
                    if (marketID == -1)

                    {
                        if (items.isEmpty())
                        {
                            System.out.println("The report has no item - can't create an empty report. Please choose at least one item. =");
                            continue;
                        }
                        break;
                    }
                    if (marketID < 0)
                        throw new InvalidParameterException("Market id must be a positive number, please try again. \n");
                    if(!supp.supplierHasMarketId(marketID))
                        throw new InvalidParameterException("Supplier doesn't supply an item with given market id,please try again.\n");
                } catch (InputMismatchException e) {
                    scan.nextLine();
                    System.out.println("Market id must be a positive number, please try again. \n");
                    continue;
                } catch (InvalidParameterException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                System.out.println("Please enter amount of units from this item.");

                try {
                    amount = scan.nextInt();
                    scan.nextLine();
                    if (amount < 0)
                        throw new InvalidParameterException("Amount must be a positive number, please try again. \n");
                    boolean canSupply = supp.canSupplyMarketItem(Pair.create(marketID, amount));
                    if (!canSupply)
                        throw new InvalidParameterException("Supplier can't supply this item with this amount, please try again.\n");
                } catch (InputMismatchException e) {
                    scan.nextLine();
                    System.out.println(e.getMessage());
                    continue;
                } catch (InvalidParameterException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                items.put(marketID, amount);

            }
            if (this.orderController.createNewPeriodicReport(reportId,branchNumber, supp, day, items) != null)
            {
                System.out.println("Periodic report was added successfully.");
                return true;
            }
            return false;
        }
    }
    public static void main(String[] args){
        int branchId = getBranchID();
        if(branchId >= 0)
            StockKeeperMenu.getInstance(branchId).communicate();
    }
}
