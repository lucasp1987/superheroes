-- "user" definition
DROP TABLE IF EXISTS "user" cascade;
CREATE TABLE "user" (
	id bigserial NOT NULL,
	email varchar(255) NOT NULL,
	"password" varchar(255) NOT NULL,
    first_name varchar(255) NULL,
	last_name varchar(255) NULL,
	age integer NULL,
	enabled bool NOT NULL,
	token_expired bool NOT NULL,
	CONSTRAINT user_pkey PRIMARY KEY (id)
);

-- "role" definition
DROP TABLE IF EXISTS "role" cascade;
CREATE TABLE "role" (
	id serial NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT role_pkey PRIMARY KEY (id)
);

-- privilege definition
DROP TABLE IF EXISTS privilege cascade;
CREATE TABLE privilege (
	id serial NOT NULL,
	"name" varchar(255) NULL,
	CONSTRAINT privilege_pkey PRIMARY KEY (id)
);

-- users_roles definition
-- ALTER TABLE IF EXISTS users_roles DROP CONSTRAINT IF EXISTS fkb1ip7h4hxtw8hw3axfhvxwl8;
-- ALTER TABLE IF EXISTS users_roles DROP CONSTRAINT IF EXISTS fkj53n5vhpbsurvdnj9y1x4p0id;
DROP TABLE IF EXISTS users_roles cascade;
CREATE TABLE users_roles (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	role_id int8 NOT NULL,
	CONSTRAINT users_roles_pkey PRIMARY KEY (id),
	CONSTRAINT fkb1ip7h4hxtw8hw3axfhvxwl8 FOREIGN KEY (user_id) REFERENCES "user"(id),
	CONSTRAINT fkj53n5vhpbsurvdnj9y1x4p0id FOREIGN KEY (role_id) REFERENCES "role"(id)
);

-- roles_privileges definition
-- ALTER TABLE IF EXISTS users_roles DROP CONSTRAINT IF EXISTS fkg0k6ch6kn7260hw7qb2d1jj5t;
-- ALTER TABLE IF EXISTS users_roles DROP CONSTRAINT IF EXISTS fkgc9kgxc50pwykdwiy2sxljgob;
DROP TABLE IF EXISTS roles_privileges cascade;
CREATE TABLE roles_privileges (
	id bigserial NOT NULL,
	user_role_id int8 NOT NULL,
	privilege_id int8 NOT NULL,
	CONSTRAINT roles_privileges_pkey PRIMARY KEY (id),
	CONSTRAINT fker3ticsav5641cbuu1dppwkc FOREIGN KEY (user_role_id) REFERENCES users_roles(id),
	CONSTRAINT fkgc9kgxc50pwykdwiy2sxljgob FOREIGN KEY (privilege_id) REFERENCES privilege(id)
);