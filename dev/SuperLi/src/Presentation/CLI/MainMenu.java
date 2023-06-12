package SuperLi.src.Presentation.CLI;

import SuperLi.src.BusinessLogic.StockManagementFacade;


public class MainMenu extends AMenu{
    private static MainMenu mainMenu = null;
    private MainMenu() {}
    public static MainMenu getInstance(){
        if (mainMenu == null)
            mainMenu = new MainMenu();
        return mainMenu;
    }
    public void printMenu(){
        System.out.println("Welcome to Super-Li market!\nPlease tell us who you are.");
        System.out.println("1. I'm the admin.");
//      System.out.println("2. I'm a branch stock manager.");
        System.out.println("2. I'm a stock-keeper.");
        System.out.println("3. I'm a supplier manager.");
//        System.out.println("5. I'm the order manager.");
    }
    public void communicate() {
        int branchId = -1;
        while (true) {
            printMenu();
            System.out.print("Please enter your option: ");
            int choice = inputNumber();
            if (2 == choice) {
                System.out.print("Please enter your branch id: ");
                branchId = inputNumber();
                if (!(0 < branchId && branchId <= StockManagementFacade.getInstance().getBranchCount())) {
                    System.out.println("Invalid branch ID");
                    continue;
                }
            }
            switch (choice) {
                case 1 -> AdminMenu.getInstance().communicate();
                // Skipping branch stock manager menu
                case 2 -> StockKeeperMenu.getInstance(branchId).communicate();
                case 3 -> SupplierMenu.getInstance().communicate();
//                case 5 -> OrderMenu.getInstance().communicate();
                default -> System.out.println("Invalid option");
            }
        }
//        System.out.println("Thank you for using Super - li system, we hope to see you soon.");
    }
}
