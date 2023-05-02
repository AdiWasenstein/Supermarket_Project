package SuperLi.src;

import SuperLi.src.BusinessLogic.Order;
import SuperLi.src.BusinessLogic.Supplier;
import SuperLi.src.BusinessLogic.Branch;
import SuperLi.src.Presentation.AdminMenu;
import SuperLi.src.Presentation.OrderMenu;
import SuperLi.src.Presentation.SupplierMenu;

import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.Scanner;
import java.sql.*;

public class SystemManagment {
    static LinkedList<Supplier> allSuppliers = new LinkedList<>();
    static LinkedList<Order> allOrders = new LinkedList<>();
    static LinkedList<Branch> allBranches = new LinkedList<>();

    public static void startSystem() {
        Scanner scan = new Scanner(System.in);
        int choise;
        boolean flag = true;
        while (flag)
        {
            System.out.println("Welcome to Super - li market!");
            System.out.println("Please tell us who you are.");
            System.out.println("1. I'm a supplier manager.");
            System.out.println("2. I'm the admin.");
            System.out.println("3. I'm the order manager.");
            System.out.println("4. Actually, i would like to exit menu.");
            try
            {
                choise = scan.nextInt();
                scan.nextLine();
                while (choise < 1 || choise > 4)
                {
                    System.out.println("Please enter a number between 1-4.");
                    choise = scan.nextInt();
                    scan.nextLine();
                }
            }
            catch (InputMismatchException e)
            {
                scan.nextLine();
                System.out.println("Please enter a number between 1-4.");
                continue;
            }
            switch (choise)
            {
                case 1:
                    SupplierMenu.supplierMenu();
                    break;
                case 2:
                    AdminMenu.getInstance().adminMenu();
                    break;
                case 3:
                    OrderMenu.getInstance().ordersrMenu();
                    break;
                case 4:
                    System.out.println("Thank you for using Super - li system, we hope to see you soon.");
                    flag = false;
                    break;
            }
        }
    }
    public static void main(String[] args) throws SQLException
    {
        Connection conn = DriverManager.getConnection("jdbc:sqlite:StockSuppliers.db");
        Statement stmt = conn.createStatement();
        String sql = "CREATE TABLE Suppliers (supplierId INTEGER PRIMARY KEY);";//V
        stmt.executeUpdate(sql);
        String sq = "CREATE TABLE Branches (branchNumber INTEGER PRIMARY KEY, address TEXT);";
        stmt.executeUpdate(sq);
        String sql1 = "CREATE TABLE Contacts (phoneNumber TEXT PRIMARY KEY, name TEXT, email TEXT);";//V
        stmt.executeUpdate(sql1);
        String sqlA = "CREATE TABLE SuppliersANDContacts (supplierId INTEGER, phoneNumber TEXT," +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId)," +
                "FOREIGN KEY (phoneNumber) REFERENCES Contacts(phoneNumber)," +
                "PRIMARY KEY (supplierId,phoneNumber));";//V
        stmt.executeUpdate(sqlA);
        String sqlB = "CREATE TABLE SuppliersDeliverRegular (supplierId INTEGER, day TEXT, FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId), " +
                "PRIMARY KEY (supplierId,day);)";//V
        stmt.executeUpdate(sqlB);
        String sqlD = "CREATE TABLE SuppliersDeliverERegular (supplierId INTEGER, numberOfDays INTEGER, " +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId), PRIMARY KEY (supplierId);)";//V
        stmt.executeUpdate(sqlD);
        String sqlE = "CREATE TABLE SuppliersANDCategories (supplierId INTEGER, category TEXT, " +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId), PRIMARY KEY(supplierId, category));";//V
        stmt.executeUpdate(sqlE);
        String sqlF = "CREATE TABLE SuppliersANDManufacturers (supplierId INTEGER, manufacturer TEXT, " +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId), PRIMARY KEY(supplierId, manufacturer));";//V
        stmt.executeUpdate(sqlF);
        String sql2 = "CREATE TABLE SuppliersItems (catalogNumber INTEGER, itemName TEXT, " +
                "manufacturer TEXT,catagory TEXT, unitPrice DOUBLE,unitWeight DOUBLE, numberOfUnits INTEGER, " +
                "marketId INTEGER, supplierId INTEGER, FOREIGN KEY (supplierId) REFERENCES Supplier(supplierId), " +
                "FOREIGN KEY (marketId) REFERENCES CatalogItems(id)) , PRIMARY KEY(catalogNumber, supplierId));";//V
        stmt.executeUpdate(sql2);
        String sql3 = "CREATE TABLE OrdersItems (amount INTEGER, itemDiscount DOUBLE,finalPrice DOUBLE, supplierCatalogNumber INTEGER, " +
                "orderId INTEGER, FOREIGN KEY (supplierCatalogNumber) REFERENCES SuppliersItems(catalogNumber)," +
                "FOREIGN KEY (orderId) REFERENCES Orders(orderId), PRIMARY KEY(supplierCatalogNumber,orderId));";
        stmt.executeUpdate(sql3);
        String sql4 = "CREATE TABLE Orders (orderNumber INTEGER PRIMARY KEY, supplierId INTEGER, OrderDate DATE, " +
                "costOfOrder DOUBLE, branchNumber INTEGER, FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId)," +
                "FOREIGN KEY (branchNumber) REFERENCES Branches(branchNumber);)";
        stmt.executeUpdate(sql4);
        String sqlG = "CREATE TABLE SuppliersANDOrders (supplierId INTEGER, orderId INTEGER, " +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId),FOREIGN KEY (orderId) REFERENCES Orders(orderId)" +
                "PRIMARY KEY(supplierId, orderId));";
        stmt.executeUpdate(sqlG);
        String sql5 = "CREATE TABLE SupplierCards (supplierId INTEGER PRIMARY KEY, bankAccount TEXT, supplierName TEXT, " +
                "supplierAddress TEXT, paymentWay TEXT," +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId));";
        stmt.executeUpdate(sql5);
        String sql6 = "CREATE TABLE ItemUnitsDiscounts (discountSize DOUBLE, discountType TEXT, numberOfUnitsOfItem INTEGER," +
                "supplierId INTEGER, supplierCatalogNumber INTEGER," +
                "FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId)," +
                "FOREIGN KEY (supplierCatalogNumber) REFERENCES SuppliersItems(catalogNumber)," +
                "PRIMARY KEY(supplierId, supplierCatalogNumber, numberOfUnitsOfItem));";
        stmt.executeUpdate(sql6);
        String sql7 = "CREATE TABLE OrderUnitsDiscounts (discountSize DOUBLE, discountType TEXT, numberOfUnitsInOrder INTEGER" +
                "supplierId INTEGER, FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId),PRIMARY KEY(supplierId,numberOfUnitsInOrder));";
        stmt.executeUpdate(sql7);
        String sql8 = "CREATE TABLE OrderCostDiscounts (discountSize DOUBLE, discountType TEXT, cost DOUBLE" +
                "supplierId INTEGER, FOREIGN KEY (supplierId) REFERENCES Suppliers(supplierId),PRIMARY KEY(supplierId,cost));";
        stmt.executeUpdate(sql8);
        String sql9 = "CREATE TABLE PeriodicReports ()";//NEED TO COMPLETE!
//        stmt.executeUpdate(sql9);
        conn.close();
//        UserMenu us = new UserMenu();
//        if (us.initialize_data() == 1) {
//            initialize_Catalog(us);
//            for (int i = 0; i < 15; i++)
//                initialize_Items(us, i);
//            initialize_discount(us);
//        }
//        us.communicate();// Labohen
        startSystem();
    }

}
