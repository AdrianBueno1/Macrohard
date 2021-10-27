package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectLoginException;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.User;

/**
 * Service for the user domain.
 * <p>
 * This is a domain service just to allow the endpoints querying the entities
 * they are asked for.
 *
 */
public interface UserService {

	/**
	 * Creates a new account.
	 * 
	 * @param user user to sign up
	 * @throws DuplicateUserException
	 */
	void signUp(User user) throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException;

	/**
	 * Logs in an account.
	 * 
	 * @param userName userName of the user
	 * @param password password of the user
	 * @return the user
	 */
	User login(String userName, String password) throws IncorrectLoginException;

	User checkUser(Long userId) throws InstanceNotFoundException;

	void becomePremium(String userName);

	void rateUser(long userId, long ratedUserId, int rating);

	int getRating(long userId, long ratedUserId);
}
