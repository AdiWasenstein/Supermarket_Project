package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Contact;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class ContactDataMapper implements IDataMapper<Contact>{
    private HashMap<String,Contact> contactsIdentityMap;
    private static ContactDataMapper instance = new ContactDataMapper();

    private ContactDataMapper()
    {
        this.contactsIdentityMap = new HashMap<>();
    }

    public static ContactDataMapper getInstance()
    {
        return instance;
    }

    public Optional<Contact> find(String phoneNumber)
    {
        return null;//REMOVE
    }
    public LinkedList<Contact> findAll(String phoneNumber)//NOT SURE WE NEED THIS HERE
    {
        return null;//REMOVE
    }
    public void insert(Contact con)
    {

    }
    public void update(Contact con)
    {

    }
    public void delete(Contact con)
    {

    }
}