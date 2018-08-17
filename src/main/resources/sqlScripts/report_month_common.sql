CREATE TABLE report_month_common
(
  year               INTEGER NOT NULL,
  month              INTEGER NOT NULL,
  total_expenses     DOUBLE PRECISION,
  electricity        DOUBLE PRECISION,
  enterprenuer_tax   DOUBLE PRECISION,
  sum_salary         DOUBLE PRECISION,
  sum_bonuses        DOUBLE PRECISION,
  building_rent      DOUBLE PRECISION,
  line_amortization  DOUBLE PRECISION,
  sum_price_consumes DOUBLE PRECISION,
  other_expenses     DOUBLE PRECISION,
  sum_area           DOUBLE PRECISION,
  total_income       DOUBLE PRECISION,
  other_consumes     DOUBLE PRECISION,
  sum_price          DOUBLE PRECISION,
  closed             TIMESTAMP,
  done_by_id         BIGINT
    CONSTRAINT report_month_common_done_by_id_fkey
    REFERENCES user_details,
  CONSTRAINT report_month_common_pkey
  PRIMARY KEY (year, month)
);