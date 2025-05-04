create database Budget_Buddy;
use Budget_Buddy;


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
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(100),
    transaction_date DATE NOT NULL,
    CONSTRAINT transactionsPK PRIMARY KEY (transaction_id),
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(user_id)
        ON DELETE CASCADE
);

#One plant per goal streak
CREATE TABLE plants (
    plant_id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    plant_type VARCHAR(100) NOT NULL,  -- e.g., "Rose", "Sunflower"
    growth_stage INT DEFAULT 0,         -- 0 = seed, 1–3 = growth stages
    is_wilted BOOLEAN DEFAULT FALSE,
    is_in_garden BOOLEAN default false,
    CONSTRAINT plantsPK PRIMARY KEY (plant_id),
    CONSTRAINT fk_plant_user FOREIGN KEY (user_id) REFERENCES users(user_id)
);

#each goal is tied to a user, plant, and a category with a spending limit
create table budget (
	budget_id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    category VARCHAR(100) NOT NULL, 
    goal_amount DOUBLE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    amount_spent DOUBLE DEFAULT 0,
    status Enum('IN_PROGRESS','COMPLETED','FAILED') DEFAULT 'IN_PROGRESS',
    plant_id INT NOT NULL,
    CONSTRAINT budgetPK PRIMARY KEY (budget_id),
    CONSTRAINT budgetUserFK FOREIGN KEY (user_id) REFERENCES users(user_id),
    CONSTRAINT budgetPlantFK FOREIGN KEY (plant_id) REFERENCES plants(plant_id)

);
