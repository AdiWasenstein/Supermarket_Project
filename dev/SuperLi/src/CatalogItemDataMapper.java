package SuperLi.src;

import java.util.*;

public class CatalogItemDataMapper implements IDataMapper<CatalogItem>{
    Map<Integer, CatalogItem> catalogItemsIdentitiyMap;
    static CatalogItemDataMapper catalogItemDataMapper = null;
    private CatalogItemDataMapper(){
        catalogItemsIdentitiyMap = new HashMap<>();
    }
    public static CatalogItemDataMapper getInstance() {
        if (catalogItemDataMapper == null)
            catalogItemDataMapper = new CatalogItemDataMapper();
        return catalogItemDataMapper;
    }
    public Optional<CatalogItem> find(String param){
        return Optional.empty();
    }
    public LinkedList<CatalogItem> findAll(String param){
        return new LinkedList<>();
    }
    public void insert(CatalogItem object){
    }
    public void update(CatalogItem object){
    }
    public void delete(CatalogItem object){
    }
}
