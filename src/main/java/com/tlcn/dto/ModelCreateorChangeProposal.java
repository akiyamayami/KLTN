package com.tlcn.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.model.SttProposal;

public class ModelCreateorChangeProposal {
	private int proposalID;
	private String name;
	private String detail;
	
	private String destination;
	private String pickuplocation;
	
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date pickuptime;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date usefromdate;
	
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date usefromtime;
	
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date usetodate;
	
	@Temporal(TemporalType.TIME)
	@DateTimeFormat(pattern = "HH:mm")
	private Date usetotime;
	
	private MultipartFile file;
	private int carID;
	private boolean fileexist;
	private SttProposal stt;
	public ModelCreateorChangeProposal() {
		super();
	}
	




	public ModelCreateorChangeProposal(int proposalID, String name, String detail, String destination,
			String pickuplocation, Date pickuptime, Date usefromdate, Date usefromtime, Date usetodate, Date usetotime,
			MultipartFile file, int carID, boolean fileexist, SttProposal stt) {
		super();
		this.proposalID = proposalID;
		this.name = name;
		this.detail = detail;
		this.destination = destination;
		this.pickuplocation = pickuplocation;
		this.pickuptime = pickuptime;
		this.usefromdate = usefromdate;
		this.usefromtime = usefromtime;
		this.usetodate = usetodate;
		this.usetotime = usetotime;
		this.file = file;
		this.carID = carID;
		this.fileexist = fileexist;
		this.stt = stt;
	}





	public ModelCreateorChangeProposal(int proposalID, String name, String detail, String destination,
			String pickuplocation, Date pickuptime, Date usefromdate, Date usefromtime, Date usetodate, Date usetotime,
			int carID, boolean fileexist, SttProposal stt) {
		super();
		this.proposalID = proposalID;
		this.name = name;
		this.detail = detail;
		this.destination = destination;
		this.pickuplocation = pickuplocation;
		this.pickuptime = pickuptime;
		this.usefromdate = usefromdate;
		this.usefromtime = usefromtime;
		this.usetodate = usetodate;
		this.usetotime = usetotime;
		this.carID = carID;
		this.fileexist = fileexist;
		this.stt = stt;
	}



	public int getProposalID() {
		return proposalID;
	}
	public void setProposalID(int proposalID) {
		this.proposalID = proposalID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public Date getUsefromdate() {
		return usefromdate;
	}
	public void setUsefromdate(Date usefromdate) {
		this.usefromdate = usefromdate;
	}
	public Date getUsetodate() {
		return usetodate;
	}
	public void setUsetodate(Date usetodate) {
		this.usetodate = usetodate;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public int getCarID() {
		return carID;
	}
	public void setCarID(int carID) {
		this.carID = carID;
	}
	

	public boolean isFileexist() {
		return fileexist;
	}

	public void setFileexist(boolean fileexist) {
		this.fileexist = fileexist;
	}


	public String getDestination() {
		return destination;
	}


	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getPickuplocation() {
		return pickuplocation;
	}

	public void setPickuplocation(String pickuplocation) {
		this.pickuplocation = pickuplocation;
	}


	public Date getPickuptime() {
		return pickuptime;
	}




	public void setPickuptime(Date pickuptime) {
		this.pickuptime = pickuptime;
	}




	public Date getUsefromtime() {
		return usefromtime;
	}




	public void setUsefromtime(Date usefromtime) {
		this.usefromtime = usefromtime;
	}




	public Date getUsetotime() {
		return usetotime;
	}




	public void setUsetotime(Date usetotime) {
		this.usetotime = usetotime;
	}



	public SttProposal getStt() {
		return stt;
	}



	public void setStt(SttProposal stt) {
		this.stt = stt;
	}

	
	
}
