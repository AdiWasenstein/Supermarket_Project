package SuperLi.src.Presentation.GUI;

import SuperLi.src.BusinessLogic.PaymentsWays;
import SuperLi.src.BusinessLogic.Supplier;
import SuperLi.src.BusinessLogic.SupplierController;
import SuperLi.src.BusinessLogic.SupplierItem;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Function;

public class SupplierMenuGUI extends AMenuGUI {
    private SupplierController supplierController;
    private static SupplierMenuGUI instance = null;

//    private Supplier curr_supplier;
    public SupplierMenuGUI() {
        supplierController = SupplierController.getInstance();
        showMainMenu();
//        chooseSupplierPage();
    }

    public static SupplierMenuGUI getInstance() {
        if (instance == null)
            instance = new SupplierMenuGUI();
        return instance;
    }

//    public void chooseSupplierPage()
//    {
//        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
//        LinkedList<String> labels = new LinkedList<>();
//
//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
//        Function<LinkedList<String>, Boolean> operation = this::chooseSupplierOperation;
//        String success = "Supplier found.";
//        String failure = "Contact was not added to supplier.";
//        showFillPage(labels, optionsForField, operation, success, failure, true, 3);
//
//    }

//    public boolean chooseSupplierOperation(LinkedList<String> values)
//    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }
//        curr_supplier = supplier;
//        showMainMenu();
//        return true;
//    }
    public void showMainMenu() {
//        chooseSupplierPage();
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();

        optionsNames.add("1.Add new contact."); //V
        optionsNames.add("2.Remove contact."); //V
        optionsNames.add("3.Add new category.");
        optionsNames.add("4.Add new manufacturer.");
        optionsNames.add("5.Show order history.");
        optionsNames.add("6.Update details."); //V
        optionsNames.add("7.Add new supplier item."); //V
        optionsNames.add("8.Remove supplier item."); //V
        optionsNames.add("9.Add new discount."); //V
        optionsNames.add("10.Remove discount."); //V
        optionsNames.add("11.Update item's details."); //V

        operations.add(this::addNewContactPage);
        operations.add(this::removeContactPage);
        operations.add(this::addNewCatagoryPage);
        operations.add(this::addNewManufacturerPage);
        operations.add(this::showOrderHistoryPage);
        operations.add(this::updateDetailsPage);
        operations.add(this::addNewItemPage);
        operations.add(this::removeItemPage);
        operations.add(this::addDiscountPage);
        operations.add(this::removeDiscountPage);
        operations.add(this::updateItemDetailsPage);

        showOptionsMenu(optionsNames, operations);
    }

    public void addNewContactPage() {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Name");
        optionsForField.add(new LinkedList<>());
        labels.add("Phone number");
        optionsForField.add(new LinkedList<>());
        labels.add("Email address");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::addNewContactOperation;
        String success = "Contact added successfully.";
        String failure = "Contact was not added to supplier.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean addNewContactOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }
        String name = values.get(1);
        String phone = values.get(2);
        String email = values.get(3);
        try {
            supplierController.addContactToSupplier(supplier, name, phone, email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeContactPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));

        labels.add("Phone number");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeContactOperation;
        String success = "Contact was removed successfully.";
        String failure = "Contact was not removed from supplier.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);
   }

    public boolean removeContactOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String phone = values.get(1);

        try {
            supplierController.removeContactFromSupplier(supplier, phone);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void addNewCatagoryPage()
    {

    }

    public boolean addNewCatagoryOperation()
    {
        return true;
    }

    public void addNewManufacturerPage()
    {

    }

    public boolean addNewManufacturerOperation(LinkedList<String> values)
    {
        return true;
    }

    public void showOrderHistoryPage()
    {

    }

    public boolean showOrderHistoryOperation(LinkedList<String> values)
    {
        return true;
    }

    public void updateDetailsPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Update payment way.");
        optionsNames.add("2. Update bank account number.");
        optionsNames.add("3. Update address.");
        operations.add(this::updatePaymentPage);
        operations.add(this::updateBankPage);
        operations.add(this::updateAddressPage);
        showOptionsMenu(optionsNames, operations);
    }
    
    public void updatePaymentPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New Payment way");
        optionsForField.add(new LinkedList<>(Arrays.asList("direct", "plus30", "plus60")));


        Function<LinkedList<String>, Boolean> operation = this::updatePaymentOperation;
        String success = "Payment was updated successfully.";
        String failure = "Supplier was not found.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updatePaymentOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        PaymentsWays newPay = PaymentsWays.valueOf(values.get(1));
        supplierController.updateSupplierPaymentWay(supplier, newPay);
        return true;
    }


    public void updateBankPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New bank account");
        optionsForField.add(new LinkedList<>());


        Function<LinkedList<String>, Boolean> operation = this::updateBankOperation;
        String success = "Bank account was updated successfully.";
        String failure = "Bank account was not updated.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updateBankOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String newBank = values.get(1);
        supplierController.updateSupplierBankAccount(supplier, newBank);
        return true;
    }

    public void updateAddressPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New address");
        optionsForField.add(new LinkedList<>());


        Function<LinkedList<String>, Boolean> operation = this::updateAddressOperation;
        String success = "Address was updated successfully.";
        String failure = "Address was not updated.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updateAddressOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String newAdd = values.get(1);
        supplierController.updateSupplierAddress(supplier, newAdd);
        return true;
    }

    public void addNewItemPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());
        labels.add("Item's name");
        optionsForField.add(new LinkedList<>());
        labels.add("Category");
        optionsForField.add(new LinkedList<>());
        labels.add("Manufacturer");
        optionsForField.add(new LinkedList<>());
        labels.add("Number of units");
        optionsForField.add(new LinkedList<>());
        labels.add("Unit's price");
        optionsForField.add(new LinkedList<>());
        labels.add("Unit's weight");
        optionsForField.add(new LinkedList<>());


        Function<LinkedList<String>, Boolean> operation = this::addItemOperation;
        String success = "Item was added successfully.";
        String failure = "Couldn't add this item.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    private Boolean addItemOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        String name = values.get(2);
        String category = values.get(3);
        String manufacturer = values.get(4);
        int numberOfUnits = generateInt(values.get(5));
        double priceOfUnit = generateDouble(values.get(6));
        double weightOfUnit = generateDouble(values.get(7));
        if (!supplierController.checkIfItemDetailsValid(supplier, catNum, manufacturer, category))
            return false;
        try
        {
            supplierController.addSupplierItemToSupplier(supplier, catNum, name, manufacturer, priceOfUnit, weightOfUnit, numberOfUnits, category);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }


    public void removeItemPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeItemOperation;
        String success = "Item was removed successfully.";
        String failure = "Couldn't remove this item.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean removeItemOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        try
        {
            boolean removed = supplierController.removeItem(supplier, catNum);
            return removed;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void addDiscountPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Add item discount");
        optionsNames.add("2. Add order discount");
        operations.add(this::addItemDiscountPage);
        operations.add(this::addOrderDiscountPage);
        showOptionsMenu(optionsNames, operations);
    }

    public void addItemDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount kind");
        optionsForField.add(new LinkedList<>(Arrays.asList("Item Units Discount")));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal amounts for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::addItemDiscountOperation;
        String success = "Item's discount was added successfully.";
        String failure = "Couldn't add this item discount.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean addItemDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        String kind = values.get(2);
        String type = values.get(3);
        int minimalAmount = generateInt(values.get(4));
        double size = generateDouble(values.get(5));
        try
        {
            supplierController.addItemUnitsDiscount(supplier, catNum, kind, type, size, minimalAmount);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void addOrderDiscountPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Add order units discount");
        optionsNames.add("2. Add order cost discount");
        operations.add(this::addOrderUnitsDiscountPage);
        operations.add(this::addOrderPriceDiscountPage);
        showOptionsMenu(optionsNames, operations);
    }

    public void addOrderUnitsDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal amounts for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::addOrderUnitsDiscountOperation;
        String success = "Order discount was added successfully.";
        String failure = "Couldn't add this order discount.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);
    }

    public boolean addOrderUnitsDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String type = values.get(1);
        int minimalAmount = generateInt(values.get(2));
        double size = generateDouble(values.get(3));
        try
        {
            supplierController.AddOrderDiscount(supplier, "OrderUnitsDiscount",type, size, minimalAmount);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void addOrderPriceDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal cost for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::addOrderPriceDiscountOperation;
        String success = "Order discount was added successfully.";
        String failure = "Couldn't add this order discount.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean addOrderPriceDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String type = values.get(1);
        double minimalCost = generateDouble(values.get(2));
        double size = generateDouble(values.get(3));
        try
        {
            supplierController.AddOrderDiscount(supplier, "OrderCostDiscount",type, size, minimalCost);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void removeDiscountPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Remove item discount");
        optionsNames.add("2. Remove order discount");
        operations.add(this::removeItemDiscountPage);
        operations.add(this::removeOrderDiscountPage);
        showOptionsMenu(optionsNames, operations);
    }

    public void removeItemDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount kind");
        optionsForField.add(new LinkedList<>(Arrays.asList("Item Units Discount")));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal amounts for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeItemDiscountOperation;
        String success = "Item's discount was removed successfully.";
        String failure = "Couldn't remove this item discount- discount doesn't exits.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean removeItemDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        String kind = values.get(2);
        String type = values.get(3);
        int minimalAmount = generateInt(values.get(4));
        double size = generateDouble(values.get(5));
        try
        {
            supplierController.removeItemDiscount(supplier, catNum, kind, type, size, minimalAmount);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void removeOrderDiscountPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Remove order units discount");
        optionsNames.add("2. Remove order cost discount");
        operations.add(this::removeOrderUnitsDiscountPage);
        operations.add(this::removeOrderCostDiscountPage);
        showOptionsMenu(optionsNames, operations);
    }

    public void removeOrderUnitsDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal amounts for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeOrderUnitsDiscountOperation;
        String success = "Order discount was removed successfully.";
        String failure = "Couldn't remove this order discount.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean removeOrderUnitsDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String type = values.get(1);
        int minimalAmount = generateInt(values.get(2));
        double size = generateDouble(values.get(3));
        try
        {
            supplierController.removeOrderDiscount(supplier, "OrderUnitsDiscount",type, size, minimalAmount);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void removeOrderCostDiscountPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Discount type");
        optionsForField.add(new LinkedList<>(Arrays.asList("Percentage", "Constant")));
        labels.add("Minimal cost for discount");
        optionsForField.add(new LinkedList<>());
        labels.add("Discount size");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeOrderCostDiscountOperation;
        String success = "Order discount was removed successfully.";
        String failure = "Couldn't remove this order discount.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean removeOrderCostDiscountOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        String type = values.get(1);
        double minimalCost = generateDouble(values.get(2));
        double size = generateDouble(values.get(3));
        try
        {
            supplierController.removeOrderDiscount(supplier, "OrderCostDiscount",type, size, minimalCost);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void updateItemDetailsPage()
    {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();
        optionsNames.add("1. Update unit's price.");
        optionsNames.add("2. Update number of units.");
        operations.add(this::updateItemPricePage);
        operations.add(this::updateItemUnitsPage);
        showOptionsMenu(optionsNames, operations);
    }

    public void updateItemPricePage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());
        labels.add("New price");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::updateItemPriceOperation;
        String success = "Item's price was updated successfully.";
        String failure = "Update failed.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }
    public boolean updateItemPriceOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        SupplierItem item = supplier.getSupplierItemAccordingToCatalogNumber(catNum);
        if (item == null)
            return false;
        double price = generateDouble(values.get(2));
        try
        {
            boolean updated = supplierController.updatePriceOfItem(supplier, item, price);
            return updated;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void updateItemUnitsPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(new LinkedList<>());
        labels.add("New amount");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::updateItemUnitsOperation;
        String success = "Item's amount was updated successfully.";
        String failure = "Update failed.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }
    public boolean updateItemUnitsOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }

        int catNum = generateInt(values.get(1));
        SupplierItem item = supplier.getSupplierItemAccordingToCatalogNumber(catNum);
        if (item == null)
            return false;
        int amount = generateInt(values.get(1));
        try
        {
            boolean updated = supplierController.updateUnitsofItem(supplier, item, amount);
            return updated;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public static void main(String[] args){
        SupplierMenuGUI.getInstance();}

}
