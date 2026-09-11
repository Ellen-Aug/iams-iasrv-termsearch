package hk.org.ha.iams.termsearch.validation;

import java.io.Serializable;
import hk.org.ha.iams.termsearch.validation.ReturnMessage.Error;

public class ValidateResult implements Serializable {

	private static final long serialVersionUID = 4978035884314321246L;

	private boolean valid;
	private Error error;
	private String errorMessage;

	public ValidateResult() {
		super();
	}

	public ValidateResult(boolean valid, Error error) {
		this.valid = valid;
		this.error = error;
	}

	public ValidateResult(boolean valid, Error error, String errorMessage) {
		this.valid = valid;
		this.error = error;
		this.errorMessage = errorMessage;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean isValid() {
		return valid;
	}

	public void setError(Error error) {
		this.error = error;
	}

	public Error getError() {
		return error;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
