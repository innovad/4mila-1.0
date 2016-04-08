insert into rt_client (client_nr) values (1);

insert into rt_account(account_nr,username,password,email,first_name,last_name,global_client_nr)
values(1,'admin','KnLCYf/GW8CEoKp5Kswmtw==','adi.moser@gmail.com','Adrian','Moser',1);

insert into rt_user (user_nr, client_nr, username, password, language_uid) values (1,1,'admin','ISMvKXpXpadDiUoOSoAfww==',1152)
