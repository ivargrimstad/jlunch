package com.jlunch.batch;

import com.jlunch.batch.JLunchMain.LunchDay;
import com.jlunch.fwk.JLunchProperties;
import com.jlunch.fwk.util.ListUtil;
import com.jlunch.fwk.util.SetUtil;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.SortedSet;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author griv
 */
public class MailSender {
    
    private final String message;
    private final List<Address> recipients = ListUtil.newArrayList();
    
    public MailSender( final String message, final List<String> recipients ) {
        this.message = message;
        
        try {
            for( String recipient : recipients ) {
                this.recipients.add( new InternetAddress( recipient + "@memo.ikea.com" ) );
            }
            
        } catch (AddressException ex) {
            ex.printStackTrace();
        }
    }
    
    public void send( final Map<LunchDay, Map<String, List<String>>> result ) {
        
        try {
            
            Properties props = System.getProperties();
            props.put("mail.smtp.host", JLunchProperties.getStringProperty( "host" ) );
            
            Session session = Session.getDefaultInstance(props, null);
            MimeMessage mailMessage = new MimeMessage(session);
            
            mailMessage.setFrom( new InternetAddress( JLunchProperties.getStringProperty( "from" ) ) );
            mailMessage.addRecipients( Message.RecipientType.TO, recipients.toArray( new Address[ recipients.size() ] ) );
            mailMessage.setSubject( JLunchProperties.getStringProperty( "subject" ) );
            
            mailMessage.setContent( createMailContent( result ), "text/plain; charset=\"UTF-8\"" );
            
            Transport.send( mailMessage );
            
        } catch (MessagingException ex) {
            ex.printStackTrace();
        } catch( Exception e ) {
            e.printStackTrace();
        }
    }
    
    private String createMailContent( final Map<LunchDay, Map<String, List<String>>> result ) {
        
        StringBuilder returnValue = new StringBuilder();
        
        returnValue.append( message != null && !message.trim().equals( "" ) ? "\n" + message + "\n" : "" );
        
        for( LunchDay day : result.keySet() ) {
            returnValue.append( "\n" );
            returnValue.append( day );
            returnValue.append( "\n======" );
            
            SortedSet<String> restaurants = SetUtil.newSortedSet();
            restaurants.addAll( result.get( day ).keySet() );
            
            for( String restaurant : restaurants ) {
                
                returnValue.append( "\n" );
                returnValue.append( restaurant );
                
                for( String menu : result.get( day ).get( restaurant ) ) {
                    returnValue.append( "\n" );
                    returnValue.append( "\t" + menu );
                }
            }
            
        }
        
        return returnValue.toString();
    }
}
