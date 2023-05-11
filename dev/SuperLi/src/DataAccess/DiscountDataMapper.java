package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.Discount;
import SuperLi.src.BusinessLogic.ItemUnitsDiscount;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class DiscountDataMapper extends ADataMapper<Discount>{
    private static class MyKey {
        private int supplierId;
        private int catalogNumber;
        private int numberOfUnits;

        public MyKey(int supplierId, int catalogNumber, int numberOfUnits) {
            this.supplierId = supplierId;
            this.catalogNumber = catalogNumber;
            this.numberOfUnits = numberOfUnits;
        }

        public int getSupplierId()
        {
            return this.supplierId;
        }

        public int getCatalogNumber()
        {
            return this.catalogNumber;
        }

        public int getNumberOfUnits()
        {
            return this.numberOfUnits;
        }

        // Implement the equals() and hashCode() methods
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof MyKey)) return false;
            MyKey myKey = (MyKey) o;
            return supplierId == myKey.supplierId &&
                    catalogNumber == myKey.catalogNumber &&
                    numberOfUnits == myKey.numberOfUnits;
        }

        @Override
        public int hashCode() {
            return Objects.hash(supplierId, catalogNumber, numberOfUnits);
        }
    }

    Map<MyKey,ItemUnitsDiscount> itemUnitsDiscountIdentityMap;
    static DiscountDataMapper discountDataMapper = null;
    private DiscountDataMapper()
    {
        itemUnitsDiscountIdentityMap = new HashMap<>();
    }
    public static DiscountDataMapper getInstance()
    {
        if(discountDataMapper == null)
            discountDataMapper = new DiscountDataMapper();
        return discountDataMapper;
    }

    //remove unit items discounts from identity map whenever deleting supplierItem.
    public void removeFromIdentityMap(int supplierId, int catalogNumber)
    {
        for(MyKey key : this.itemUnitsDiscountIdentityMap.keySet())
        {
            if(key.getSupplierId() == supplierId && key.getCatalogNumber() == catalogNumber)
                this.itemUnitsDiscountIdentityMap.remove(key);
        }
    }

}
