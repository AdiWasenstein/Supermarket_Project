package SuperLi.src.DataAccess;
import java.sql.*;
import java.util.*;
import java.util.function.*;

public abstract class ADataMapper<T> {
    public abstract String findQuery(String object);
    public abstract String findAllQuery();
    public abstract String insertQuery(T object);
    public abstract String deleteQuery(T object);
    public abstract String updateQuery(T object);

    public void executeVoidQuery(Function<T, String> queryFunc, T object){
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
            stmt.executeUpdate(queryFunc(object));
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
    public void insert(T object){
        executeVoidQuery(insertQuery, object);
    }
    public void update(T object){
		executeVoidQuery(updateQuery, object);
    }
    public void delete(T object){
		executeVoidQuery(deleteQuery, object);
    }
    public ResultSet getMatching(String selectQuery){
		Connection conn;
        Statement stmt;
		ResultSet matches = null;
        try{
            conn = DriverManager.getConnection("jdbc:sqlite:SuppliersStock.db");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
            return matches;
        }
        try{
            stmt = conn.createStatement();
            matches = stmt.executeUpdate(selectQuery));
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
		return matches;
	}
}