package com.jlunch.fwk;

import com.jlunch.fwk.util.ListUtil;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 *
 * @author ivar.grimstad
 */
public class JLunchProperties {
    
    private static final Logger logger = Logger.getLogger( "com.jlunch.fwk" );
    
    private PropertyResourceBundle properties;
    private static final JLunchProperties INSTANCE = new JLunchProperties();
    
    private JLunchProperties() {
        
        properties = (PropertyResourceBundle) PropertyResourceBundle.getBundle( "jlunch" );
    }
    
    /**
     * Gets a String property.
     *
     * @param key The property key
     * @return The property value
     */
    public static String getStringProperty( String key ) {
        return INSTANCE.properties.getString( key.toLowerCase() );
    }
    
    /**
     * Gets an int property.
     *
     * @param key The property key
     * @return The property value
     */
    public static int getIntProperty( String key ) {
        return ( (Integer) INSTANCE.properties.getObject( key ) ).intValue();
    }
    
    public static List<String> getList( String key ) {
        
        List<String> returnValue = ListUtil.newArrayList();
        
        Scanner listScanner = new Scanner( getStringProperty( key ) ).useDelimiter( "\\|" );
        
        while( listScanner.hasNext() ) {
            returnValue.add( listScanner.next() );
        }
        
        return returnValue;
    }
}
