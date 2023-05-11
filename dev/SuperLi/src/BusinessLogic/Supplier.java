package SuperLi.src.BusinessLogic;

//import org.graalvm.collections.Pair;
import SuperLi.src.BusinessLogic.Pair;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public abstract class Supplier {
    LinkedList<String> supplierCatagories;
    LinkedList<String> supplierManufacturers;
    SupplierCard supplierCard;
    SupplierContract supplierContract;
    LinkedList<Order> orders;

    public abstract boolean needDelivery();
//    public abstract Date arrivalTime(Date dateOfOrder);
    public abstract String printSupplyTimeData();

    public Supplier(LinkedList<String> supplierCatagories, LinkedList<String> supplierManufacturers, SupplierCard supplierCard, SupplierContract supplierContract) {
        if (supplierCatagories.isEmpty())
            throw new InvalidParameterException("Supplier must supply items of at least one catagory");
        if (supplierManufacturers.isEmpty())
            throw new InvalidParameterException("Supplier must supply items of at least one manufacturer");
        if (supplierCard == null)
            throw new InvalidParameterException("Supplier must have supplier card");
        this.supplierCatagories = supplierCatagories;
        this.supplierManufacturers = supplierManufacturers;
        this.supplierCard = supplierCard;
        this.supplierContract = supplierContract;
        this.orders = new LinkedList<>();
    }

    public boolean isContactExist(String phone, LinkedList<Contact> contacts)
    {
        return this.getSupplierCard().isContactExist(phone,contacts);
    }

    public void addNewContact(String name, String phone, String email)throws InvalidParameterException
    {
        this.getSupplierCard().addNewContact(name,phone,email);
    }

    public LinkedList<String> getSupplierCatagories()
    {
        return this.supplierCatagories;
    }

    public LinkedList<String> getSupplierManufacturers()
    {
        return this.supplierManufacturers;
    }

    public SupplierCard getSupplierCard()
    {
        return this.supplierCard;
    }

    public int getSupplierId()
    {
        return this.supplierCard.getSupplierId();
    }

    public SupplierContract getSupplierContract()
    {
        return this.supplierContract;
    }

    public LinkedList<Order> getOrders()
    {
        return this.orders;
    }

    public void AddNewCategory(String newCat)
    {
        if(newCat.equals(""))
        {
            throw new InvalidParameterException("category must have a name");
        }
        if (this.supplierCatagories.contains(newCat))
        {
            throw new InvalidParameterException("catagory already exist");

        }
        this.supplierCatagories.add(newCat);
    }

    public void AddNewManufacturer(String newMan)
    {
        if(newMan.equals(""))
        {
            throw new InvalidParameterException("manufacturer must have a name");
        }
        if (this.supplierManufacturers.contains(newMan))
        {
            throw new InvalidParameterException("manufacturer already exist");

        }
        this.supplierManufacturers.add(newMan);
    }

    public boolean equals(Supplier other)
    {
        return this.getSupplierId() == other.getSupplierId();
    }

    public boolean isIdEquals(int id)
    {
        return id == this.getSupplierId();
    }

    public boolean canSupplyMarketItem(Pair<Integer, Integer> pairMarketIdAndQuantity)
    {
        return this.supplierContract.checkItemIDandAmount(pairMarketIdAndQuantity);
    }

    public int getNumberOfItemUnitsCanSupply(int marketId)
    {
        return this.supplierContract.numberOfUnitsCanSupply(marketId);
    }

    public void setPayment(PaymentsWays newPayment)
    {
        if (newPayment == null)
            return;
        this.supplierCard.setPayment(newPayment);
        this.supplierContract.setPayment(newPayment);
    }

    public void setBankAccount(String newBank)
    {
        if (newBank.equals(""))
            return;
        this.supplierCard.setBankAccount(newBank);
    }

    public void setAddress(String newAddress)
    {
        if (newAddress.equals(""))
            return;
        this.supplierCard.setSupplierAddress(newAddress);
    }

    public void setSupplierContract(SupplierContract contract)
    {
        if (contract != null)
        {
            this.supplierContract = contract;
        }
    }

    public void addItem(SupplierItem newItem)
    {
        this.getSupplierContract().addItem(newItem);
    }

    public SupplierItem findMatchToMarketItem(int marketID)
    {
        for (SupplierItem item : this.getSupplierContract().getSupplierItems())
        {
            if (item.GetMarketId() == marketID)
                return item;
        }
        return null;
    }

    public SupplierItem getSupplierItemAccordingToCatalogNumber(int catalogNumber)
    {
       if(this.getSupplierContract().checkIfItemExists(catalogNumber) == -1)
           return null;
       int index = this.getSupplierContract().checkIfItemExists(catalogNumber);
       return this.supplierContract.getSupplierItems().get(index);
    }

    public LinkedList<Contact> getContacts()
    {
        return this.supplierCard.getContacts();
    }

    public void addOrder(Order order)
    {
        if (order != null)
            this.orders.add(order);
    }


    public LinkedList<SupplierItem> getAllSuppItem()
    {
        return this.supplierContract.getSupplierItems();
    }

    public String allDataOfSupplier()
    {
        return "SuperLi.src.BusinessLogic.Supplier Name: " + this + "\n" +  supplierCard + "\n Catagories: " + supplierCatagories + "\n Manufacturers: " + supplierManufacturers
                +" \n Items: " + getAllSuppItem() + "\n Supply information: " + printSupplyTimeData();
    }
    @Override
    public String toString()
    {
        return "Supplier's id: " + this.supplierCard.getSupplierId()+ " , supplier's name: " + this.getSupplierCard().getSupplierName()+" \n";
    }


    public abstract int daysTillArrives(Day dayOfOrder);
}
