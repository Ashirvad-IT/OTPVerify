package com.ipsator.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ipsator.Entity.User;
import com.ipsator.payload.ApiResponse;
import com.ipsator.payload.Error;
import com.ipsator.payload.ServiceResponse;
import com.ipsator.service.SignUpOtpVerify;
/**
 * 
 * @author Ashirvad Kumar
 * This class is oneTimePassword controller
 */
@RestController
@RequestMapping("/sinup")
public class SignUpOtpVerifyController {

    private final SignUpOtpVerify otpService;

    @Autowired
    public SignUpOtpVerifyController(SignUpOtpVerify otpService) {
        this.otpService = otpService;
    }
    /**
     * This method is calling generateOtp method which belong from OneTimePasswordServiceImpl class
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/otp")
    public ResponseEntity<ApiResponse> verifyOtp(@RequestParam String email, @RequestParam String otp) throws Exception {
    	ServiceResponse<User> response= otpService.verifyOTP(email,otp);
    	if(response.isSuccess()) {
    		return new ResponseEntity<ApiResponse>(new ApiResponse("Success",response.getData(),null),HttpStatus.CREATED);
    	}
    	return new ResponseEntity(new ApiResponse("Error", null, new Error(response.getMessage())),HttpStatus.BAD_REQUEST);
    }
}
