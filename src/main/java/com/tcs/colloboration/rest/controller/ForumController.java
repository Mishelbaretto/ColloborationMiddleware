package com.tcs.colloboration.rest.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.colloboration.dao.ForumDAO;
import com.tcs.colloboration.model.Blog;
import com.tcs.colloboration.model.Forum;



@RestController
public class ForumController 
{

	@Autowired
	ForumDAO forumDAO;
	
	@Autowired
	HttpSession session;
	@GetMapping(value="/listForum")
	public ResponseEntity<List<Forum>> getForumList()
	{
		
		List<Forum> listForums=forumDAO.listAllForums();
		
		if(listForums.size()>0)
		{
			return new ResponseEntity<List<Forum>>(listForums,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<List<Forum>>(listForums,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/deleteForum/{forumId}")
	public ResponseEntity<String> deleteForum(@PathVariable("forumId")int forumId)
	{
		Forum forum=forumDAO.getForum(forumId);
		
		if(forumDAO.deleteForum(forum))
		{
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error Occured",HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value="/addForum")
	public ResponseEntity<String> addForum(@RequestBody Forum forum)
	{
		forum.setCreatedDate(new java.util.Date());
		forum.setLikes(0);
		
		forum.setStatus("NA");
		String emailID = (String) session.getAttribute("emailID");
		forum.setEmailID(emailID);
		if(forumDAO.addForum(forum))
		{
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error Occured",HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping(value="/approveForum/{forumId}")
	public ResponseEntity<String> approveForum(@PathVariable("forumId")int forumId)
	{
		Forum forum=(Forum)forumDAO.getForum(forumId);
		
		if(forumDAO.approveForum(forum))
		{
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error Occured",HttpStatus.NOT_FOUND);
		}
	}
	
	
   @GetMapping(value="/incLike/{forumId}")
	public ResponseEntity<String> incrementForum(@PathVariable("forumId")int forumId){
	if(forumDAO.incLike(forumId)) {
		return new ResponseEntity<String>("Success",HttpStatus.OK);
	}
	else {
		return new ResponseEntity<String>("Error Occured",HttpStatus.NOT_FOUND);
	}
		
	}
	
	@GetMapping(value="/rejectForum/{forumId}")
	public ResponseEntity<String> rejectForum(@PathVariable("forumId")int forumId)
	{
		Forum forum=(Forum)forumDAO.getForum(forumId);
		if(forumDAO.rejectForum(forum))
		{
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error Occured",HttpStatus.NOT_FOUND);
		}
	}
	
}


