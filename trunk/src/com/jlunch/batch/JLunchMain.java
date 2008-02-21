package com.jlunch.batch;

import com.jlunch.fwk.util.MapUtil;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author griv
 */
public class JLunchMain  {
    
    private final String message;
    private final List<String> recipients;
    
    public enum LunchDay {
        mandag( Calendar.MONDAY, "Monday", "Måndag" ),
        tisdag( Calendar.TUESDAY, "Tuesday", "Tisdag" ),
        onsdag( Calendar.WEDNESDAY, "Wednesday", "Onsdag" ),
        torsdag( Calendar.THURSDAY, "Thursday", "Torsdag" ),
        fredag( Calendar.FRIDAY, "Friday", "Fredag" );
        
        public int dayOfWeek;
        public String english;
        public String swedish;
        
        LunchDay( int dayOfWeek, String english, String swedish ) {
            this.dayOfWeek = dayOfWeek;
            this.english = english;
            this.swedish = swedish;
            
        }
    }
    
    public JLunchMain( String message, List<String> recipients ) {
        this.message = message;
        this.recipients = recipients;
    }
    
    public void importAndMailLunches() {
        
        new MailSender( message, recipients ).send( importLunches() );
    }
    
    public Map<LunchDay, Map<String, List<String>>> importLunches() {
        
        final Calendar cal = Calendar.getInstance();
        
        Logger.getLogger( getClass().getName() ).log( Level.INFO, "Restaurant import started..." );
        
        Map<LunchDay, Map<String,List<String>>> returnValue = MapUtil.newTreeMap();
        
        try {
            
            new HbgLunchImporter().importLunches( returnValue );
            new SamsLunchImporter().importLunches( returnValue );
            new ThaiCornerLunchImporter().importLunches( returnValue );
            new AmicaLunchImporter().importLunches( returnValue );
            new PastamannenLunchImporter().importLunches( returnValue );
            new HappyDuckLunchImporter().importLunches( returnValue );
            
        } finally {
            
            Logger.getLogger( getClass().getName() ).log( Level.INFO, "...Restaurant import finnished!!" );
        }
        
        return returnValue;
    }
}
