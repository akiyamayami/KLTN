package com.tlcn.error;

public class NotOwnerOfProposalException extends RuntimeException{
	
	private static final long serialVersionUID = 1L;
	
	public NotOwnerOfProposalException(){
		super();
	}
	
	public NotOwnerOfProposalException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotOwnerOfProposalException(final String message) {
        super(message);
    }

    public NotOwnerOfProposalException(final Throwable cause) {
        super(cause);
    }
}
