/*
 * The MIT License
 *
 * Copyright 2017 Elizabeth Bland.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
