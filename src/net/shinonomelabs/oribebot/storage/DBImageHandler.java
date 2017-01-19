/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot.storage;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Eliza Bland
 */
public class DBImageHandler implements YasunaImageHandler {
    private final DBHandler dbh;
    
    public DBImageHandler(String dburl, String dbusr, String dbpass) {
        DBHandler dbh = null;
        try {
            dbh = new DBHandler(dburl, dbusr, dbpass);
        } catch(SQLException sqle) {
            // TODO log, handle
            sqle.printStackTrace();
        }
        
        this.dbh = dbh;
    }
    
    @Override
    public Yasuna getNextYasuna() {
        if(dbh==null) return null;
        List<Yasuna> yasunas = dbh.getPostableYasunas();
        Collections.shuffle(yasunas);
        Yasuna next = yasunas.get(0);
        dbh.setPosted(next.hash);
        return (verifyExistence(next)) ? next : null;
    }

    @Override
    public boolean storeYasuna(Yasuna y) {
        return dbh.storeDescription(y.hash,y.description);
    }

    @Override
    public boolean bulkStoreYasunas(List<Yasuna> ys) {
        List<Yasuna> existing = dbh.getAllYasunas();
        for(Yasuna y : ys) {
            if(!existing.contains(y)) this.storeYasuna(y);
        }
        return true; // TODO actually check these!!!!!!
    }

    public boolean verifyExistence(Yasuna y) {
        return y.file.exists();
    }

    @Override
    public int count() {
        return dbh.getPostableYasunas().size();
    }
    
}
