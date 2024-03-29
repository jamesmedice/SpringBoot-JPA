
package com.medici.app.spring.sevice;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.medici.app.spring.model.Authority;
import com.medici.app.spring.model.User;
import com.medici.app.spring.repository.AuthorityRepository;
import com.medici.app.spring.repository.UserRepository;
import com.medici.app.spring.sevice.dto.UserDTO;
import com.medici.app.spring.util.AuthoritiesConstants;
import com.medici.app.spring.util.Constants;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

	private final Logger log = LoggerFactory.getLogger(UserService.class);

	private final UserRepository userRepository;

	private final AuthorityRepository authorityRepository;

	public UserService(UserRepository userRepository, AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}

	public Optional<User> activateRegistration(String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository.findOneByActivationKey(key).map(user -> {
			// activate given user for the registration key.
			user.setActivated(true);
			user.setActivationKey(null);
			log.debug("Activated user: {}", user);
			return user;
		});
	}

	public Optional<User> completePasswordReset(String newPassword, String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key).filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400))).map(user -> {
			user.setPassword(newPassword);
			user.setResetKey(null);
			user.setResetDate(null);
			return user;
		});
	}

	public Optional<User> requestPasswordReset(String mail) {
		return userRepository.findOneByEmailIgnoreCase(mail).filter(User::getActivated).map(user -> {
			user.setResetKey(null);
			user.setResetDate(Instant.now());
			return user;
		});
	}

	public User registerUser(UserDTO userDTO, String password) {

		User newUser = new User();
		String encryptedPassword = password;
		newUser.setLogin(userDTO.getLogin());
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(userDTO.getFirstName());
		newUser.setLastName(userDTO.getLastName());
		newUser.setEmail(userDTO.getEmail());
		newUser.setImageUrl(userDTO.getImageUrl());
		newUser.setLangKey(userDTO.getLangKey());
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(null);
		Set<Authority> authorities = new HashSet<>();
		authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(UserDTO userDTO) {
		User user = new User();
		user.setLogin(userDTO.getLogin());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setEmail(userDTO.getEmail());
		user.setImageUrl(userDTO.getImageUrl());
		if (userDTO.getLangKey() == null) {
			user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
		} else {
			user.setLangKey(userDTO.getLangKey());
		}
		if (userDTO.getAuthorities() != null) {
			Set<Authority> authorities = userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet());
			user.setAuthorities(authorities);
		}
		String encryptedPassword = null;
		user.setPassword(encryptedPassword);
		user.setResetKey(null);
		user.setResetDate(Instant.now());
		user.setActivated(true);
		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	/**
	 * Update all information for a specific user, and return the modified user.
	 *
	 * @param userDTO
	 *            user to update
	 * @return updated user
	 */
	public Optional<UserDTO> updateUser(UserDTO userDTO) {
		return Optional.of(userRepository.findById(userDTO.getId())).filter(Optional::isPresent).map(Optional::get).map(user -> {
			user.setLogin(userDTO.getLogin());
			user.setFirstName(userDTO.getFirstName());
			user.setLastName(userDTO.getLastName());
			user.setEmail(userDTO.getEmail());
			user.setImageUrl(userDTO.getImageUrl());
			user.setActivated(userDTO.isActivated());
			user.setLangKey(userDTO.getLangKey());
			Set<Authority> managedAuthorities = user.getAuthorities();
			managedAuthorities.clear();
			userDTO.getAuthorities().stream().map(authorityRepository::findById).filter(Optional::isPresent).map(Optional::get).forEach(managedAuthorities::add);
			log.debug("Changed Information for User: {}", user);
			return user;
		}).map(UserDTO::new);
	}

	public void deleteUser(String login) {
		userRepository.findOneByLogin(login).ifPresent(user -> {
			userRepository.delete(user);
			log.debug("Deleted User: {}", user);
		});
	}
 

	@Transactional(readOnly = true)
	public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
		return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities(Long id) {
		return userRepository.findOneWithAuthoritiesById(id);
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthorities(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login);
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
		for (User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}

	/**
	 * @return a list of all the authorities
	 */
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}
}
