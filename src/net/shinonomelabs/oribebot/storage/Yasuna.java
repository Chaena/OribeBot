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
package net.shinonomelabs.oribebot.storage;

import java.io.File;
import net.shinonomelabs.oribebot.Main;

/**
 *
 * @author Eliza Bland
 */
public class Yasuna {
    public final File file;
    public final String hash;
    public final String description; // TODO fix violation of accessor-mutator model
    
    public Yasuna(String hash, String d) {
        this.hash = hash;
        this.file = new File(Main.path + Main.SPR + hash + ".png");
        this.description = d;
    }
    
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Yasuna)) return false;
        Yasuna cast = (Yasuna)o;
        return this.hash.equals(cast.hash);
    }
}
