package com.application.se2.model.customserializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.application.se2.model.Customer;
import com.application.se2.model.Note;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;


/**
 * Custom-Serialization for Customer class. Class is attached to Customer class
 * by @JsonSerialize(using = CustomerJSONSerializer.class)
 * \\
 * Source: https://www.baeldung.com/jackson-custom-serialization
 * https://www.baeldung.com/jackson-deserialization
 * 
 * @author Sven Graupner
 *
 */
public class CustomerJSONSerializer extends StdSerializer<Customer> {
	private static final long serialVersionUID = 1L;

	public static final SimpleDateFormat createdDateFormat = new SimpleDateFormat( "dd.MM.yyyy HH:mm:ss" );	// "16.06.2019 21:25:36"

	public static enum Attr { id, name, address, contacts, notes, created, status };


	final static String ContactSeperator = "; ";


	/**
	 * Public constructor.
	 */
	public CustomerJSONSerializer() {
		this( null );
	}

	/**
	 * Public constructor.
	 * 
	 * @param e entity object to serialize.
	 */
	public CustomerJSONSerializer( Class<Customer> e ) {
		super( e );
	}


	/**
	 * Public method to deserialize an object from a Jackson parser.
	 * 
	 * @param entity object to serialize.
	 * @param jgen Jackson JSON generator.
	 * @param provider Jackson serializer provider.
	 * @exception JsonProcessingException exception thrown for object serialization errors
	 * @exception IOException exception thrown for IO errors
	 */
	@Override
	public void serialize( Customer entity, JsonGenerator jgen, SerializerProvider provider ) throws IOException, JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField( Attr.id.name(), entity.getId() );
		jgen.writeStringField( Attr.name.name(), entity.getName() );
		jgen.writeStringField( Attr.address.name(), entity.getAddress() );

		//serialize contacts.
		StringBuffer sb = new StringBuffer();
		for( String contact : entity.getContacts() ) {
			sb.append( sb.length() > 0? ContactSeperator : "" ).append( contact );
		}
		jgen.writeStringField( Attr.contacts.name(), sb.toString() );
		sb.setLength(0);

		//serialize notes.
		if( entity.getNotes().size() > 0 ) {
			jgen.writeArrayFieldStart( Attr.notes.name() );
			// jgen.writeRaw( "\n" );
			for( final Note note : entity.getNotes() ) {
				String noteAsString = note.externalize();
				jgen.writeRaw( "\n\t" );
				jgen.writeString( noteAsString );
			}
			jgen.writeEndArray();
		}

		//serialize creation date.
		Date create = entity.getCreationDate();
		String createdAsStr = createdDateFormat.format( create );
		jgen.writeStringField( Attr.created.name(), createdAsStr );

		//serialize status.
		jgen.writeStringField( Attr.status.name(), entity.getStatus().name() );

	    jgen.writeEndObject();		
	}
}
