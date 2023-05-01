package SuperLi.src.DataAccess;
import java.util.*;

public interface IDataMapper<T> {
    Optional<T> find(String param);
    void insert(T object);
    void update(T object);
    void delete(T object);
}