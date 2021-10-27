package es.udc.fi.dc.fd.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.fi.dc.fd.model.exceptions.DuplicateEmailException;
import es.udc.fi.dc.fd.model.exceptions.DuplicateUserException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectCreditCardException;
import es.udc.fi.dc.fd.model.exceptions.IncorrectLoginException;
import es.udc.fi.dc.fd.model.exceptions.InstanceNotFoundException;
import es.udc.fi.dc.fd.model.persistence.Ad;
import es.udc.fi.dc.fd.model.persistence.Rating;
import es.udc.fi.dc.fd.model.persistence.User;
import es.udc.fi.dc.fd.model.persistence.User.RoleType;
import es.udc.fi.dc.fd.repository.AdRepository;
import es.udc.fi.dc.fd.repository.RatingRepository;
import es.udc.fi.dc.fd.repository.UserRepository;

/**
 * Default implementation of the user service.
 * 
 */
@Service("userService")
@Transactional
public class UserServiceDefaultImpl implements UserService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AdService adService;

	@Autowired
	private AdRepository adRepository;

	
	@Autowired
	RatingRepository ratingRepository;
	
	// Modificamos el valor de la variable premium de anuncios para indicar si el propietario
	// del anuncio es un usuario premium o normal, si es un usuario normal establecemos la variable
	// premium  a false y si es un usuario premium a true
	private List<Ad> changeAdPremium(List<Ad> userAdList, boolean premium) {

		for (int i = 0; i < userAdList.size(); i++) {
			userAdList.get(i).setPremium(premium);
			adRepository.updateAdPremium(premium, userAdList.get(i).getId());
		}

		return userAdList;

	}

	@Override
	public void signUp(User user) throws DuplicateUserException, DuplicateEmailException, IncorrectCreditCardException {

		if (userRepository.existsByUserName(user.getUserName()))
			throw new DuplicateUserException();

		if (userRepository.existsByEmail(user.getEmail()))
			throw new DuplicateEmailException();

		if (user.getCreditCard().length() != 16) {
			throw new IncorrectCreditCardException();
		}

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setRole(User.RoleType.USER);

		userRepository.save(user);

	}

	@Override
	@Transactional(readOnly = true)
	public User login(String userName, String password) throws IncorrectLoginException {

		Optional<User> user = userRepository.findByUserName(userName);
		if (!user.isPresent()) {
			throw new IncorrectLoginException(userName, password);
		}

		if (!passwordEncoder.matches(password, user.get().getPassword())) {
			throw new IncorrectLoginException(userName, password);
		}

		return user.get();

	}

	@Override
	@Transactional(readOnly = true)
	public User checkUser(Long userId) throws InstanceNotFoundException {

		Optional<User> user = userRepository.findById(userId);

		if (!user.isPresent()) {
			throw new InstanceNotFoundException("project.entities.user", userId);
		}

		return user.get();

	}

	@Override
	public void becomePremium(String userName) {
		Optional<User> user = userRepository.findByUserName(userName);
		List<Ad> userAdList = adService.showUserAds(userName);
		if (user.get().getRole() == RoleType.PREMIUM) {
			user.get().setRole(RoleType.USER);
			changeAdPremium(userAdList, false);

		} else {
			user.get().setRole(RoleType.PREMIUM);
			changeAdPremium(userAdList, true);
		}
	}

	@Override
	public int getRating(long userId, long ratedUserId) {
		Rating r = ratingRepository.findByUserIdAndRatedUserId(userId, ratedUserId);

		int rating = -1;

		if (r != null) {

			rating = r.getValue();
		}

		return rating;

	}

	@Override
	public void rateUser(long userId, long ratedUserId, int rating) {

		User user = userRepository.findById(ratedUserId).get();
		float actualRating = user.getRate();
		int timesRated = user.getTimesRated();

		int ratingPreviousValue = getRating(userId, ratedUserId);

		int newTimesRated = 0;
		float newValue = 0;

		if (ratingPreviousValue == -1) {
			newTimesRated = timesRated + 1;
			newValue = (timesRated * actualRating + rating) / newTimesRated;

		} else {
			newTimesRated = timesRated;
			newValue = (timesRated * actualRating - ratingPreviousValue + rating) / newTimesRated;

		}

		user.setTimesRated(newTimesRated);
		user.setRate(newValue);
		userRepository.save(user);

		Rating r = ratingRepository.findByUserIdAndRatedUserId(userId, ratedUserId);
		if (r != null) {
			r.setValue(rating);
			ratingRepository.save(r);
		} else {
			ratingRepository.save(new Rating(0L, userId, ratedUserId, rating));
		}

	}

}
