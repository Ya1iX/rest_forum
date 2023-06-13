CREATE TABLE technical_data (
        is_public boolean not null,
        id varchar(255) not null,
        notes varchar(255),
        text text not null,
        primary key (id)
)

GO

