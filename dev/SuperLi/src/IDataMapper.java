package SuperLi.src;
import java.util.*;

public interface IDataMapper<T> {
    public Optional<T> find(String param);
    public void insert(T object);
    public void update(T object);
    public void delete(T object);
}