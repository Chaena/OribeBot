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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import net.shinonomelabs.oribebot.storage.Yasuna;
import net.shinonomelabs.oribebot.storage.YasunaImageHandler;

/**
 *
 * @author Elizabeth Bland
 */
public class Scheduler {
    private final YasunaImageHandler yh;
    private final Map<Long,Yasuna> schedule = new TreeMap<>();
    private final List<Yasuna> postedYasunas = new ArrayList<>();
    
    public Scheduler(YasunaImageHandler yh) {
        this.yh = yh;
        refreshYasunas();
    }
    
    public void printSchedule() {
        Iterator<Map.Entry<Long,Yasuna>> it = schedule.entrySet().iterator();
        System.out.println("Time\t\tDescription");
        while(it.hasNext()) {
            Map.Entry<Long,Yasuna> pair = it.next();
            System.out.println(pair.getKey() + "\t" + pair.getValue().description);
        }
    }
    
    public boolean refreshYasunas() {
        System.out.println("Loading some Yasunas...");
        List<Yasuna> yasunas = yh.getPostableYasunas();
        Collections.shuffle(yasunas);
        long current = System.currentTimeMillis();
        long wait = (3600*6*1000) - ((current + 3600*3*1000) % (3600*6*1000));
        long next = wait + current;
        
        for(long i = next; !yasunas.isEmpty(); i += 60*60*6*1000) {
            schedule.put(i,yasunas.get(0));
            yasunas.remove(0);
        }
        return !schedule.isEmpty();
    }
    
    public void checkYasunas() {
        System.out.println("Checking Yasunas...");
        if(schedule.isEmpty()) refreshYasunas();
    }
    public List<Yasuna> getDueYasunas(long time) {
        ArrayList<Yasuna> ret = new ArrayList<>();
        ArrayList<Long> keys = new ArrayList<>();
        Iterator<Map.Entry<Long,Yasuna>> it = schedule.entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<Long,Yasuna> pair = it.next();
            if(pair.getKey() > time) continue;
            Yasuna yasuna = pair.getValue();
            if(!postedYasunas.contains(yasuna)) { ret.add(yasuna); keys.add(pair.getKey()); }
            
        }
        postedYasunas.addAll(ret);
        for(Long l : keys) schedule.remove(l);
        checkYasunas();
        return ret;
    }
    
}
