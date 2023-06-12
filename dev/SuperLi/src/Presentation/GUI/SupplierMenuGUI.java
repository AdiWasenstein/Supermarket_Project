package SuperLi.src.Presentation.GUI;

import SuperLi.src.BusinessLogic.Supplier;
import SuperLi.src.BusinessLogic.SupplierController;

import java.util.LinkedList;
import java.util.function.Function;

public class SupplierMenuGUI extends AMenuGUI {
    private SupplierController supplierController;
    private static SupplierMenuGUI instance = null;

    public SupplierMenuGUI() {
        supplierController = SupplierController.getInstance();
        showMainMenu();
    }

    public static SupplierMenuGUI getInstance() {
        if (instance == null)
            instance = new SupplierMenuGUI();
        return instance;
    }


    public void showMainMenu() {
        LinkedList<String> optionsNames = new LinkedList<>();
        LinkedList<Runnable> operations = new LinkedList<>();

        optionsNames.add("1.Add new contact.");
        optionsNames.add("2.Remove contact.");
        optionsNames.add("3.Add new category.");
        optionsNames.add("4.Add new manufacturer.");
        optionsNames.add("5.Show order history.");
        optionsNames.add("6.Update details.");
        optionsNames.add("7.Add new item to contract.");
        optionsNames.add("8.Remove item from contract.");
        optionsNames.add("9.Add new discount.");
        optionsNames.add("10.Remove discount.");
        optionsNames.add("11.Update item's details.");

        operations.add(this::addNewContactPage);
        operations.add(this::removeContactPage);
        operations.add(this::addNewCatagoryPage);
        operations.add(this::addNewManufacturerPage);
        operations.add(this::showOrderHistoryPage);
        operations.add(this::updateDetailsPage);
        operations.add(this::addNewItemPage);
        operations.add(this::removItemPage);
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

    public void showOrderHistoryPage()
    {

    }

    public void updateDetailsPage()
    {

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


    public void removItemPage()
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

    }

    public void removeDiscountPage()
    {

    }

    public void updateItemDetailsPage()
    {

    }
    public static void main(String[] args){
        SupplierMenuGUI.getInstance();}

}
