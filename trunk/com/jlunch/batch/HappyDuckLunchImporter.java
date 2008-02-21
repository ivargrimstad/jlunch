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
public class HappyDuckLunchImporter extends AbstractLunchImporter {
    
    public HappyDuckLunchImporter() {
        super( "Happy Duck" );
    }
    
        public void importLunches(Map<JLunchMain.LunchDay, Map<String, List<String>>> result) {
        
        try {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) != Calendar.MONDAY ) {
                importHappyDuck( result );
            }
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void importHappyDuck( final Map<LunchDay, Map<String, List<String>>> result ) throws ProtocolException, MalformedURLException, IOException {
        
        String res = readFrom( new URL( "http://www.hungarian-restaurant.com/pages/lunch.php" ) );
        
        Map<LunchDay, List<String>> menus = parseHappyDuck( res );
        
        for( LunchDay day : LunchDay.values() ) {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek ) {
                
                if( result.get( day ) == null ) {
                    Map<String, List<String>> tmp = MapUtil.newHashMap();
                    tmp.put( name, new ArrayList<String>() );
                    result.put( day, tmp );
                }
                
                if( result.get( day ).get( name ) == null ) {
                    result.get( day ).put( name, new ArrayList<String>() );
                }
                
                result.get( day ).get( name ).addAll( menus.get( day ) );
            }
        }
    }
    
    private Map<LunchDay, List<String>> parseHappyDuck( String html ) {
        
        Map<LunchDay, List<String>> returnValue = MapUtil.newHashMap();
        String menuHtml = format( html.substring( html.indexOf( "<table border='0' cellspacing='5' cellpadding='5' width='100%'>" ) ) );

        for( LunchDay day : LunchDay.values() ) {
            
            Pattern menuPattern = Pattern.compile( day.swedish + ":" );
            Matcher menuMatcher = menuPattern.matcher( menuHtml );
            
            if( menuMatcher.find() ) {
            
                String menu = menuHtml.substring( menuMatcher.end() + 112, menuMatcher.end() + 112 + menuHtml.substring( menuMatcher.end() + 112 ).indexOf( "</td>" ) ); 
                Scanner menuScanner = new Scanner( menu ).useDelimiter( " </font><br>" );
                
                while( menuScanner.hasNext() ) {
                    String rawMenuItem = menuScanner.next();
                    String menuItem = rawMenuItem.substring( rawMenuItem.indexOf( ">" ) + 1 );
                    
                    if( menuItem.trim().equals( "" ) ) {
                        break;
                    }
                    
                    menuItem = menuItem.replaceAll( "</font>", "" );//.replaceAll( " ", "" );
                    
                    if( returnValue.get( day ) == null ) {
                        returnValue.put( day, new ArrayList<String>() );
                    }
                    returnValue.get( day ).add( menuItem );
                }
                
                returnValue.get( day ).set( returnValue.get( day ).size() - 1, returnValue.get( day ).get( returnValue.get( day ).size() - 1 ) + "\n" );
            }
            
        }
        
        return returnValue;
    }

}
