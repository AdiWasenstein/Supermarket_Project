package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.Supplier;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Optional;

public class SupplierDataMapper implements IDataMapper<Supplier> {

    private HashMap<Integer, Supplier> suppliersIdentityMap;
    private static SupplierDataMapper instance = new SupplierDataMapper();

    private SupplierDataMapper()
    {
        this.suppliersIdentityMap = new HashMap<>();
    }

    public static SupplierDataMapper getInstance()
    {
        return instance;
    }

    public Optional<Supplier> find(String param)//get from db the first record that fits the parameters given
    {
        return null;
    }
    public LinkedList<Supplier> findAll(String param){//get from db all records that fit the parameters given
        return null;
    }
    public void insert(Supplier sup){}
    public void update(Supplier sup){}
    public void delete(Supplier sup){}
}
