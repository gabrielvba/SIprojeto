package exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ufcg.si1.util.CustomErrorType;

public class findByIdException extends Exception {
	
	public findByIdException (String erro) {
		super("Queixa with id not found" + erro);
	}
}
