package SuperLi.src;
import SuperLi.src.BusinessLogic.*;
import SuperLi.src.Presentation.CLI.AdminMenuCLI;
import SuperLi.src.Presentation.CLI.StockKeeperMenuCLI;
import SuperLi.src.Presentation.CLI.SupplierMenuCLI;
import SuperLi.src.Presentation.GUI.AdminMenuGUI;
import SuperLi.src.Presentation.GUI.StockKeeperMenuGUI;
import SuperLi.src.Presentation.GUI.SupplierMenuGUI;

public class Main {
    public static void main(String[] args) {
        OrderController.getInstance().runEveryDayToMakeOrders();//this func has to be executed in main, so it will run every day automatically.
        if (args.length != 2)
            return;
        switch (args[1] + args[0]){
            case "StockKeeperCLI" -> StockKeeperMenuCLI.getInstance().communicate();
            case "StoreManagerCLI" -> AdminMenuCLI.getInstance().communicate();
            case "SupplierManagerCLI" -> SupplierMenuCLI.getInstance().communicate();
            case "StockKeeperGUI" -> StockKeeperMenuGUI.getInstance().communicate();
            case "StoreManagerGUI" -> AdminMenuGUI.getInstance().communicate();
            case "SupplierManagerGUI" -> SupplierMenuGUI.getInstance().communicate();
            default -> System.out.println("Invalid Running Arguments");
        }
    }
}