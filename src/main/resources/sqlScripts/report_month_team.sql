CREATE TABLE report_month_team
(
  year           INTEGER NOT NULL,
  month          INTEGER NOT NULL,
  team_id        BIGINT  NOT NULL
    CONSTRAINT report_month_team_team_id_fkey
    REFERENCES team,
  team_name      VARCHAR(255),
  area           DOUBLE PRECISION,
  income         DOUBLE PRECISION,
  paint_expenses DOUBLE PRECISION,
  paint_price    DOUBLE PRECISION,
  salary         DOUBLE PRECISION,
  bonus          DOUBLE PRECISION,
  closed         TIMESTAMP,
  done_by_id     BIGINT
    CONSTRAINT report_month_team_done_by_id_fkey
    REFERENCES user_details,
  CONSTRAINT report_month_team_pkey
  PRIMARY KEY (year, month, team_id)
);