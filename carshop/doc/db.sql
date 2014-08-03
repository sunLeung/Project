DROP TABLE vehicle;
CREATE TABLE appointment_team (
  id char(32) NOT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  model VARCHAR2(30) DEFAULT NULL,
  member_id VARCHAR2(30) DEFAULT NULL
);

DROP TABLE appointment_team;
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


DROP TABLE appointment_team_dayoff;
CREATE TABLE appointment_team_dayoff (
  id char(32) NOT NULL,
  team_id char(32) DEFAULT NULL,
  dayoff_date DATE DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);

DROP TABLE appointment_consultant_dayoff;
CREATE TABLE appointment_consultant_dayoff (
  id char(32) NOT NULL,
  consultant_id char(32) DEFAULT NULL,
  dayoff_date DATE DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL
);


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
  appointment_day DATE DEFAULT NULL,
  register_no VARCHAR2(20) DEFAULT NULL,
  vin VARCHAR2(20) DEFAULT NULL,
  status INTEGER DEFAULT NULL
);



DROP TABLE appointment_rating;
CREATE TABLE appointment_rating (
  id char(32) NOT NULL,
  appointment_detail_id char(32) DEFAULT NULL,
  team_grade INTEGER DEFAULT NULL,
  consultant_grade INTEGER DEFAULT NULL,
  client_id char(32) DEFAULT NULL
);

---------------------------------活动-------------------------------

DROP TABLE maketing_event;
CREATE TABLE maketing_event (
  id char(32) NOT NULL,
  branch_id char(32) DEFAULT NULL,
  name char(32) DEFAULT NULL,
  signup_from DATE DEFAULT NULL,
  signup_to DATE DEFAULT NULL,
  description varchar2(128) DEFAULT NULL,
  has_picture INTEGER DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  audit_user char(32) DEFAULT NULL,
  create_time DATE DEFAULT NULL,
  modiry_time DATE DEFAULT NULL,
  audit_time DATE DEFAULT NULL
);


DROP TABLE maketing_event_signup;
CREATE TABLE maketing_event_signup (
  id char(32) NOT NULL,
  event_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  open_id char(32) DEFAULT NULL,
  signup_date DATE DEFAULT NULL
);


DROP TABLE maketing_event_points_config;
CREATE TABLE maketing_event_points_config (
  id char(32) NOT NULL,
  event_id char(32) DEFAULT NULL,
  points_amount INTEGER DEFAULT NULL,
  remark char(32) DEFAULT NULL
);

DROP TABLE maketing_eventpoints_code_log;
CREATE TABLE maketing_eventpoints_code_log (
  id char(32) NOT NULL,
  event_points_config_id char(32) DEFAULT NULL,
  operate_user char(32) DEFAULT NULL,
  exec_time DATE DEFAULT NULL
);


DROP TABLE maketing_event_points;
CREATE TABLE maketing_event_points (
  id char(32) NOT NULL,
  event_points_config_id char(32) DEFAULT NULL,
  client_id char(32) DEFAULT NULL,
  gain_date DATE DEFAULT NULL
);

CREATE SEQUENCE  "SYSTEM"."SEQ_INDEX"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 477967 NOCACHE  NOORDER  NOCYCLE  NOPARTITION ;