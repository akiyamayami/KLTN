package com.tlcn.error;

public class ProposalHasBeenRemoveException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public ProposalHasBeenRemoveException(){
		super();
	}
	
	public ProposalHasBeenRemoveException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ProposalHasBeenRemoveException(final String message) {
        super(message);
    }

    public ProposalHasBeenRemoveException(final Throwable cause) {
        super(cause);
    }
}
