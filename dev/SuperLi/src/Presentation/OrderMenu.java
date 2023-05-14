package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.*;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class OrderMenu extends AMenu{
    static OrderMenu instance = new OrderMenu();
    private OrderController orderController;

    private OrderMenu() { this.orderController = OrderController.getInstance();};

    public static OrderMenu getInstance(){ return instance;}

    // this method called by the main menu when order manager choose to create new periodic order
    // the method will create new periodic report and order and returns boolean value if the report created successfully
    public boolean makeNewPeriodicOrder() {
        System.out.println("Creating new periodic report. Please enter details. \n");
        Scanner scan = new Scanner(System.in);
        while (true) {

            // receive branch number from user and find the branch
            System.out.println("Please enter branch number or -1 to cancel. \n");
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
                    throw new InvalidParameterException("Branch number must be number of existing branch.\n");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Branch number must be a positive number, please try again. \n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // receive day of the periodic order from the user
            System.out.println("Please enter the day you want the order will be sent each week (number between 1-7) " +
                    "or -1 to cancel. \n");
            int dayInt;
            try {
                dayInt = scan.nextInt();
                scan.nextLine();
                if (dayInt == -1)
                    return false;
                if (dayInt < 0 || dayInt > 7)
                    throw new InvalidParameterException("Day number must be positive number in range of 1-7.\n");
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
            System.out.println("Here is a list of all suppliers in the system. Please enter id of the wanted supplier. \n");
            System.out.println(allSupp.toString());
            System.out.println("Please enter supplier's id or -1 to exit. \n");
            int id;
            Supplier supp;
            try {
                id = scan.nextInt();
                scan.nextLine();
                if (id == -1)
                    return false;
                if (id < 0)
                    throw new InvalidParameterException("id must be a positive number, please try again. \n");
                supp = this.orderController.getSuppByID(id);
                if (supp == null)
                    throw new InvalidParameterException("Supplier id must be id of supplier in the system. \n");
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("id must be a positive number, please try again. \n");
                continue;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
                continue;
            }

            // print all the items of supplier and choosing items for the report
            LinkedList<SupplierItem> supplierItems = this.orderController.getAllItemsOfSupplier(id);
            if (supplierItems.isEmpty())
            {
                System.out.println("Supplier has no items. please choose another supplier.");
                continue;
            }
            System.out.println("Here is all the items that the chosen supplier supply. \n");
            System.out.println(supplierItems.toString());
            int marketID = 0, amount = 0;
            HashMap<Integer,Integer> items = new HashMap<>();

            while (marketID != -1)
            {
                System.out.println("Please enter market id of item you want to add to the report, or -1 for exit. \n");

                try {
                    marketID = scan.nextInt();
                    scan.nextLine();
                    if (marketID == -1)

                    {
                        if (items.isEmpty())
                        {
                            System.out.println("The report has no item - can't create an empty report. Please choose at least one item. ");
                            continue;
                        }
                        break;

                    }
                    if (marketID < 0)
                        throw new InvalidParameterException("Market id must be a positive number, please try again. \n");
                } catch (InputMismatchException e) {
                    scan.nextLine();
                    System.out.println("Market id must be a positive number, please try again. \n");
                    continue;
                } catch (InvalidParameterException e) {
                    System.out.println(e.getMessage());
                    continue;
                }

                System.out.println("Please enter amount of units from this item. \n");

                try {
                    amount = scan.nextInt();
                    scan.nextLine();
                    if (amount < 0)
                        throw new InvalidParameterException("Amount must be a positive number, please try again. \n");
                    boolean canSupply = supp.canSupplyMarketItem(Pair.create(marketID, amount));
                    if (!canSupply)
                        throw new InvalidParameterException("Supplier can't supply this item with this amount. \n");
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
            if (this.orderController.createNewPeriodicReport(branchNumber, supp, day, items) != null)
                return true;
            return false;


        }
    }

    @Override
    public void communicate() {
        boolean run = true;
//        int flag? = -1;
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
        System.out.println("What would you like to do? \n");
        System.out.println("1. Create new periodic order. \n");
        System.out.println("2.Return to main menu. \n");
    }






    //    public void makeOrder()// Need to change- paremeter report!
//    {
//        Scanner scan = new Scanner(System.in);
////        LinkedList<Pair<Integer, Integer>> missingItems = getAllMissingItems(scan);
////        if (missingItems.isEmpty())
////            return;
//        int branchNumber;
//        while(true)
//        {
//            try
//            {
//                if(SuperLi.src.Presentation.MainMenu.allBranches.isEmpty())
//                {
//                    System.out.println("There are no branches in the system, so it's impossible to make an order.");
//                    return;
//                }
//                System.out.println("Enter number of branch");
//                branchNumber = scan.nextInt();
//                scan.nextLine();
//                if (branchNumber <= 0 || branchNumber > SuperLi.src.Presentation.MainMenu.allBranches.size())
//                    throw new InvalidParameterException("Business.Branch number must be in range of 1 - " + SuperLi.src.Presentation.MainMenu.allBranches.size());
//                break;
//            }
//            catch (InputMismatchException e) {
//                scan.nextLine();
//                System.out.println("Business.Branch number must be only digits.");
//            } catch (InvalidParameterException e) {
//                System.out.println(e.getMessage());
//            }
//        }
////        LinkedList<Pair<Integer, Integer>> missingItems = getAllMissingItems(scan);
////        if (missingItems.isEmpty())
////            return;
//        LinkedList<Order> orders = new LinkedList<>();
//        try
//        {
////          orders = OrderManagment.creatOrder(missingItems, branchNumber);
//            orders = this.orderController.createNewMissingItemsOrder(); //TODO parameter
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//            System.out.println("Not able to make the order.");
//            return;
//        }
//        System.out.println("This is the orders:");
//        double totalCost = 0;
//        for (Order order: orders)
//        {
//            System.out.println(order);
//            System.out.println("Total cost of order, after discounts (if exist): " + order.getCostOfOrder());
//            totalCost += order.getCostOfOrder();
//        }
//        System.out.println("Total cost of all orders: "+ totalCost);
//        System.out.println("Press 'y' to confirm, or anything else for cancel");
//        String confirmation = scan.nextLine();
//        if (!confirmation.equalsIgnoreCase("y"))
//        {
//            System.out.println("Orders was canceled.");
//            return;
//        }
//        OrderManagment.confirmOrders(orders, branchNumber-1);
//        System.out.println("SuperLi.src.BusinessLogic.Order was made successfully. ");
//
//    }

//    private static LinkedList<Pair<Integer, Integer>> getAllMissingItems (Scanner scan)
//    {
//        LinkedList<Pair<Integer, Integer>> missingItems = new LinkedList<>();
//        while (true)
//        {
//            try
//            {
//                System.out.println("Enter market id of missing item or 0 to exit");
//                int itemId = scan.nextInt();
//                scan.nextLine();
//                if (itemId == 0)
//                    break;
//                if (itemId < 0)
//                {
//                    throw new InvalidParameterException("Market Id must be non negative number");
//                }
//                if (!SuperLi.src.Presentation.MainMenu.marketStock.isItemExist(itemId))
//                {
//                    throw new InvalidParameterException("Given market id does not belong to any of stock items.");
//                }
//                System.out.println("Enter amount of missing item");
//                int amount = scan.nextInt();
//                scan.nextLine();
//                if (amount <= 0)
//                {
//                    throw new InvalidParameterException("Amount must be non negative number");
//                }
////                Pair<Integer, Integer> currItem = Pair.create(itemId, amount);
////                missingItems.add(currItem);
//            }
//            catch (InputMismatchException e)
//            {
//                scan.nextLine();
//                System.out.println("Market id and amount must contains only digits.");
//            }
//            catch (InvalidParameterException e)
//            {
//                System.out.println(e.getMessage());
//            }
//        }
//        return missingItems;
//    }
public void ordersrMenu()
{
    Scanner scan = new Scanner(System.in);
    int choise;
    boolean flag = true;
    while (flag) {
//        System.out.println("What would you like to do?");
//        System.out.println("1. Create new periodic order. \n");
//        System.out.println("2.Return to main menu.");
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
