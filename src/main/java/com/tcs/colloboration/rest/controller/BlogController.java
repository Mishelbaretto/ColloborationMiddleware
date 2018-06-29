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

import com.tcs.colloboration.dao.BlogDAO;
import com.tcs.colloboration.model.Blog;
import com.tcs.colloboration.model.BlogComment;
import com.tcs.colloboration.model.User;



@RestController
public class BlogController 
{
	
	@Autowired
	BlogDAO blogDAO;
	
	@Autowired
	User user;
	
	@Autowired
	HttpSession session;
	
	@RequestMapping("/listAllBlogComments/{blogId}")
	public ResponseEntity<List<BlogComment>> showBlogComments(@PathVariable("blogId") int blogId)
	{
		List<BlogComment> listBlogComment=blogDAO.listBlogComments(blogId);
		
		if(listBlogComment.isEmpty())
		{
			return new ResponseEntity<List<BlogComment>>(listBlogComment,HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<List<BlogComment>>(listBlogComment,HttpStatus.OK);
		}
	}
	
	
	@GetMapping("/showAllApprovedBlogs")
	public ResponseEntity<List<Blog>> showAllApprovedBlogs(HttpSession session)
	{
		//String loginname=((UserDetail)session.getAttribute("userDetail")).getLoginname();
		List<Blog> listBlogs=blogDAO.listApprovedBlogs();
		
		if(listBlogs!=null)
		{
		return new ResponseEntity<List<Blog>>(listBlogs,HttpStatus.OK);
		}
		else
		{
		return new ResponseEntity<List<Blog>>(listBlogs,HttpStatus.INTERNAL_SERVER_ERROR);	
		}
	}
	
	@GetMapping("/showAllBlogs")
	public ResponseEntity<List<Blog>> showAllBlog()
	{
		List<Blog> listAllBlogs=blogDAO.listAllBlogs();
		
		if(listAllBlogs!=null)
		{
		return new ResponseEntity<List<Blog>>(listAllBlogs,HttpStatus.OK);
		}
		else
		{
		return new ResponseEntity<List<Blog>>(listAllBlogs,HttpStatus.INTERNAL_SERVER_ERROR);	
		}
	}
	
	@PostMapping("/addBlog")
	public ResponseEntity<String> addBlogg(@RequestBody Blog blog)
	{
		String emailID = (String) session.getAttribute("emailID");
		blog.setEmailID(emailID);
		System.out.println("Blog Name:"+blog.getBlogName());
		System.out.println("Blog Content:"+blog.getBlogContent());
		System.out.println("Emailid:"+blog.getEmailID());
		if(blogDAO.addBlog(blog))
		{
			return new ResponseEntity<String>("Blog Added",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Failure",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/approveBlog/{blogId}")
	public ResponseEntity<String> approveBlog(@PathVariable("blogId") int blogId)
	{
		Blog blog=(Blog)blogDAO.getBlog(blogId);
		
		if(blogDAO.approveBlog(blog))
		{
			return new ResponseEntity<String>("Approved",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error",HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/rejectBlog/{blogId}")
	public ResponseEntity<String> rejectBlog(@PathVariable("blogId") int blogId)
	{
		Blog blog=(Blog)blogDAO.getBlog(blogId);
		
		if(blogDAO.rejectBlog(blog))
		{
			return new ResponseEntity<String>("Rejected",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error",HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/deleteBlog/{blogId}")
	public ResponseEntity<String> deleteBlog(@PathVariable("blogId")int blogId)
	{
		
		if(blogDAO.deleteBlog(blogId))
		{
			return new ResponseEntity<String>("Deleted",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Error",HttpStatus.NOT_FOUND);
		}
	}
	
	@GetMapping("/incrementLike/{blogId}")
	public ResponseEntity<String> incrmentBlog(@PathVariable("blogId") int blogId)
	{
		Blog blog=(Blog)blogDAO.getBlog(blogId);
		
		if(blogDAO.incrementLike(blog))
			return new ResponseEntity<String>("Incremented",HttpStatus.OK);
		else
			return new ResponseEntity<String>("Error Occured",HttpStatus.OK);
	}
	
	@GetMapping("getABlog/{blogId}")
	public ResponseEntity<Blog> getABlog(@PathVariable("blogId")int blogId)
	{
		Blog blog=blogDAO.getBlog(blogId);
		
		if(blog!=null)
		{
			return new ResponseEntity<Blog>(blog,HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<Blog>(blog,HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addComment")
	public ResponseEntity<String> addComment(@RequestBody BlogComment blogComment)
	{
		String emailID = (String) session.getAttribute("emailID");
		blogComment.setEmailID(emailID);
		if(blogDAO.addBlogComment(blogComment))
		{
			return new ResponseEntity<String>("Success",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("Failure",HttpStatus.NOT_FOUND);
		}
		
	}
	
	@GetMapping(value="/deleteblogcomment/{commentId}")	
	public ResponseEntity<String> deleteblogcomment(@PathVariable("commentId") int commentId)
	{
		BlogComment blogcomment=(BlogComment)blogDAO.getBlogComment(commentId);
		if(blogDAO.deleteBlogComment(blogcomment))
		{
			return new ResponseEntity<String>("BlogComment deletd",HttpStatus.OK);
		}
		else
		{
			return new ResponseEntity<String>("delete Failed",HttpStatus.NOT_FOUND);
		}
		
	
	}
	@GetMapping(value="/getblogcomment/{commentId}")
	public ResponseEntity<BlogComment> getBlogComment(@PathVariable("commentId") int commentId)
	{
		BlogComment bc=blogDAO.getBlogComment(commentId);
		if(bc.equals(null))
		{
			return new ResponseEntity<BlogComment>(bc,HttpStatus.NOT_FOUND);
		}
		else
		{
			return new ResponseEntity<BlogComment>(bc,HttpStatus.OK);
		}
	}

	
}
