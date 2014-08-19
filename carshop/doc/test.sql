DROP TABLE appointment_team;
CREATE TABLE appointment_team (
  id char(32) NOT NULL,
  own_no VARCHAR2(3) DEFAULT NULL,
  name varchar(128) DEFAULT NULL,
  description varchar(128) DEFAULT NULL,
  display_order varchar(128) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);
insert into appointment_team(id,own_no,name) values('747fe9c1c0bd4d37b1887fea7f240e4e','115','shop01TeamA');
insert into appointment_team(id,own_no,name) values('6576aaca316c464480a58d1d297f1b73','115','shop01TeamB');
insert into appointment_team(id,own_no,name) values('4cbb22957ca94c60ae29b09c152ba6ca','114','shop01TeamC');
insert into appointment_team(id,own_no,name) values('4b82cac951e74166ac89530df3c53d74','115','shop02TeamA');
insert into appointment_team(id,own_no,name) values('e4b7ce69877647479e1b29f3d057c3e7','114','shop02TeamB');
select * from appointment_team;

DROP TABLE appointment_consultant;
CREATE TABLE appointment_consultant (
  id char(32) NOT NULL,
  own_no VARCHAR2(3) DEFAULT NULL,
  name varchar(128) DEFAULT NULL,
  display_order varchar(128) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);
insert into appointment_consultant(id,own_no,name) values('8f066ce862334f56b7af7267883e1f13','115','shop01顾问A');
insert into appointment_consultant(id,own_no,name) values('483e55e4401b48489eca641a9d68b816','115','shop01顾问B');
insert into appointment_consultant(id,own_no,name) values('09bfe65388c547ad928498fa2bcf0aec','114','shop01顾问C');
insert into appointment_consultant(id,own_no,name) values('84ebdc65b9ca4ed48162dcc713490cb3','115','shop02顾问A');
insert into appointment_consultant(id,own_no,name) values('184c696317234a648fdf09bcc62be4d0','115','shop02顾问B');
select * from appointment_consultant;

DROP TABLE appointment_team_capacity_cfg;
CREATE TABLE appointment_team_capacity_cfg (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  start_mins_of_day INTEGER DEFAULT NULL,
  end_mins_of_day INTEGER DEFAULT NULL,
  capacity INTEGER DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);
insert into appointment_team_capacity_cfg(id,team_id,start_mins_of_day,end_mins_of_day,capacity) values('391fc8dd802940bb8cc44a02f6ab1b06','747fe9c1c0bd4d37b1887fea7f240e4e',540,600,2);
insert into appointment_team_capacity_cfg(id,team_id,start_mins_of_day,end_mins_of_day,capacity) values('e2122897d49a4e5685976989c3745211','6576aaca316c464480a58d1d297f1b73',540,600,2);
insert into appointment_team_capacity_cfg(id,team_id,start_mins_of_day,end_mins_of_day,capacity) values('6b72b066d8cc46ea8505813139214f20','4cbb22957ca94c60ae29b09c152ba6ca',600,660,1);
insert into appointment_team_capacity_cfg(id,team_id,start_mins_of_day,end_mins_of_day,capacity) values('eff91132b7ba4e74a21bfcc785b18594','4b82cac951e74166ac89530df3c53d74',540,600,2);
insert into appointment_team_capacity_cfg(id,team_id,start_mins_of_day,end_mins_of_day,capacity) values('648e22e989d84462a39a4702ad9d1a79','e4b7ce69877647479e1b29f3d057c3e7',720,780,1);
select * from appointment_team_capacity_cfg;

DROP TABLE appointment_team_dayoff;
CREATE TABLE appointment_team_dayoff (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  dayoff_date DATE DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);
insert into appointment_team_dayoff(id,team_id,dayoff_date) values('b074409379da4546b8a33f37e8f90e2c','6576aaca316c464480a58d1d297f1b73',to_date('2014-08-03','yyyy-mm-dd'));
select * from appointment_team_dayoff;

DROP TABLE appointment_consultant_dayoff;
CREATE TABLE appointment_consultant_dayoff (
  id char(32) NOT NULL,
  consultant_id char(32) DEFAULT NULL,
  dayoff_date DATE DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);
insert into appointment_consultant_dayoff(id,consultant_id,dayoff_date) values('0b607f9eb59d4d29935cc5c80b321ab5','8f066ce862334f56b7af7267883e1f13',to_date('2014-08-03','yyyy-mm-dd'));
select * from appointment_consultant_dayoff;

DROP TABLE appointment_team_remains;
CREATE TABLE appointment_team_remains (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  appointment_start DATE DEFAULT NULL,
  appointment_end DATE DEFAULT NULL,
  hour_of_day INTEGER DEFAULT NULL,
  remains INTEGER DEFAULT NULL
);

DROP TABLE appointment_detail;
CREATE TABLE appointment_detail (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  consultant_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  vin VARCHAR2(20) DEFAULT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  status INTEGER DEFAULT NULL,
  appointment_start DATE DEFAULT NULL,
  appointment_end	DATE DEFAULT NULL,
  model_code VARCHAR2(32) DEFAULT NULL,
  model VARCHAR2(32) DEFAULT NULL,
  own_no VARCHAR2(3) DEFAULT NULL

);

DROP TABLE appointment_rating;
CREATE TABLE appointment_rating (
  id char(32) NOT NULL,
  appointment_detail_id char(32) DEFAULT NULL,
  team_grade INTEGER DEFAULT NULL,
  consultant_grade INTEGER DEFAULT NULL,
  client_id char(32) DEFAULT NULL
);

DROP TABLE maketing_event;
CREATE TABLE maketing_event (
  id char(32) NOT NULL,
  own_no char(3) DEFAULT NULL,
  name char(32) DEFAULT NULL,
  signup_from DATE DEFAULT NULL,
  signup_to DATE DEFAULT NULL,
  description varchar2(128) DEFAULT NULL,
  title_img varchar2(32) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  audit_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL,
  audit_time DATE DEFAULT NULL
);
insert into maketing_event(id,own_no,name,title_img,signup_from,signup_to,description) values('f09e305a48f54efa86bf5f0bbe736c87','115','周末免费检测','test.png',to_date('2014-08-20','yyyy-mm-dd'),to_date('2014-08-23','yyyy-mm-dd'),'<div>大众polo免费送</div><br><img src="/img/polo.jpg"/>');
insert into maketing_event(id,own_no,name,title_img,signup_from,signup_to,description) values('f09e305a48f54efa86bf5f0bbe736c86','115','老顾客送好礼','test.png',to_date('2014-08-21','yyyy-mm-dd'),to_date('2014-08-23','yyyy-mm-dd'),'<div>活动内容XXXX</div><br><div>just activity</div>');

DROP TABLE maketing_event_signup;
CREATE TABLE maketing_event_signup (
  id char(32) NOT NULL,
  event_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  open_id VARCHAR2(64) DEFAULT NULL,
  signup_date DATE DEFAULT NULL
);

DROP TABLE maketing_event_points_config;
CREATE TABLE maketing_event_points_config (
  id char(32) NOT NULL,
  event_id char(32) DEFAULT NULL,
  points_amount INTEGER DEFAULT NULL,
  remark varchar2(32) DEFAULT NULL
);
insert into maketing_event_points_config(id,event_id,points_amount) values('4a4bbb21f898496ea9c99aaae51a3771','f09e305a48f54efa86bf5f0bbe736c87',10);
insert into maketing_event_points_config(id,event_id,points_amount) values('19c7dc88d3be48199fe204c5bcdcfc03','f09e305a48f54efa86bf5f0bbe736c86',16);

DROP TABLE wechat_identity_correlation;
CREATE TABLE wechat_identity_correlation (
	id char(32) NOT NULL,
	account_id VARCHAR2(64) DEFAULT NULL,
	open_id VARCHAR2(64) DEFAULT NULL,
	client_id CHAR(32) DEFAULT NULL,
	vehicle_id CHAR(32) DEFAULT NULL,
	status NUMBER(5) DEFAULT NULL,
	correlation_time DATE DEFAULT NULL,
	uncorrelation_time DATE  DEFAULT NULL
);
insert into wechat_identity_correlation(id,open_id,client_id,vehicle_id,status) values('1a15154657c846cca4b574968cbe9e07','liangyx','2df9a11c64ac4d63b693f77ab73c852f','ba19ab18ad424a009885084c9dcaaea4',1);
insert into wechat_identity_correlation(id,open_id,client_id,vehicle_id,status) values('7469f79a8de848a6a03dc1e34e9b9ebe','liangyx','2df9a11c64ac4d63b693f77ab73c852f','60b358f7648a48aa9122ac4801582052',1);
insert into wechat_identity_correlation(id,open_id,client_id,vehicle_id,status) values('53e12fcf0ccd45578a35a8178eb3e054','liang','e218457dc895492dbd36861c97775a38','c695dc5794484ad1804612c99fe3dcea',1);

DROP TABLE vehicle;
CREATE TABLE vehicle (
	id char(32) NOT NULL,
	vin VARCHAR2(25) DEFAULT NULL,
	register_no VARCHAR2(20) DEFAULT NULL,
	factory_code VARCHAR2(20) DEFAULT NULL,
	factory VARCHAR2(20) DEFAULT NULL,
	model_code VARCHAR2(20) DEFAULT NULL,
	model VARCHAR2(30) DEFAULT NULL,
	member_id VARCHAR2(30) DEFAULT NULL,
	member_type VARCHAR2(14) DEFAULT NULL,
	member_reg_name VARCHAR2(8) DEFAULT NULL,
	member_remark VARCHAR2(60) DEFAULT NULL,
	own_no CHAR(3) DEFAULT NULL,
	member_limit DATE DEFAULT NULL,
	member_no VARCHAR2(20) DEFAULT NULL
);
insert into vehicle(id,vin,register_no,model_code,model,own_no) values('ba19ab18ad424a009885084c9dcaaea4','1237121','粤A2128L','kadh1933h1k','高尔夫7','115');
insert into vehicle(id,vin,register_no,model_code,model,own_no) values('60b358f7648a48aa9122ac4801582052','adad21f','粤A91371','9aadf33h1ok','Polo','114');
insert into vehicle(id,vin,register_no,model_code,model,own_no) values('c695dc5794484ad1804612c99fe3dcea','8127ndsl','粤ALast','adsfffawqok','途观','114');