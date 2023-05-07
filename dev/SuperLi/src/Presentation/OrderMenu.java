package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.OrderController;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OrderMenu extends AMenu{
    static OrderMenu instance = new OrderMenu();
    private OrderController orderController;

    private OrderMenu() { this.orderController = OrderController.getInstance();};

    public static OrderMenu getInstance(){ return instance;}

    // this methood called by the main menu when order manager choose to create new periodic order
    // the methood will create new periodic report and order
    public void makeNewPeriodicOrder()
    {
        // get branch number
        // ask from controller all suppliers and choose one
        // then ask for all items of that supplier and choose items and amount
        // then ask for creating new order
    }

    @Override
    public void communicate() {
        // TODO
    }
    @Override
    public void printMenu(){

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
        System.out.println("What would you like to do?");
        System.out.println("1. Create new periodic order. \n");
        System.out.println("2.Return to main menu.");
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
