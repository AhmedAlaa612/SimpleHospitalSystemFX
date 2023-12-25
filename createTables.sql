create table specialization(
	spec_id int primary key auto_increment,
	name varchar(50) not null
);
create table doctor(
	id int primary key auto_increment,
    fname varchar(50) not null,
    lname varchar(50) not null,
    salary int not null,
    spec_id int not null,
    phone varchar(50) not null,
    foreign key(spec_id) references specialization(spec_id)
);

create table patient(
	pat_id int primary key auto_increment,
    fname varchar(50) not null,
    lname varchar(50) not null, 
    phone varchar(50) not null, 
    gender varchar(20) not null,
    birthdate date not null
);


create table p_status(
	stat_id int primary key,
    name text not null
);


create table appointments(
	id int primary key auto_increment,
	doc_id int not null,
    stat_id int not null,
    pat_id int not null,
    foreign key(doc_id) references doctor(id),
    foreign key(stat_id) references p_status(stat_id),
    foreign key(pat_id) references patient(pat_id)
);
insert into p_status
values	(0, 'Normal'),
		(1, 'Urgent'),
        (2, 'emergency');
