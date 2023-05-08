package SuperLi.src.DataAccess;
import java.sql.*;
import java.util.*;

public abstract class ADataMapper<T> {
    public abstract String findQuery(String object);
    public abstract String findAllQuery();
    public abstract String insertQuery(T object);
    public abstract String deleteQuery(T object);
    public abstract String updateQuery(T object);

    public void executeQuery(Function<T, String> func, T object){

    }
    public void insert(T object){
        executeQuery(insertQuery, object);
    }
    public void insert(T object){
        Connection conn;
        Statement stmt;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:SuppliersStock.db");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return;
        }
        try{
            stmt = conn.createStatement();
            stmt.executeUpdate(insertQuery(object));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        try{
            conn.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    void update(T object){

    }
    void delete(T object){

    }
    Optional<T> find(String param){return null;}
}