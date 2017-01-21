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
package net.shinonomelabs.oribebot;

import java.io.File;
import net.shinonomelabs.oribebot.storage.DBImageHandler;
import net.shinonomelabs.oribebot.posting.ThreadedPoster;
import net.shinonomelabs.oribebot.posting.TwitterHandler;

/**
 *
 * @author Eliza Bland
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Properties p = new Properties();
        Properties dbp = new Properties();
        Properties twp = new Properties();
        for(int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-p": case "--path": p.storeProperty("path", args[++i]); UtilityMethods.GLOBAL_PROPERTIES.storeProperty("path", args[i]); break;
                case "--db-credentials": dbp = UtilityMethods.readCSV(new File(args[++i])); break;
                case "--tw-credentials": twp = UtilityMethods.readCSV(new File(args[++i])); break;
                case "-a": case "--announce-online": p.storeProperty("announceOnStart", true); break;
                case "--postnow": p.storeProperty("postOnStart",true); break;
            }
        }
        TwitterHandler th = new TwitterHandler(twp);
        DBImageHandler dih = new DBImageHandler(dbp);
        
        ThreadedPoster poster = new ThreadedPoster(dih,p,th);
        poster.start();
    }
    
}
