package com.application.se2.model;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;

import com.application.se2.misc.EntityProperty;


/**
 * Class Note represents a short text line to store short hints or comments
 * attached to Customer or Article Entities.
 * 
 * A Note consists of a timeStamp, a separator (comma) and noteText.
 * Example: "2018-04-02 10:16:24:868, This is a short note."
 * 
 * @author sgra64
 */
@Entity
@Table(name="NOTES")
public class Note implements Serializable {
	private static final long serialVersionUID = 1L;

	private static final String FieldSeparator = ";; ";
	private static long lastTimeStamp = 0L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id")
	private long id;

	@Column(name="TIME")
	private Date timeStamp = null;		// TimeStamp part of Note.

	@Column(name="TEXT")
	private String noteText = null;		// Text part of Note.

	@ManyToOne(
		fetch = FetchType.LAZY
	)
	private Customer customer;


	/**
	 * Default constructor needed by JSON deserialization and Hibernate (private
	 * is sufficient). Public default constructor needed by Hibernate/JPA access.
	 * Otherwise Hibernate Error: HHH000142: Bytecode enhancement failed).
	 */
	private Note() {
		this.timeStamp = new Date();
		this.noteText = "";
	}

	/**
	 * Public constructor.
	 * Example noteStr: "2018-04-02 10:16:24:868, This is a text entry."
	 * 
	 * @param noteStr input to parse Note instance.
	 */
	public Note( String noteStr ) {
		Object[] parts = parselogStr( noteStr );
		this.timeStamp = parts[0]==null? null : (Date)parts[0];
		this.noteText = (String)parts[1];
		if( this.timeStamp == null ) {
			this.timeStamp = nextUniqueTimeStamp();
		}
	}


	/**
	 * Return note's text section.
	 * 
	 * @return note text.
	 */
	public String getText() {
		return noteText;
	}


	/**
	 * Set Note's text section.
	 * 
	 * @param text note text.
	 */
	public void setText( String text ) {
		this.noteText = text;
	}

	/**
	 * Return Customer.
	 * 
	 * @return Customer.
	 */
	public Customer getCustomer() {
		return customer;
	}


	/**
	 * Set Customer
	 * 
	 * @param customer Customer.
	 */
	public void setCustomer( Customer customer ) {
		this.customer = customer;
	}

	/**
	 * Externalize Note as String. Example: "2018-03-15 20:10:27.730, Customer 1234 created".
	 * @return Note as externalized String.
	 */
	public String externalize() {
		return EntityProperty.DF_yyyy_MM_dd_HH_mm_ss_SSS.format( timeStamp ) + FieldSeparator + noteText;
	}


	/*
	 * Private methods.
	 */

	/**
	 * Helper function to split an externalized "timestamp;noteStr"-string into
	 * its parts that are separated by Separator.
	 * @param noteStr input Note as externalized String.
	 * @return Array of parts with types of Note properties.
	 */
	private Object[] parselogStr( String noteStr ) {
		Object[] res = new Object[] { null, null };
		String[] spl = noteStr.split( FieldSeparator, 2 );	// return max 2 splits, allows ',' to be used in logLine
		if( spl.length > 1 ) {
			// two parts, try to parse date
			try {
				res[0] = EntityProperty.DF_yyyy_MM_dd_HH_mm_ss_SSS.parse( spl[ 0 ] );
				res[1] = spl[ 1 ];

			} catch( ParseException e ) {
			}
		}
		if( res[1]==null ) {
			res[1] = noteStr;
		}
		return res;
	}

	/**
	 * Helper function to generate a unique timeStamp that differs at least by 1 msec
	 * from a prior call to nextUniqueTimeStamp().
	 * @return unique timeStamp as Date object.
	 */
	private Date nextUniqueTimeStamp() {
		Date now = new Date();
		long nowL = now.getTime();
		if( nowL <= lastTimeStamp ) {
			now = new Date( ++lastTimeStamp );
		} else {
			lastTimeStamp = nowL;
		}
		return now;
	}

}
