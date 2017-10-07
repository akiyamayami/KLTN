insert into rights(detail,name) values("","FIND_PROPOSAL");
insert into rights(detail,name) values("","CREATE_PROPOSAL");
insert into rights(detail,name) values("","CONFIRM_PROPOSAL");
insert into rights(detail,name) values("","CHANGE_PROPOSAL");
insert into rights(detail,name) values("","CHECK_OR_FIND_CARS");

insert into role(name) values("USER");
insert into role(name) values("P.TBVT");
insert into role(name) values("BGM");

insert into role_right values(1,1);
insert into role_right values(1,2);
insert into role_right values(1,4);
insert into role_right values(1,5);
insert into role_right values(3,1);
insert into role_right values(3,3);
insert into role_right values(2,1);
insert into role_right values(2,3);
insert into role_right values(2,5);

insert into user values("akiyamayami1@gmail.com","1996-09-24","Nguyễn Văn A","123456","",3);
insert into user values("lyphucloi.it@gmail.com","1996-09-24","Nguyễn Văn B","123456","",2);
insert into user values("123@gmail.com","1996-09-24","Nguyễn Văn C","123456","",1);

insert into sttcar(name) values("Bình Thường");
insert into sttcar(name) values("Bảo trì");

insert into sttdriver(name) values("Bình Thường");
insert into sttdriver(name) values("Bệnh");

insert into typeproposal(name) values("Tạo");
insert into typeproposal(name) values("Chỉnh Sửa");
insert into typeproposal(name) values("Hủy");



insert into car(licenseplate,seats,type,sttcarid) 
	values("00-00 6875",4,"BWM",1);
insert into car(licenseplate,seats,type,sttcarid) 
	values("00-00 6876",6,"Lexus",1);
insert into car(licenseplate,seats,type,sttcarid) 
	values("00-00 6877",10,"Audi",1);
insert into car(licenseplate,seats,type,sttcarid) 
	values("00-00 6878",16,"Kia",1);
insert into car(licenseplate,seats,type,sttcarid) 
	values("00-00 6879",30,"Land Rover",1);


insert into driver values("400@gmail.com","","1996-09-24",3,"A1","Nguyen Van B","0123456789",1);
insert into driver values("401@gmail.com","","1996-09-24",3,"A1","Nguyen Van B","0123456789",1);

insert into driver_car values("400@gmail.com",1);
insert into driver_car values("400@gmail.com",2);
insert into driver_car values("400@gmail.com",3);
insert into driver_car values("401@gmail.com",4);
insert into driver_car values("401@gmail.com",5);

insert into sttproposal values(1,"Đã duyệt");
insert into sttproposal values(0,"Chưa duyệt");


insert into proposal(file,name,detail,sttproposalid,usefromdate,usetodate,typeid,expired,destination,pickuplocation,pickuptime,usefromtime,usetotime)
	values(null,"Tham Quan Renasas","Tham quan công ty renasas .....",0,"2017-07-25","2017-07-26",1,false,"TP.HCM","TPHCM","12:30","12:30","15:30");
insert into proposal(file,name,detail,sttproposalid,usefromdate,usetodate,typeid,expired,destination,pickuplocation,pickuptime,usefromtime,usetotime)
	values(null,"Tham Quan Renasas","Tham quan công ty renasas .....",0,"2017-07-25","2017-07-26",1,false,"TP.HCM","TPHCM","12:30","12:30","15:30");

insert into notifyevent(date_up_event,proposalid,email_user) values(CURDATE(),1,"123@gmail.com");
insert into notifyevent(date_up_event,proposalid,email_user) values(CURDATE(),2,"123@gmail.com");

insert into registercar values(1,1);
insert into registercar values(2,2);

insert into registerproposal(dateregister,proposalid,emailuser) values("2017-07-08",1,"123@gmail.com");
insert into registerproposal(dateregister,proposalid,emailuser) values("2017-07-08",2,"123@gmail.com");