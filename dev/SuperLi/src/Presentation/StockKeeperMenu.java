package SuperLi.src.Presentation;

import java.time.LocalDate;
import SuperLi.src.BusinessLogic.StockManagementFacade;

public class StockKeeperMenu extends AMenu{
    StockManagementFacade stockManagementFacade;
    int branchId;
    static StockKeeperMenu stockKeeperMenu = null;
    private StockKeeperMenu(int branchId){
        stockManagementFacade = StockManagementFacade.getInstance();
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
        System.out.println("1. Add Stock Item");
        System.out.println("2. Remove Stock Item.");
        System.out.println("3. Adding Damage to Item.");
        System.out.println("4. Move stock item.");
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
                case 0 -> run = false;
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
}
