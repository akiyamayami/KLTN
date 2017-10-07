package com.tlcn.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.tlcn.model.Car;

public class ModelCreateorChangeDriver {
	private String email;
	private String name;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	private Date birthday;
	private String phone;
	private int experience;
	private String license;
	private String address;
	
	private int sttdriverID;
	private List<Car> listcar;
	private MultipartFile file;
	
	
	public ModelCreateorChangeDriver() {
		super();
	}
	
	
	public ModelCreateorChangeDriver(String email, String name, Date birthday, String phone, int experience,
			String license, String address, int sttdriverID, List<Car> listcar) {
		super();
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.phone = phone;
		this.experience = experience;
		this.license = license;
		this.address = address;
		this.sttdriverID = sttdriverID;
		this.listcar = listcar;
	}


	public ModelCreateorChangeDriver(String email, String name, Date birthday, String phone, int experience,
			String license, String address, int sttdriverID, List<Car> listcar, MultipartFile file) {
		super();
		this.email = email;
		this.name = name;
		this.birthday = birthday;
		this.phone = phone;
		this.experience = experience;
		this.license = license;
		this.address = address;
		this.sttdriverID = sttdriverID;
		this.listcar = listcar;
		this.file = file;
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

	public int getSttdriverID() {
		return sttdriverID;
	}
	public void setSttdriverID(int sttdriverID) {
		this.sttdriverID = sttdriverID;
	}
	public List<Car> getListcar() {
		return listcar;
	}
	public void setListcar(List<Car> listcar) {
		this.listcar = listcar;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	@Override
	public String toString() {
		return "ModelCreateorChangeDriver [email=" + email + ", name=" + name + ", birthday=" + birthday + ", phone="
				+ phone + ", experience=" + experience + ", license=" + license + ", address=" + address
				+ ", sttdriverID=" + sttdriverID + ", listcar=" + listcar + ", file=" + file + "]";
	}
	
	
}
