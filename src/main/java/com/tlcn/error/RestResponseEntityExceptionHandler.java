package com.tlcn.error;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.tlcn.dto.ModelException;
import com.tlcn.util.GenericResponse;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler{
	
	@Autowired
    private MessageSource messages;

    public RestResponseEntityExceptionHandler() {
        super();
    }
    
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.error("400 Status Code", ex);
        final BindingResult result = ex.getBindingResult();
        final GenericResponse bodyOfResponse = new GenericResponse(result.getFieldErrors(), result.getGlobalErrors());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
    
    @ExceptionHandler({ InvalidOldPasswordException.class })
    public String handleInvalidOldPassword(final RuntimeException ex, Model model, final WebRequest request) {
        logger.error("400 Status Code", ex);
    	model.addAttribute("x", new ModelException("InvalidOldPassword", 
    			messages.getMessage("message.invalidOldPassword", null, request.getLocale()), 
    			HttpStatus.BAD_REQUEST.value(),HttpStatus.BAD_REQUEST.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({NotOwnerOfProposalException.class})
    public String handleNotOwnerOfThisProposal(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("403 Status Code", ex);
    	model.addAttribute("x", new ModelException("NotOwnerOfThisProposal", 
    			messages.getMessage("message.notOwnerOfThisProposal", null, request.getLocale()), 
    			HttpStatus.FORBIDDEN.value(),HttpStatus.FORBIDDEN.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ ProposalNotFoundException.class })
    public String handleProposalNotFound( RuntimeException ex, Model model, WebRequest request){
    	logger.error("404 Status Code", ex);
    	model.addAttribute("x", new ModelException("ProposalNotFound", 
    			messages.getMessage("message.proposalNotFound", null, request.getLocale()), 
    			HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.getReasonPhrase()));
    	return "messageException";
    	/*logger.error("404 Status Code", ex);
    	final GenericResponse bodyOfResponse = new GenericResponse(messages.getMessage("message.proposalNotFound", null, request.getLocale()), "ProposalNotFound");
    	return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);*/
    }
    
    @ExceptionHandler({ DriverNotFoundException.class })
    public String handleDriverNotFound(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("404 Status Code", ex);
    	model.addAttribute("x", new ModelException("DriverNotFound", 
    			messages.getMessage("message.driverNotFound", null, request.getLocale()), 
    			HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ CarNotFoundException.class })
    public String handleCarNotFound(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("404 Status Code", ex);
    	model.addAttribute("x", new ModelException("CarNotFound", 
    			messages.getMessage("message.carNotFound", null, request.getLocale()), 
    			HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ UserNotFoundException.class })
    public String handleUserNotFound(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("404 Status Code", ex);
    	model.addAttribute("x", new ModelException("UserNotFound", 
    			messages.getMessage("message.userNotFound", null, request.getLocale()), 
    			HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ ProposalHasBeenRemoveException.class })
    public String handleProposalHasBeenRemove(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("404 Status Code", ex);
    	model.addAttribute("x", new ModelException("ProposalHasBeenRemove", 
    			messages.getMessage("message.proposalHasBeenRemove", null, request.getLocale()), 
    			HttpStatus.NOT_FOUND.value(),HttpStatus.NOT_FOUND.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ HaveProposalInTimeUseException.class })
    public String handleHaveProposalInTimeUse(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("405 Status Code", ex);
    	model.addAttribute("x", new ModelException("HaveProposalInTimeUse", 
    			messages.getMessage("message.haveProposalInTimeUse", null, request.getLocale()), 
    			HttpStatus.METHOD_NOT_ALLOWED.value(),HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({IOException.class, MultipartException.class})
    public String handleErrorProcessingFile(final RuntimeException ex, Model model, final WebRequest request){
    	logger.error("500 Status Code", ex);
    	model.addAttribute("x", new ModelException("ErrorProcessingFile", 
    			messages.getMessage("message.ErrorProcessingFile", null, request.getLocale()), 
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    	return "messageException";
    }
    
    @ExceptionHandler({ Exception.class })
    public String handleInternal(final RuntimeException ex, Model model, final WebRequest request) {
        logger.error("500 Status Code", ex);
    	model.addAttribute("x", new ModelException("InternalError", 
    			messages.getMessage("message.error", null, request.getLocale()), 
    			HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
    	return "messageException";
    }
}
