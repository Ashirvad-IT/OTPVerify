package com.ipsator.Record;
import java.time.*;
public record OtpDetails(String email,String otp,LocalDateTime expirationTime) {

}
