package com.tlcn.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.tlcn.dto.ModelCreateorChangeDriver;

@Entity(name="driver")
public class Driver {
	@Id
	private String email;
	private String name;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date birthday;
	private String phone;
	private int experience;
	private String license;
	private String address;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="sttdriverID")
	private SttDriver sttdriver;
	
	
	@OneToMany(mappedBy="driver")
	private List<Car> listcar;
	

	public Driver(String name, Date birthday, String phone, int experience, String license, String address,
			SttDriver sttdriver, List<Car> listcar) {
		super();
		this.name = name;
		this.birthday = birthday;
		this.phone = phone;
		this.experience = experience;
		this.license = license;
		this.address = address;
		this.sttdriver = sttdriver;
		this.listcar = listcar;
	}


	public Driver(String email, String name, Date birthday, String phone, int experience, String license,
			String address, SttDriver sttdriver, List<Car> listcar) {
		super();
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.phone = phone;
		this.experience = experience;
		this.license = license;
		this.address = address;
		this.sttdriver = sttdriver;
		this.listcar = listcar;
	}



	public Driver() {
		super();
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getBirthday() {
		return birthday;
	}


	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public int getExperience() {
		return experience;
	}


	public void setExperience(int experience) {
		this.experience = experience;
	}


	public String getLicense() {
		return license;
	}


	public void setLicense(String license) {
		this.license = license;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public SttDriver getSttdriver() {
		return sttdriver;
	}


	public void setSttdriver(SttDriver sttdriver) {
		this.sttdriver = sttdriver;
	}


	public List<Car> getListcar() {
		return listcar;
	}


	public void setListcar(List<Car> listcar) {
		this.listcar = listcar;
	}


	
	
}
