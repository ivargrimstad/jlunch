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
public class ThaiCornerLunchImporter extends AbstractLunchImporter {
    
    public ThaiCornerLunchImporter() {
        super( "8859_1", "Thai Corner" );
    }
    
    public void importLunches(Map<JLunchMain.LunchDay, Map<String, List<String>>> result) {

        try {
            
            importThaiCorner( result );
            
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    private void importThaiCorner( final Map<LunchDay, Map<String, List<String>>> result) throws ProtocolException, MalformedURLException, IOException {
        
        for( LunchDay day : LunchDay.values() ) {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek ) {
                
                URL url = new URL( "http://www.thaicorner.se/pages/lunch/page.php" );
                String res = readFrom( url );
                
                if( result.get( day ) == null ) {
                    Map<String, List<String>> tmp = MapUtil.newHashMap();
                    tmp.put( name, new ArrayList<String>() );
                    result.put( day, tmp );
                }
                
                if( result.get( day ).get( name ) == null ) {
                    result.get( day ).put( name, new ArrayList<String>() );
                }
                
                Map<LunchDay, List<String>> restaurants = parseThaiCorner( res );
                result.get( day ).get( name ).addAll( restaurants.get( day ) );
            }
        }
        
    }
    
    private Map<LunchDay, List<String>> parseThaiCorner( String html ) {
        
        Map<LunchDay, List<String>> returnValue = MapUtil.newHashMap();
        String menuHtml = html.substring( html.lastIndexOf( "<!-- Lunchdagar Start -->" ) );
        for( LunchDay day : LunchDay.values() ) {
            
            returnValue.put( day, new ArrayList<String>() );
            
            Pattern dayPattern = Pattern.compile( "imgLunch" + day.english + ".gif" );
            Matcher dayMatcher = dayPattern.matcher( menuHtml );
            dayMatcher.find();
            
            String dayHtml = menuHtml.substring( dayMatcher.end() + 266, dayMatcher.end() + 1500 );//1387 );
            
            Pattern menuPattern = Pattern.compile( "<td class='boldtext' width='70%'>" );
            Matcher menuMatcher = menuPattern.matcher( dayHtml );
            menuMatcher.find();
            
            StringBuffer menuA = new StringBuffer();
            menuA.append( "A. " );
            menuA.append( format( dayHtml.substring( menuMatcher.end(), menuMatcher.end() + dayHtml.substring( menuMatcher.end() ).indexOf( "</td>" ) ) ) );
            menuA.append( " - " );
            
            Pattern desc1Pattern = Pattern.compile( "<td colspan='2' class='plaintext' width='300'>" );
            Matcher desc1Matcher = desc1Pattern.matcher( dayHtml );
            desc1Matcher.find();
            
            menuA.append( format( dayHtml.substring( desc1Matcher.end(), desc1Matcher.end() + dayHtml.substring( desc1Matcher.end() ).indexOf( "</td>" ) ) ) );
            returnValue.get( day ).add( menuA.toString() );
            
            menuMatcher.find();
            
            StringBuffer menuB = new StringBuffer();
            menuB.append( "B. " );
            menuB.append( format( dayHtml.substring( menuMatcher.end(), menuMatcher.end() + dayHtml.substring( menuMatcher.end() ).indexOf( "</td>" ) ) ) );
            menuB.append( " - " );

            Pattern desc2Pattern = Pattern.compile( "<td colspan='2' class='plaintext'>" );
            Matcher desc2Matcher = desc2Pattern.matcher( dayHtml );
            desc2Matcher.find();
            
            menuB.append( format( dayHtml.substring( desc2Matcher.end(), desc2Matcher.end() + dayHtml.substring( desc2Matcher.end() ).indexOf( "</td>" ) ) ) );
            returnValue.get( day ).add( menuB.toString() + "\n" );
        }
        
        return returnValue;
    }
    
    
}
