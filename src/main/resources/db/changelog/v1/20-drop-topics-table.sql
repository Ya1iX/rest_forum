ALTER TABLE IF EXISTS topics
       DROP CONSTRAINT IF EXISTS FK_topics_1

GO

ALTER TABLE IF EXISTS topics
       DROP CONSTRAINT IF EXISTS FK_topics_2

GO

DROP TABLE IF EXISTS topics CASCADE

GO

DROP SEQUENCE IF EXISTS topics_sequence

GO