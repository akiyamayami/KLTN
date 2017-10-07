package com.tlcn.error;

public class ProposalNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ProposalNotFoundException(){
		super();
	}
	
	public ProposalNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProposalNotFoundException(final String message) {
        super(message);
    }

    public ProposalNotFoundException(final Throwable cause) {
        super(cause);
    }
	
}
