package SuperLi.src.DataAccess;

import java.sql.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.*;

public abstract class ADataMapper<ObjectType> {
    static Connection connection = null;
    public void openConnection(){
        if(connection != null)
            return;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:SuppliersStock.db");
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
    }
    public void closeConnection(){
        if(connection == null)
            return;
        try{
            connection.close();
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        connection = null;
    }
    protected abstract String insertQuery(ObjectType object); // Including inserting to identityMap
    protected abstract String deleteQuery(ObjectType object); // Including deleting from identityMap
    protected abstract String updateQuery(ObjectType object);
    protected abstract String findQuery(String ...key);
    protected abstract String findAllQuery();
    protected abstract ObjectType findInIdentityMap(String ...key);
    protected abstract ObjectType insertIdentityMap(ResultSet match) throws SQLException;
    public void insert(ObjectType object){executeVoidQuery(this::insertQuery, object);}
    public void update(ObjectType object){executeVoidQuery(this::updateQuery, object);}
    public void delete(ObjectType object){executeVoidQuery(this::deleteQuery, object);}
    public void executeVoidQuery(String query){
        Statement stmt;
        openConnection();
        if(connection == null)
            return;
        try{
            stmt = connection.createStatement();
            stmt.executeUpdate(query);
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        closeConnection();
    }
    public void executeVoidQuery(Function<ObjectType, String> queryFunc, ObjectType object){
        executeVoidQuery(queryFunc.apply(object));
    }
    public ResultSet executeSelectQuery(String query){
        openConnection();
        if(connection == null)
            return null;
        Statement stmt;
        ResultSet matches = null;
        try{
            stmt = connection.createStatement();
            matches = stmt.executeQuery(query);
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
        Optional<ObjectType> object = Optional.empty();
        try{
            if(matches == null)
                throw new SQLException("SELECT QUERY FAILED");
            if(matches.next())
                object = Optional.of(insertIdentityMap(matches));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        closeConnection();
        return object;
    }
    public LinkedList<ObjectType> findAll(){
        LinkedList<ObjectType> objects = new LinkedList<>();
        openConnection();
        if(connection == null)
            return objects;
        ResultSet matches = executeSelectQuery(findAllQuery());
        if(matches == null)
            return objects;
        try{
            while (matches.next())
                objects.add(insertIdentityMap(matches));
        }
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
        closeConnection();
        return objects;
    }
}