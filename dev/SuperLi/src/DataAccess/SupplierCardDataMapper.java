package SuperLi.src.DataAccess;
import SuperLi.src.BusinessLogic.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Optional;

public class SupplierCardDataMapper extends ADataMapper<SupplierCard>
{
    Map<Integer, SupplierCard> supplierCardIdentityMap;
    static SupplierCardDataMapper supplierCardDataMapper = null;
    private SupplierCardDataMapper()
    {
        supplierCardIdentityMap = new HashMap<>();
    }
    public static SupplierCardDataMapper getInstance()
    {
        if(supplierCardDataMapper == null)
            supplierCardDataMapper = new SupplierCardDataMapper();
        return supplierCardDataMapper;
    }

    private void insertContactToContactTable(SupplierCard card)
    {
        for(Contact contact : card.getContacts())
        {
            Optional<Contact> contactOptional = ContactDataMapper.getInstance().find(contact.GetPhoneNumber());
            if(contactOptional.isEmpty())//contact doesn't already exists in db
                ContactDataMapper.getInstance().insert(contact);
        }
    }

    private void insertContactToContactAndSuppliersTable(SupplierCard card)
    {
        for(Contact contact : card.getContacts())
        {
            String query = String.format("SELECT * FROM SuppliersANDContacts WHERE supplierId=%d" +
                            "AND phoneNumber=%s", card.getSupplierId(), contact.GetPhoneNumber());
            ResultSet matches = executeSelectQuery(query);
            if (matches == null)//need to add to SuppliersANDContacts table the contact
            {
                String queryFields = String.format("INSERT INTO SuppliersANDContacts(supplierId, phoneNumber)" +
                        " VALUES (%d, %s)", card.getSupplierId(),contact.GetPhoneNumber());
                executeVoidQuery(queryFields);
            }
            closeConnection();
        }
    }

    protected String insertQuery(SupplierCard card)
    {
        this.supplierCardIdentityMap.put(card.getSupplierId(),card);
        //insert contacts
        this.insertContactToContactTable(card);
        this.insertContactToContactAndSuppliersTable(card);
        //insert to SupplierCards Table
        String queryFields = String.format("INSERT INTO SupplierCards(supplierId, bankAccount, supplierName," +
                "supplierAddress, paymentWay) VALUES (%d, %s,%s,%s,%s)", card.getSupplierId(), card.getBankAccount(),card.getSupplierName(),card.getSupplierAddress(),
                card.getPayment().toString());
        return queryFields;
    }

    protected String deleteQuery(SupplierCard object) {
        return "";
    }


    protected String updateQuery(SupplierCard card)
    {
        //insert contacts that don't already exist
        this.insertContactToContactTable(card);
        this.insertContactToContactAndSuppliersTable(card);
        //update card
        String queryFields = String.format("UPDATE SupplierCards SET supplierId = %d , bankAccount = %s, supplierName = %s," +
                        "supplierAddress = %s, paymentWay = %s",card.getSupplierId(), card.getBankAccount(),card.getSupplierName(),card.getSupplierAddress(),
                card.getPayment().toString());
        return queryFields;
    }

    protected SupplierCard findInIdentityMap(String ...key)
    {
        return this.supplierCardIdentityMap.get(Integer.valueOf(key[0]));
    }

    protected String findQuery(String ... key)
    {
        return String.format("SELECT * FROM SupplierCards WHERE supplierId = %s" ,key[0]);
    }

    protected String findAllQuery() {
        return "";
    }

    protected SupplierCard insertIdentityMap(ResultSet match) throws SQLException {
        if(match == null)
            return null;
        SupplierCard card = this.supplierCardIdentityMap.get(match.getInt("supplierId"));
        if(card != null)
            return card;
        int supplierId = match.getInt("supplierId");
        String bankAccount = match.getString("bankAccount");
        String supplierName = match.getString("supplierName");
        String supplierAddress = match.getString("supplierAddress");
        String payment = match.getString("paymentWay");
        PaymentsWays pay;
        if(payment.equals("direct"))
            pay = PaymentsWays.direct;
        else if(payment.equals("plus30"))
            pay = PaymentsWays.plus30;
        else // if plus60
        {
            pay = PaymentsWays.plus60;
        }
        LinkedList<Contact> contacts = ContactDataMapper.getInstance().findAllByKey(Integer.toString(supplierId));
        card = new SupplierCard(supplierName,supplierAddress,supplierId,bankAccount,pay,contacts);
        this.supplierCardIdentityMap.put(supplierId,card);
        return card;
    }

}
