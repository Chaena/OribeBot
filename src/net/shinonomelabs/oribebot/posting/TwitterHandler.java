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

import java.io.File;
import net.shinonomelabs.oribebot.OribeMeta;
import net.shinonomelabs.oribebot.Properties;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author Eliza Bland
 */
public class TwitterHandler {
    private final TwitterFactory tf;
    
    public TwitterHandler(Properties p) {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey((String)p.getProperty("twitter_consumerkey", null));
        cb.setOAuthConsumerSecret((String)p.getProperty("twitter_consumersecret",null));
        cb.setOAuthAccessToken((String)p.getProperty("twitter_accesstoken",null));
        cb.setOAuthAccessTokenSecret((String)p.getProperty("twitter_accesstokensecret",null));
        this.tf = new TwitterFactory(cb.build());
    }
    
    public void status(String text) {
        Twitter t = tf.getInstance();
        StatusUpdate su = new StatusUpdate(text);
        try {
            t.updateStatus(su);
        } catch(TwitterException te) {
            te.printStackTrace();
        }
    }
    
    public void post(File yasuna, String text) {
        System.out.println("Posting Yasuna \"" + text + "\"");
        Twitter t = tf.getInstance();
        StatusUpdate su = new StatusUpdate(text);
        su.setMedia(yasuna);
        try {
            t.updateStatus(su);
        } catch(TwitterException te) {
            te.printStackTrace();
        }
    }
}
