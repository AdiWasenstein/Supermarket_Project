package SuperLi.src.DataAccess;

import SuperLi.src.BusinessLogic.CatalogItem;
import SuperLi.src.BusinessLogic.Category;

import java.util.*;

public class CatalogItemDataMapper implements IDataMapper<CatalogItem> {
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
    public void insert(CatalogItem object){
    }
    public void update(CatalogItem object){
    }
    public void delete(CatalogItem object){
    }
    public LinkedList<CatalogItem> findAllFromCategory(Category category){return new LinkedList<>();}
    public LinkedList<CatalogItem> findAllFromCategory(LinkedList<String> categories) {
        return new LinkedList<>();
    }
    public LinkedList<CatalogItem> findAll(){return new LinkedList<>();}
}
