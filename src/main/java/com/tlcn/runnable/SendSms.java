package com.tlcn.runnable;

import com.twilio.Twilio; 
import com.twilio.rest.api.v2010.account.Message; 
import com.twilio.rest.api.v2010.account.MessageCreator; 
import com.twilio.type.PhoneNumber; 
  

public class SendSms implements Runnable{
	private final static String ACCOUNT_SID = "ACd1d711aa02b5b8b38273c42c3e035993"; 
    private final static String AUTH_TOKEN = "1eb458a2d35a9ff56f8a1ff89c705d0f";
    
	@Override
	public void run() {
		sendsms("+84965439140","hello");
		
		
	} 
    public void sendsms(String phoneTo,String text){
    	Twilio.init(ACCOUNT_SID, AUTH_TOKEN); 
        
    	Message message = Message.creator(
    		    new PhoneNumber(phoneTo),  // To number
    		    new PhoneNumber("+18042987372"),  // From number
    		    "Hello world!"                    // SMS body
    		).create();

    		System.out.println(message.getSid());
        System.out.println(message.getSid());
    }
    
}
