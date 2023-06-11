package SuperLi.src.Presentation.GUI;
import SuperLi.src.BusinessLogic.*;

import java.util.LinkedList;

public class SuppliersMenuGUI extends AMenuGUI{
    private SupplierController supplierController;
    private static SuppliersMenuGUI instance = null;

    public SuppliersMenuGUI()
    {
        supplierController = SupplierController.getInstance();
        showMainMenu();
    }

    public static SuppliersMenuGUI getInstance()
    {
        if (instance == null)
            instance = new SuppliersMenuGUI();
        return instance;
    }


    public void showMainMenu()
    {
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
        optionsNames.add("12.Return to main menu.");

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

    public void addNewContactPage()
    {

    }

    public void removeContactPage()
    {

    }

    public void addNewCatagoryPage()
    {

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

    }

    public void removItemPage()
    {

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


}
