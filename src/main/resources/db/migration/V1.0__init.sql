-- Use tt_user here as table name instead of user as PostgreSQL does not allow it.
CREATE TABLE tt_user
(
    id UUID NOT NULL,
    PRIMARY KEY (id)
)
