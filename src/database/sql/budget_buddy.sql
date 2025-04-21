/* Since the user uploads a file dynamically, MySQL does not automatically have permission to access it*/
/*we read the CSV file in Java, extract the data, and insert it into MySQL*/
/*We will use Java to:
	1.Read the uploaded CSV file (which the user chooses).
	2.Extract transaction data (user_id, amount, category, date, description, source).
	3.Insert each row into the MySQL database*/




USE bugeting_system;
show tables;

create table users (
	user_id int auto_increment primary key,
    username varchar(100) not null,
    email varchar(100) not null unique,
    password varchar(255) not null
); 												   /*stores user login details*/

create table transactions (
	transaction_id int auto_increment primary key,
    user_id int,
    amount decimal(10,2) not null,
    category varchar(50) not null,
    transaction_date date not null,
    /*description text,*/
    /*source varchar(50),*/
    foreign key(user_id) references users(user_id) on delete cascade
);									           /*stores transactions uploaded by users from csv*/

create table budgets (
	budget_id int auto_increment primary key,
    user_id int,
    category varchar(50) not null,
    amount decimal(10,2) not null,
    start_date date not null,
    end_date date not null,
    foreign key(user_id) references users(user_id) on delete cascade
); /*stores the users budget limits per category/ represents how much a user plan to spend per category within a specific time period*/

Create table notification (
	id int auto_increment primary key,
    user_id int not null,
    category varchar(100),
    message text,
    timestamp datetime default current_timestamp,
    is_read boolean default false,
    foreign key (user_id) references users(user_id) on delete cascade
); /* stores alerts per user
track category
include a message and timestamp
mark if it's been read and delete alerts is the user is deleted*/



/*create table goals (
	goal_id int auto_increment primary key,
    user_id int,
    goal_name varchar(100) not null,
    target_amount decimal(10,2) not null,
    saved_amount decimal(10,2) default 0,
    foreign key (user_id) references users(user_id) on delete cascade
); 				/*represent how much a user wants to save for a specific purpose*/*/

/*create table report_settings(
	setting_id int auto_increment primary key,
    user_id int,
    report_type enum('daily', 'weekly', 'monthly') not null,
    start_date date,
    end_date date,
    chart_type enum('piechart','barchart') not null,
    foreign key(user_id) references users(user_id) on delete cascade
);        /*stores user preferences for reports*/ */