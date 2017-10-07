package com.tlcn.error;

public class HaveProposalInTimeUseException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public HaveProposalInTimeUseException(){
		super();
	}
	
	public HaveProposalInTimeUseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public HaveProposalInTimeUseException(final String message) {
        super(message);
    }

    public HaveProposalInTimeUseException(final Throwable cause) {
        super(cause);
    }
}
