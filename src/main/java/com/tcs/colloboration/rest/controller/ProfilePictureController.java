package com.tcs.colloboration.rest.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.tcs.colloboration.dao.ProfilePictureDAO;
import com.tcs.colloboration.dao.UserDAO;
import com.tcs.colloboration.model.ProfilePicture;
import com.tcs.colloboration.model.User;

@RestController
public class ProfilePictureController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private User user;
	
	
	@Autowired
	ProfilePictureDAO profilePictureDAO;
	
	@Autowired
	ProfilePicture profilePicture;
	
	@Autowired
	HttpSession session;
	
	/*@RequestMapping(value="/uploadPicture",method=RequestMethod.POST)*/
	@PostMapping("/uploadPicture")
	public ResponseEntity<?> uploadProfilePicture(@RequestParam(value="file")CommonsMultipartFile fileUpload){
		//User user=(User)session.getAttribute("user");
		
		ProfilePicture profilePicture= new ProfilePicture();
		String emailID = (String) session.getAttribute("emailID");
		profilePicture.setEmailID(emailID);
		profilePicture.setImage(fileUpload.getBytes());
		
		
	//	System.out.println("Inside Profile picture controller : "+user.getEmailID());
		if(profilePictureDAO.save(profilePicture)) {
			return new ResponseEntity<Void>(HttpStatus.OK); 
		}else
		{
			return new ResponseEntity<Void>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*if(user==null)
		{
			
			return new ResponseEntity<String>("Unauthorized User",HttpStatus.NOT_FOUND);
		}
		else
		{	
			ProfilePicture profilePicture=new ProfilePicture();
			
			profilePicture.setEmailID(user.getEmailID());
			profilePicture.setImage(fileUpload.getBytes());
			System.out.println("I am in doUpload");
			profilePictureDAO.save(profilePicture);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}*/
		
		
	
	
	}
	
	@RequestMapping("/getImage/{emailID}")
	public @ResponseBody byte[] getProfilePicturee(@PathVariable String emailID)
	{
		 emailID = (String) session.getAttribute("emailID");
		
		 
		
		if(emailID==null)
		{
			return null;
		}
		else
		{/*
			ProfilePicture profilePicture=profilePictureDAO.getProfilePicture(emailID);
			
			if(profilePicture!=null)
			{
				return profilePicture.getImage();
			}
			else
			{
				return null;
			}*/
			profilePicture=profilePictureDAO.getProfilePicture(emailID);
			if(profilePicture!=null)
			{
				return profilePicture.getImage();
			}
			else
			{
				return null;
			}
		}
	}

}
