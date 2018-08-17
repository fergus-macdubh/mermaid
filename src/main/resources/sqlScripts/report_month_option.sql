CREATE TABLE report_month_option
(
  year  INTEGER      NOT NULL,
  month INTEGER      NOT NULL,
  name  VARCHAR(255) NOT NULL,
  value VARCHAR(255),
  CONSTRAINT report_month_option_pkey
  PRIMARY KEY (year, month, name)
);