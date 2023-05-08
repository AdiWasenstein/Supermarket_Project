package SuperLi.src.DataAccess;

import java.sql.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.*;

public abstract class ADataMapper<ObjectType> {
    protected abstract String insertQuery(ObjectType object); // Including inserting to identityMap
    protected abstract String deleteQuery(ObjectType object); // Including deleting from identityMap
    protected abstract String updateQuery(ObjectType object);
    protected abstract String findQuery(String ...key);
    protected abstract String findAllQuery();
    protected abstract ObjectType findInIdentityMap(String ...key);
    protected abstract ObjectType insertIdentityMap(ResultSet match) throws SQLException;
    public void insert(ObjectType object){
        executeVoidQuery(this::insertQuery, object);
    }
    public void update(ObjectType object){executeVoidQuery(this::updateQuery, object);}
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
    public Optional<ObjectType> find(String ...key){
        ObjectType identityMapObject = findInIdentityMap(key);
        if(identityMapObject != null)
            return Optional.of(identityMapObject);
        ResultSet matches = executeSelectQuery(findQuery(key));
        try{
            if(matches == null)
                throw new SQLException("SELECT query failed");
            if(matches.next())
                return Optional.of(insertIdentityMap(matches));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }
    public LinkedList<ObjectType> findAll(){
        LinkedList<ObjectType> objects = new LinkedList<>();
        ResultSet matches = executeSelectQuery(findAllQuery());
        if(matches == null)
            return objects;
        try{
            while (!matches.next())
                objects.add(insertIdentityMap(matches));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return objects;
    }
}