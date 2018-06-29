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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tcs.colloboration.dao.JobDAO;
import com.tcs.colloboration.dao.UserDAO;
import com.tcs.colloboration.model.Job;
import com.tcs.colloboration.model.JobApplication;

@RestController("/job/")    
public class JobController {

	@Autowired
	private JobDAO jobDAO;
	
	@Autowired
	private Job job;
	
	
	@Autowired
	private UserDAO userDAO;
	@Autowired
	HttpSession session;
	
	@Autowired
	private JobApplication jobApplication;
	//Job RestController.........................
	
	@GetMapping("/list")
	public ResponseEntity<List<Job>> getAlljobs(){
		
		
		//if there no jobs are available we should provide proper message
	List<Job> jobs=	jobDAO.list();
	if(jobs.isEmpty()) {
		job.setMessage("no jobs are available");
		jobs.add(job);
		return	new ResponseEntity<List<Job>>(jobs,HttpStatus.NOT_FOUND);
	}
	return	new ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
	}
	
	
	
	//save new job ---post job
	
	@PostMapping("/save")
	public ResponseEntity<Job> saveJob(@RequestBody Job job){
		if(jobDAO.save(job)){
			job.setMessage("Job successfully saved");
			return new ResponseEntity<Job>(job,HttpStatus.OK);
		}
		
		else {
			job.setMessage("colud not save the job");
			return new ResponseEntity<Job>(job,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	//update the job sending json object
	
	
	@PostMapping("/update")
	public ResponseEntity<Job> update(@RequestBody Job job){
		if(jobDAO.save(job)){
			job.setMessage("Job successfully updated");
			return new ResponseEntity<Job>(job,HttpStatus.OK);
		}
		
		else {
			job.setMessage("colud not update the job");
			return new ResponseEntity<Job>(job,HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	
	//update the job status by providing only job id and status through url
	@GetMapping("/update/{jobID}/{status}")
	public ResponseEntity<Job> update(@PathVariable int jobID,@PathVariable char status ){
		
		
		//get job by calling jobdao
		
	job=	jobDAO.get(jobID);
	//before updating the status,check whether job
	//existing or not with this jobid
	
	if(job==null) {
		job =new Job();
		job.setMessage("no job existed with this job: "+jobID);
		return new ResponseEntity<Job>(job,HttpStatus.NOT_FOUND);
		
	}
	
	//update the status
	job.setStatus(status);
	if(jobDAO.update(job)) {
		job.setMessage("successfully updated the status of job");
		return new ResponseEntity<Job>(job,HttpStatus.OK);
	}
	return new ResponseEntity<Job>(job,HttpStatus.INTERNAL_SERVER_ERROR);
	
	}
	
	
	//delte the job status=C ----------update the job
	
	
	
	//get the list of jobs based on status
	//http://localhost:8081/colloboration/job/list/N C or P
	@GetMapping("/job/list/{status}")
	public ResponseEntity<List<Job>> getAllJobs(@PathVariable char status){
		
	List<Job> jobs=	jobDAO.list(status);
	
	if(jobs.isEmpty()) {
		job.setMessage("no jobs are available with status"+status);
		jobs.add(job);
		return new	ResponseEntity<List<Job>>(jobs,HttpStatus.NOT_FOUND);
	}
	
	return new	ResponseEntity<List<Job>>(jobs,HttpStatus.OK);
		
	
	
	
	}
	
	
	
	
	@DeleteMapping("/job/delete/{id}")
	public ResponseEntity<Job> deleteJob(@PathVariable int id)
	{
		Job j = jobDAO.get(id);
		
		if(jobDAO.deleteJob(id))
		{
			job.setMessage("Job Deleted Successfully with Jobid: "+id);
			return new ResponseEntity<Job>(job, HttpStatus.OK);
		}
		else
		{
			job = new Job();
			job.setMessage("Cannot delete Job with Jobid: "+id+", please try again later..");
			return new ResponseEntity<Job>(job, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	 @GetMapping(value="/showmyjobs")
		public ResponseEntity <List<JobApplication>> showMyJobs()
		{
		
		 
		 String emailID = (String) session.getAttribute("emailID");
			List<JobApplication> l=jobDAO.list(emailID);
			if(l.size()>0)
			{
				return new ResponseEntity<List<JobApplication>>(l,HttpStatus.OK);
			}
			else
			{
				return new ResponseEntity<List<JobApplication>>(l,HttpStatus.NOT_FOUND);
			}
			
		}
	
	
	
	
	 @RequestMapping("/job/get/{id}")
		public ResponseEntity<Job> getJob(@PathVariable int id)
		{
			job = jobDAO.get(id);
			if(job == null)						// check whether the job with this id is exist or not
			{
				job = new Job();
				job.setMessage("No job is found with JobID: "+id);
				return new ResponseEntity<Job>(job, HttpStatus.NOT_FOUND);
			}
			else
			{
				job.setMessage("This is the particular Job Details of JobID: "+id);
				return new ResponseEntity<Job>(job, HttpStatus.OK);
			}
		}
	
	
	
	
	
	//Jobapplication ...............................
	
	//aply for a particular job
	/*
	
	@GetMapping("/apply/{jobID}/{emailID}")
	public ResponseEntity<JobApplication> applyForJob(int jobID,String emailID){
		//user exist or not
		if(userDAO.get( emailID)==null) {
			jobApplication.setMessage("user des not exist");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
			
		}
		
		//job exist or not
		
		
		if(jobDAO.get(jobID)==null) {
			jobApplication.setMessage("Job des not exist");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
			
		}
		//if  user already applied or not
		if(jobDAO.get(emailID,jobID)==null) {
			jobApplication.setMessage("You already applied gor this job");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
			
		}
		//still the job is opened or not
		
		if(jobDAO.get(jobID,'N')==null) {
			jobApplication.setMessage("This job is not opened");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
		}
		
		
		if(jobDAO.save(jobApplication)) {
			jobApplication.setMessage("Successfully applied for the job");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.OK);
		}
		
		return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
		
		
	}
	
	*/
	
	
	@PostMapping("/apply/job")                                                                   //tested
	public ResponseEntity<JobApplication> jobRegistration(@RequestBody JobApplication jobApplication)
	{
		//	check whether job is opened or not, if not, user should not apply for this job
		if(!jobDAO.isJobOpened(jobApplication.getJobID()))
		{
			jobApplication.setMessage("This job is closed !!!");
			return new ResponseEntity<JobApplication>(jobApplication, HttpStatus.CONFLICT);
		}
				
		if(jobDAO.save(jobApplication))
		{
			jobApplication.setMessage("Registered Successfully..");
			return new ResponseEntity<JobApplication>(jobApplication, HttpStatus.OK);
		}
		else
		{
			jobApplication.setMessage("Cannot Register right now, please try again later..");
			return new ResponseEntity<JobApplication>(jobApplication, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	
	
	
	
	
	//accept/reject/select/call for interview ---admin activities
	
	@GetMapping("/application/update/{jobID}/{emailID}/{status}")
	public ResponseEntity<JobApplication> update(@PathVariable int jobID,@PathVariable String emailID,@PathVariable char status){
		if(userDAO.get( emailID)==null) {
			jobApplication.setMessage("user des not exist");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.OK);
			
		}
		
		//job exist or not
		
		
		if(jobDAO.get(jobID)==null) {
			jobApplication.setMessage("Job des not exist");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.OK);
			
		}
		//if  user already applied or not
		
		if(jobDAO.get(emailID,jobID)!=null) {
			jobApplication.setMessage("You already applied gor this job");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.OK);
			
		}
		//still the job is opened or not
		
		if(jobDAO.get(jobID,'N')==null) {
			jobApplication.setMessage("This job is not opened");
			return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.OK);
		}
		
		jobApplication.setStatus(status);
		if(jobDAO.update(jobApplication)) {
			jobApplication.setMessage("Job application status successfully updated");
		}
		
		return new	ResponseEntity<JobApplication>(jobApplication,HttpStatus.NOT_FOUND);
		
	}
	
	
	
	

	
	//list of ppl who applied for a particular jb                                      //tested
	
	@GetMapping("/application/list/{jobID}")
	public ResponseEntity<List<JobApplication>> list(@PathVariable int jobID){
	List<JobApplication> jobApplications=	jobDAO.list(jobID);
	if(jobApplications.isEmpty()) {
		jobApplication.setMessage("nobody applied for this job till now");
		jobApplications.add(jobApplication);
		return new	ResponseEntity<List<JobApplication>>(jobApplications,HttpStatus.NOT_FOUND);
	}
	return new	ResponseEntity<List<JobApplication>>(jobApplications,HttpStatus.OK);
	}
}
