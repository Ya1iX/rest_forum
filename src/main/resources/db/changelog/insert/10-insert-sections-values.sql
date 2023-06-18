INSERT INTO sections
        (id, name, description, tags, created_at, changed_at, is_secured, password, iconurl, is_deleted, is_hidden, is_pinned, is_users_allowed)
    VALUES
        (nextval('sections_sequence'), 'Машины', 'Раздел для обсуждения различных машин', 'car,speed,technology', now(), null, false, null, null, false, false, false, true)

GO

INSERT INTO sections
        (id, name, description, tags, created_at, changed_at, is_secured, password, iconurl, is_deleted, is_hidden, is_pinned, is_users_allowed)
    VALUES
        (nextval('sections_sequence'), 'Компьютеры', 'Раздел для обсуждения различных сборок и комплектующих', 'pc,computer,gaming,work,technologies', now(), null, false, null, null, false, false, false, true)

GO

INSERT INTO sections
        (id, name, description, tags, created_at, changed_at, is_secured, password, iconurl, is_deleted, is_hidden, is_pinned, is_users_allowed)
    VALUES
        (nextval('sections_sequence'), 'Информационные технологии', 'Здесь вы можете обсуждать различные ЯП, инструменты, фреймворки, задавать вопросы и т.п.', 'pc,web,apps,programming,learning', now(), null, false, null, null, false, false, false, true)

GO