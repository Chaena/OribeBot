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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import net.shinonomelabs.oribebot.Properties;

/**
 *
 * @author Eliza Bland
 */
public class DBImageHandler implements YasunaImageHandler {
    private final DBHandler dbh;
    
    public DBImageHandler(Properties p) {
        String dburl = (String)p.getProperty("dburl", "localhost");
        String dbusr = (String)p.getProperty("dbuser", "root");
        String dbpass= (String)p.getProperty("dbpass", "");
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
        if(yasunas.isEmpty()) return null;
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
