package com.application.se2.components;

import com.application.se2.model.Entity;

/**
 * CustomerManager is a simple application component to manage customers in a repository.
 * It features a view part (TableView on the GUI) and logic (CustomerManager.java).
 * 
 * @author sgra64
 *
 */
public class CustomerManagerComp extends ComponentBase {


	/**
	 * Interface of UserManager's view part is ComponentIntf.TableViewIntf
	 */

	/**
	 * Interface of UserManager's logic part.
	 */
	public interface LogicIntf extends ComponentIntf.CRUDLogicIntf {

		/**
		 * Find method that returns entity that matches the regular expression. If more than
		 * one entity match, a random matching entity is returned.
		 * 
		 * @param regEx regular expression to match getName() property.
		 * @return Optional of entity matching name.
		 */
		public Iterable<? extends Entity> findByName( String match );

	}

}
