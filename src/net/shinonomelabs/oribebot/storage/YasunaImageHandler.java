/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot.storage;

import java.io.File;
import java.util.List;

/**
 *
 * @author Eliza Bland
 */
public interface YasunaImageHandler {
    public int count();
    public boolean storeYasuna(Yasuna y);
    public Yasuna getNextYasuna();
    public boolean bulkStoreYasunas(List<Yasuna> ys);
}
