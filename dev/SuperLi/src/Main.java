package SuperLi.src;
import SuperLi.src.Presentation.CLI.MainMenu;
import SuperLi.src.BusinessLogic.*;

public class Main {
    public static void main(String[] args) {
        OrderController.getInstance().runEveryDayToMakeOrders();//this func has to be executed in main so it will run every day automatically.


        MainMenu.getInstance().communicate();
    }
}