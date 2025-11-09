-- Clean up database before each test
DELETE FROM todo;
ALTER TABLE todo ALTER COLUMN id RESTART WITH 1;
