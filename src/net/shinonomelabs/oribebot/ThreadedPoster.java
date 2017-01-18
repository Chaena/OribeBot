/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Eliza Bland
 */
public class ThreadedPoster extends Thread {
    private final List<File> order;
    private final Map<File,String> descriptions;
    
    public ThreadedPoster(ArrayList<File> order, Map<File,String> descriptions) {
        this.order = order;
        this.descriptions = descriptions;
    }
    
    @Override
    public void run() {
        TwitterHandler th = new TwitterHandler();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd, kk.mm.ss");
        SimpleDateFormat time = new SimpleDateFormat("kk.mm.ss");
        
        long ctime = System.currentTimeMillis() / 1000L;
        long wait = 21600 - (ctime+3600*3)%21600;
        //th.status("YasunaBot " + YasunaMeta.BOT_VERSION + " went online at " + sdf.format(new Date()) + ". I have " + order.size() + " Yasunas ready! Next Yasuna will be posted in " + wait + " seconds.");
        System.out.println("YasunaBot is online.");
        while(true) {
            System.out.println("next Yasuna in " + wait + " sec");
            try {
                Thread.sleep(wait*1000);
            } catch(InterruptedException ie) {
                System.err.println("ThreadedPoster " + this.toString() + " interrupted!");
            }
            File nextF = order.get(0);
            order.remove(nextF);
            System.out.println("Posting " + nextF.getName() + ", \"" + descriptions.get(nextF) + "\"...");
            th.post(nextF, descriptions.get(nextF));
            
            ctime = System.currentTimeMillis() / 1000L;
            wait = 21600 - (ctime+3600*3)%21600;
        }
        
    }
}
