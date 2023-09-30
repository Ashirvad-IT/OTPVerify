package com.ipsator.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ipsator.Record.UserRecord;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.UserService;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
//import org.springframework.cloud.sleuth.annotation.TraceContext;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api")
public class AppController {
	
	private final UserService userService;
	private final Tracer tracer;
	private final Logger log = LoggerFactory.getLogger(AppController.class);
	
	@Autowired
	public AppController(UserService userService, Tracer tracer) {
		this.userService=userService;
		this.tracer=tracer;
	}
	
	@GetMapping("/trace-example")
    @NewSpan
    public String traceExample(@SpanTag("customTag") String customTagValue) {
        Span currentSpan = this.tracer.currentSpan();
        String traceId = currentSpan.context().traceId();

        // Log the trace ID
        log.info("Trace ID: {}", traceId);

        // Rest of your controller logic

        return "Trace example";
    }
	
	@PostMapping("/user")
	public ResponseEntity<ApiResponse> createUser(@RequestBody UserRecord userRecord){
		if(userRecord==null) {
			//new ResponseEntity(new ApiResponse("Error",null,new Error(result.getMessage())),HttpStatus.BAD_REQUEST)
			return new ResponseEntity<>(new ApiResponse("Error",null,new Error("Please enter the details")),HttpStatus.BAD_REQUEST);
		}
		ServiceResponse<UserRecord> result  = userService.createUser(userRecord);
		if(result.isSuccess()) {
			return new ResponseEntity<>(new ApiResponse<>("Success",result.getData(),null),HttpStatus.OK);
		}else {
			return new ResponseEntity<>(new ApiResponse<>("Error",null,new Error(result.getMessage())),HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping()
	@PreAuthorize("hasAuthority('User:Read')")
	public ResponseEntity<String> secureEndpoint(){
		return new ResponseEntity<String>("It is secure end point",HttpStatus.OK);
	}
	 
	@PostMapping()
	@PreAuthorize("hasAuthority('User:Write')")
	public ResponseEntity<String> createResource(){
		return new ResponseEntity<String>("Resource got created",HttpStatus.CREATED);
	}
	
	@DeleteMapping()
	@PreAuthorize("hasAuthority('User:Delete')")
	public ResponseEntity<String> deleteResource(){
		return new ResponseEntity<String>("Resource got deleted",HttpStatus.OK);
	}
	
	@PutMapping()
	@PreAuthorize("hasAuthority('User:Update')")
	public ResponseEntity<String> updateResource(){
		return new ResponseEntity<String>("Resource got updated",HttpStatus.OK);
	}
}
