DROP TABLE vehicle;
CREATE TABLE vehicle (
  id char(32) NOT NULL,
  vin VARCHAR2(20) DEFAULT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  model VARCHAR2(30) DEFAULT NULL,
  member_id VARCHAR2(30) DEFAULT NULL
);

insert into vehicle (id,vin,register_no,model,member_id) values('6450ac24866d4b75a2ddbf1a55c19303','1923791','粤AMe168','大众高尔夫','liangyx');

CREATE TABLE appointment_team (
  id char(32) NOT NULL,
  branch_id char(32) DEFAULT NULL,
  name varchar(128) DEFAULT NULL,
  description varchar(128) DEFAULT NULL,
  display_order varchar(128) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);

insert into appointment_team(id,branch_id,name) values('747fe9c1c0bd4d37b1887fea7f240e4e','shop01','shop01TeamA');
insert into appointment_team(id,branch_id,name) values('6576aaca316c464480a58d1d297f1b73','shop01','shop01TeamB');
insert into appointment_team(id,branch_id,name) values('4cbb22957ca94c60ae29b09c152ba6ca','shop01','shop01TeamC');
insert into appointment_team(id,branch_id,name) values('4b82cac951e74166ac89530df3c53d74','shop02','shop02TeamA');
insert into appointment_team(id,branch_id,name) values('e4b7ce69877647479e1b29f3d057c3e7','shop02','shop02TeamB');

select * from appointment_team;

DROP TABLE appointment_consultant;
CREATE TABLE appointment_consultant (
  id char(32) NOT NULL,
  branch_id char(32) DEFAULT NULL,
  name varchar(128) DEFAULT NULL,
  display_order varchar(128) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);

insert into appointment_consultant(id,branch_id,name) values('8f066ce862334f56b7af7267883e1f13','shop01','shop01顾问A');
insert into appointment_consultant(id,branch_id,name) values('483e55e4401b48489eca641a9d68b816','shop01','shop01顾问B');
insert into appointment_consultant(id,branch_id,name) values('09bfe65388c547ad928498fa2bcf0aec','shop01','shop01顾问C');
insert into appointment_consultant(id,branch_id,name) values('84ebdc65b9ca4ed48162dcc713490cb3','shop02','shop02顾问A');
insert into appointment_consultant(id,branch_id,name) values('184c696317234a648fdf09bcc62be4d0','shop02','shop02顾问B');
select * from appointment_consultant;

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

CREATE TABLE appointment_team_remains (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  appointment_start DATE DEFAULT NULL,
  appointment_end DATE DEFAULT NULL,
  hour_of_day INTEGER DEFAULT NULL,
  remains INTEGER DEFAULT NULL
);

CREATE TABLE appointment_detail (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  consultant_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  appointment_day DATE DEFAULT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  status INTEGER DEFAULT NULL
);

CREATE TABLE appointment_detail (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  consultant_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  appointment_day DATE DEFAULT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  vin VARCHAR2(20) DEFAULT NULL,
  status INTEGER DEFAULT NULL
);

CREATE TABLE appointment_rating (
  id char(32) NOT NULL,
  appointment_detail_id char(32) DEFAULT NULL,
  team_grade INTEGER DEFAULT NULL,
  consultant_grade INTEGER DEFAULT NULL,
  client_id char(32) DEFAULT NULL
);

CREATE TABLE maketing_event (
  id char(32) NOT NULL,
  branch_id char(32) DEFAULT NULL,
  name char(32) DEFAULT NULL,
  signup_from DATE DEFAULT NULL,
  signup_to DATE DEFAULT NULL,
  d varchar2(128) DEFAULT NULL,
  has_picture INTEGER DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  audit_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL,
  audit_time DATE DEFAULT NULL
);

insert into maketing_event(id,branch_id,name,signup_from,signup_to,description) values('f09e305a48f54efa86bf5f0bbe736c88','shop01','活动1',to_date('2014-08-06','yyyy-mm-dd'),to_date('2014-08-07','yyyy-mm-dd'),'<div>活动内容XXXX</div><br><div>just activity</div>');