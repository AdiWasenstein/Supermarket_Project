package SuperLi.src.test.StockTests.SuperLi.src.tests;

import SuperLi.src.BusinessLogic.*;
import SuperLi.src.DataAccess.SupplierDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

public class SupplierTest {
    private Supplier supp;
    @BeforeEach
    void setUp()
    {
            supp = SupplierDataMapper.getInstance().find(new String[]{"1"}).get();
    }

    @AfterEach
    void tearDown()
    {
        supp = null;
    }



    @Test
    void addTests()
    {
        supp.addNewContact("dan", "0524560846", "dans@gmail.com");
        Assertions.assertTrue(supp.isContactExist("0524560846", supp.getContacts()));

        supp.AddNewCategory("Soap");
        Assertions.assertTrue(supp.getSupplierCatagories().contains("Soap"));




        supp.AddNewManufacturer("Lalin");
        Assertions.assertTrue(supp.getSupplierManufacturers().contains("Lalin"));



    }

    @Test
    void canSupplyMarketItem()
    {
        Assertions.assertTrue(supp.canSupplyMarketItem(new Pair<>(12, 10)));
        Assertions.assertFalse(supp.canSupplyMarketItem(new Pair<>(12, 600)));
        Assertions.assertFalse(supp.canSupplyMarketItem(new Pair<>(2000, 1)));
    }

    @Test
    void setTests()
    {
        PaymentsWays paymentsWays = PaymentsWays.plus30;
        supp.setPayment(paymentsWays);
        Assertions.assertEquals(paymentsWays.ordinal(), supp.getPaymentWay().ordinal());

        supp.setBankAccount("1234");
        Assertions.assertEquals("1234", supp.getBankAccount());

        supp.setAddress("Givaa st.");
        Assertions.assertEquals("Givaa st.", supp.getAddress());

    }

    @Test
    void toStringTest()
    {
        String suppString = "Supplier's id: 1 , supplier's name: adi \n";
        Assertions.assertEquals(supp.toString(), suppString);

    }

    @Test
    void supplierItemToString()
    {
        SupplierItem item = supp.getSupplierItemAccordingToCatalogNumber(1);
        Assertions.assertEquals("Catalog number: 1, Market Id: 12, Item name: Milk 3%, Max amount to supply: 550", item.toString());
    }

    @Test
    void daysTillArrives()
    {
        Assertions.assertEquals(2, supp.daysTillArrives(Day.friday));
    }

    @Test
    void itemGettersTest()
    {

        SupplierItem item = supp.getSupplierItemAccordingToCatalogNumber(1);
        System.out.println(item.toString());
        Assertions.assertEquals(1, item.getCatalogNumber());
        Assertions.assertEquals("Milk 3%", item.getItemName());
        Assertions.assertEquals(10, item.getUnitPrice());
        Assertions.assertEquals(2, item.getUnitWeight());
        Assertions.assertEquals(550, item.getNumberOfUnits());
        Assertions.assertEquals("Dairy Products", item.getCatagory());
        Assertions.assertEquals("Tnuva", item.getManufacturer());

    }

    @Test
    void itemSettersTest()
    {

        SupplierItem item = supp.getSupplierItemAccordingToCatalogNumber(1);
        int numberUnits = item.getNumberOfUnits();
        double price = item.getUnitPrice();

        item.SetNumberOfUnits(60);
        Assertions.assertEquals(60, item.getNumberOfUnits());
        item.SetUnitPrice(40);
        Assertions.assertEquals(40, item.getUnitPrice());

        item.SetNumberOfUnits(numberUnits);
        item.SetUnitPrice(price);

    }

    @Test
    void itemDiscount()
    {
        SupplierContract supplierContract = supp.getSupplierContract();
        supplierContract.addItemDiscount(1, "ItemUnitDiscount", "percentage", 50, 50);
        Discount disc = supp.getSupplierContract().findItemDiscount(1, 50);
        Assertions.assertNotNull(disc);
        Assertions.assertTrue(disc.canUseTheDiscount(60));
        Assertions.assertFalse(disc.canUseTheDiscount(4));
        try
        {
            supp.getSupplierContract().removeItemDiscount(1, "ItemUnitDiscount", "percentage", 50, 50 );

        }
        catch (Exception e)
        {
            Assertions.assertNotNull(supp.getSupplierContract().findItemDiscount(1, 50));
        }
    }

    @Test
    void orderDiscount()
    {
        SupplierContract supplierContract = supp.getSupplierContract();
        supplierContract.addOrderDiscount("OrderUnitsDiscount", "Constant", 50, 40);
        Assertions.assertTrue(supplierContract.getDiscountDocument().supplierHasOrderDiscount());
    }
}