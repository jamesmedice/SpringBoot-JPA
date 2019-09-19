package com.medici.app.spring.rest;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.medici.app.spring.model.User;
import com.medici.app.spring.repository.UserRepository;
import com.medici.app.spring.rest.vm.KeyAndPasswordVM;
import com.medici.app.spring.rest.vm.ManagedUserVM;
import com.medici.app.spring.sevice.UserService;
import com.medici.app.spring.sevice.dto.UserDTO;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UserRepository userRepository;

	private final UserService userService;

	public AccountResource(UserRepository userRepository, UserService userService) {

		this.userRepository = userRepository;
		this.userService = userService;
	}

	/**
	 * POST /register : register the user.
	 *
	 * @param managedUserVM
	 *            the managed user View Model
	 * @throws InvalidPasswordException
	 *             400 (Bad Request) if the password is incorrect
	 * @throws EmailAlreadyUsedException
	 *             400 (Bad Request) if the email is already used
	 * @throws LoginAlreadyUsedException
	 *             400 (Bad Request) if the login is already used
	 */
	@PostMapping("/register")

	@ResponseStatus(HttpStatus.CREATED)
	public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) throws Exception {
		try {

			if (!checkPasswordLength(managedUserVM.getPassword())) {
				throw new Exception("InvalidPassword");
			}

			userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).ifPresent(u -> {
			});

			userRepository.findOneByEmailIgnoreCase(managedUserVM.getEmail()).ifPresent(u -> {

			});
		} catch (Exception e) {
		}

		User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
	}

	/**
	 * GET /activate : activate the registered user.
	 *
	 * @param key
	 *            the activation key
	 * @throws Exception
	 * @throws RuntimeException
	 *             500 (Internal Server Error) if the user couldn't be activated
	 */
	@GetMapping("/activate")

	public void activateAccount(@RequestParam(value = "key") String key) throws Exception {
		Optional<User> user = userService.activateRegistration(key);
		if (!user.isPresent()) {
			throw new Exception("InternalServerError : No user was found for this activation key");
		}
	}

	/**
	 * GET /authenticate : check if the user is authenticated, and return its
	 * login.
	 *
	 * @param request
	 *            the HTTP request
	 * @return the login if the user is authenticated
	 */
	@GetMapping("/authenticate")

	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	/**
	 * GET /account : get the current user.
	 * 
	 * @param id
	 *
	 * @return the current user
	 * @throws Exception
	 * @throws RuntimeException
	 *             500 (Internal Server Error) if the user couldn't be returned
	 */
	@GetMapping("/account")

	public UserDTO getAccount(Long id) throws Exception {
		return userService.getUserWithAuthorities(id).map(UserDTO::new).orElseThrow(() -> new Exception("InternalServerError: User could not be found"));
	}

	/**
	 * POST /account : update the current user information.
	 *
	 * @param userDTO
	 *            the current user information
	 * @throws Exception 
	 * @throws EmailAlreadyUsedException
	 *             400 (Bad Request) if the email is already used
	 * @throws RuntimeException
	 *             500 (Internal Server Error) if the user login wasn't found
	 */
	@PostMapping("/account")

	public void saveAccount(@Valid @RequestBody UserDTO userDTO) throws Exception {
		Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
		if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userDTO.getLogin()))) {
			throw new Exception("EmailAlreadyUsed");
		}
		Optional<User> user = userRepository.findOneByLogin(userDTO.getLogin());
		if (!user.isPresent()) {
			throw new Exception("InternalServerError: User could not be found");
		}
		userService.updateUser(userDTO);
	}

	/**
	 * POST /account/reset-password/finish : Finish to reset the password of the
	 * user
	 *
	 * @param keyAndPassword
	 *            the generated key and the new password
	 * @throws Exception
	 * @throws InvalidPasswordException
	 *             400 (Bad Request) if the password is incorrect
	 * @throws RuntimeException
	 *             500 (Internal Server Error) if the password could not be
	 *             reset
	 */
	@PostMapping(path = "/account/reset-password/finish")

	public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) throws Exception {
		if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
			throw new Exception("InvalidPassword");
		}
		Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

		if (!user.isPresent()) {
			throw new Exception("InternalServerError: No user was found for this reset key");
		}
	}

	private static boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
	}
}
