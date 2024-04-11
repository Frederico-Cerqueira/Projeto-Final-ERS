create table if not exists USERS (
  id serial primary key,
  email varchar(50) not null unique check (email like '%@%'),
  name varchar(50) not null,
  password varchar(50) not null,
  token varchar(255)
);
create table if not exists ROBOT (
    id serial primary key,
    name varchar(255) not null,
    status integer not null,
    characteristics varchar(255) not null
);

create table if not exists TASK (
    id serial primary key,
    userId integer not null,
    robotId integer not null,
    name varchar(255) not null,
    status integer not null,
    foreign key (userId) references USERS(id) on delete cascade on update cascade,
    foreign key (robotId) references ROBOT(id) on delete cascade on update cascade
);

create table if not exists AREA (
    id serial primary key,
    taskId integer not null,
    height integer not null,
    width integer not null,
    foreign key (taskId) references TASK(id) on delete cascade on update cascade
);

create table TIME (
       id serial primary key,
       taskId integer not null,
       weekday varchar(9) not null check ( weekday in ('Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday')),
       start_time time not null,
       end_time time not null,
       foreign key (taskId) references TASK(id) on delete cascade on update cascade
)



