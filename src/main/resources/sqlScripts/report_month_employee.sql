CREATE TABLE report_month_employee
(
  year          INTEGER NOT NULL,
  month         INTEGER NOT NULL,
  employee_id   BIGINT  NOT NULL
    CONSTRAINT report_month_employee_employee_id_fkey
    REFERENCES user_details,
  employee_name VARCHAR(255),
  team_id       BIGINT
    CONSTRAINT report_month_employee_team_id_fkey
    REFERENCES team,
  role          VARCHAR(50),
  area          DOUBLE PRECISION,
  salary        DOUBLE PRECISION,
  bonus         DOUBLE PRECISION,
  closed        TIMESTAMP,
  done_by_id    BIGINT
    CONSTRAINT report_month_employee_done_by_id_fkey
    REFERENCES user_details,
  CONSTRAINT report_month_employee_pkey
  PRIMARY KEY (year, month, employee_id)
);