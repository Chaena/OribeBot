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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Elizabeth Bland
 */
public class Properties {
    private final Map<String,Object> propertyMap = new HashMap<>();
    
    public void storeProperty(String key, Object val) {
        propertyMap.put(key,val);
    }
    
    public Object getProperty(String key, Object dflt) {
        return (propertyMap.containsKey(key)) ? propertyMap.get(key) : dflt;
    }
    
    public void mergeProperties(Properties p, boolean prioritiseThis) {
        Iterator<Map.Entry<String,Object>> that = p.propertyMap.entrySet().iterator();
        while(that.hasNext()) {
            Map.Entry<String,Object> pair = that.next();
            if(!prioritiseThis || (prioritiseThis && this.propertyMap.containsKey(pair.getKey()))) propertyMap.put(pair.getKey(), pair.getValue());
        }
    }
}
