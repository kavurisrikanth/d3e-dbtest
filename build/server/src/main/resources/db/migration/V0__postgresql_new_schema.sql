create table _dfile ( _id varchar(255) not null, _name varchar(255), _size int8 not null, primary key (_id) );

create table _anonymous_user(_id int8 not null, _save_status int4, primary key (_id));

create table _avatar(_id int8 not null, _save_status int4, _size int8 not null, _width int8 not null, _height int8 not null, _file_id varchar(255), _create_from varchar(255), primary key (_id));

create table _creatable(_id int8 not null, _save_status int4, _name varchar(255), _ref_id int8, _child_id int8, _file_id varchar(255), _emb_name varchar(255), primary key (_id));

create table _creatable_ref_coll(_creatable_id int8 not null, _ref_coll_id int8 not null, _ref_coll_order int4 not null, primary key (_creatable_id, _ref_coll_order));

create table _creatable_child_coll(_creatable_id int8 not null, _child_coll_id int8 not null, _child_coll_order int4 not null, primary key (_creatable_id, _child_coll_order));

create table _non_creatable(_id int8 not null, _save_status int4, _name varchar(255) NonCreatable name, _emb_name varchar(255), primary key (_id));

create table _one_time_password(_id int8 not null, _save_status int4, _input varchar(255) not null, _input_type varchar(255) not null, _user_type varchar(255) not null, _success bool not null, _error_msg varchar(255), _token varchar(255), _code varchar(255), _user_id int8, _expiry timestamp, primary key (_id));

create table _report_config(_id int8 not null, _save_status int4, _identity varchar(255) not null, primary key (_id));

create table _report_config_values(_report_config_id int8 not null, _values_id int8 not null, _values_order int4 not null, primary key (_report_config_id, _values_order));

create table _report_config_option(_id int8 not null, _save_status int4, _identity varchar(255) not null, _value varchar(255) not null, primary key (_id));

create table _user(_id int8 not null, _save_status int4, _is_active bool, _device_token text, primary key (_id));

create table _user_session(_id int8 not null, _save_status int4, _user_session_id text not null, primary key (_id));

alter table if exists _creatable_ref_coll drop constraint if exists UK_7610666e7241c22ba5fbbb7b5526e058;
alter table if exists _creatable_ref_coll add constraint UK_7610666e7241c22ba5fbbb7b5526e058 unique (_ref_coll_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable_child_coll drop constraint if exists UK_670d13f38eb6d767cb2155a01bef9050;
alter table if exists _creatable_child_coll add constraint UK_670d13f38eb6d767cb2155a01bef9050 unique (_child_coll_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _report_config_values drop constraint if exists UK_11b5cd6fc9a28a262e626bcd964c7ded;
alter table if exists _report_config_values add constraint UK_11b5cd6fc9a28a262e626bcd964c7ded unique (_values_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _anonymous_user add constraint FKeade347c9b950d74e0769e3329c0848a foreign key (_id) references _user DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable add constraint FKf515b3a7886917c8bed51e7020b25c55 foreign key (_ref_id) references _creatable DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable_ref_coll add constraint FK7610666e7241c22ba5fbbb7b5526e058 foreign key (_ref_coll_id) references _creatable DEFERRABLE INITIALLY DEFERRED;
alter table if exists _creatable_ref_coll add constraint FK327f2f79fe977b472e6e52129c68359d foreign key (_creatable_id) references _creatable DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable add constraint FK6dd4e70d796ad8a41239f5011d7c1a3c foreign key (_child_id) references _non_creatable DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable_child_coll add constraint FK670d13f38eb6d767cb2155a01bef9050 foreign key (_child_coll_id) references _non_creatable DEFERRABLE INITIALLY DEFERRED;
alter table if exists _creatable_child_coll add constraint FKc88739facb4a79da6c1e684f1b2617f4 foreign key (_creatable_id) references _creatable DEFERRABLE INITIALLY DEFERRED;

alter table if exists _creatable add constraint FK21b6ce5535e28a0455018b260d4de501 foreign key (_file_id) references _dfile DEFERRABLE INITIALLY DEFERRED;

alter table if exists _one_time_password add constraint FKd85dc405a5145f1d14e1f920c7ad1330 foreign key (_user_id) references _user DEFERRABLE INITIALLY DEFERRED;

alter table if exists _report_config_values add constraint FK11b5cd6fc9a28a262e626bcd964c7ded foreign key (_values_id) references _report_config_option DEFERRABLE INITIALLY DEFERRED;
alter table if exists _report_config_values add constraint FK7fef41cf399803336bfa284138ff2960 foreign key (_report_config_id) references _report_config DEFERRABLE INITIALLY DEFERRED;