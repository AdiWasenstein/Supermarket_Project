package SuperLi.src.BusinessLogic;


import SuperLi.src.DataAccess.SupplierDataMapper;
import SuperLi.src.DataAccess.SupplierItemDataMapper;
import com.sun.source.tree.TryTree;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class SupplierController {
    private static SupplierController instance = new SupplierController();
    private SupplierDataMapper supplierDataMapper;
    private SupplierItemDataMapper supplierItemDataMapper;

    private SupplierController()
    {

        this.supplierDataMapper = SupplierDataMapper.getInstance();
        this.supplierItemDataMapper = SupplierItemDataMapper.getInstance();
    }

    public static SupplierController getInstance()
    {
        return instance;
    }

    private boolean supplierHasContact(Supplier sup, String phone)//CHANGE!
    {
//        LinkedList<Contact> contacts = supplier.getSupplierCard().getContacts();
        return false;
    }

    //the func throws Exception if contact with given phone number already exists for the supplier,
    //InvalidParameterException if can't create contact (details are not valid).
    //else, adds the contact to supplier.
    public void addContactToSupplier(Supplier sup, String name, String phone, String email)throws InvalidParameterException, Exception//CHANGE!
    {
        //CHECK IF SUPPLIERHASCONTACT - THROW EXCEPTION.
//        supplier.addNewContact(name,phone,email);
    }

    // based on given id finds if exist in system or not.
    // returns the supplier if exist, else returns null.
    public Supplier findSupplierById(int id)//CHANGE!
    {
//        for (Supplier supplier : SuperLi.src.Presentation.MainMenu.allSuppliers)
//        {
//            if (supplier.isIdEquals(id))
//                return supplier;
//        }
        return null;
    }

    //the func gets a list of categories, and adds all the categories that not already exist in the supplier, to the supplier.
    public void addCategoriesToSupplier(Supplier sup, LinkedList<String> addedCategories)//CHANGE!
    {
//        for (String catagory : addedCategories)
//        {
//            if (!(supplier.getSupplierCatagories().contains(catagory)))
//                supplier.AddNewCategory(catagory);
//        }
    }

    //the func gets a list of manufacturers, and adds all the manufacturers that not already exist in the supplier, to the supplier.
    public void addManufacturersToSupplier(Supplier sup, LinkedList<String> addedManufacturers)//CHANGE!
    {
//        for (String manufacturer : addedManufacturers)
//        {
//            if (!(supplier.getSupplierManufacturers().contains(manufacturer)))
//                supplier.AddNewManufacturer(manufacturer);
//        }
    }

    //this func shows all order history of supplier
    public void showSupplierOrders(Supplier sup)//CHANGE!
    {
//        if(supplier.getOrders().isEmpty())
//        {
//            System.out.println("There are no orders to show.");
//            return;
//        }
//        for (SuperLi.src.BusinessLogic.Order order : supplier.getOrders())
//            System.out.println(order.toString());
    }

    //this func updates supplier paymentway
    public void updateSupplierPaymentWay(Supplier sup, PaymentsWays newPayment)//CHANGE!
    {
//        sup.setPayment(newPayment);
    }

    //this func updates supplier bank account
    public void updateSupplierBankAccount(Supplier sup, String newBank)//CHANGE!
    {
//        sup.setBankAccount(newBank);
    }

    public void updateSupplierAddress(Supplier sup, String newAddress)//CHANGE!
    {
//        sup.setAddress(newAddress);
    }

    //returns true if supplier works with given manufacturer AND category. else, false.
    public boolean checkIfSupplierHasManufacturerAndCategory(Supplier sup, String manufacturer, String category)//CHANGE!
    {
//        !(supplier.getSupplierContract().canAddItem(catalogNumber, manufacturer, category))
        return false;
    }


    //this func adds new supplier item to supplier if possible. else, throws EXCEPTION.
    public void addSupplierItemToSupplier(Supplier sup, int catalogNumber, String itemName, String manufacturer, double unitPrice, double unitWeight, int numberOfUnits, String category)throws Exception
    {
        if(sup == null)
            return;
        SupplierContract contract = sup.getSupplierContract();
        if(!contract.canAddItem(catalogNumber,manufacturer,category))
            throw new Exception("Adding new Item is impossible.");
        int marketId = StockManagementFacade.getInstance().catalogIdAccordingToNameManufacturerCategory(itemName, manufacturer, category);
        SupplierItem sItem = null;
        try
        {
            sItem = new SupplierItem(catalogNumber,itemName,manufacturer,unitPrice,unitWeight,numberOfUnits,category,marketId);
        }
        catch (InvalidParameterException e)
        {
            String message = e.getMessage();
            throw new Exception(message);
        }
        contract.addItem(sItem);
        this.supplierItemDataMapper.insert(sItem,sup.getSupplierId());
    }

    //this func adds new itemUnitsDiscount to suppliers discount document, or throws Exception if impossible.
    public void addItemUnitsDiscount(Supplier sup, int catalogNumber, String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
//        sup.getSupplierContract().addItemDiscount(catalogNumber, discountKind, discountType, discountSize, numberOfUnits)
    }

    //this func adds new orderDiscount to suppliers discount document, or throws Exception if impossible.
    public void AddOrderDiscount(Supplier sup,String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
//        if(sup.getSupplierContract().addOrderDiscount(discountKind, discountType, discountSize, numberOfUnits))
    }

    //this func removes item discount of supplier if possible. else, throws Exception.
    public void removeItemDiscount(Supplier sup, int catNumber, String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
//        sup.getSupplierContract().removeItemDiscount(catNumber, discountKind, discountType, discountSize, numberOfUnits);
    }

    //this func removes order discount of supplier if possible. else, throws Exception.
    public void removeOrderDiscount(Supplier sup ,String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
//        sup.getSupplierContract().removeOrderDiscount(discountKind, discountType, discountSize, numberOfUnits);
    }

    //this func removes contact from supplier if possible. else, throws Exception.
    public void removeContactFromSupplier(Supplier sup, String phone)throws Exception
    {
//        supplier.getSupplierCard().removeContact(phone);
    }

    // given supplier and catalog number return the supplier item object if exist, null if not.
    public SupplierItem getSupplierItemByCatalog(Supplier sup, int catalogNumber)
    {
        //check if supplier has an item with this catalog number.
//            if(!supplier.getSupplierContract().isitemExists(catalogNum))
//            {
//                System.out.println("There is no item with given catalog number.");
//                return;
//            }
        return null;
    }

    public boolean updateUnitsofItem(Supplier sup, SupplierItem item, int newAmount)
    {
        //sup.getSupplierItemAccordingToCatalogNumber(catNum).SetNumberOfUnits(numUnits);
//        System.out.println("Number of units was updated successfully.");
        return false;
    }

    public boolean updatePriceOfItem(Supplier sup, SupplierItem item, double newPrice)
    {
        //        sup.getSupplierItemAccordingToCatalogNumber(catNum).SetUnitPrice(price);
//        System.out.println("Price was updated successfully.");
        return false;
    }

    public boolean removeItem(Supplier sup, int catalogNumber)
    {
        SupplierContract contract = sup.getSupplierContract();
        //removing from contract
        contract.removeItem();
        //removing from periodic reports
        //removing from itemUnitsDiscounts
//        supplier.getSupplierContract().removeItem(catalogNumber);
//        System.out.println("Item was removed successfully.");
            return false;
    }
}
