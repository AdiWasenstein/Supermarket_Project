package Presentation.GUI;

import BusinessLogic.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

public class SupplierMenuGUI extends AMenuGUI {
    private final SupplierController supplierController;
    private static SupplierMenuGUI instance = null;

    private Supplier curr_supplier;
    public SupplierMenuGUI() {
        supplierController = SupplierController.getInstance();
    }

    public static SupplierMenuGUI getInstance() {
        if (instance == null)
            instance = new SupplierMenuGUI();
        return instance;
    }
    public static SupplierMenuGUI getInstance(Runnable exitFunction){
        instance = getInstance();
        instance.exitFunction = exitFunction;
        return instance;
    }
    public void communicate(){
        chooseSupplierPage();
    }
    public void chooseSupplierPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Supplier's ID");
        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        Function<LinkedList<String>, Boolean> operation = this::chooseSupplierOperation;
        String success = "Supplier found.";
        String failure = "Supplier was not found.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean chooseSupplierOperation(LinkedList<String> values)
    {
        int id = generateInt(values.get(0));
        if (id == -1)
            return false;
        Supplier supplier = supplierController.findSupplierById(id);
        if(supplier == null)
        {
            return false;
        }
        curr_supplier = supplier;
        showMainMenu();
        return true;
    }
    public void showMainMenu() {
        if(curr_supplier == null){
            communicate();
            return;
        }
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();

        optionsNames.add("1.Add new contact."); //V
        optionsNames.add("2.Remove contact."); //V
        optionsNames.add("3.Add new category."); //V
        optionsNames.add("4.Add new manufacturer."); //V
        optionsNames.add("5.Show order history."); //V
        optionsNames.add("6.Update details."); //V
        optionsNames.add("7.Add new supplier item."); //V
        optionsNames.add("8.Remove supplier item."); //V
        optionsNames.add("9.Add new discount."); //V
        optionsNames.add("10.Remove discount."); //V
        optionsNames.add("11.Update item's details."); //V
        optionsNames.add("12.Change supplier."); //V

        operations.add(this::addNewContactPage);
        operations.add(this::removeContactPage);
        operations.add(this::addNewCategoryPage);
        operations.add(this::addNewManufacturerPage);
        operations.add(this::showOrderHistoryPage);
        operations.add(this::updateDetailsPage);
        operations.add(this::addNewItemPage);
        operations.add(this::removeItemPage);
        operations.add(this::addDiscountPage);
        operations.add(this::removeDiscountPage);
        operations.add(this::updateItemDetailsPage);
        operations.add(this::chooseSupplierPage);

        showOptionsMenu(optionsNames, operations);
    }

    public void addNewContactPage() {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Name of contact");
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier != null)
//        {
//            return false;
//        }
        String name = values.get(0);
        String phone = values.get(1);
        String email = values.get(2);
        try {
            supplierController.addContactToSupplier(curr_supplier, name, phone, email);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void removeContactPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));

        labels.add("Phone number");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::removeContactOperation;
        String success = "Contact was removed successfully.";
        String failure = "Contact was not removed from supplier.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);
    }

    public boolean removeContactOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String phone = values.get(0);

        try {
            supplierController.removeContactFromSupplier(curr_supplier, phone);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public void addNewCategoryPage()
    {
//        chooseSupplierPage();

        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Category");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<LinkedList<String>>, Boolean> operation = this::addNewCategoryOperation;
        String success = "Added all possible categories.";
        String failure = "Failed to add- categories already exists.";
        showInfiniteFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean addNewCategoryOperation(LinkedList<LinkedList<String>> values)
    {
        LinkedList<String> categories = new LinkedList<>();
        for (LinkedList<String> list : values)
            categories.add(list.get(0));
        try
        {
            supplierController.addCategoriesToSupplier(curr_supplier, categories);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void addNewManufacturerPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

        labels.add("Manufacturer");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<LinkedList<String>>, Boolean> operation = this::addNewManufacturerOperation;
        String success = "Added all possible manufacturers.";
        String failure = "Failed to add- manufacturers already exists.";
        showInfiniteFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean addNewManufacturerOperation(LinkedList<LinkedList<String>> values)
    {
        LinkedList<String> manufacturers = new LinkedList<>();
        for (LinkedList<String> list : values)
            manufacturers.add(list.get(0));
        try
        {
            supplierController.addManufacturersToSupplier(curr_supplier, manufacturers);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public void showOrderHistoryPage()
    {
        LinkedList<String> labelNames = new LinkedList<>();
        LinkedList<Order> ordersOfSupplier = supplierController.getAllOrdersOfSupplier(curr_supplier.getSupplierId());
        for (Order curr : ordersOfSupplier ) {
            labelNames.add("Order " + curr.getOrderNumber());
        }
        orderSelector(labelNames, ordersOfSupplier);

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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New Payment way");
        optionsForField.add(new LinkedList<>(Arrays.asList("direct", "plus30", "plus60")));


        Function<LinkedList<String>, Boolean> operation = this::updatePaymentOperation;
        String success = "Payment was updated successfully.";
        String failure = "Supplier was not found.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updatePaymentOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }
        if (generateInt(values.get(0)) == -1)
            return false;
        PaymentsWays newPay = PaymentsWays.valueOf(values.get(0));
        supplierController.updateSupplierPaymentWay(curr_supplier, newPay);
        return true;
    }


    public void updateBankPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New bank account");
        optionsForField.add(new LinkedList<>());


        Function<LinkedList<String>, Boolean> operation = this::updateBankOperation;
        String success = "Bank account was updated successfully.";
        String failure = "Bank account was not updated.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updateBankOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String newBank = values.get(0);
        supplierController.updateSupplierBankAccount(curr_supplier, newBank);
        return true;
    }

    public void updateAddressPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("New address");
        optionsForField.add(new LinkedList<>());


        Function<LinkedList<String>, Boolean> operation = this::updateAddressOperation;
        String success = "Address was updated successfully.";
        String failure = "Address was not updated.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean updateAddressOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String newAdd = values.get(0);
        supplierController.updateSupplierAddress(curr_supplier, newAdd);
        return true;
    }

    public void addNewItemPage()
    {
        LinkedList<LinkedList<String>> optionsForField = new LinkedList<>();
        LinkedList<String> labels = new LinkedList<>();

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        String name = values.get(1);
        String category = values.get(2);
        String manufacturer = values.get(3);
        int numberOfUnits = generateInt(values.get(4));
        double priceOfUnit = generateDouble(values.get(5));
        double weightOfUnit = generateDouble(values.get(6));
        if (!supplierController.checkIfItemDetailsValid(curr_supplier, catNum, manufacturer, category))
            return false;
        try
        {
            supplierController.addSupplierItemToSupplier(curr_supplier, catNum, name, manufacturer, priceOfUnit, weightOfUnit, numberOfUnits, category);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));

        Function<LinkedList<String>, Boolean> operation = this::removeItemOperation;
        String success = "Item was removed successfully.";
        String failure = "Couldn't remove this item.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }

    public boolean removeItemOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        if (catNum == -1)
            return false;
        try
        {
            return supplierController.removeItem(curr_supplier, catNum);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));
        labels.add("Discount kind");
        optionsForField.add(new LinkedList<>(List.of("Item Units Discount")));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        String kind = values.get(1);
        String type = values.get(2);
        int minimalAmount = generateInt(values.get(3));
        double size = generateDouble(values.get(4));
        if (minimalAmount == -1 || size == -1 || catNum == -1)
            return false;
        try
        {
            supplierController.addItemUnitsDiscount(curr_supplier, catNum, kind, type, size, minimalAmount);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String type = values.get(0);
        int minimalAmount = generateInt(values.get(1));
        double size = generateDouble(values.get(2));
        if (minimalAmount == -1 || size == -1)
            return false;
        try
        {
            supplierController.AddOrderDiscount(curr_supplier, "OrderUnitsDiscount",type, size, minimalAmount);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String type = values.get(0);
        double minimalCost = generateDouble(values.get(1));
        double size = generateDouble(values.get(2));
        if (minimalCost == -1 || size == -1)
            return false;
        try
        {
            supplierController.AddOrderDiscount(curr_supplier, "OrderCostDiscount",type, size, minimalCost);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));
        labels.add("Discount kind");
        optionsForField.add(new LinkedList<>(List.of("Item Units Discount")));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        String kind = values.get(1);
        String type = values.get(2);
        int minimalAmount = generateInt(values.get(3));
        double size = generateDouble(values.get(4));
        if (minimalAmount == -1 || size == -1 || catNum == -1)
            return false;
        try
        {
            supplierController.removeItemDiscount(curr_supplier, catNum, kind, type, size, minimalAmount);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String type = values.get(0);
        int minimalAmount = generateInt(values.get(0));
        double size = generateDouble(values.get(1));
        if (minimalAmount == -1 || size == -1)
            return false;
        try
        {
            supplierController.removeOrderDiscount(curr_supplier, "OrderUnitsDiscount",type, size, minimalAmount);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
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
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        String type = values.get(0);
        double minimalCost = generateDouble(values.get(1));
        double size = generateDouble(values.get(2));
        if (minimalCost == -1 || size == -1)
            return false;
        try
        {
            supplierController.removeOrderDiscount(curr_supplier, "OrderCostDiscount",type, size, minimalCost);
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
//
//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));
        labels.add("New price");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::updateItemPriceOperation;
        String success = "Item's price was updated successfully.";
        String failure = "Update failed.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }
    public boolean updateItemPriceOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        if (catNum == -1)
            return false;
        SupplierItem item = curr_supplier.getSupplierItemAccordingToCatalogNumber(catNum);
        if (item == null)
            return false;
        double price = generateDouble(values.get(1));
        if (price == -1)
            return false;
        try
        {
            return supplierController.updatePriceOfItem(curr_supplier, item, price);
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

//        labels.add("Supplier's ID");
//        optionsForField.add(new LinkedList<>(supplierController.findAllSuppliersID()));
        labels.add("Catalog number");
        optionsForField.add(supplierController.getAllSupplierItemsID(curr_supplier));
        labels.add("New amount");
        optionsForField.add(new LinkedList<>());

        Function<LinkedList<String>, Boolean> operation = this::updateItemUnitsOperation;
        String success = "Item's amount was updated successfully.";
        String failure = "Update failed.";
        showFillPage(labels, optionsForField, operation, success, failure, true, 3);

    }
    public boolean updateItemUnitsOperation(LinkedList<String> values)
    {
//        int id = generateInt(values.get(0));
//        Supplier supplier = supplierController.findSupplierById(id);
//        if(supplier == null)
//        {
//            return false;
//        }

        int catNum = generateInt(values.get(0));
        if (catNum == -1)
            return false;
        SupplierItem item = curr_supplier.getSupplierItemAccordingToCatalogNumber(catNum);
        if (item == null)
            return false;
        int amount = generateInt(values.get(1));
        if (amount == -1)
            return false;
        try
        {
            return supplierController.updateUnitsofItem(curr_supplier, item, amount);
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public static void main(String[] args){
        SupplierMenuGUI.getInstance().communicate();}

}
