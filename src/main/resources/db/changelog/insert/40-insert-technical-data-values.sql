INSERT INTO technical_data
        (id, text, notes, is_hidden, is_deleted, changed_at, created_at)
    VALUES
        ('forum_rules_v1', '1. Уважительно относиться к другим участникам форума.\n2. Не использовать вредоносные ссылки.\n3. Не представляться администрацией.', 'Первая версия правил', false, false, null, now())

GO