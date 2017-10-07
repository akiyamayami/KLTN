package com.tlcn.error;

public class CarNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public CarNotFoundException(){
		super();
	}
	
	public CarNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CarNotFoundException(final String message) {
        super(message);
    }

    public CarNotFoundException(final Throwable cause) {
        super(cause);
    }
}
