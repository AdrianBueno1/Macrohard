package es.udc.fi.dc.fd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.repository.UserRepository;

/**
 * Implementation of the permission checker utilities.
 *
 */
@Service
@Transactional(readOnly = true)
public class PermissionCheckerImpl implements PermissionChecker {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void checkUserExists(Long userId) throws InstanceNotFoundException {

		if (!userRepository.existsById(userId)) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

	}

}
