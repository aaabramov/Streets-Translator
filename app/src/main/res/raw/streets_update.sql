DROP TABLE IF EXISTS streets;

CREATE TABLE streets (
  _id INTEGER PRIMARY KEY AUTOINCREMENT,
  old_name TEXT,
  old_name_cmp TEXT,
  new_name TEXT,
  new_name_cmp TEXT,
  description TEXT
);