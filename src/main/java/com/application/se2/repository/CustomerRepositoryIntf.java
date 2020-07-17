package com.application.se2.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.application.se2.model.Customer;


/**
 * Public interface to access Customer repository with CRUD methods (Create, Read, Update, Delete).
 * Spring will create a Repository component (-> @Repository annotation) for this interface that
 * can be @Autowired for getting access.
 * 
 *	@Autowired
 *	private CustomerRepositoryIntf customerRepository;
 * 
 * @author Sven Graupner
 *
 */
@Repository
public interface CustomerRepositoryIntf extends CrudRepository<Customer, String> {

	/**
	 * Find method that returns entity that matches the regular expression. If more than
	 * one entity match, a random matching entity is returned.
	 * 
	 * @param regEx regular expression to match getName() property.
	 * @return Optional of entity matching name.
	 * /
	public Optional<Customer> findByName( String regEx );


	/**
	 * Find method that returns List of entities matching the name-field.
	 * 
	 * @param regEx regular expression to match getName() property
	 * @param limit max number of matching entities returned
	 * @return List of matching entites (up to limit)
	 * /
	public Iterable<Customer> findByName( String regEx, long limit );
*/

	/*
	 * All interface methods are inherited from CrudRepository, see:
	 *	- https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.core-concepts
	 *	- https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html
	 *
	 * public interface CrudRepository <T, ID> extends Repository<T, ID> {
	 * 
	 *    long count();
	 *
	 *    void delete( T entity );
	 *    void deleteAll();
	 *    void deleteAll( Iterable<? extends T> entities );
	 *    void deleteById( ID id );
	 *
	 *    boolean existsById( ID id );
	 *
	 *    Iterable<T> findAll();
	 *    Iterable<T> findAllById( Iterable<ID> ids );
	 *    Optional<T> findById( ID id );
	 *
	 *    <S extends T> S save( S entity );
	 *    <S extends T> Iterable<S> save( Iterable<S> entities );
	 * }
	 */
}
