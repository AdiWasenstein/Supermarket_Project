package SuperLi.src.Presentation.CLI;

import SuperLi.src.BusinessLogic.*;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class OrderMenu extends AMenu{
    static OrderMenu instance = new OrderMenu();
    private OrderController orderController;

    private OrderMenu() { this.orderController = OrderController.getInstance();}

    public static OrderMenu getInstance(){ return instance;}

    // this method called by the main menu when order manager choose to create new periodic order
    // the method will create new periodic report and order and returns boolean value if the report created successfully
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

    @Override
    public void communicate() {
        boolean run = true;
        while (run) {
            printMenu();
            System.out.print("Please enter your option: ");
            int choice = inputNumber();

            switch (choice) {
                case 1 -> makeNewPeriodicOrder();
                case 2 -> run = false;
                default -> System.out.println("Invalid option \n");
            }
        }
    }

    @Override
    public void printMenu(){
        System.out.println("\nWhat would you like to do?");
        System.out.println("1. Create new periodic report.");
        System.out.println("2.Return to main menu.");
    }
public void ordersrMenu()
{
    Scanner scan = new Scanner(System.in);
    int choise;
    boolean flag = true;
    while (flag) {
        try {
            choise = scan.nextInt();
            scan.nextLine();
            while (choise < 1 || choise > 2) {
                System.out.println("Please enter a number between 1-2.");
                choise = scan.nextInt();
                scan.nextLine();
            }
        } catch (InputMismatchException e) {
            scan.nextLine();
            System.out.println("You must enter a number between 1-2, please try again.");
            continue;
        }
        switch (choise) {
            case 1:
                makeNewPeriodicOrder();
                break;
            case 2:
                return;
            }
        }
    }
}