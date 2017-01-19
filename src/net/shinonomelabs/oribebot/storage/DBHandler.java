/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot.storage;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Eliza Bland
 */
public class DBHandler {
    private Connection con = null;
   
    public DBHandler(String url, String user, String pass) throws SQLException {
        con = DriverManager.getConnection(url,user,pass);
    }
    
    public List<Yasuna> getAllYasunas() {
        ArrayList<Yasuna> yasunas = new ArrayList<>();
        try(Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT image_id,description FROM oribebot.data");
            while(rs.next()) yasunas.add(new Yasuna(rs.getString("image_id"),rs.getString("description")));
        } finally {
            return yasunas;
        }
    }
    
    public List<Yasuna> getPostableYasunas() {
        ArrayList<Yasuna> yasunas = new ArrayList<>();
        try(Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT image_id,description FROM oribebot.data WHERE posted=0");
            while(rs.next()) yasunas.add(new Yasuna(rs.getString("image_id"),rs.getString("description")));
        } finally {
            return yasunas;
        }
    }
    
    public String getDescription(String imageHash) {
        try(Statement stmt = con.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT description FROM oribebot.data WHERE image_id=\"" + imageHash + "\"");
            rs.next();
            String retVal = rs.getString("description");
            rs.close();
            return retVal;
        } catch(SQLException sqle) {
            // TODO log
            return null;
        }
    }
    
    public boolean storeDescription(String imageHash, String description) {
        try(Statement stmt = con.createStatement()) {
            return stmt.execute("INSERT INTO oribebot.data VALUES(\"" + imageHash + "\",\"" + description + "\",0)");
        } catch(SQLException sqle) {
            // TODO log
            return false;
        }
    }
    
    public boolean setPosted(String imageHash) {
        try(Statement stmt = con.createStatement()) {
            return stmt.execute("UPDATE oribebot.data SET posted=1 WHERE image_id=\"" + imageHash + "\"");
        } catch(SQLException sqle) {
            // TODO log
            return false;
        }
    }
    
    public boolean setUnposted(String imageHash) {
        try(Statement stmt = con.createStatement()) {
            return stmt.execute("UPDATE oribebot.data SET posted=0 WHERE image_id=\"" + imageHash + "\"");
        } catch(SQLException sqle) {
            // TODO log
            return false;
        }
    }
}
