CREATE TABLE IF NOT EXISTS calculation_history (
 id SERIAL PRIMARY KEY,
 timestamp TIMESTAMP NOT NULL,
 endpoint VARCHAR(255),
 number1 NUMERIC,
 number2 NUMERIC,
 percentage_used NUMERIC,
 response TEXT
);