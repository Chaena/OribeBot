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

import net.shinonomelabs.oribebot.storage.DBImageHandler;
import net.shinonomelabs.oribebot.posting.ThreadedPoster;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author Eliza Bland
 */
public class Main {
    public static final String SPR = System.getProperty("file.separator");
    public static String path = ".";
    public static boolean announce = false;
    public static String hash(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        byte[] buf = new byte[fis.available()];
        fis.read(buf);
        fis.close();
        
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            md.update(buf);
            return new HexBinaryAdapter().marshal(md.digest()).toLowerCase();
        } catch(NoSuchAlgorithmException ex) { } // won't be thrown
        return null;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        String path = null;
        for(int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-p": case "--path":
                    path = args[++i];
                    
                case "-a": case "--announce-online":
                    Main.announce = true;
            }
        }
        Main.path = path;
        DBImageHandler dih = new DBImageHandler(OribeMeta.MYSQL_URL, OribeMeta.MYSQL_USER, OribeMeta.MYSQL_PASS);
        
        /*File[] files = new File(Main.path).listFiles();
        List<File> pngs = Arrays.stream(files).collect(Collectors.toList());
        ArrayList<File> toRemove = new ArrayList<>();
        for(int i = 0; i < pngs.size(); i++) {
            File next = pngs.get(i);
            String[] parts = next.getName().split("\\.");
            if(!parts[parts.length-1].equals("png")) toRemove.add(next);
        }
        
        for(File f : toRemove) {
            pngs.remove(f);
        }
        
        List<File> ypngs = new ArrayList<>();
        
        for(File png : pngs) {
            String[] parts = png.getName().split("\\.");
            String hash = hash(png);
            File dest = new File(Main.path + Main.SPR + hash + ".png");
            if(!parts[parts.length-2].equals(hash(png))) png.renameTo(dest);
            ypngs.add(dest);
        }*/
        
        ThreadedPoster poster = new ThreadedPoster(dih);
        poster.start();
    }
    
}
