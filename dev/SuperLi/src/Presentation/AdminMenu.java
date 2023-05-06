package SuperLi.src.Presentation;
import SuperLi.src.BusinessLogic.*;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;

public class AdminMenu extends AMenu {

    private static AdminMenu adminMenu = null;
    private AdminMenu(){}
    public static AdminMenu getInstance(){
        if(adminMenu == null)
            adminMenu = new AdminMenu();
        return adminMenu;
    }
    public void communicate(){
        // TO DO
    }

    private AdminController adminCon;

    private static int adminPassword = 123;
    private static AdminMenu instance = new AdminMenu();

//    private AdminMenu()
//    {
//        this.adminCon = AdminController.getInstance();
//    }
    private int GetId(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Enter supplier id");
                int id = scan.nextInt();
                scan.nextLine();
                if (id < 0)
                    throw new InvalidParameterException("Supplier id must be a number");
                if (adminCon.isSupplierIdExists(id))
                    throw new InvalidParameterException("Supplier id already exists in system, please try again.");
                return id;
            }
            catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Supplier id must be a positive number");
            }
            catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public String getBankAccount(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Enter bank account");
                String account = scan.nextLine();
                if (!(account.matches("[0-9]+")))
                    throw new InvalidParameterException();
                return account;
            }
            catch(InvalidParameterException e)
            {
                System.out.println("Bank account number is not valid");
            }
        }
    }

    private String getName(Scanner scan)
    {
        while (true)
        {
            try
            {
                System.out.println("Enter name");
                String name = scan.nextLine();
                if (!(name.matches("[a-zA-Z]+")))
                    throw new InvalidParameterException();
                return name;
            }
            catch(InvalidParameterException e)
            {
                System.out.println("Name is not valid");
            }
        }
    }

    public String getAddress(Scanner scan)
    {
        while (true)
        {
            try
            {
                System.out.println("Enter address");
                String address = scan.nextLine();
                if (address.equals(""))
                    throw new InvalidParameterException();
                return address;
            }
            catch(InvalidParameterException e)
            {
                System.out.println("supplier must have an address");
            }
        }
    }

    public PaymentsWays getPaymentWays(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Please choose Payment way:");
                System.out.println("1. direct");
                System.out.println("2. plus30");
                System.out.println("3. plus60");
                int pay = scan.nextInt();
                scan.nextLine();
                if (pay == 1) {
                    return PaymentsWays.direct;
                } else if (pay == 2) {
                    return PaymentsWays.plus30;
                } else if (pay == 3) {
                    return PaymentsWays.plus60;
                }
                else
                {
                    System.out.println("please enter a number between 1-3");
                    continue;
                }
            }
            catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("please enter a number between 1-3");
            }
        }
    }

    public LinkedList<String> getCategories(Scanner scan)
    {
        LinkedList<String> categories = new LinkedList<String>();
        while (true) {
            try {
                System.out.println("Enter category");
                String category = scan.nextLine();
                if (!(category.matches("[a-zA-Z]+")))
                    throw new InvalidParameterException("category is not valid");
                if(categories.contains(category))
                    throw new InvalidParameterException("impossible to add the same category twice.");
                categories.add(category);
                System.out.println("if you want to stop entering categories, please enter 1. else, press anything to continue");
                String choise = scan.nextLine();
                if(choise.equals("1"))
                    break;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
        return categories;
    }

    public LinkedList<String> getManufacturers(Scanner scan)
    {
        LinkedList<String> manufacturers = new LinkedList<String>();
        while (true) {
            try {
                System.out.println("Enter manufacturer");
                String manufacturer = scan.nextLine();
                if (!(manufacturer.matches("[a-zA-Z]+")))
                    throw new InvalidParameterException("manufacturer is not valid");
                if(manufacturers.contains(manufacturer))
                    throw new InvalidParameterException("impossible to add the same manufacturer twice.");
                manufacturers.add(manufacturer);
                System.out.println("if you want to stop entering manufacturers, please enter 1. else, press anything to continue");
                String choise = scan.nextLine();
                if(choise.equals("1"))
                    break;
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
        return manufacturers;
    }

    private boolean isDelivering(Scanner scan)
    {
        System.out.println("Are you delivering or not?");
        System.out.println("1 - yes, i'm delivering");
        System.out.println("2 - no, i'm not delivering");
        while(true)
        {
            try
            {
                int choise = scan.nextInt();
                scan.nextLine();
                if(choise==1)
                {
                    return true;
                }
                else if(choise==2)
                {
                    return false;
                }
                else
                {
                    System.out.println("please choose a number between 1-2");
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("please enter a number between 1-2");
            }
        }
    }

    private boolean isRegularDelivering(Scanner scan)
    {
        System.out.println("Do you supply on regular days?");
        System.out.println("1 - yes");
        System.out.println("2 - no");
        while(true)
        {
            try
            {
                int choise = scan.nextInt();
                scan.nextLine();
                if(choise==1)
                {
                    return true;
                }
                else if(choise==2)
                {
                    return false;
                }
                else
                {
                    System.out.println("please choose a number between 1-2");
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("please enter a number between 1-2");
            }
        }
    }

    private Day matchStringToDay(String s_day)
    {
        if(s_day.equalsIgnoreCase("sunday"))
            return Day.sunday;
        else if(s_day.equalsIgnoreCase("monday"))
            return Day.monday;
        else if(s_day.equalsIgnoreCase("tuesday"))
            return Day.tuesday;
        else if(s_day.equalsIgnoreCase("wednesday"))
            return Day.wednesday;
        else if(s_day.equalsIgnoreCase("thursday"))
            return Day.thursday;
        else if(s_day.equalsIgnoreCase("friday"))
            return Day.friday;
        else
            return Day.saturday;
    }

    private LinkedList<Day> ChooseDays(Scanner scan)
    {
        LinkedList<Day> days = new LinkedList<>();
        LinkedList<String> daysOfTheWeek = new LinkedList<>(Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));
        while(true)
        {
            System.out.println("Please enter day for delivering:");
            String chosenDay = scan.nextLine();
            int index = -1;
            for (int i = 0; i < daysOfTheWeek.size(); i++) {
                if (daysOfTheWeek.get(i).equalsIgnoreCase(chosenDay)) {
                    index = i;
                    break;
                }
            }
            if (index == -1)
            {
                if(daysOfTheWeek.size() > 0)
                {
                    System.out.println("Please enter valid day");
                    continue;
                }
                else
                {
                    System.out.println("There are no more possible days to enter.");
                    break;
                }
            }
            else
            {
                Day day = matchStringToDay(daysOfTheWeek.get(index));
                days.add(day);
                daysOfTheWeek.remove(index);
                System.out.println("if you want to stop entering days of delivering, please enter 1. else, press anything to continue");
                String choise = scan.nextLine();
                if(choise.equals("1"))
                    break;
            }
        }
        return days;
    }

    private int getNumberOfDaysToDeliver(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Enter how many days after ordering you will deliver");
                int numDaysToDeliver = scan.nextInt();
                scan.nextLine();
                if(numDaysToDeliver < 0)
                    throw new InvalidParameterException();
                return numDaysToDeliver;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("number of days must be a number");
            }
            catch(InvalidParameterException e)
            {
                System.out.println("number of days until supplier delivers must be positive");
            }
        }
    }

    private String getContactName(Scanner scan)
    {
        System.out.println("Please enter contact's details:");
        System.out.println("Enter contact's name");
        String name = scan.nextLine();
        return name;
    }
    private String getContactPhone(Scanner scan)
    {
            System.out.println("Enter contact's phone number");
            String phone = scan.nextLine();
            return phone;

    }
      private String getContactEmail(Scanner scan) {

          System.out.println("Enter contact's email address");
          String email = scan.nextLine();
          return email;
      }

    public void AddNewSupplierToSystem()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Please enter the next details:");
        int id = GetId(scan);
        String bankAcc = getBankAccount(scan);
        String name = getName(scan);
        String address = getAddress(scan);
        PaymentsWays payment = getPaymentWays(scan);
        String contactName = getContactName(scan);
        String contactPhone = getContactPhone(scan);
        String contactEmail = getContactEmail(scan);
        LinkedList<String> categories = getCategories(scan);
        LinkedList<String> manufacturers = getManufacturers(scan);
        LinkedList<Day> days = null;
        int numDaysToDeliver = -1;
        if(isDelivering(scan))//delivering
        {
            if(isRegularDelivering(scan))//delivers regularly
            {
                days = ChooseDays(scan);
            }
            else //doesn't deliver regularly
            {
                numDaysToDeliver = getNumberOfDaysToDeliver(scan);
            }
        }
        try
        {
            SupplierCard supCard;
            adminCon.addNewSupplier(name, address, id, bankAcc, payment, contactName, contactPhone, contactEmail,categories,manufacturers,supCard,days,numDaysToDeliver);
        }
        catch (InvalidParameterException e)
        {
            System.out.println(e.getMessage());
            System.out.println("Details are incorrect, no supplier was added. please try again next time.");
        }
    }

    public void AddNewBranchToSystem()
    {
        Scanner scan = new Scanner(System.in);
        while (true)
        {
            System.out.println("Please enter the new branch's address:");
            String address = scan.nextLine();
            if(address.equals(""))
            {
                System.out.println("please enter valid address.");
                continue;
            }
            else
            {
                adminCon.addNewBranch(address);
                break;
            }
        }
    }

    public void adminMenu()
    {
        Scanner scan = new Scanner(System.in);
        System.out.println("Hello, to make sure you are the admin, please enter the admin's password:");
        try
        {
            int password = scan.nextInt();
            scan.nextLine();
            if(password != AdminMenu.adminPassword)
            {
                System.out.println("Password incorrect, you are not allowed to have access to admin's menu.");
                return;
            }
        }
        catch (InputMismatchException e)
        {
            System.out.println("Password incorrect, you are not allowed to have access to admin's menu.");
            return;
        }
        boolean flag = true;
        while (flag)
        {
            System.out.println("Dear admin, what would you like to do?");
            System.out.println("1. Add new supplier to system.");
            System.out.println("2. Add new branch to system.");
            System.out.println("3. Print all orders were made.");
            System.out.println("4. Print all suppliers in the system.");
            System.out.println("5. Print all orders were made for specific branch.");
            System.out.println("6. Return to main menu.");
            int option = 0;
            try
            {
                option = scan.nextInt();
                scan.nextLine();
                while (option < 1 || option > 6) {
                    System.out.println("Please enter a number between 1-6.");
                    option = scan.nextInt();
                    scan.nextLine();
                }
            }
            catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("option number must be only digits, please try again.");
                continue;
            }
            switch (option) {
                case 1:
                    AddNewSupplierToSystem();
                    break;
                case 2:
                    AddNewBranchToSystem();
                    break;
                case 3:
                    printAllOrdersInSystem();
                    break;
                case 4:
                    printAllSuppliersInSystem();
                    break;
//                case 5:
//                    printAllMarketItems();
//                    break;
                case 5:
                    printAllBranchOrders();
                    break;
                case 6:
                    flag = false;
                    break;
            }
        }
    }

    public void printAllOrdersInSystem()
    {
        LinkedList<Order> allOrders = adminCon.getAllOrdersInSystem();
        for (Order order: allOrders)
        {
            System.out.println(order.toString());
        }
    }

    public void printAllSuppliersInSystem()
    {
        LinkedList<Supplier> allSuppliers = adminCon.getAllSuppliersInSystem();
        for (Supplier supplier: allSuppliers)
            System.out.println(supplier.allDataOfSupplier());
    }

    public void printAllBranchOrders()
    {
        Scanner scan = new Scanner(System.in);
        //NEED TO PRINT ALL BRANCHES
        System.out.println("Please enter number of branch.");
        int branchNumber;
        try
        {
            branchNumber = scan.nextInt();
            scan.nextLine();
//            if(branchNumber <= 0 || branchNumber > SuperLi.src.Presentation.MainMenu.allBranches.size())
//                throw new InvalidParameterException("Business.Branch number must be in range of 1 - " + SuperLi.src.Presentation.MainMenu.allBranches.size());
        }
        catch (InputMismatchException e) {
            scan.nextLine();
            System.out.println("Business.Branch number must be only digits.");
            return;
        }
//        catch(InvalidParameterException e)
//        {
//            System.out.println(e.getMessage());
//            return;
//        }
        try
        {
            LinkedList<Order> allBranchOrders = adminCon.getAllOrdersOfBranch(branchNumber);
            //PRINT ALL ORDERS.
        }
        catch (InvalidParameterException e)
        {
            System.out.println(e.getMessage());
        }
    }

//admin menu of Stock
//    static AdminMenu adminMenu = null;
//    private AdminMenu(){}
//    public static AdminMenu getInstance(){
//        if(adminMenu == null)
//            adminMenu = new AdminMenu();
//        return adminMenu;
//    }
//    public void addBranch(){}
//    public void removeBranch(){}
//    public void addCatalogItem(){}
//    public void removeCatalogItem(){}
//    public void changeCostumerPrice(){}
//    public void changeMinCapacity(){}
//    public void setCostumerDiscount(){}
}
