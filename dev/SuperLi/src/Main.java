
import Presentation.CLI.AdminMenuCLI;
import Presentation.CLI.StockKeeperMenuCLI;
import Presentation.CLI.SupplierMenuCLI;
import Presentation.GUI.AdminMenuGUI;
import Presentation.GUI.StockKeeperMenuGUI;
import Presentation.GUI.SupplierMenuGUI;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Invalid number of arguments");
            return;
        }
        switch (args[1] + args[0]){
            case "StockKeeperCLI" -> StockKeeperMenuCLI.getInstance().communicate();
            case "StoreManagerCLI" -> AdminMenuCLI.getInstance().communicate();
            case "SupplierManagerCLI" -> SupplierMenuCLI.getInstance().communicate();
            case "StockKeeperGUI" -> StockKeeperMenuGUI.getInstance().communicate();
            case "StoreManagerGUI" -> AdminMenuGUI.getInstance().communicate();
            case "SupplierManagerGUI" -> SupplierMenuGUI.getInstance().communicate();
            default -> System.out.println("Invalid Running Arguments: " + args[0] + ", " + args[1]);
        }
    }
}