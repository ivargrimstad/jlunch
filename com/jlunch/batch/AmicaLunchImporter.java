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
public class AmicaLunchImporter extends AbstractLunchImporter {
    
    public AmicaLunchImporter() {
        super( "utf-8", "Amica" );
    }
    
    public void importLunches(Map<JLunchMain.LunchDay, Map<String, List<String>>> result) {
        
        try {
            
            importAmica( result );
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    
    private void importAmica( final Map<LunchDay, Map<String, List<String>>> result ) throws ProtocolException, MalformedURLException, IOException {
        
        String res = readFrom( new URL( "http://www.fazeramica.se/amicahbg" ) );
        
        Map<LunchDay, List<String>> menus = parseAmica( res );
        
        for( LunchDay day : LunchDay.values() ) {

            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek &&
                menus != null && menus.get( day ) != null ) {

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
    
    private Map<LunchDay, List<String>> parseAmica( String html ) {
        
        Map<LunchDay, List<String>> returnValue = MapUtil.newHashMap();

        if( html == null || html.trim().length() == 0 ) {
            return returnValue;
        }

        String menuHtml = html.substring( html.indexOf( "<div class=\"menufactstext\" >" ), html.indexOf( "<div class=\"menufactstext\" >" ) + html.substring( html.indexOf( "<div class=\"menufactstext\" >" ) ).indexOf( "</span>" ) );
        for( LunchDay day : LunchDay.values() ) {
            
            Pattern menuPattern = Pattern.compile( day.swedish + " *<br />" );
            Matcher menuMatcher = menuPattern.matcher( menuHtml );
            
            if( menuMatcher.find() ) {
            
                String menu = format( menuHtml.substring( menuMatcher.end(), menuMatcher.end() + menuHtml.substring( menuMatcher.end() ).indexOf( "</p>" ) ) );
                Scanner menuScanner = new Scanner( menu ).useDelimiter( "<br />" );
                
                while( menuScanner.hasNext() ) {
                    String menuItem = menuScanner.next();
                    
                    if( menuItem.equals( "Prova gärna våra goda baguetter och sallader i Amica Express" ) || 
                        menuItem.equals( "                            " ) ) {
                        break;
                    }
                    
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
