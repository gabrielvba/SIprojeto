package exceptions;

public class deleteQueixaByIdException extends Exception {

	public deleteQueixaByIdException(String erro) {
		super( erro + "Queixa with id not found");
	}
}
