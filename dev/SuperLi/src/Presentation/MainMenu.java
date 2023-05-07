package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.StockManagementFacade;

import java.util.*;


public class MainMenu extends AMenu{
    private static MainMenu mainMenu = null;
    private MainMenu() {}
    public static MainMenu getInstance(){
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }
    public void printMenu(){
        System.out.println("Welcome to Super - li market!\nPlease tell us who you are.");
        System.out.println("1. I'm the admin.");
        System.out.println("2. I'm a branch stock manager.");
        System.out.println("3. I'm a stock-keeper.");
        System.out.println("4. I'm a supplier manager.");
        System.out.println("5. I'm the order manager.");
        System.out.println("0. Actually, I would like to exit menu.");
    }
    public void communicate() {
        boolean run = true;
        int branchId = -1;
        while (run) {
            printMenu();
            System.out.print("Please enter your option: ");
            int choice = inputNumber();
            if (2 <= choice && choice <= 3) {
                System.out.print("Please enter your branch id: ");
                branchId = inputNumber();
                if (!(0 < branchId && branchId <= StockManagementFacade.getInstance().getBranchCount())) {
                    System.out.println("Invalid branch ID");
                    continue;
                }
            }
            switch (choice) {
                case 1 -> AdminMenu.getInstance().communicate();
                case 2 -> BranchStockManagerMenu.getInstance(branchId).communicate();
                case 3 -> StockKeeperMenu.getInstance(branchId).communicate();
                case 4 -> SupplierMenu.getInstance().communicate();
                case 5 -> OrderMenu.getInstance().communicate();
                case 0 -> run = false;
                default -> System.out.println("Invalid option");
            }
        }
        System.out.println("Thank you for using Super - li system, we hope to see you soon.");
    }
}
