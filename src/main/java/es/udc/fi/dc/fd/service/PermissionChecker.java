package es.udc.fi.dc.fd.service;

import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;

/**
 * Utilities for checking permissions of the users.
 * 
 */
public interface PermissionChecker {

	/**
	 * Checks if the user identified by the identifier exists. If not an exception
	 * is thrown
	 * 
	 * @param userId identifier of the user
	 */
	public void checkUserExists(Long userId) throws InstanceNotFoundException;

}
