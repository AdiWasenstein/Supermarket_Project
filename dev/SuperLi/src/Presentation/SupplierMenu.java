package SuperLi.src.Presentation;

import SuperLi.src.BusinessLogic.*;
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
        }
        catch (Exception e2)
        {
            System.out.println(e2.getMessage());
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
       LinkedList<String> addedCategories = AdminMenu.getInstance().getCategory(scan);//NEED TO CHANGE!
       if (addedCategories.isEmpty())
           System.out.println("No catagories added.");
       else
       {
           supplierCon.addCategoriesToSupplier(supplier,addedCategories);
           System.out.println("New categories were added.");
       }
   }
    public void addManufacturer()//V
    {
        Scanner scan = new Scanner(System.in);
        Supplier supplier = findSupplierUser(scan);
        if (supplier == null)
            return;
        LinkedList<String> addedManufacturers = AdminMenu.getManufacturers(scan);//NEED TO CHANGE!
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
        supplierCon.showSupplierOrders(supplier);
    }

    public void updatePaymentWay(Scanner scan, Supplier sup)
    {
        PaymentsWays newPayment = AdminMenu.getPaymentWays(scan);//NEED TO CHANGE!
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
        String newBank = AdminMenu.getBankAccount(scan);//NEED TO CHANGE!
        if (newBank.equals(""))
            System.out.println("No update was made.");
        else {
            supplierCon.updateSupplierBankAccount(sup,newBank);
            System.out.println("Bank account was updated.");
        }
    }

    public void updateAddress(Scanner scan, Supplier sup)
    {
        String newAddress = AdminMenu.getAddress(scan);//NEED TO CHANGE!
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
        int catalogNumber, numberOfUnits, marketId;
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
            if (!supplierCon.checkIfSupplierHasManufacturerAndCategory(supplier,manufacturer,category))
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
                marketId = supplierCon.getCatalogItemId(itemName, manufacturer, category);
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
                break;
            }
            try
            {
                supplierCon.addSupplierItemToSupplier(supplier,catalogNumber,itemName,manufacturer,unitPrice,unitWeight,numberOfUnits,category,marketId);
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
            System.out.println("SuperLi.src.BusinessLogic.Discount was added successfully.");
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
                System.out.println("SuperLi.src.BusinessLogic.Discount size must be a number");
            }
            catch(InvalidParameterException e)
            {
                System.out.println("SuperLi.src.BusinessLogic.Discount size must be a positive number");
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
        System.out.println("1 - Order Units SuperLi.src.BusinessLogic.Discount - according to total number of units in order.");
        System.out.println("2 - Order Cost SuperLi.src.BusinessLogic.Discount - according to total cost of order.");
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
                discKind = "SuperLi.src.BusinessLogic.OrderUnitsDiscount";
            else
                discKind = "SuperLi.src.BusinessLogic.OrderCostDiscount";
            String discountType = getDiscountType(scan);
            double discountSize = getDiscountSize(scan);
            double numberOfUnits = getValue(scan);
            try
            {
                supplierCon.AddOrderDiscount(sup,discKind,discountType,discountSize,numberOfUnits);
                System.out.println("SuperLi.src.BusinessLogic.Discount was added successfully.");
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
            discKind = "SuperLi.src.BusinessLogic.OrderUnitsDiscount";
        else
            discKind = "SuperLi.src.BusinessLogic.OrderCostDiscount";
        String discountType = getDiscountType(scan);
        double discountSize = getDiscountSize(scan);
        double numberOfUnits = getValue(scan);
        try
        {

            supplierCon.removeOrderDiscount(sup,discKind, discountType, discountSize, numberOfUnits);
            System.out.println("SuperLi.src.BusinessLogic.Order discount was removed successfully.");
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
        if (!updated)
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
        if (!updated)
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
    public static void supplierMenu()
    {
        Scanner scan = new Scanner(System.in);
        int choise;
        boolean flag = true;
        while (flag)
        {
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
            try
            {
                choise = scan.nextInt();
                scan.nextLine();
                while (choise < 1 || choise > 12)
                {
                    System.out.println("Please enter a number between 1-12.");
                    choise = scan.nextInt();
                    scan.nextLine();
                }
            }
            catch(InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("You must enter a number between 1-12, please try again.");
                continue;
            }
            switch (choise)
            {
                case 1:
                    addNewContact();
                    break;
                case 2:
                    removeContact();
                    break;
                case 3:
                    addCategory();
                    break;
                case 4:
                    addManufacturer();
                    break;
                case 5:
                    showOrderHistory();
                    break;
                case 6:
                    UpdateSupplierDetails();
                    break;
                case 7:
                    addItem();
                    break;
                case 8:
                    removeItem();
                    break;
                case 9:
                    addDiscount();
                    break;
                case 10:
                    removeDiscount();
                    break;
                case 11:
                    UpdateItemDetails();
                    break;
                case 12:
                    System.out.println("Thank you for using supplier menu, we hope to see you soon.");
                    flag = false;
                    break;
            }
        }
    }

//    public static void main(String[] args)
//    {
//        SupplierController s = new SupplierController();
////        for(int i=0;i<3;i++)
////        {
////            s.AddNewSupplierToSystem();
////        }
//        LinkedList<String> cat = new LinkedList<>();
//        cat.add("soap");
//        LinkedList<String> man = new LinkedList<>();
//        man.add("tnuva");
//        SuperLi.src.BusinessLogic.Contact c = new SuperLi.src.BusinessLogic.Contact("adi", "0526207807", "a@.com");
//        LinkedList<SuperLi.src.BusinessLogic.Contact> cons = new LinkedList<>();
//        cons.add(c);
//        SuperLi.src.BusinessLogic.SupplierCard cardAdi = new SuperLi.src.BusinessLogic.SupplierCard("adi", "add", 1, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//        SuperLi.src.BusinessLogic.SupplierCard cardTiltan = new SuperLi.src.BusinessLogic.SupplierCard("tiltan", "add", 2, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//        SuperLi.src.BusinessLogic.SupplierCard cardYoav = new SuperLi.src.BusinessLogic.SupplierCard("yoav", "add", 3, "123", SuperLi.src.BusinessLogic.PaymentsWays.direct, cons);
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers adi = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardAdi,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, adi);
//        adi.supplierContract = contract;
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers tiltan = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardTiltan,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract2 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, tiltan);
//        tiltan.supplierContract = contract2;
//
//        SuperLi.src.BusinessLogic.SupplierNotDelivers yoav = new SuperLi.src.BusinessLogic.SupplierNotDelivers(cat, man, cardYoav,null);
//        SuperLi.src.BusinessLogic.SupplierContract contract1 = new SuperLi.src.BusinessLogic.SupplierContract(SuperLi.src.BusinessLogic.PaymentsWays.direct, yoav);
//        yoav.supplierContract = contract1;
//
//        s.allSuppliers.add(adi);
//        s.allSuppliers.add(tiltan);
//        s.allSuppliers.add(yoav);
//
//        MarketItem s1 = new MarketItem(1, "milk", "tnuva");
//        MarketItem s2 = new MarketItem(2, "shoco", "tnuva");
//        MarketItem s3 = new MarketItem(3, "cheese", "tnuva");
//        MarketItem s4 = new MarketItem(4, "bread", "tnuva");
//        MarketItem s5 = new MarketItem(5, "bamba", "tnuva");
//        MarketItem s6 = new MarketItem(6, "bisli", "tnuva");
//
//
//        SupplierItem m1 = new SupplierItem(12, "cheese", "tnuva", 7, 10, 10, "soap", 3);
//        SupplierItem m2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
//        SupplierItem m3 = new SupplierItem(16, "bread", "tnuva", 10, 10, 10, "soap", 4);
//        SupplierItem m4 = new SupplierItem(16, "bamba", "tnuva", 10, 10, 10, "soap", 5);
//        SupplierItem m5 = new SupplierItem(16, "bisli", "tnuva", 10, 10, 10, "soap", 6);
//        SupplierItem m6 = new SupplierItem(16, "shoco", "tnuva", 5, 10, 10, "soap", 2);
//
//        SupplierItem n1 = new SupplierItem(12, "cheese", "tnuva", 10, 10, 10, "soap", 3);
//        SupplierItem n2 = new SupplierItem(14, "milk", "tnuva", 10, 10, 10, "soap", 1);
//        SupplierItem n3 = new SupplierItem(16, "bread", "tnuva", 800, 10, 10, "soap", 4);
//        SupplierItem n4 = new SupplierItem(16, "bamba", "tnuva", 200, 10, 10, "soap", 5);
//        SupplierItem n5 = new SupplierItem(16, "bisli", "tnuva", 500, 10, 10, "soap", 6);
//        SupplierItem n6 = new SupplierItem(16, "shoco", "tnuva", 2, 10, 10, "soap", 2);
//
////        adi.addItem(n1);
//        adi.addItem(n2);
//        adi.addItem(n6);
////        adi.addItem(m2);
//
////        tiltan.addItem(m1);
//        tiltan.addItem(m2);
//        tiltan.addItem(m6);
//
//
////        tiltan.addItem(m6);
////        tiltan.addItem(m3);
////        tiltan.addItem(m4);
////        tiltan.addItem(m5);
////        tiltan.addItem(m2);
////        tiltan.addItem(n2);//tiltan
//
//
//        yoav.addItem(m1);
//        yoav.addItem(m2);
////        yoav.addItem(m6);
//
//
////        yoav.addItem(m2);
////        yoav.addItem(m6);
////        yoav.addItem(m1);
////        yoav.addItem(m3);
////        yoav.addItem(m5);
//
//
//
////        for(int j=0;j<10;j++)
////            s.addItem();
////        s.addNewContact();
//        SuperLi.src.BusinessLogic.OrderManagment o = new SuperLi.src.BusinessLogic.OrderManagment();
//        LinkedList<Pair<Integer,Integer>> items = new LinkedList<>();
//        Pair<Integer,Integer> p1 = Pair.create(1, 10);
//        Pair<Integer,Integer> p2 = Pair.create(2, 10);
//        Pair<Integer,Integer> p3 = Pair.create(3, 10);
//        Pair<Integer,Integer> p4 = Pair.create(4, 10);
//        Pair<Integer,Integer> p5 = Pair.create(5, 10);
//        Pair<Integer,Integer> p6 = Pair.create(6, 10);
//        items.add(p1);
//        items.add(p2);
//        items.add(p3);
////        items.add(p4);
////        items.add(p5);
////        items.add(p6);
////        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>>> resultList = new LinkedList<>();
//        LinkedList<HashMap<SuperLi.src.BusinessLogic.Supplier,Integer>> resultList = new LinkedList<>();
////        HashMap<SuperLi.src.BusinessLogic.Supplier,LinkedList<Pair<Integer,Integer>>> combination = new HashMap<>();
//        HashMap<SuperLi.src.BusinessLogic.Supplier,Integer> combination = new HashMap<>();
////        o.combinationsForFullItems(s.allSuppliers,items,resultList,combination);
////        System.out.println(o.findCheappestCombination(resultList));
////        System.out.println(resultList);
//
//
////        s.AddNewSupplierToSystem();
//////        s.addItem();
////        o.combinationsForPartialItems(s.allSuppliers,1,26,resultList,combination);
////        System.out.println(resultList);
////        o.ITryMyBestOK(s.allSuppliers,1,31,combination,resultList);
////        System.out.println(resultList);
//        try {
//            System.out.println(o.startOrderProcess(items, s.allSuppliers));
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//    }
}
