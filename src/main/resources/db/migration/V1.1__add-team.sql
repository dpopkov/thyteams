CREATE TABLE team
(
    id UUID NOT NULL,
    version BIGINT NOT NULL,
    name VARCHAR NOT NULL,
    coach_id UUID NOT NULL,
    PRIMARY KEY(id)
);

ALTER TABLE team
    ADD CONSTRAINT FK_team_to_user FOREIGN KEY (coach_id) REFERENCES tt_user;
