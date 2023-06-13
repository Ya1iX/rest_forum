CREATE SEQUENCE sections_sequence START WITH 1 INCREMENT BY 1

GO

CREATE TABLE sections (
        is_deleted boolean not null,
        is_hidden boolean not null,
        is_pinned boolean not null,
        is_secured boolean not null,
        is_users_allowed boolean not null,
        changed_at timestamp(6),
        created_at timestamp(6) not null,
        id bigint not null,
        name varchar(100) not null unique,
        tags varchar(100),
        description varchar(300),
        iconurl varchar(255),
        password varchar(255),
        primary key (id)
)

GO