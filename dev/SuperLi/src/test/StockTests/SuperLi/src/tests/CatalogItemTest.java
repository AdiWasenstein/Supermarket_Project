package SuperLi.src.tests;
import SuperLi.src.BusinessLogic.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.LinkedList;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CatalogItemTest {
    private CatalogItem catalogItem;
    private LinkedList<String> categories = new LinkedList<>();
    private CostumerDiscount costumerDiscount;
    @BeforeEach
    void beforeEach() {
        categories.add("Dairy Products");
        categories.add("Milk");
        catalogItem = new CatalogItem(1, "Milk 3%", "Tnuva", 7.8, 100,
                new Category(categories, new Size(1000, MeasureUnit.ML)), 334, 1377);
        costumerDiscount = new CostumerDiscount(LocalDate.of(23, 5, 13), 50, true, 1);
    }
    @AfterEach
    void AfterEach() {
        categories = null;
        catalogItem = null;
        costumerDiscount = null;
    }
    @Test
    void testCheckCatalog() {
        assertEquals(1, catalogItem.getId());
        assertEquals("Milk 3%", catalogItem.getName());
        assertEquals("Tnuva", catalogItem.getManufacturer());
        assertEquals(7.8, catalogItem.getSellPrice());
        assertEquals(100, catalogItem.getMinCapacity());
        assertEquals(334, catalogItem.getShelvesLocation());
        assertEquals(1377, catalogItem.getBackLocation());
        assertEquals(7, catalogItem.getShelfLife());
        assertEquals(1000, catalogItem.getCategory().getSizeAmount());
        assertEquals(1, catalogItem.getCategory().getMeasureUnit().ordinal());
        assertEquals(categories, catalogItem.getCategory().getCategories());
    }
    @Test
    void testSetCatalog() {
        catalogItem.setMinCapacity(30);
        catalogItem.setSellPrice(1.33);
        catalogItem.setCostumerDiscount(costumerDiscount);
        assertEquals(30, catalogItem.getMinCapacity());
        assertEquals(1.33, catalogItem.getSellPrice());
        assertEquals(costumerDiscount, catalogItem.getCostumerDiscount());
    }
    @Test
    void testCheckCategory() {
        LinkedList<String> names = new LinkedList<>();
        names.add("Bread");
        Category category = new Category(names, new Size(10, MeasureUnit.UNIT));
        Assertions.assertNotEquals(category, catalogItem.getCategory());
        assertEquals(this.categories, catalogItem.getCategory().getCategories());
    }
    @Test
    void testToString() {
        Assertions.assertNotEquals("", catalogItem.toString());
        assertEquals("ID: 1; Dairy Products, Milk, 1000.0 MLs; Milk 3%; Manufacturer: Tnuva; 7.8ILS; Min Capacity: 100; Shelf Life: 7; CostumerDiscount: ", catalogItem.toString());
    }
}