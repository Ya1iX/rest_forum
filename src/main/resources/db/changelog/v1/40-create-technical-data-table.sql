CREATE TABLE technical_data (
        is_deleted boolean not null,
        is_hidden boolean not null,
        changed_at timestamp(6),
        created_at timestamp(6) not null,
        id varchar(255) not null,
        notes varchar(255),
        text text not null,
        primary key (id)
    )

GO

