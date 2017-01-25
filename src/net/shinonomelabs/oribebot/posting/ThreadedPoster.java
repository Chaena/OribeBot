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
package net.shinonomelabs.oribebot.posting;

import net.shinonomelabs.oribebot.posting.TwitterHandler;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import net.shinonomelabs.oribebot.Main;
import net.shinonomelabs.oribebot.OribeMeta;
import net.shinonomelabs.oribebot.Properties;
import net.shinonomelabs.oribebot.storage.Yasuna;
import net.shinonomelabs.oribebot.storage.YasunaImageHandler;
import twitter4j.Query;

/**
 *
 * @author Eliza Bland
 */
public class ThreadedPoster extends Thread {
    private final YasunaImageHandler handler;
    private final Properties properties;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("YYYY/MM/dd, HH.mm.ss");
    private final TwitterHandler th;
    
    public ThreadedPoster(YasunaImageHandler h, Properties p, TwitterHandler th) {
        this.handler = h;
        this.properties = p;
        this.th = th;
    }
    
    private String getDate() {
        return getDate(System.currentTimeMillis());
    }
    
    private String getDate(long time) {
        Date d = new Date();
        d.setTime(time);
        return DATE_FORMAT.format(d);
    }
    
    private void makePost(TwitterHandler th) {
        int tryCount = 0;
        Yasuna next = null;
        while(next == null) {
            if(tryCount>3) break;
            next = handler.getNextYasuna();
            tryCount++;
        }
        if(next==null) {
            System.err.println("Yasuna is null!");
            th.status("OribeBot ran into a problem and couldn't post a Yasuna right now D:");
        } else {
            System.out.println("Valid Yasuna found after " + tryCount + " attempts.");
            th.post(next.file, next.description);
            handler.setPosted(next);
        }
    }
    
    @Override
    public void run() {
        long ctime = System.currentTimeMillis() / 1000L;
        long wait = 21600 - (ctime+3600*3)%21600; // get the next post time, next of 3-9-15-21 daily
        if((boolean)this.properties.getProperty("clearOldAnnouncements",false)) {
            th.removeWithQuery("\"went online at\" from:OribeBot");
        }
        if((boolean)this.properties.getProperty("announceOnStart", false))
            th.status("OribeBot " + OribeMeta.BOT_VERSION + " went online at " + getDate() + ". I have " + handler.count() + " Yasunas ready! Next Yasuna will be posted at " + getDate(1000*(ctime+wait)));
        if((boolean)this.properties.getProperty("postOnStart", false)) makePost(th);
        System.out.println("OribeBot " + OribeMeta.BOT_VERSION + " is online.");
        
        while(true) {
            System.out.println("next Yasuna is at " + getDate(1000*(ctime+wait)));
            try {
                Thread.sleep(wait*1000);
            } catch(InterruptedException ie) {
                System.err.println("ThreadedPoster " + this.toString() + " interrupted!");
            }
            
            makePost(th);
            wait += 21600;
        }
    }
}
