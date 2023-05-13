package SuperLi.src.BusinessLogic;

import java.security.InvalidParameterException;
import java.util.LinkedList;

public class SupplierCard {
    int supplierId;
    String bankAccount;
    String supplierName;
    String supplierAddress;
    PaymentsWays payment;
    LinkedList<Contact> contacts;

    public SupplierCard(String supplierName, String supplierAddress, int supplierId, String bankAccount, PaymentsWays payment, String nameCon, String phoneCon, String emailCon) throws InvalidParameterException
    {
        if (!(bankAccount.matches("[0-9]+")))
            throw new InvalidParameterException("Bank account number is not valid");
        if (!(supplierName.matches("[a-zA-Z]+")))
            throw new InvalidParameterException("Name is not valid");
        if (supplierId < 0 )
            throw new InvalidParameterException("Id is not valid");
        if (supplierAddress.equals(""))
            throw new InvalidParameterException("Address is not valid");
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.payment = payment;
        Contact newCon = new Contact(nameCon,phoneCon,emailCon);
        this.contacts = new LinkedList<>();
        this.contacts.add(newCon);
    }

    public SupplierCard(String supplierName, String supplierAddress, int supplierId, String bankAccount, PaymentsWays payment, LinkedList<Contact> contacts) throws InvalidParameterException
    {
        if (!(bankAccount.matches("[0-9]+")))
            throw new InvalidParameterException("Bank account number is not valid");
        if (!(supplierName.matches("[a-zA-Z]+")))
            throw new InvalidParameterException("Name is not valid");
        if (supplierId < 0 )
            throw new InvalidParameterException("Id is not valid");
        if (supplierAddress.equals(""))
            throw new InvalidParameterException("Address is not valid");
        this.supplierName = supplierName;
        this.supplierAddress = supplierAddress;
        this.supplierId = supplierId;
        this.bankAccount = bankAccount;
        this.payment = payment;
        this.contacts = contacts;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        if (!(supplierName.matches("[a-zA-Z]+")))
            throw new InvalidParameterException("Name is not valid");
        this.supplierName = supplierName;
    }

    public String getSupplierAddress() {
        return supplierAddress;
    }

    public void setSupplierAddress(String supplierAddress) {
        this.supplierAddress = supplierAddress;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        if (!(bankAccount.matches("[0-9]+")))
            throw new InvalidParameterException("Bank account number is not valid");
        this.bankAccount = bankAccount;
    }

    public PaymentsWays getPayment() {
        return payment;
    }

    public void setPayment(PaymentsWays payment) {
        this.payment = payment;
    }

    public LinkedList<Contact> getContacts() {
        return contacts;
    }

//    public void setContacts(LinkedList<SuperLi.src.BusinessLogic.Contact> contacts) {
//        this.contacts = contacts;
//    }

    public void addNewContact(String name, String phone, String email)throws InvalidParameterException
    {
        Contact newCon = new Contact(name, phone, email);
        this.contacts.add(newCon);
    }

    //this method returns the index of the searched contact if exists. else, returns -1.
    private int indexOfContactIfExists(String phoneNumber)
    {
        for(int i=0;i<this.contacts.size();i++)
        {
            if(contacts.get(i).GetPhoneNumber().equals(phoneNumber))
            {
                return i;
            }
        }
        return -1;
    }

    public Contact getContact(String phoneNumber)
    {
        int index = this.indexOfContactIfExists(phoneNumber);
        if(index == -1)
            return null;
        return contacts.get(index);
    }

    public void removeContact(String phoneNumber)throws Exception
    {
        if (indexOfContactIfExists(phoneNumber) == -1)
        {
            throw new Exception("there is no contact of this supplier with given phone number.");
        }
        else if(contacts.size()==1)
        {
            throw new Exception("cant delete the last contact of supplier");
        }
        else
        {
            contacts.remove(indexOfContactIfExists(phoneNumber));
        }

    }

    public String toString()
    {
        return "SuperLi.src.BusinessLogic.Supplier card info: \n" + "Id: " +supplierId + "\n Bank account: " + bankAccount+ "\n Address: " +
                supplierAddress + "\n Payment way: " + payment+ "\n contacts: " + contacts;
    }

    public boolean isContactExist(String phone, LinkedList<Contact> contacts)
    {
        for (Contact contact : getContacts())
        {
            if (contact.GetPhoneNumber().equals(phone))
                return true;
        }
        return false;
    }
}
