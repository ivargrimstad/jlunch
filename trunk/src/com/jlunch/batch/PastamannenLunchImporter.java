package com.jlunch.batch;

import com.jlunch.batch.JLunchMain.LunchDay;
import com.jlunch.fwk.util.ListUtil;
import com.jlunch.fwk.util.MapUtil;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 *
 * @author griv
 */
public class PastamannenLunchImporter implements LunchImporter {
    
    private final String name = "Pastamannen";
    
    public void importLunches(Map<JLunchMain.LunchDay, Map<String, List<String>>> result) {
        
        List<String> menu = ListUtil.newArrayList();
        menu.add( "Kebabmeny med valfri sås" );
        menu.add( "Kycklingmeny med valfri sås\n" );
        
        for( LunchDay day : LunchDay.values() ) {
            
            if( Calendar.getInstance().get( Calendar.DAY_OF_WEEK ) == day.dayOfWeek ) {
                result.get( day ).put( name, menu );
            }
        }
    }
    
}
