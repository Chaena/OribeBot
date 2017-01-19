/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.shinonomelabs.oribebot.posting;

import java.io.File;
import net.shinonomelabs.oribebot.OribeMeta;
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
    private final Configuration c;
    
    public TwitterHandler() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(OribeMeta.TWITTER_CONSUMER_KEY);
        cb.setOAuthConsumerSecret(OribeMeta.TWITTER_CONSUMER_SECRET);
        cb.setOAuthAccessToken(OribeMeta.TWITTER_ACCESS_TOKEN);
        cb.setOAuthAccessTokenSecret(OribeMeta.TWITTER_ACCESS_TOKEN_SECRET);
        c = cb.build();
    }
    
    public void status(String text) {
        Twitter t = new TwitterFactory(c).getInstance();
        StatusUpdate su = new StatusUpdate(text);
        try {
            t.updateStatus(su);
        } catch(TwitterException te) {
            te.printStackTrace();
        }
    }
    
    public void post(File yasuna, String text) {
        System.out.println("Posting Yasuna \"" + text + "\"");
        /*Twitter t = new TwitterFactory(c).getInstance();
        StatusUpdate su = new StatusUpdate(text);
        su.setMedia(yasuna);
        try {
            t.updateStatus(su);
        } catch(TwitterException te) {
            te.printStackTrace();
        }*/
    }
}
