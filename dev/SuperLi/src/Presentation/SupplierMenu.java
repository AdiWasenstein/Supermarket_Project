package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.OrderDataMapper;

import java.security.InvalidParameterException;
import java.util.*;

public class SupplierMenu extends AMenu {

    private SupplierController supplierCon;
    private static SupplierMenu instance = new SupplierMenu();

    private SupplierMenu()
    {
        this.supplierCon = SupplierController.getInstance();
    }

    public static SupplierMenu getInstance()
    {
        return instance;
    }
    public void printMenu(){
        System.out.println("What would you like to do?");
        System.out.println("1.Add new contact.");
        System.out.println("2.Remove contact.");
        System.out.println("3.Add new category.");
        System.out.println("4.Add new manufacturer.");
        System.out.println("5.Show order history.");
        System.out.println("6.Update details.");
        System.out.println("7.Add new item to contract.");
        System.out.println("8.Remove item from contract.");
        System.out.println("9.Add new discount.");
        System.out.println("10.Remove discount.");
        System.out.println("11.Update item's details.");
        System.out.println("12.Return to main menu.");
    }
    public void communicate() {
        boolean run = true;
        while (run) {
            printMenu();
            int option = inputNumber();
            switch (option) {
                case 1 -> addNewContact();
                case 2 -> removeContact();
                case 3 -> addCategory();
                case 4 -> addManufacturer();
                case 5 -> showOrderHistory();
                case 6 -> UpdateSupplierDetails();
                case 7 -> addItem();
                case 8 -> removeItem();
                case 9 -> addDiscount();
                case 10 -> removeDiscount();
                case 11 -> UpdateItemDetails();
                case 12 -> run = false;
                default -> System.out.println("Invalid option");
            }
        }
        System.out.println("Thank you for using supplier menu, we hope to see you soon.");
    }
    public void addNewContact()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        System.out.println("Please enter contact's details:");
        System.out.println("Enter contact's name");
        String name = scan.nextLine();
        System.out.println("Enter contact's phone number");
        String phone = scan.nextLine();
        System.out.println("Enter contact's email address");
        String email = scan.nextLine();
        try
        {
            supplierCon.addContactToSupplier(supplier, name, phone, email);
            System.out.println("Contact added successfully.");
        }
        catch(InvalidParameterException e1)
        {
            System.out.println(e1.getMessage());
            System.out.println("Contact wasn't added.");
        }
        catch (Exception e2)
        {
            System.out.println(e2.getMessage());
            System.out.println("Contact wasn't added.");
        }
    }


   private Supplier findSupplierUser(Scanner scan)
   {
       while (true)
       {
           System.out.println("Please enter supplier's id or -1 to exit");
           int id=0;
           try
           {
               id = scan.nextInt();
               scan.nextLine();
               if (id == -1)
                   return null;
               if(id < 0)
                   throw new InvalidParameterException();
           }
           catch (InputMismatchException e)
           {
               scan.nextLine();
               System.out.println("id must be a positive number, please try again.");
               continue;
           }
           catch (InvalidParameterException e)
           {
               System.out.println("id must be a positive number, please try again.");
               continue;
           }
           Supplier supplier = supplierCon.findSupplierById(id);
           if(supplier == null)
           {
               System.out.println("Supplier was not found, please try again.");
               continue;
           }
           return supplier;
       }
   }

   public void addCategory()
   {
       Scanner scan = new Scanner(System.in);
       Supplier supplier = findSupplierUser(scan);
       if (supplier == null)
           return;
       LinkedList<String> addedCategories = AdminMenu.getInstance().getSupplierCategory();
       if (addedCategories.isEmpty())
           System.out.println("No catagories added.");
       else
       {
           supplierCon.addCategoriesToSupplier(supplier,addedCategories);
           System.out.println("New categories were added.");
       }
   }
    public void addManufacturer()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        LinkedList<String> addedManufacturers = AdminMenu.getInstance().getSupplierManufacturers();
        if (addedManufacturers.isEmpty())
            System.out.println("No manufacturers added.");
        else
        {
           supplierCon.addManufacturersToSupplier(supplier,addedManufacturers);
            System.out.println("New manufacturers were added.");
        }
    }

    public void showOrderHistory()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        for (Order order : this.supplierCon.getAllOrdersOfSupplier(supplier.getSupplierId()))
            System.out.println(order.toString());
    }

    public void updatePaymentWay(Scanner scan, Supplier sup)
    {
        PaymentsWays newPayment = AdminMenu.getInstance().getSupplierPaymentWays();//NEED TO CHANGE!
        if (newPayment == null)
        {
            System.out.println("No update was made.");
        }
        else
        {
            supplierCon.updateSupplierPaymentWay(sup,newPayment);
            System.out.println("Payment was updated.");
        }
    }

    public void updateBankAccount(Scanner scan, Supplier sup)
    {
        String newBank = AdminMenu.getInstance().getSupplierBankAccount();//NEED TO CHANGE!
        if (newBank.equals(""))
            System.out.println("No update was made.");
        else {
            supplierCon.updateSupplierBankAccount(sup,newBank);
            System.out.println("Bank account was updated.");
        }
    }

    public void updateAddress(Scanner scan, Supplier sup)
    {
        String newAddress = AdminMenu.getInstance().getSupplierAddress();//NEED TO CHANGE!
        if (newAddress.equals(""))
            System.out.println("No update was made.");
        else {
            supplierCon.updateSupplierAddress(sup,newAddress);
            System.out.println("Address was updated.");
        }
    }


    public void addItem()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        int catalogNumber, numberOfUnits;
        double unitPrice, unitWeight;
        String itemName,manufacturer, category;
        while (true)
        {
            try
            {
                System.out.println("Please enter catalog number.");
                catalogNumber = scan.nextInt();
                scan.nextLine();
                if (catalogNumber <= 0)
                {
                    throw new InvalidParameterException();
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("catalog number must be a positive number, please try again.");
                continue;
            }
            catch (InvalidParameterException e)
            {
                System.out.println("Catalog number is not valid.");
                continue;
            }
            System.out.println("Please enter manufacturer.");
            manufacturer = scan.nextLine();
            System.out.println("Please enter category.");
            category = scan.nextLine();
            if (!supplierCon.checkIfItemDetailsValid(supplier,catalogNumber,manufacturer,category))
            {
                System.out.println("Parameters do not fit the information from the supplier. Re-enter details.");
                continue;
            }
           break;
        }
        while (true)
        {
            System.out.println("Please enter name of item.");
            itemName = scan.nextLine();
            if (itemName.equals(""))
            {
                System.out.println("Item must have a name.");
                continue;
            }
            try
            {
                System.out.println("Please enter number of unit supplier can supply.");
                numberOfUnits = scan.nextInt();
                scan.nextLine();
                if (numberOfUnits <= 0) {
                    System.out.println("Supplier must supply at least one unit from the item. Re-enter details.");
                    continue;
                }
            }
            catch(InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("number of units to supply must be a positive number.Re-enter details.");
                continue;
            }
            try
            {
                System.out.println("Please enter unit price.");
                unitPrice = scan.nextDouble();
                scan.nextLine();
                if (unitPrice <= 0) {
                    System.out.println("Price need to be non-negative number. Re-enter details.");
                    continue;
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("price must be a positive number.Re-enter details.");
                continue;
            }
            try
            {
                System.out.println("Please enter unit weight.");
                unitWeight = scan.nextDouble();
                scan.nextLine();
                if (unitWeight <= 0) {
                    System.out.println("Weight need to be non-negative number. Re-enter details.");
                    continue;
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("Weight need to be non-negative number. Re-enter details.");
                continue;
            }
            try
            {
                supplierCon.addSupplierItemToSupplier(supplier,catalogNumber,itemName,manufacturer,unitPrice,unitWeight,numberOfUnits,category);
                System.out.println("Item added successfully");
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("Item wasn't added");
            }
            break;
        }
    }

    private void addItemDiscount(Scanner scan, Supplier sup)
    {
        int catNumber;
        while (true)
        {
            try
            {
                System.out.println("Enter the catalog number of the item you want to add the discount to:");
                catNumber = scan.nextInt();
                scan.nextLine();
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("Catalog number must be a number");
                continue;
            }
            if(addItemUnitsDiscount(scan, catNumber, sup))
                break;
        }
    }
    private boolean addItemUnitsDiscount(Scanner scan, int catalogNumber, Supplier sup)//V
    {
        String discountKind = getItemDiscountKind(scan);
        String discountType = getDiscountType(scan);
        double discountSize = getDiscountSize(scan);
        double numberOfUnits = getValue(scan);
        try {
            supplierCon.addItemUnitsDiscount(sup,catalogNumber,discountKind,discountType,discountSize,numberOfUnits);
            System.out.println("Discount was added successfully.");
            return true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("details aren't valid, item discount wasn't added. please try again.");
        }
        return false;
    }

    private String getItemDiscountKind(Scanner scan)
    {
        System.out.println("Choose the discount kind:");
        System.out.println("ItemUnitDiscount - according to number of item's units.");
        int discountKind = 0;
        while (true)
        {
            try
            {
                discountKind = scan.nextInt();
                scan.nextLine();
                if (discountKind != 1)
                {
                    System.out.println("Right now we have only one kind of item discount. Please press 1 to choose it.");
                }
                else
                    break;
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("please enter a number");
            }

        }
        if (discountKind == 1)
            return "ItemUnitDiscount";
        return "";
    }

    private String getDiscountType(Scanner scan)
    {
        System.out.println("Choose the type of discount:");
        System.out.println("1 - percentage");
        System.out.println("2 - constant value");
        while(true)
        {
            try
            {
                int choise = scan.nextInt();
                scan.nextLine();
                if(choise==1)
                {
                    return "Percentage";
                }
                else if(choise==2)
                {
                    return "Constant";
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

    private double getDiscountSize(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Enter the size of discount:");
                double size = scan.nextDouble();
                scan.nextLine();
                if(size <= 0)
                    throw new InvalidParameterException();
                return size;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Discount size must be a number");
            }
            catch(InvalidParameterException e)
            {
                System.out.println("Discount size must be a positive number");
            }
        }
    }

    private double getValue(Scanner scan)
    {
        while (true)
        {
            try {
                System.out.println("Enter value:");
                double numOfUnits = scan.nextDouble();
                scan.nextLine();
                if(numOfUnits <= 0)
                    throw new InvalidParameterException();
                return numOfUnits;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("value must be a number");
            }
            catch(InvalidParameterException e)
            {
                System.out.println("value must be a positive number");
            }
        }
    }

    private int getOrderDiscountKind(Scanner scan)//V
    {
        System.out.println("Choose the discount kind:");
        System.out.println("1 - Order Units Discount - according to total number of units in order.");
        System.out.println("2 - Order Cost Discount - according to total cost of order.");
        int discountKind = 0;
        while (true)
        {
            try
            {
                discountKind = scan.nextInt();
                scan.nextLine();
                if (discountKind != 1 && discountKind != 2)
                {
                    System.out.println("please choose a number between 1-2");
                }
                else
                    break;
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("please enter a number between 1-2");
            }

        }

        return discountKind;
    }
    private void addOrderDiscount(Scanner scan, Supplier sup)//V
    {
        while (true)
        {
            int discountKind = getOrderDiscountKind(scan);
            String discKind;
            if (discountKind == 1)
                discKind = "OrderUnitsDiscount";
            else
                discKind = "OrderCostDiscount";
            String discountType = getDiscountType(scan);
            double discountSize = getDiscountSize(scan);
            double numberOfUnits = getValue(scan);
            try
            {
                supplierCon.AddOrderDiscount(sup,discKind,discountType,discountSize,numberOfUnits);
                System.out.println("Discount was added successfully.");
                break;
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                System.out.println("details aren't valid, order discount wasn't added. please try again.");
            }
        }
    }

    public void addDiscount()//V
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        System.out.println("Press 1- if you would like to add a discount for an item.");
        System.out.println("Press 2- if you would like to add a discount for an order.");
        while(true)
        {
            try
            {
                int choise = scan.nextInt();
                scan.nextLine();
                if(choise==1) //itemDiscount
                {
                    addItemDiscount(scan, supplier);
                    break;
                }
                else if(choise==2) //orderDiscount
                {
                    addOrderDiscount(scan,supplier);
                    break;
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

    private void removeItemDiscount(Scanner scan, Supplier sup)
    {
        int catNumber;
//        if(!sup.getSupplierContract().getDiscountDocument().supplierHasItemDiscount()) //supplier doesn't even have item discount
//        {
//            System.out.println("Supplier doesn't have any item discounts.");
//            return;
//        }
        while (true)
        {
            try
            {
                System.out.println("Enter the catalog number of the item you want to remove an item discount to:");
                catNumber = scan.nextInt();
                scan.nextLine();
                break;
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("Catalog number must be a number");
            }
        }
        String discountKind = getItemDiscountKind(scan);
        String discountType = getDiscountType(scan);
        double discountSize = getDiscountSize(scan);
        double numberOfUnits = getValue(scan);
        try
        {
            supplierCon.removeItemDiscount(sup,catNumber,discountKind,discountType,discountSize,numberOfUnits);
            System.out.println("Item discount was removed successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    private void removeOrderDiscount(Scanner scan, Supplier sup)
    {
//        if(!sup.getSupplierContract().getDiscountDocument().supplierHasOrderDiscount()) //supplier doesn't even have order discount
//        {
//            System.out.println("SuperLi.src.BusinessLogic.Supplier doesn't have any order discounts.");
//            return;
//        }
        String discKind;
        int discountKind = getOrderDiscountKind(scan);
        if (discountKind == 1)
            discKind = "OrderUnitsDiscount";
        else
            discKind = "OrderCostDiscount";
        String discountType = getDiscountType(scan);
        double discountSize = getDiscountSize(scan);
        double numberOfUnits = getValue(scan);
        try
        {
            supplierCon.removeOrderDiscount(sup,discKind, discountType, discountSize, numberOfUnits);
            System.out.println("Order discount was removed successfully.");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void removeDiscount()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        System.out.println("Press 1- if you would like to remove a discount for an item.");
        System.out.println("Press 2- if you would like to remove a discount for an order.");
        while(true)
        {
            try
            {
                int choise = scan.nextInt();
                scan.nextLine();
                if(choise==1) //itemDiscount
                {
                    removeItemDiscount(scan, supplier);
                    break;
                }
                else if(choise==2) //orderDiscount
                {
                    removeOrderDiscount(scan,supplier);
                    break;
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
    public void removeContact()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        System.out.println("Please enter the phone number of the contact you would like to remove:");
        String phone = scan.nextLine();
        try
        {
            supplierCon.removeContactFromSupplier(supplier,phone);
            System.out.println("Contact was removed successfully.");
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("Contact wasn't removed.");
        }
    }

    public void removeItem()
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        int catalogNumber;
        while(true)
        {
            try
            {
                System.out.println("Please enter the catalog number of the item you would like to remove: ");
                catalogNumber = scan.nextInt();
                scan.nextLine();
                if (catalogNumber <= 0)
                    throw new InvalidParameterException("catalog number must be non negative");
                break;
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("catalog number must be a number, please try again.");
            }
            catch (InvalidParameterException e)
            {
                System.out.println(e.getMessage());
            }
        }
        try
        {
            boolean removed = this.supplierCon.removeItem(supplier, catalogNumber);
            if (removed)
            {
                System.out.println("Item was removed successfully.");
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    public void UpdateSupplierDetails() {
        Scanner scan = new Scanner(System.in);
        int choise;
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        System.out.println("What details would you like to update?");
        System.out.println("1.Update payment way.");
        System.out.println("2.Update bank account number.");
        System.out.println("3.Update address.");
        while (true) {
            try {
                choise = scan.nextInt();
                scan.nextLine();
                while (choise < 1 || choise > 3) {
                    System.out.println("Please enter a number between 1-3.");
                    choise = scan.nextInt();
                    scan.nextLine();
                }
                break;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("You must enter a number between 1-3, please try again.");
            }
        }
        switch (choise)
        {
            case 1:
                updatePaymentWay(scan,supplier);
                break;
            case 2:
                updateBankAccount(scan,supplier);
                break;
            case 3:
                updateAddress(scan,supplier);
                break;
        }
    }

    private void updateUnitPrice(Scanner scan, Supplier sup, SupplierItem item) { //V
        double price;
        while (true)
        {
            System.out.println("Please enter the new price of item's unit:");
            try {
                price = scan.nextDouble();
                scan.nextLine();
                if (price <= 0) {
                    throw new InvalidParameterException("Price must be a positive number.");
                }
                break;
            } catch (InputMismatchException e) {
                scan.nextLine();
                System.out.println("Price must be a positive number.");
            } catch (InvalidParameterException e) {
                System.out.println(e.getMessage());
            }
        }
        boolean updated = this.supplierCon.updatePriceOfItem(sup, item, price);
        if (updated)
        {
             System.out.println("Price was updated successfully.");
        }
        else
            System.out.println("Could not update price.");

    }

    private void updateNumberOfUnits(Scanner scan, Supplier sup, SupplierItem item) //V
    {
        int numUnits;
        while (true)
        {
            System.out.println("Please enter the new number of units to supply for this item:");
            try
            {
                numUnits = scan.nextInt();
                scan.nextLine();
                if (numUnits <= 0)
                {
                    throw new InvalidParameterException("Number of units must be a positive number.");
                }
                break;
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("Number of units must be a positive number.");
            }
            catch (InvalidParameterException e)
            {
                System.out.println(e.getMessage());
            }
        }
        boolean updated = this.supplierCon.updateUnitsofItem(sup, item, numUnits);
        if (updated)
        {
            System.out.println("Amount of units was updated successfully.");
        }
        else
            System.out.println("Could not update Amount of units.");


    }
    public void UpdateItemDetails () //V
        {
            Scanner scan = new Scanner(System.in);
            int choise;
            Supplier supplier = findSupplierUser(scan);
            if (supplier == null)
                return;
            //get item's catalog number
            int catalogNum;
            System.out.println("Please enter item's catalog number:");
            while (true)
            {
                try
                {
                    catalogNum = scan.nextInt();
                    scan.nextLine();
                    break;
                }
                catch (InputMismatchException e)
                {
                    scan.nextLine();
                    System.out.println("Catalog number must be a number, please try again.");
                }
            }
            // find the item from the supplier
            SupplierItem item = supplierCon.getSupplierItemByCatalog(supplier, catalogNum);
            if (item == null)
            {
                System.out.println("There is no item with given catalog number.");
                return;
            }
            System.out.println("What details would you like to update for your item?");
            System.out.println("1.Update price for item's unit.");
            System.out.println("2.Update number of units to supply for this item.");
            while (true)
            {
                try
                {
                    choise = scan.nextInt();
                    scan.nextLine();
                    while (choise < 1 || choise > 2) {
                        System.out.println("Please enter a number between 1-2.");
                        choise = scan.nextInt();
                        scan.nextLine();
                    }
                    break;
                } catch (InputMismatchException e)
                {
                    scan.nextLine();
                    System.out.println("You must enter a number between 1-2, please try again.");
                }
            }
            switch (choise)
            {
                case 1:
                    updateUnitPrice(scan,supplier,item);
                    break;
                case 2:
                    updateNumberOfUnits(scan,supplier,item);
                    break;
            }
        }
//    public void supplierMenu()
//    {
//        Scanner scan = new Scanner(System.in);
//        int choise;
//        boolean flag = true;
//        while (flag)
//        {
//            System.out.println("What would you like to do?");
//            System.out.println("1.Add new contact.");
//            System.out.println("2.Remove contact.");
//            System.out.println("3.Add new category.");
//            System.out.println("4.Add new manufacturer.");
//            System.out.println("5.Show order history.");
//            System.out.println("6.Update details.");
//            System.out.println("7.Add new item to contract.");
//            System.out.println("8.Remove item from contract.");
//            System.out.println("9.Add new discount.");
//            System.out.println("10.Remove discount.");
//            System.out.println("11.Update item's details.");
//            System.out.println("12.Return to main menu.");
//            try
//            {
//                choise = scan.nextInt();
//                scan.nextLine();
//                while (choise < 1 || choise > 12)
//                {
//                    System.out.println("Please enter a number between 1-12.");
//                    choise = scan.nextInt();
//                    scan.nextLine();
//                }
//            }
//            catch(InputMismatchException e)
//            {
//                scan.nextLine();
//                System.out.println("You must enter a number between 1-12, please try again.");
//                continue;
//            }
//            switch (choise)
//            {
//                case 1:
//                    addNewContact();
//                    break;
//                case 2:
//                    removeContact();
//                    break;
//                case 3:
//                    addCategory();
//                    break;
//                case 4:
//                    addManufacturer();
//                    break;
//                case 5:
//                    showOrderHistory();
//                    break;
//                case 6:
//                    UpdateSupplierDetails();
//                    break;
//                case 7:
//                    addItem();
//                    break;
//                case 8:
//                    removeItem();
//                    break;
//                case 9:
//                    addDiscount();
//                    break;
//                case 10:
//                    removeDiscount();
//                    break;
//                case 11:
//                    UpdateItemDetails();
//                    break;
//                case 12:
//                    System.out.println("Thank you for using supplier menu, we hope to see you soon.");
//                    flag = false;
//                    break;
//            }
//        }
//    }
}
