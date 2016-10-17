create table accounts
(
	ID VARCHAR(30) not null primary key,
	NAME VARCHAR(50) not null,
	URL VARCHAR(300),
	DEFAULT_ACCOUNT INTEGER(1) default false not null,
	COLOR VARCHAR(150)
)