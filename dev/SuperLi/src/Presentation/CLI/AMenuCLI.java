package Presentation.CLI;
import BusinessLogic.OrderController;
import BusinessLogic.StockManagementFacade;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.time.LocalDate;

public abstract class AMenuCLI {
    static Scanner input = new Scanner(System.in);
    static boolean firstMenu = true;
    public abstract void printMenu();
    public abstract void communicate();
    public AMenuCLI(){
        if (firstMenu) {
            OrderController.getInstance().runEveryDayToMakeOrders();//this func has to be executed in main, so it will run every day automatically.
            firstMenu = false;
        }
    }
    public static int inputNumber(){
        int num;
        try{
            num = input.nextInt();
        }
        catch(Exception e) {
            num = -1;
        }
        input.nextLine();
        return num < 0 ? -1 : num;
    }
    public double inputDouble(){
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
    public LocalDate inputDate(){
        String date = input.nextLine();
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("d/M/yy"));
        }
        catch(Exception e){
            return null;
        }
    }
    protected static int getBranchID() {
        System.out.print("Please enter your branch id: ");
        int branchId = inputNumber();
        if (!(0 < branchId && branchId <= StockManagementFacade.getInstance().getBranchCount())) {
            System.out.println("Invalid branch ID");
            return -1;
        }
        return branchId;
    }
}
