CREATE TABLE macscode_schema.users
(
    id       BIGINT                DEFAULT NEXTVAL('macscode_schema.user_id_seq') PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL DEFAULT 'USER'
);