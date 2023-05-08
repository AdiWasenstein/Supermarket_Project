package SuperLi.src.DataAccess;

import java.sql.*;
import java.util.Optional;
import java.util.function.*;

public abstract class ADataMapper<ObjectType> {
    public abstract String insertQuery(ObjectType object);
    public abstract String deleteQuery(ObjectType object);
    public abstract String updateQuery(ObjectType object);
    public abstract Optional<ObjectType> find(String key);
    //TODO - Check if key needs to be generic (and if find needs to be included in the interface)
    public void insert(ObjectType object){
        executeVoidQuery(this::insertQuery, object);
    }
    public void update(ObjectType object){
        executeVoidQuery(this::updateQuery, object);
    }
    public void delete(ObjectType object){
        executeVoidQuery(this::deleteQuery, object);
    }
    public void executeVoidQuery(Function<ObjectType, String> queryFunc, ObjectType object){
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
            stmt.executeUpdate(queryFunc.apply(object));
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
    public ResultSet executeSelectQuery(String query){
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
            matches = stmt.executeQuery(query);
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