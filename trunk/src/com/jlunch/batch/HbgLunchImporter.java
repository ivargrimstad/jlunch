package com.jlunch.batch;

import com.jlunch.batch.JLunchMain.LunchDay;
import com.jlunch.fwk.util.MapUtil;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author griv
 */
public class HbgLunchImporter extends AbstractLunchImporter {
    
    public HbgLunchImporter() {
        super( "8859_1", null );
    }
    
    public void importLunches(Map<LunchDay, Map<String, List<String>>> result) {
        try {
            
            importHbgLunches( result );
            
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void importHbgLunches(final Map<LunchDay, Map<String, List<String>>> result) throws ProtocolException, MalformedURLException, IOException {
        
        for( LunchDay day : LunchDay.values() ) {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek ) {
                
                URL url = new URL( "http://www.hbglunch.nu/dag.asp?dag=" + day + "&helg=manhelg" );
                
                String res = readFrom( url );
                
                Map<String, List<String>> restaurants = parseHbgLunch( res );
                
                if( result.get( day ) == null ) {
                    
                    result.put( day, restaurants );
                    
                } else {
                    
                    for( String restaurant : restaurants.keySet() ) {
                        
                        if( result.get( day ).get( restaurant ) == null ) {
                            result.get( day ).put( restaurant, new ArrayList<String>() );
                        }
                        
                        for( String menu : restaurants.get( restaurant ) ) {
                            result.get( day ).get( restaurant ).add( menu );
                        }
                    }
                }
            }
        }
    }
    
    private Map<String, List<String>> parseHbgLunch( String html ) {
        
        Pattern restaurantPattern = Pattern.compile( "<a href.*restaurang.asp.restaurang.*titel." );
        Matcher restaurantMatcher = restaurantPattern.matcher( html );
        Pattern menuPattern = Pattern.compile( "</b>" );
        
        Map<String, List<String>> restaurants = MapUtil.newHashMap();
        
        while( restaurantMatcher.find() ) {
            
            String restaurant = html.substring(restaurantMatcher.end(), restaurantMatcher.end() + html.substring(restaurantMatcher.end()).indexOf("\">") );
            restaurants.put( restaurant, new ArrayList<String>() );
            
            String menuHtml = html.substring( restaurantMatcher.end() + html.substring( restaurantMatcher.end() ).indexOf( "\">" ) );
            
            Matcher menuMatcher = menuPattern.matcher( menuHtml );
            menuMatcher.find();
            String menu = format( menuHtml.substring( menuMatcher.end(), menuMatcher.end() + menuHtml.substring( menuMatcher.end() ).indexOf( "</td>" ) ) );
            
            Scanner menuScanner = new Scanner( menu ).useDelimiter( "<br>" );
            
            while( menuScanner.hasNext() ) {
                restaurants.get( restaurant ).add( menuScanner.next() );
            }
        }
        
        return restaurants;
    }
    
}
