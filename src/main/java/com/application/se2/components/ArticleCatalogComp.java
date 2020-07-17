package com.application.se2.components;


/**
 * Catalog is a simple application component to manage articles in a repository.
 * It features a view part (TableView on the GUI) and logic (ArticleCatalogManager.java).
 * 
 * @author sgra64
 *
 */
public class ArticleCatalogComp extends ComponentBase {


	/**
	 * Interface of ArticleCatalog's view part is ComponentIntf.TableViewIntf
	 */

	/**
	 * Interface of ArticleCatalog's logic part.
	 */
	public interface LogicIntf extends ComponentIntf.LogicIntf, ComponentIntf.CRUDLogicIntf {
		/* no additions */
	}

}
