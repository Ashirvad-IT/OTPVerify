package com.ipsator.Record;
import java.time.*;
/**
 * 
 * @author Ashirvad Kumar
 * we are using OtpDetails when we have to provide the response to user regarding oneTimePassword entity.
 */
public record OtpDetails(String email,String otp,LocalDateTime expirationTime) {

}
