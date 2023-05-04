CREATE INDEX IF NOT EXISTS idx_substance_index ON substance (index) INCLUDE (name, ec_no, cas_no, creation_date, update_date);
CREATE INDEX IF NOT EXISTS idx_hazard_name_code ON hazard (name, code) INCLUDE (creation_date, update_date);
CREATE INDEX IF NOT EXISTS idx_hazardsubstance ON substance_hazard (substance_index, hazard_id) INCLUDE (creation_date, update_date);