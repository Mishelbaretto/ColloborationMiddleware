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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.colloboration.dao.FriendDAO;
import com.tcs.colloboration.dao.UserDAO;
import com.tcs.colloboration.model.Friend;
import com.tcs.colloboration.model.User;

@RestController
public class FriendController {
	
	@Autowired
	FriendDAO friendDAO;
	
	@Autowired
	Friend friend;
	
	@Autowired
	User user;
	
	@Autowired
	UserDAO userDAO;
	
	@Autowired
	HttpSession session;
	@PostMapping(value="/sendFriendRequest/{name}")
	public ResponseEntity<Friend> sendFriendRequest(@PathVariable String name)
	{
		String emailID = (String) session.getAttribute("emailID");
		User u = userDAO.getname(name);
		friend.setEmailID(emailID);
		friend.setFriendemailID(u.getEmailID());
		if(friendDAO.sendFriendRequest(friend))
		{
			return new ResponseEntity<Friend>(friend,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Friend>(friend,HttpStatus.NOT_FOUND);
		}
	}
	
	
	@RequestMapping(value="/showFriendList")
	public ResponseEntity<List<Friend>> showFriendList()
	{
		String emailID = (String) session.getAttribute("emailID");
	
		System.out.println("Inside showFriendList - from HttpSession - EmailID:" + emailID);
		List<Friend> listFriends=friendDAO.showFriendList(emailID);
		
		if(listFriends.isEmpty())
		{
			Friend friend = new Friend();
		
		//	friend.setsetStatusMessage("No Friends Yet !!!");

			return new ResponseEntity<List<Friend>>(listFriends,HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<List<Friend>>(listFriends,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value="/showPendingRequest")
	public ResponseEntity<List<Friend>> showPendingRequest(HttpSession session)
	{
	
		String emailID = (String) session.getAttribute("emailID");
		
		List<Friend> pendingFriendRequest=friendDAO.showPendingRequest(emailID);
		
		if(pendingFriendRequest.size()>0)
		{
			return new ResponseEntity<List<Friend>>(pendingFriendRequest,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<List<Friend>>(pendingFriendRequest,HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/showSuggestedFriend")
	public ResponseEntity<List<User>> showSuggestedFriend(HttpSession session)
	{
		/*String emailID=((User)session.getAttribute("user")).getEmailID();*/
		String emailID = (String) session.getAttribute("emailID");
		List<User> showSuggestedFriend=friendDAO.showSuggestedFriend(emailID);
		
		if(showSuggestedFriend.size()>0)
		{
			return new ResponseEntity<List<User>>(showSuggestedFriend,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<List<User>>(showSuggestedFriend,HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping(value="/acceptFriendRequest/{friendID}")
	public ResponseEntity<String> acceptFriendRequest(@PathVariable("friendID") int friendId)
	{
		if(friendDAO.acceptFriendRequest(friendId))
		{
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("failure",HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping(value="/deleteFriendRequest/{friendID}")
	public ResponseEntity<String> deleteFriendRequest(@PathVariable("friendID") int friendID)
	{
		if(friendDAO.deleteFriendRequest(friendID))
		{
			return new ResponseEntity<String>("success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("failure",HttpStatus.NOT_FOUND);
		}
	}

}
