/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.TreeMap;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 *
 * @author Eliza Bland
 */
public class Main {
    public static final String SPR = System.getProperty("file.separator");
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
            }
        }
        
        if(path==null) path = ".";
        System.out.println("Path: " + path);
        final String pathFinal = path;
        File pathDir = new File(pathFinal);
        // TODO check existence
        File[] files = pathDir.listFiles();
        ArrayList<File> yasunas = new ArrayList<>();
        for(File f : files) {
            String[] parts = f.getName().split("\\.");
            if(parts[parts.length-1].equals("png")) yasunas.add(f);
        }
        
        ArrayList<File> validYasunas = new ArrayList<>();
        
        System.out.printf("Found %d Yasunas!\n",yasunas.size());
        System.out.println("Checking Yasuna hashes...");
        yasunas.stream().forEach((y) -> {
            try {
                String hash = hash(y);
                String[] parts = y.getName().split("\\.");
                if(hash.equals(parts[parts.length-2])) {
                    //System.out.println("Valid Yasuna " + hash + " found.");
                    validYasunas.add(y);
                } else {
                    //System.out.println("Invalid Yasuna " + y.getName() + " found.");
                    //System.out.println("Changing to " + hash + ".");
                    File dest = new File(pathFinal + SPR + hash + ".png");
                    y.renameTo(dest);
                    validYasunas.add(dest);
                }
            } catch(IOException ex) {
                System.err.println("Failed to read Yasuna " + y.getName() + "!");
            }
        });
        
        System.out.printf("%d valid Yasunas found!\n",validYasunas.size());
        
        File data = new File(path + SPR + "_data.csv");
        if(data.isDirectory()) {
            System.out.println("data.csv doesn't exist, creating");
            FileOutputStream fos = new FileOutputStream(data);
            fos.close();
        }
        FileInputStream fis = new FileInputStream(data);
        byte[] buf = new byte[fis.available()];
        fis.read(buf);
        fis.close();
        String bufData = new String(buf);
        
        String[] dataLines = bufData.split("\n");
        
        TreeMap<String,String> dataDescriptions = new TreeMap<>();
        for(String l : dataLines) {
            if(l.split(",").length!=2)continue;
            dataDescriptions.put(l.split(",")[0],l.split(",")[1]);
        }
        
        TreeMap<File,String> yasunaDescriptions = new TreeMap<>();
        Iterator<Map.Entry<String,String>> it = dataDescriptions.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<String,String> pair = it.next();
            File f = new File(path + SPR + pair.getKey() + ".png");
            if(validYasunas.contains(f)) yasunaDescriptions.put(f,pair.getValue());
        }
        int diff = validYasunas.size() - yasunaDescriptions.size();
        System.out.println(diff + " Yasunas lack a description.");
        if(diff>0) {
            fis = new FileInputStream(data);
            buf = new byte[fis.available()];
            fis.read(buf);
            fis.close();
            FileOutputStream fos = new FileOutputStream(data);
            fos.write(buf);
            Scanner s = new Scanner(System.in);
            for(File validYasuna : validYasunas) {
                if(yasunaDescriptions.get(validYasuna) != null) continue;
                System.out.print("Describe " + validYasuna.getName() + ": ");
                String desc = s.nextLine();
                
                fos.write(validYasuna.getName().split("\\.")[0].getBytes());
                fos.write(',');
                fos.write(desc.getBytes());
                fos.write('\n');
                yasunaDescriptions.put(validYasuna,desc);
            }
            fos.close();
        }
        
        Collections.shuffle(validYasunas, new Random(System.nanoTime()));
        
        
        
        ThreadedPoster poster = new ThreadedPoster(validYasunas,yasunaDescriptions);
        poster.start();
    }
    
}
