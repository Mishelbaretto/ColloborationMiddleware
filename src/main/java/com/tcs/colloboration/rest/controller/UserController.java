package com.tcs.colloboration.rest.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.colloboration.dao.JobDAO;
import com.tcs.colloboration.dao.UserDAO;
import com.tcs.colloboration.model.JobApplication;
import com.tcs.colloboration.model.User;

@RestController
public class UserController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private User user;
	
	@Autowired
	private JobDAO jobDAO;
	
	
	@Autowired
	HttpSession session;
	
	@Autowired
	private JobApplication jobApplication;
	
	//http://localhost:8080/ColloborationRestService
	
	@GetMapping("/")
	public String serverTest() {
		return "This is my first webservice";
	}

	
	//http://localhost:8081/CollaborationRestService/user/list
	
		@GetMapping("user/list")
		public ResponseEntity< List<User>> getAllUsers()
		{
			List<User> users =  userDAO.list();
			if(users.size()==0) {
				 user.setMessage("No user exited in the app");
			}
			return new ResponseEntity<List<User>>(users, HttpStatus.OK);
		}
		
		//// http://localhost:8081/CollaborationRestService/user/get/jaya@gmail.com
		/**
		 * This method will return user object based on emailID
		 * if emailID exist, will return user object
		 * else empty
		 * @param emailID
		 * @return
		 */
		@GetMapping("user/get/{emailID}")
		public ResponseEntity<User>     getUser(@PathVariable String emailID)
		{
		user=	userDAO.get(emailID);
		if(user==null)
		{
			user=new User();
			 user.setMessage("No user exist with his emailid");
			return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		}
		
		
		@PostMapping("/user/validate")
		public   ResponseEntity<User>      validateCredentials(@RequestBody User user) 
		{
		  user=	userDAO.validate(user.getEmailID(), user.getPassword());
		  
		  if(user==null)
			{
			  user=new User();//to avoid null pointer exception
			  user.setMessage("Invalid credentials pls try again");
				return new ResponseEntity<User>(user,HttpStatus.UNAUTHORIZED);
			}
			else
			{
				String emailID = user.getEmailID();
				session.setAttribute("emailID", emailID);
				 user.setMessage("Successfully logged in");
				return new ResponseEntity<User>(user,HttpStatus.OK);
			}
		  
		}
		
		
		//create new record
		@PostMapping("/user/create")
		public ResponseEntity<User>  createUser(@RequestBody User user)
		{
		
		if(	userDAO.get(user.getEmailID())!=null) {
			
			//if the user already exist with this emailid
			user.setMessage("User already exists with this emailid");
			return new ResponseEntity<User>(user,HttpStatus.CONFLICT);
		}
			
			
		  if(userDAO.save(user))
		  {
			  user.setMessage("User created successfully");
			  return new ResponseEntity<User>(user,HttpStatus.OK);
		  }
		  else
		  {
			  user.setMessage("Internal server problem");
			  return new ResponseEntity<User>(user,HttpStatus.INTERNAL_SERVER_ERROR);
		  }
		}
		
		@PutMapping("user/update")
		public ResponseEntity<User> updateUser(@RequestBody User user){
			
			//check wheteher user exist or not
			if(userDAO.get(user.getEmailID())==null) {

				  user.setMessage("No record exist with this emailid"+user.getEmailID());
				  return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
			}
			if(userDAO.update(user)) {

				  user.setMessage("Successfully updated the details");
				  return new ResponseEntity<User>(user,HttpStatus.OK);
			
			}
			user.setMessage("No record exist with this emailid"+user.getEmailID());
			  return new ResponseEntity<User>(user,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	
	@DeleteMapping("user/delete/{emailID}")
	public ResponseEntity<User> deleteUser(@PathVariable String emailID){
		
		//check wheteher user exist or not
		if(userDAO.get(user.getEmailID())==null) {

			  user.setMessage("No record exist with this emailid"+user.getEmailID());
			  return new ResponseEntity<User>(user,HttpStatus.NOT_FOUND);
		}
		//wheteher this user applied for any job or not
		if(!jobDAO.list(emailID).isEmpty()) {
			
			 user.setMessage("WE should not delete as"+emailID+"applied for the job");
			  return new ResponseEntity<User>(user,HttpStatus.NOT_ACCEPTABLE);
			
		}
		
		if(userDAO.delete(emailID)) {

			  user.setMessage("User Successfully deleted");
			  return new ResponseEntity<User>(user,HttpStatus.OK);
		}
		
		 user.setMessage("Could not delete the record");
		  return new ResponseEntity<User>(user,HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@GetMapping("user/job/list/{emailID}")
	public ResponseEntity<List<JobApplication>> getJobApplicationsAppliedByMe(@PathVariable String emailID){
		
		
	List<JobApplication> jobApplications=	jobDAO.list(emailID);
	
	
	if(jobApplications.isEmpty()) {

		 jobApplication.setMessage("You hace not applied for any job");
		 jobApplications.add(jobApplication);
		  return new ResponseEntity<List<JobApplication>>(jobApplications,HttpStatus.NOT_FOUND);
	}
	 return new ResponseEntity<List<JobApplication>>(jobApplications,HttpStatus.OK);
	}
}
