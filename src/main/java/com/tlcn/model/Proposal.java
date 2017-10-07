package com.tlcn.model;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.tlcn.dto.ModelCreateorChangeProposal;

@Entity(name="proposal")
public class Proposal {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int proposalID;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="typeID")
	private TypeProposal type;
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
	
	
	private String file;
	
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sttproposalID")
	private SttProposal stt;
	
	private boolean expired = false;
	
	@OneToMany(mappedBy = "notifyOfProposal", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private List<NotifyEvent> listnotifyofproposal;
	
	@ManyToOne
	@JoinTable
	(
			name = "registercar",
			joinColumns={ @JoinColumn(name="proposalID", unique=true) },
			inverseJoinColumns={ @JoinColumn(name="carID")}
	)
	private Car car;
	
	@OneToOne(mappedBy = "proposal", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private RegisterProposal userregister;

	@OneToOne(mappedBy = "proposalapproved", cascade = CascadeType.REMOVE, orphanRemoval = true)
	private ConfirmProposal infoconfirm;
	
	
	public Proposal() {
		super();
	}
	
	public boolean checkEqual(ModelCreateorChangeProposal p){
		SimpleDateFormat x = new SimpleDateFormat("HH:mm");
		SimpleDateFormat y = new SimpleDateFormat("dd-MM-yyyy");
		System.out.println(x.format(p.getPickuptime()) + "/////" + x.format(this.getPickuptime()));
		System.out.println(p.toString());
		System.out.println(this.toString());
		if( !p.getName().equals(this.getName()) || !p.getDetail().equals(this.getDetail())
				|| !p.getDestination().equals(this.getDestination()) 
				|| !p.getPickuplocation().equals(this.getPickuplocation()) 
				|| !x.format(p.getPickuptime()).equals(x.format(this.getPickuptime()))
				|| !y.format(p.getUsefromdate()).equals(y.format(this.getUsefromdate())) 
				|| !x.format(p.getUsefromtime()).equals(x.format(this.getUsefromtime())) 
				|| !y.format(p.getUsetodate()).equals(y.format(this.getUsetodate())) 
				|| !x.format(p.getUsetotime()).equals(x.format(this.getUsetotime()))  
				|| p.getCarID() != this.getCar().getCarID()){
			
			return false;
		}
		return true;
	}
	

	public Proposal(TypeProposal type, String name, String detail, String destination, String pickuplocation,
			Date pickuptime, Date usefromdate, Date usefromtime, Date usetodate, Date usetotime, String file,
			SttProposal stt, boolean expired, List<NotifyEvent> listnotifyofproposal, Car car,
			RegisterProposal userregister, ConfirmProposal infoconfirm) {
		super();
		this.type = type;
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
		this.stt = stt;
		this.expired = expired;
		this.listnotifyofproposal = listnotifyofproposal;
		this.car = car;
		this.userregister = userregister;
		this.infoconfirm = infoconfirm;
	}


	

	public Proposal(TypeProposal type, String name, String detail, String destination, String pickuplocation,
			Date pickuptime, Date usefromdate, Date usefromtime, Date usetodate, Date usetotime, SttProposal stt,
			Car car) {
		super();
		this.type = type;
		this.name = name;
		this.detail = detail;
		this.destination = destination;
		this.pickuplocation = pickuplocation;
		this.pickuptime = pickuptime;
		this.usefromdate = usefromdate;
		this.usefromtime = usefromtime;
		this.usetodate = usetodate;
		this.usetotime = usetotime;
		this.stt = stt;
		this.car = car;
	}




	public int getProposalID() {
		return proposalID;
	}

	public void setProposalID(int proposalID) {
		this.proposalID = proposalID;
	}

	public TypeProposal getType() {
		return type;
	}

	public void setType(TypeProposal type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public SttProposal getStt() {
		return stt;
	}

	public void setStt(SttProposal stt) {
		this.stt = stt;
	}

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public RegisterProposal getUserregister() {
		return userregister;
	}

	public void setUserregister(RegisterProposal userregister) {
		this.userregister = userregister;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}


	public List<NotifyEvent> getListnotifyofproposal() {
		return listnotifyofproposal;
	}


	public void setListnotifyofproposal(List<NotifyEvent> listnotifyofproposal) {
		this.listnotifyofproposal = listnotifyofproposal;
	}


	public ConfirmProposal getInfoconfirm() {
		return infoconfirm;
	}


	public void setInfoconfirm(ConfirmProposal infoconfirm) {
		this.infoconfirm = infoconfirm;
	}


	public boolean isExpired() {
		return expired;
	}


	public void setExpired(boolean expired) {
		this.expired = expired;
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

	
	
}
