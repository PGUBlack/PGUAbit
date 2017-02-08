package abiturient.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import abiturient.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import abiturient.model.User;
import abiturient.model.Spec;
import abiturient.model.App;
import abiturient.model.UserProfile;


@Controller
@RequestMapping("/")
@SessionAttributes("roles")
public class AppController {

	@Autowired
	UserService userService;

	@Autowired
	SpecService specService;

	@Autowired
	AppService appService;
	
	@Autowired
	UserProfileService userProfileService;
	
	@Autowired
	MessageSource messageSource;

	@Autowired
	PersistentTokenBasedRememberMeServices persistentTokenBasedRememberMeServices;
	
	@Autowired
	AuthenticationTrustResolver authenticationTrustResolver;

	/**
	 *MENU
	 */

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String menu(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "menu";
	}

	
	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<User> users = userService.findAllUsers();
		model.addAttribute("users", users);
		model.addAttribute("loggedinuser", getPrincipal());
		return "userslist";
	}

	/**
	 * This method will list all existing specs.
	 */
	@RequestMapping(value = { "/specs" }, method = RequestMethod.GET, produces={"text/plain; charset=UTF-8"})
	public String listSpecs(ModelMap model) {

		List<Spec> specs = specService.findAllSpecs();
		model.addAttribute("specs", specs);
		model.addAttribute("loggedinuser", getPrincipal());
		return "speclist";
	}

	/**
	 * This method will list all existing applications.
	 */
	@RequestMapping(value = { "/apps" }, method = RequestMethod.GET)
	public String listApps(ModelMap model) {

		List<App> apps = appService.findAllApps();
		model.addAttribute("apps", apps);
		model.addAttribute("loggedinuser", getPrincipal());
		return "applist";
	}

	/**
	 * This method will provide the medium to add a new user.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		User user = new User();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}

	/**
	 * This method will provide the medium to add a new spec.
	 */
	@RequestMapping(value = { "/newspec" }, method = RequestMethod.GET)
	public String newSpec(ModelMap model) {
		DBWork dbWork = new DBWork();

		Spec spec = new Spec();
		model.addAttribute("levels", dbWork.getLevels());
		model.addAttribute("spec", spec);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "specadd";
	}

	/**
	 * This method will provide the medium to add a new application.
	 */
	@RequestMapping(value = { "/newapp" }, method = RequestMethod.GET)
	public String newApp(ModelMap model) {
		App app = new App();
		model.addAttribute("app", app);
		model.addAttribute("edit", false);
		model.addAttribute("loggedinuser", getPrincipal());
		return "appadd";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid User user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*
		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
		 * and applying it on field [sso] of Model class [User].
		 * 
		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
		 * framework as well while still using internationalized messages.
		 * 
		 */
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}
		
		userService.saveUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		//return "success";
		return "registrationsuccess";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newspec" }, method = RequestMethod.POST, produces={"text/plain; charset=UTF-8"})
	public String saveSpec(@Valid Spec spec, BindingResult result,
						   ModelMap model) {

		if (result.hasErrors()) {
			return "specadd";
		}

		if(!specService.isSpecNameUnique(spec.getId(), spec.getName())){
			FieldError nameError =new FieldError("spec","name",messageSource.getMessage("non.unique.name", new String[]{spec.getName()}, Locale.getDefault()));
			result.addError(nameError);
			return "specadd";
		}

		specService.saveSpec(spec);

		model.addAttribute("success", "Spec registered successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		//return "success";
		return "specaddsuccess";
	}

    /**
     * This method will be called on form submission, handling POST request for
     * saving user in database. It also validates the user input
     */
    @RequestMapping(value = { "/newapp" }, produces={"text/plain; charset=UTF-8"}, method = RequestMethod.POST)
    public String saveApp(@Valid App app, BindingResult result,
                           ModelMap model) {

        appService.saveApp(app);

        model.addAttribute("success", "Карточка добавлена");
        model.addAttribute("loggedinuser", getPrincipal());
        //return "success";
        return "appaddsuccess";
    }

	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		User user = userService.findBySSO(ssoId);

		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "registration";
	}

	/**
	 * This method will provide the medium to update an existing spec.
	 */
	@RequestMapping(value = { "/edit-spec-{id}" }, method = RequestMethod.GET)
	public String editSpec(@PathVariable int id, ModelMap model) {
		Spec spec = specService.findById(id);
		DBWork dbWork = new DBWork();

		model.addAttribute("levels", dbWork.getLevels());
		model.addAttribute("spec", spec);
		model.addAttribute("edit", true);
		model.addAttribute("loggedinuser", getPrincipal());
		return "specadd";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid User user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "registration";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "registrationsuccess";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * updating spec in database. It also validates the spec input
	 */
	@RequestMapping(value = { "/edit-spec-{id}" }, method = RequestMethod.POST)
	public String updateSpec(@Valid Spec spec, BindingResult result,
							 ModelMap model, @PathVariable int id) {

		if (result.hasErrors()) {
			return "specadd";
		}

		specService.updateSpec(spec);

		model.addAttribute("success", "Spec " + spec.getCode() + " "+ spec.getName() + " updated successfully");
		model.addAttribute("loggedinuser", getPrincipal());
		return "specaddsuccess";
	}

	
	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}

	/**
	 * This method will delete an spec by it's ID value.
	 */
	@RequestMapping(value = { "/delete-spec-{id}" }, method = RequestMethod.GET)
	public String deleteSpec(@PathVariable int id) {
		specService.deleteById(id);
		return "redirect:/specs";
	}
	

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<UserProfile> initializeProfiles() {
		return userProfileService.findAll();
	}
	
	/**
	 * This method handles Access-Denied redirect.
	 */
	@RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
	public String accessDeniedPage(ModelMap model) {
		model.addAttribute("loggedinuser", getPrincipal());
		return "accessDenied";
	}

	/**
	 * This method handles login GET requests.
	 * If users is already logged-in and tries to goto login page again, will be redirected to list page.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
	    } else {
	    	return "redirect:/list";  
	    }
	}

	/**
	 * This method handles logout requests.
	 * Toggle the handlers if you are RememberMe functionality is useless in your app.
	 */
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){    
			//new SecurityContextLogoutHandler().logout(request, response, auth);
			persistentTokenBasedRememberMeServices.logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/login?logout";
	}

	/**
	 * This method returns the principal[user-name] of logged-in user.
	 */
	private String getPrincipal(){
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		if (principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} else {
			userName = principal.toString();
		}
		return userName;
	}
	
	/**
	 * This method returns true if users is already authenticated [logged-in], else false.
	 */
	private boolean isCurrentAuthenticationAnonymous() {
	    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    return authenticationTrustResolver.isAnonymous(authentication);
	}

	@RequestMapping(value = "/getSpecs", method = RequestMethod.GET, produces={"text/plain; charset=UTF-8"})
	public @ResponseBody String getSpecs(
			@RequestParam("action") int action,
			@RequestParam("code") int code) throws Exception{
		DBWork dbWork = new DBWork();
		String res=dbWork.getSpecs(code, action);
		return res;
	}


}