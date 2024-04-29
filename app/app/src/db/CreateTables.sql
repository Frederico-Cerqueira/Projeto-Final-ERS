create table if not exists USERS (
    ID serial primary key,
    email varchar(50) not null unique check (email like '%@%'),
    name varchar(50) not null,
    hashPass integer not null,
    token varchar(255)
);
create table if not exists ROBOT (
    ID serial primary key,
    name varchar(255) not null,
    status varchar(255) not null check (status in ('available', 'busy', 'maintenance', 'unavailable', 'charging', 'error')),
    characteristics varchar(255) not null
);

create table if not exists TASK (
    ID serial primary key,
    userID integer not null,
    robotID integer not null,
    name varchar(255) not null,
    status varchar(255) not null check (status in ('pending', 'in progress', 'completed')),
    foreign key (userID) references USERS(ID) on delete cascade on update cascade,
    foreign key (robotID) references ROBOT(ID) on delete cascade on update cascade
);

create table if not exists AREA (
    ID serial primary key,
    taskID integer not null,
    name varchar(255) not null,
    description varchar(255) not null,
    height integer not null,
    width integer not null,
    foreign key (taskID) references TASK(ID) on delete cascade on update cascade
);

create table TIME (
       ID serial primary key,
       taskID integer not null,
       weekDay varchar(255) not null,
       description varchar(255) not null,
       start_time time not null,
       end_time time not null,
       foreign key (taskID) references TASK(ID) on delete cascade on update cascade
)



