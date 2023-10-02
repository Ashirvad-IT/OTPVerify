package com.ipsator.payload;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * ServiceResponse is a generic class used to represent the response of a service operation.
 * It contains information about the success or failure of the operation, the data returned
 * by the operation, and an optional message to describe the result.
 *
 * @param <T> The type of data contained in the response.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceResponse<T> {
	
	private boolean success;
	private T data;
	private String message;
	
	
	
	/**
     * Gets a ResponseEntity containing an ApiResponse representing this ServiceResponse.
     *
     * @return A ResponseEntity<ApiResponse<T>> representing the ServiceResponse.
     */
	public ResponseEntity<ApiResponse> getResponse(){
		if(this.success) {
			// Return a ResponseEntity with a success ApiResponse
			return new ResponseEntity(new ApiResponse<>("success",this.data,null),HttpStatus.OK);
		}else {
			// Return a ResponseEntity with an error ApiResponse
			return new ResponseEntity<ApiResponse>(new ApiResponse<>("Error",null,new Error(message)),HttpStatus.BAD_REQUEST);
		}
	}
	
}
 