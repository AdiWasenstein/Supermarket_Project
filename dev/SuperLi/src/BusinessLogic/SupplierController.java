package SuperLi.src.BusinessLogic;


import SuperLi.src.DataAccess.SupplierDataMapper;
import SuperLi.src.DataAccess.SupplierItemDataMapper;
import com.sun.source.tree.TryTree;

import java.security.InvalidParameterException;
import java.util.LinkedList;
import java.util.Optional;

public class SupplierController {
    private static SupplierController instance = new SupplierController();
    private SupplierDataMapper supplierDataMapper;
    private SupplierItemDataMapper supplierItemDataMapper;

    private SupplierController()
    {

        this.supplierDataMapper = SupplierDataMapper.getInstance();
    }

    public static SupplierController getInstance()
    {
        return instance;
    }

    private boolean supplierHasContact(Supplier sup, String phone)//CHANGE!
    {
        for(Contact con : sup.getSupplierCard().getContacts())
        {
            if(con.GetPhoneNumber().equals(phone))
                return true;
        }
        return false;
    }

    //the func throws Exception if contact with given phone number already exists for the supplier,
    //InvalidParameterException if can't create contact (details are not valid).
    //else, adds the contact to supplier.
    public void addContactToSupplier(Supplier sup, String name, String phone, String email)throws InvalidParameterException, Exception
    {
        if(this.supplierHasContact(sup,phone))//contact already exists
            throw new Exception("supplier already has a contact with given phone number.");
        sup.addNewContact(name,phone,email);
        this.supplierDataMapper.update(sup);
    }

    // based on given id finds if exist in system or not.
    // returns the supplier if exist, else returns null.
    public Supplier findSupplierById(int id)
    {
        Optional<Supplier> supOptional = this.supplierDataMapper.find(Integer.toString(id));
        if(supOptional.isEmpty())
            return null;
        return supOptional.get();
    }

    //the func gets a list of categories, and adds all the categories that not already exist in the supplier, to the supplier.
    public void addCategoriesToSupplier(Supplier sup, LinkedList<String> addedCategories)
    {
        for (String category : addedCategories)
        {
            if (!(sup.getSupplierCatagories().contains(category)))
                sup.AddNewCategory(category);
        }
        this.supplierDataMapper.update(sup);
    }

    //the func gets a list of manufacturers, and adds all the manufacturers that not already exist in the supplier, to the supplier.
    public void addManufacturersToSupplier(Supplier sup, LinkedList<String> addedManufacturers)
    {
        for (String manufacturer : addedManufacturers)
        {
            if (!(sup.getSupplierManufacturers().contains(manufacturer)))
                sup.AddNewManufacturer(manufacturer);
        }
        this.supplierDataMapper.update(sup);
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
    public void updateSupplierPaymentWay(Supplier sup, PaymentsWays newPayment)
    {
        sup.setPayment(newPayment);
        this.supplierDataMapper.update(sup);
    }

    //this func updates supplier bank account
    public void updateSupplierBankAccount(Supplier sup, String newBank)
    {
        sup.setBankAccount(newBank);
        this.supplierDataMapper.update(sup);
    }

    public void updateSupplierAddress(Supplier sup, String newAddress)
    {
        sup.setAddress(newAddress);
        this.supplierDataMapper.update(sup);
    }

    //returns true if supplier works with given manufacturer AND category AND catalogNumber doesn't belong to another supplierItem. else, false.
    public boolean checkIfItemDetailsValid(Supplier sup,int catalogNumber, String manufacturer, String category)
    {
        return sup.getSupplierContract().canAddItem(catalogNumber, manufacturer, category);
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
        this.supplierDataMapper.insertSupplierItem(sItem,sup.getSupplierId());
    }

    //this func adds new itemUnitsDiscount to suppliers discount document, or throws Exception if impossible.
    public void addItemUnitsDiscount(Supplier sup, int catalogNumber, String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
        ItemDiscount newDis = sup.getSupplierContract().addItemDiscount(catalogNumber, discountKind, discountType, discountSize, numberOfUnits);
        if(newDis == null)
            throw new Exception("adding discount is impossible.");
        ItemUnitsDiscount discount;
        if(newDis instanceof ItemUnitsDiscount)
        {
            discount = (ItemUnitsDiscount)newDis;
            this.supplierDataMapper.insertItemUnitsDiscount(sup,catalogNumber,discount);
        }
    }

    //this func adds new orderDiscount to suppliers discount document, or throws Exception if impossible.
    public void AddOrderDiscount(Supplier sup,String discountKind, String discountType, double discountSize, double value)throws Exception
    {
        OrderDiscount newDis = sup.getSupplierContract().addOrderDiscount(discountKind, discountType, discountSize, value);
        if(newDis == null)
            throw new Exception("adding discount is impossible.");
        if(newDis instanceof OrderUnitsDiscount)
        {
            OrderUnitsDiscount discount = (OrderUnitsDiscount)newDis;
            this.supplierDataMapper.insertOrderUnitsDiscount(sup,discount);
        }
        else if(newDis instanceof OrderCostDiscount)
        {
            OrderCostDiscount dis = (OrderCostDiscount)newDis;
            this.supplierDataMapper.insertOrderCostDiscount(sup,dis);
        }
    }

    //this func removes item discount of supplier if possible. else, throws Exception.
    public void removeItemDiscount(Supplier sup, int catNumber, String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
        ItemDiscount removedDiscount = sup.getSupplierContract().removeItemDiscount(catNumber, discountKind, discountType, discountSize, numberOfUnits);
        if(removedDiscount instanceof ItemUnitsDiscount)
        {
            ItemUnitsDiscount discount = (ItemUnitsDiscount)removedDiscount;
            this.supplierDataMapper.deleteItemUnitsDiscount(sup, catNumber,discount);
        }
    }

    //this func removes order discount of supplier if possible. else, throws Exception.
    public void removeOrderDiscount(Supplier sup ,String discountKind, String discountType, double discountSize, double numberOfUnits)throws Exception
    {
        OrderDiscount removedDiscount = sup.getSupplierContract().removeOrderDiscount(discountKind, discountType, discountSize, numberOfUnits);
        if(removedDiscount instanceof OrderUnitsDiscount)
        {
            OrderUnitsDiscount discount = (OrderUnitsDiscount)removedDiscount;
            this.supplierDataMapper.deleteOrderUnitsDiscount(sup,discount);
        }
        else if(removedDiscount instanceof OrderCostDiscount)
        {
            OrderCostDiscount dis = (OrderCostDiscount)removedDiscount;
            this.supplierDataMapper.deleteOrderCostDiscount(sup, dis);
        }
    }

    //this func removes contact from supplier if possible. else, throws Exception.
    public void removeContactFromSupplier(Supplier sup, String phone)throws Exception
    {
        Contact con = sup.getSupplierCard().getContact(phone);
        sup.getSupplierCard().removeContact(phone);
        this.supplierDataMapper.deleteContact(sup,con);
    }


    // given supplier and catalog number return the supplier item object if exist, null if not.
    public SupplierItem getSupplierItemByCatalog(Supplier sup, int catalogNumber)
    {
        return sup.getSupplierContract().getSupplierItemByCatalogNum(catalogNumber);
    }

    public boolean updateUnitsofItem(Supplier sup, SupplierItem item, int newAmount)
    {
        try
        {
            item.SetNumberOfUnits(newAmount);
        }
        catch (InvalidParameterException e)
        {
            return false;
        }
        this.supplierDataMapper.updateSupplierItem(item,sup.getSupplierId());
        return true;
    }

    public boolean updatePriceOfItem(Supplier sup, SupplierItem item, double newPrice)
    {
        try
        {
            item.SetUnitPrice(newPrice);
        }
        catch (InvalidParameterException e)
        {
            return false;
        }
        this.supplierDataMapper.updateSupplierItem(item,sup.getSupplierId());
        return true;
    }

    public boolean removeItem(Supplier sup, int catalogNumber)
    {
        //remove suppjlierItem from supplier
        SupplierContract contract = sup.getSupplierContract();
        SupplierItem sItem = sup.getSupplierItemAccordingToCatalogNumber(catalogNumber);
        if(sItem == null)
            return false;
        try
        {
            contract.removeItem(catalogNumber);
        }
        catch (Exception e)
        {
            return false;
        }
        this.supplierDataMapper.deleteSupplierItem(sItem,sup.getSupplierId());
        return true;
    }

}
