package SuperLi.src.DataAccess;

import java.sql.*;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.*;

public abstract class ADataMapper<ObjectType> {
    static Connection connection = null;
    static Statement stmt;
    public void openConnection(){
        if(connection != null)
            return;
        try{
            connection = DriverManager.getConnection("jdbc:sqlite:SuppliersStock.db");
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
    }
    public void closeConnection(){
        if(connection == null)
            return;
        try{
            connection.close();
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        connection = null;
    }
    protected abstract String insertQuery(ObjectType object); // Including inserting to identityMap
    protected abstract String deleteQuery(ObjectType object); // Including deleting from identityMap
    protected abstract String updateQuery(ObjectType object);
    protected abstract String findQuery(String ...key);
    protected abstract String findAllQuery();
    protected String findAllQueryByKey(String ...key){return "";};
    protected abstract ObjectType findInIdentityMap(String ...key);
    protected abstract ObjectType insertIdentityMap(ResultSet match) throws SQLException;
    public void insert(ObjectType object){executeVoidQuery(this::insertQuery, object);}
    public void update(ObjectType object){executeVoidQuery(this::updateQuery, object);}
    public void delete(ObjectType object){executeVoidQuery(this::deleteQuery, object);}
    public void executeVoidQuery(String query){
        openConnection();
        if(connection == null)
            return;
        try{
            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");
            stmt.executeUpdate(query);
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
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
        ResultSet matches = null;
        try{
            stmt = connection.createStatement();
            stmt.execute("PRAGMA foreign_keys = ON;");
            matches = stmt.executeQuery(query);
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
		return matches;
	}
    public Optional<ObjectType> find(String ...key){
        ObjectType identityMapObject = findInIdentityMap(key);
        if(identityMapObject != null) {
            return Optional.of(identityMapObject);
        }
        ResultSet matches = executeSelectQuery(findQuery(key));
        Optional<ObjectType> result = Optional.empty();
        try{
            if(matches == null) {
                throw new SQLException("SELECT QUERY FAILED");
            }
            if(matches.next()){
                ObjectType object = insertIdentityMap(matches);
                if(object != null)
                    result = Optional.of(object);
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        return result;
    }
    public LinkedList<ObjectType> findAll(){
        LinkedList<ObjectType> objects = new LinkedList<>();
        openConnection();
        if(connection == null) {
            return objects;
        }
        ResultSet matches = executeSelectQuery(findAllQuery());
        if(matches == null) {
            closeConnection();
            return objects;
        }
        try{
            while (matches.next()) {
                ObjectType object = insertIdentityMap(matches);
                if(object != null)
                    objects.add(object);
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return objects;
    }
    public LinkedList<ObjectType> findAllByKey(){
        LinkedList<ObjectType> objects = new LinkedList<>();
        openConnection();
        if(connection == null) {
            return objects;
        }
        ResultSet matches = executeSelectQuery(findAllQueryByKey());
        if(matches == null) {
            closeConnection();
            return objects;
        }
        try{
            while (matches.next()) {
                ObjectType object = insertIdentityMap(matches);
                if(object != null)
                    objects.add(object);
            }
        }
        catch (SQLException e){
            System.out.println(this.getClass().toString() + e.getMessage());
        }
        closeConnection();
        return objects;
    }
}