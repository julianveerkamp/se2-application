package com.application.se2.model.customserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.application.se2.model.Customer;
import com.application.se2.model.Customer.Status;
import com.application.se2.model.Note;
import com.application.se2.model.customserializer.CustomerJSONSerializer.Attr;


/**
 * Custom-Deserialization for Customer class. Class is attached to Customer class
 * by @JsonDeserialize(using = CustomerJSONDeserializer.class).
 * \\
 * Source: https://www.baeldung.com/jackson-custom-serialization
 * https://www.baeldung.com/jackson-deserialization
 * 
 * @author Sven Graupner
 *
 */
public class CustomerJSONDeserializer extends StdDeserializer<Customer> {
	private static final long serialVersionUID = 1L;

	/**
	 * Public constructor.
	 */
	public CustomerJSONDeserializer() {
		this( null ); 
	}

	/**
	 * Public constructor.
	 * 
	 * @param vc class used to deserialize object from Jackson parser.
	 */
	public CustomerJSONDeserializer( Class<?> clazz ) {
		super( clazz );
	}


	/**
	 * Public method to deserialize an object from a Jackson parser.
	 * 
	 * @param jp Jackson parser instance.
	 * @param ctxt Jackson deserialization context.
	 * @exception JsonProcessingException exception thrown for json parse and object construction errors
	 * @exception IOException exception thrown for IO errors
	 */
	@Override
	public Customer deserialize( JsonParser jp, DeserializationContext ctxt ) throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree( jp );

		// deserialize id, name, address
		String id = hasStr( node, Attr.id.name(), "-" );
		String name = hasStr( node, Attr.name.name(), "-" );
		String address = hasStr( node, Attr.address.name(), "-" );

		// deserialize creation date.
		Date created = null;
		try {
			String createdAsStr = hasStr( node, Attr.created.name(), "-" );
			created = (Date)CustomerJSONSerializer.createdDateFormat.parse( createdAsStr );

		} catch( ParseException e ) {
			// ignore
		}

		// create new Customer object
		Customer entity = new Customer( id, name, created );
		entity.setAddress( address );

		//deserialize contacts.
		String[] contactsSplitted = hasStr( node, Attr.contacts.name(), "" ).split( CustomerJSONSerializer.ContactSeperator.trim() );
		for( String contact : contactsSplitted ) {
			entity.getContacts().add( contact.trim() );
		}

		//deserialize notes.
		JsonNode n1 = node.get( Attr.notes.name() );
		if( n1 != null && n1.isArray() ) {	// n1 is JSON ArrayNode
			if( n1.size() > 0 ) {
				entity.getNotes().clear();
			}
			// implement note-deserialization here:
			for( final JsonNode objNode : n1 ) {
				String noteTextfromJson = objNode.asText();
				entity.addNote(noteTextfromJson);
			}
		}

		//deserialize status.
		Status status = Status.valueOf(
				hasStr( node, Attr.status.name(), Status.ACT.name() ) );
		entity.setStatus( status );

		return entity;
	}


	/*
	 * Private methods.
	 */

	private String hasStr( JsonNode node, String attrName, String defaultValue ) {
		// Test if attrName is found in JSON to avoid null being returned.
		return node.has( attrName )? node.get( attrName ).asText() : defaultValue;
	}

}
