
#stores user login details 
create table users (
    user_id int auto_increment,
    username varchar(100) not null,
    email varchar(100) unique,
    hash varchar(64) not null,
    salt varchar(8) not null,
    constraint usersPK primary key (user_id)
);								

#stores transactions uploaded by users
create table transactions (
    transaction_id int auto_increment primary key,
    user_id int,
    amount decimal(10,2) not null,
    category varchar(50) not null,
    transaction_date date not null,
    description text,
    source varchar(50),
    foreign key(user_id) references users(user_id) on delete cascade
);	

#stores the user's budget limits per category 
#represents how much a user plan to spend per category within a specific time period

create table budgets (
    budget_id int auto_increment primary key,
    user_id int,
    category varchar(50) not null,
    amount decimal(10,2) not null,
    start_date date not null,
    end_date date not null,
    foreign key(user_id) references users(user_id) on delete cascade
); 

create table notifications (
    notification_id int primary key auto_increment,
    user_id int not null,
    message varchar(255) not null,
    timestamp datetime default current_timestamp,
    is_read boolean default false,
    foreign key (user_id) references users(user_id),
    index(user_id),
    index(is_read)
);
