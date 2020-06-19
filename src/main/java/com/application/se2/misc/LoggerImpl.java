package com.application.se2.misc;

import static com.application.se2.AppConfigurator.LoggerConfig;
import static com.application.se2.AppConfigurator.LoggerTopics;
import static java.util.logging.Level.*;

import com.application.se2.model.Entity;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.SimpleLayout;

import java.util.logging.Level;


/**
 * Local implementation of the Logger interface.
 * 
 * @author sgra64
 *
 */
class LoggerImpl implements Logger {
	// private final java.util.logging.Logger realLogger;
	// private final org.slf4j.Logger realLogger;
	private final org.apache.log4j.Logger realLogger;


	/**
	 * Private constructor to prevent instance creation outside getInstance().
	 * @param clazz class that identifies the logger instance.
	 */
	private LoggerImpl( final Class<?> clazz ) {
		realLogger = org.apache.log4j.Logger.getLogger( clazz.getName());
		// SimpleLayout layout = new SimpleLayout();
		// ConsoleAppender appender = new ConsoleAppender(layout);
		// this.realLogger.addAppender(appender);
		// this.realLogger.setLevel( org.apache.log4j.Level.ALL );
	}

	/**
	 * Create and return logger instance for a given class.
	 * @param clazz class that identifies the logger instance.
	 * @return logger instance for the class.
	 */
	public static LoggerImpl getInstance( final Class<?> clazz ) {

		return new LoggerImpl( clazz );
	}


	/**
	 * Method to log a message.
	 * 
	 * @param topic logs are categorized by (String) topics.
	 * @param msg log message
	 * @param args further log information
	 */
	public void log( final LoggerTopics topic, final String msg, final Object... args ) {
		String id = "<none>";
		String indicator = " - shutdown";

		if( LoggerConfig.contains( topic ) ) {

			switch( topic ) {
			case Always:
			case Info:
			case Warn:
				realLogger.info( msg );
				break;

			case Error:
				System.err.println( "ERROR: " + msg );
				break;

			case EntityCRUD:
				/*
				String cls = "";
				if( args.length > 0 ) {
					Object arg = args[ 0 ];
					arg = arg != null && arg instanceof PrimaryObject? ((PrimaryObject)arg).getObject() : arg;
					id = arg instanceof Entity? ((Entity)arg).getId() : String.valueOf( arg.hashCode() );
					cls= arg.getClass().getSimpleName();
				}
				realLogger.info( msg + " " + cls + "." + id );
				*/
				StringBuffer sb = new StringBuffer( msg );
				for( Object arg : args ) {
					sb.append( arg.toString() );
				}
				realLogger.info( sb.toString() );
				break;

			case Startup:
				indicator = " + startup";
			case Shutdown:
				realLogger.info( indicator + ": " + msg );
				break;

			case PropertiesAltered:
			case FieldAccessAltered:
				realLogger.info( msg );
				break;

			case RepositoryLoaded:
				if( args.length > 0 ) {
					Object arg = args[ 0 ];
					arg = arg != null && arg instanceof Traceable? ((Traceable)arg).getRootObject() : arg;
					id = arg instanceof Entity? ((Entity)arg).getId() : String.valueOf( arg.hashCode() );
				}
				realLogger.info( "Repository: --> " + id );
				break;

			case CSSLoaded:
				realLogger.info( msg );
				break;

			}
		}
	}


	/**
	 * Print info message.
	 * @param message log message
	 */
	@Override
	public void info( String message ) {
		log( LoggerTopics.Info, message );
	}


	/**
	 * Print warn message.
	 * @param message log message
	 */
	@Override
	public void warn( String message ) {
		log( LoggerTopics.Warn, message );
	}


	/**
	 * Print error message.
	 * @param message log message
	 * @param exception optional exception object to log.
	 */
	@Override
	public void error( String message, Exception exception ) {
		log( LoggerTopics.Error, message );
	}

}
