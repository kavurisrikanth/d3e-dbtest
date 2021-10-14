create table _dfile ( _id varchar(255) not null, _name varchar(255), _size int8 not null, primary key (_id) );

create table _anonymous_user(_id int8 not null, _save_status int4, primary key (_id));

create table _avatar(_id int8 not null, _save_status int4, _size int8 not null, _width int8 not null, _height int8 not null, _file_id varchar(255), _create_from varchar(255), primary key (_id));

create table _card_pay_method(_id int8 not null, _save_status int4, _card_number varchar(255) not null, _valid_till timestamp not null, _cvv varchar(255) not null, _name_on_card varchar(255) not null, primary key (_id));

create table _customer(_id int8 not null, _save_status int4, _name varchar(255) not null, _dob timestamp not null, _underage bool, _gender varchar(255), _guardian_id int8 not null, _guardian_agreement_id varchar(255) not null, primary key (_id));

create table _customer_payment_methods(_customer_id int8 not null, _payment_methods_id int8 not null, _payment_methods_order int4 not null, primary key (_customer_id, _payment_methods_order));

create table _inventory_item(_id int8 not null, _save_status int4, _name varchar(255) not null, _price float8 not null, primary key (_id));

create table _one_time_password(_id int8 not null, _save_status int4, _input varchar(255) not null, _input_type varchar(255) not null, _user_type varchar(255) not null, _success bool not null, _error_msg varchar(255), _token varchar(255), _code varchar(255), _user_id int8, _expiry timestamp, primary key (_id));

create table _order(_id int8 not null, _save_status int4, _customer_id int8 not null, _total_amount float8, _payment_method_id int8 not null, _payment_status varchar(255), _created_date timestamp, primary key (_id));

create table _order_items(_order_id int8 not null, _items_id int8 not null, _items_order int4 not null, primary key (_order_id, _items_order));

create table _order_item(_id int8 not null, _save_status int4, _item_id int8 not null, _quantity int8 not null, _amount float8, primary key (_id));

create table _payment_method(_id int8 not null, _save_status int4, primary key (_id));

create table _report_config(_id int8 not null, _save_status int4, _identity varchar(255) not null, primary key (_id));

create table _report_config_values(_report_config_id int8 not null, _values_id int8 not null, _values_order int4 not null, primary key (_report_config_id, _values_order));

create table _report_config_option(_id int8 not null, _save_status int4, _identity varchar(255) not null, _value varchar(255) not null, primary key (_id));

create table _upipay_method(_id int8 not null, _save_status int4, _upi_id varchar(255) not null, primary key (_id));

create table _user(_id int8 not null, _save_status int4, _is_active bool, _device_token text, primary key (_id));

create table _user_session(_id int8 not null, _save_status int4, _user_session_id text not null, primary key (_id));

alter table if exists _customer_payment_methods drop constraint if exists UK_fcf32b66a693a03b1b635eeda64c7c13;
alter table if exists _customer_payment_methods add constraint UK_fcf32b66a693a03b1b635eeda64c7c13 unique (_payment_methods_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _order_items drop constraint if exists UK_2aeadc6def3b5b0a156eed0bfcd41b3c;
alter table if exists _order_items add constraint UK_2aeadc6def3b5b0a156eed0bfcd41b3c unique (_items_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _report_config_values drop constraint if exists UK_11b5cd6fc9a28a262e626bcd964c7ded;
alter table if exists _report_config_values add constraint UK_11b5cd6fc9a28a262e626bcd964c7ded unique (_values_id) DEFERRABLE INITIALLY DEFERRED;

alter table if exists _anonymous_user add constraint FKeade347c9b950d74e0769e3329c0848a foreign key (_id) references _user DEFERRABLE INITIALLY DEFERRED;

alter table if exists _card_pay_method add constraint FK530aca9b65ac090d526dc211e8339874 foreign key (_id) references _payment_method DEFERRABLE INITIALLY DEFERRED;

alter table if exists _customer add constraint FK99abce5bc2afe759545705a6177ab2ab foreign key (_guardian_id) references _customer DEFERRABLE INITIALLY DEFERRED;

alter table if exists _customer add constraint FKa8e3b2098382057d3f75c6aee229a70d foreign key (_guardian_agreement_id) references _dfile DEFERRABLE INITIALLY DEFERRED;

alter table if exists _customer_payment_methods add constraint FKfcf32b66a693a03b1b635eeda64c7c13 foreign key (_payment_methods_id) references _payment_method DEFERRABLE INITIALLY DEFERRED;
alter table if exists _customer_payment_methods add constraint FKa3390cf1a7795a0d97f5833d22576fbd foreign key (_customer_id) references _customer DEFERRABLE INITIALLY DEFERRED;

alter table if exists _one_time_password add constraint FKd85dc405a5145f1d14e1f920c7ad1330 foreign key (_user_id) references _user DEFERRABLE INITIALLY DEFERRED;

alter table if exists _order add constraint FKff33447cac5cd989a372a1d96288a605 foreign key (_customer_id) references _customer DEFERRABLE INITIALLY DEFERRED;

alter table if exists _order_items add constraint FK2aeadc6def3b5b0a156eed0bfcd41b3c foreign key (_items_id) references _order_item DEFERRABLE INITIALLY DEFERRED;
alter table if exists _order_items add constraint FK2a388994001b7a1dd55aa7a2c8a22c0f foreign key (_order_id) references _order DEFERRABLE INITIALLY DEFERRED;

alter table if exists _order add constraint FK0470a9724683ba68b291b5015f014ee7 foreign key (_payment_method_id) references _payment_method DEFERRABLE INITIALLY DEFERRED;

alter table if exists _order_item add constraint FKa1249d1f590f9c18ca4706f9d8939cf3 foreign key (_item_id) references _inventory_item DEFERRABLE INITIALLY DEFERRED;

alter table if exists _report_config_values add constraint FK11b5cd6fc9a28a262e626bcd964c7ded foreign key (_values_id) references _report_config_option DEFERRABLE INITIALLY DEFERRED;
alter table if exists _report_config_values add constraint FK7fef41cf399803336bfa284138ff2960 foreign key (_report_config_id) references _report_config DEFERRABLE INITIALLY DEFERRED;

alter table if exists _upipay_method add constraint FKd1fde4dacc9e866cc892f87231b5816b foreign key (_id) references _payment_method DEFERRABLE INITIALLY DEFERRED;
