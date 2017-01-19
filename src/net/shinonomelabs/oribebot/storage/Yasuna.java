/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
