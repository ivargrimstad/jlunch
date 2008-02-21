package com.jlunch.batch;

import com.jlunch.batch.JLunchMain.LunchDay;
import java.util.List;
import java.util.Map;

/**
 *
 * @author griv
 */
public interface LunchImporter {
    
    void importLunches( Map<LunchDay, Map<String, List<String>>> result );
    
}
