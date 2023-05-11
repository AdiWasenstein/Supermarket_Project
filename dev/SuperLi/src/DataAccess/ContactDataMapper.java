package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class ContactDataMapper extends ADataMapper<Contact> {
    Map<String, Contact> contactIdentityMap;
    static ContactDataMapper contactDataMapper = null;
    private ContactDataMapper(){
        contactIdentityMap = new HashMap<>();
    }
    public static ContactDataMapper getInstance(){
        if(contactDataMapper == null)
            contactDataMapper = new ContactDataMapper();
        return contactDataMapper;
    }
    public String insertQuery(Contact contact)
    {
        contactIdentityMap.put(contact.GetPhoneNumber(), contact);
        return String.format("INSERT INTO Contacts(phoneNumber, name, email) VALUES ('%s', '%s', '%s')", contact.GetPhoneNumber(),
                contact.GetName(), contact.GetEmail());
    }
    public String deleteQuery(Contact contact)
    {
        contactIdentityMap.remove(contact.GetPhoneNumber());
        return String.format("DELETE FROM Branches WHERE phoneNumber = '%s'", contact.GetPhoneNumber());
    }

    public String updateQuery(Contact contact)
    {
        return String.format("UPDATE Branches SET email = '%s' WHERE phoneNumber = '%s'", contact.GetEmail(), contact.GetPhoneNumber());
    }
    public String findQuery(String ...key){
        return String.format("SELECT * FROM Contacts WHERE phoneNumber = '%s'", key[0]);
    }
    public String findAllQuery(){
        return "SELECT * FROM Contacts";
    }
    public Contact findInIdentityMap(String ...key){
        return contactIdentityMap.get((key[0]));
    }
    public Contact insertIdentityMap(ResultSet match) throws SQLException {
        if (match == null)
            return null;
        Contact contact = contactIdentityMap.get(match.getString("phoneNumber"));
        if(contact != null)
            return contact;
        contact = new Contact(match.getString("name"), match.getString("phoneNumber"), match.getString("email"));
        contactIdentityMap.put(contact.GetPhoneNumber(), contact);
        return contact;
    }


}