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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author griv
 */
public class SamsLunchImporter extends AbstractLunchImporter {

    public SamsLunchImporter() {
        super( "8859_1", "Sams" );
    }
    public void importLunches(Map<JLunchMain.LunchDay, Map<String, List<String>>> result) {
        try {
            
            importSams( result );
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void importSams( final Map<LunchDay, Map<String, List<String>>> result ) throws ProtocolException, MalformedURLException, IOException {
        
        URL url = new URL( "http://www.samsbar.se/lunch2.asp" );
        String res = readFrom( url );
        
        for( LunchDay day : LunchDay.values() ) {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek ) {
                
                Map<LunchDay, List<String>> menus = parseSams( res );
                
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
    
    private Map<LunchDay, List<String>> parseSams( String html ) {
        
        Map<LunchDay, List<String>> returnValue = MapUtil.newHashMap();
        String menuHtml = html.substring( html.lastIndexOf( "<table id=\"retterRam\"" ), html.indexOf( "<td background=\"lunchbig/lunchRight.jpg\"" ) );
        
        for( LunchDay day : LunchDay.values() ) {
            returnValue.put( day, new ArrayList<String>() );
            Pattern dayPattern = Pattern.compile( day.swedish + ":" );
            Matcher dayMatcher = dayPattern.matcher( menuHtml );
            dayMatcher.find();
            
            String dayMenu = format( menuHtml.substring( dayMatcher.end() + 91, dayMatcher.end() + 91 + menuHtml.substring( dayMatcher.end() + 91 ).indexOf( "</font>" ) ) );
            
            returnValue.get( day ).add( dayMenu );
            
            Pattern vrPattern = Pattern.compile( "Veckans rätt:" );
            Matcher vrMatcher = vrPattern.matcher( menuHtml );
            vrMatcher.find();
            
            String veckansRatt = format( menuHtml.substring( vrMatcher.end() + 90, vrMatcher.end() + 90 + menuHtml.substring( vrMatcher.end() + 90 ).indexOf( "</td>" ) ) );
            returnValue.get( day ).add( "Veckans rätt: " + veckansRatt );

            Pattern vfPattern = Pattern.compile( "Veckans fisk:" );
            Matcher vfMatcher = vfPattern.matcher( menuHtml );
            vfMatcher.find();
            
            String veckansFisk = format( menuHtml.substring( vfMatcher.end() + 90, vfMatcher.end() + 90 + menuHtml.substring( vfMatcher.end() + 90 ).indexOf( "</td>" ) ) );
            returnValue.get( day ).add( "Veckans fisk: " + veckansFisk );

            Pattern vkPattern = Pattern.compile( "Veckans kyck:" );
            Matcher vkMatcher = vkPattern.matcher( menuHtml );
            vkMatcher.find();
            
            String veckansKyck = format( menuHtml.substring( vkMatcher.end() + 90, vkMatcher.end() + 90 + menuHtml.substring( vkMatcher.end() + 90 ).indexOf( "</td>" ) ) );
            returnValue.get( day ).add( "Veckans kyck: " + veckansKyck + "\n" );
        }
        
        return returnValue;
    }
    
}
