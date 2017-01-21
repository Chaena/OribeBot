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
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author Elizabeth Bland
 */
public abstract class UtilityMethods {
    public static final String SPR = System.getProperty("file.separator");
    public static final Properties GLOBAL_PROPERTIES = new Properties();
    
    public static Properties readCSV(File path) {
        if(!path.exists() || path.isDirectory()) return null;
        try(FileInputStream fis = new FileInputStream(path)) {
            byte[] buf = new byte[fis.available()];
            fis.read(buf);
            fis.close();
            
            String file = new String(buf, "UTF-8");
            String[] lines = file.replace("\r","").split("\n");
            
            Properties ret = new Properties();
            for(String line : lines) {
                String[] fields = line.split(",");
                if(fields.length != 2) continue;
                ret.storeProperty(fields[0], fields[1]);
            }
            return ret;
        } catch(IOException ex) {
            // TODO handle
            ex.printStackTrace();
            return null;
        }
    }
    
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
}
