/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot.posting;

import net.shinonomelabs.oribebot.posting.TwitterHandler;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.shinonomelabs.oribebot.OribeMeta;
import net.shinonomelabs.oribebot.storage.Yasuna;
import net.shinonomelabs.oribebot.storage.YasunaImageHandler;

/**
 *
 * @author Eliza Bland
 */
public class ThreadedPoster extends Thread {
    private final YasunaImageHandler handler;
    
    public ThreadedPoster(YasunaImageHandler h) {
        this.handler = h;
    }
    
    @Override
    public void run() {
        TwitterHandler th = new TwitterHandler();
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd, HH.mm.ss");
        
        long ctime = System.currentTimeMillis() / 1000L;
        long wait = 21600 - (ctime+3600*3)%21600; // get the next post time, next of 3-9-15-21 daily
        
        th.status("OribeBot " + OribeMeta.BOT_VERSION + " went online at " + sdf.format(new Date()) + ". I have " + handler.count() + " Yasunas ready! Next Yasuna will be posted in " + wait + " seconds.");
        System.out.println("OribeBot is online.");
        
        while(true) {
            System.out.println("next Yasuna is at " + sdf.format(new Date()));
            try {
                Thread.sleep(wait*1000);
            } catch(InterruptedException ie) {
                System.err.println("ThreadedPoster " + this.toString() + " interrupted!");
            }
            
            Yasuna next = handler.getNextYasuna();
            if(next==null) {
                System.err.println("Yasuna is null!");
                th.status("OribeBot ran into a problem and couldn't post a Yasuna right now D:");
            } else th.post(next.file, next.description);
            wait += 21600;
        }
    }
}
