CREATE TABLE superhero (
	id bigint AUTO_INCREMENT NOT NULL,
	"descriptor" varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
    power varchar(50) NOT NULL,
	prefix varchar(25) NULL,
	suffix varchar(25) NULL,
	CONSTRAINT superhero_pkey PRIMARY KEY (id)
);