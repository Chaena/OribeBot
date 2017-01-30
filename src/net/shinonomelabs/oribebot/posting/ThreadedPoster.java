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
    
    private void makePost(Yasuna y) {
        int tryCount = 0;
        if(y==null) {
            System.err.println("Yasuna is null!");
            th.status("OribeBot ran into a problem and couldn't post a Yasuna right now D:");
        } else {
            System.out.println("Valid Yasuna found after " + tryCount + " attempts.");
            th.post(y.file, y.description);
            handler.setPosted(y);
        }
    }
    
    @Override
    public void run() {
        Scheduler schedule = new Scheduler(handler);
        while(true) {
            List<Yasuna> yasunas = schedule.getDueYasunas(System.currentTimeMillis());
            if(!yasunas.isEmpty()) {
                makePost(yasunas.get(0));
            }
            try {
                Thread.sleep(5000);
            } catch(InterruptedException ex) {
                // TODO handle
            }
        }
    }
}
