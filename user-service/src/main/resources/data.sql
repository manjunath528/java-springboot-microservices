-- Create table
CREATE TABLE IF NOT EXISTS user_table (
                                          id UUID PRIMARY KEY,
                                          name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    address VARCHAR(255) NOT NULL,
    weight DOUBLE PRECISION,
    height DOUBLE PRECISION,
    gender VARCHAR(10),
    date_of_birth DATE NOT NULL,
    fitness_goal VARCHAR(20),
    daily_step_goal INT,
    sleep_goal_hours DOUBLE PRECISION,
    notifications_enabled BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    is_active BOOLEAN DEFAULT TRUE
    );

-- Insert 10 test users with manually assigned UUIDs
INSERT INTO user_table (id, name, email, address, weight, height, gender, date_of_birth, fitness_goal, daily_step_goal, sleep_goal_hours, notifications_enabled)
VALUES
    ('a1e4567e-89b0-12d3-a456-426614174000', 'John Doe', 'john.doe@example.com', '123 Main St', 75.0, 1.78, 'MALE', '1985-06-15', 'LOSE_WEIGHT', 10000, 7.5, TRUE),
    ('b2e4567e-89b0-12d3-a456-426614174001', 'Jane Smith', 'jane.smith@example.com', '456 Elm St', 65.0, 1.65, 'FEMALE', '1990-09-23', 'GAIN_WEIGHT', 8000, 8.0, TRUE),
    ('c3e4567e-89b0-12d3-a456-426614174002', 'Alice Johnson', 'alice.johnson@example.com', '789 Oak St', 70.0, 1.70, 'FEMALE', '1978-03-12', 'MUSCLE_BUILD', 12000, 6.5, TRUE),
    ('d4e4567e-89b0-12d3-a456-426614174003', 'Bob Brown', 'bob.brown@example.com', '321 Pine St', 82.0, 1.75, 'MALE', '1982-11-30', 'LOSE_WEIGHT', 9000, 7.0, TRUE),
    ('e5e4567e-89b0-12d3-a456-426614174004', 'Emily Davis', 'emily.davis@example.com', '654 Maple St', 55.0, 1.60, 'FEMALE', '1995-02-05', 'GAIN_WEIGHT', 7000, 8.0, TRUE),
    ('f6e4567e-89b0-12d3-a456-426614174005', 'Michael Green', 'michael.green@example.com', '987 Cedar St', 88.0, 1.82, 'MALE', '1988-07-25', 'MUSCLE_BUILD', 11000, 7.0, TRUE),
    ('a7e4567e-89b0-12d3-a456-426614174006', 'Sarah Taylor', 'sarah.taylor@example.com', '123 Birch St', 60.0, 1.63, 'FEMALE', '1992-04-18', 'LOSE_WEIGHT', 9000, 7.5, TRUE),
    ('b8e4567e-89b0-12d3-a456-426614174007', 'David Wilson', 'david.wilson@example.com', '456 Ash St', 95.0, 1.90, 'MALE', '1975-01-11', 'GAIN_WEIGHT', 8000, 6.5, TRUE),
    ('c9e4567e-89b0-12d3-a456-426614174008', 'Laura White', 'laura.white@example.com', '789 Palm St', 68.0, 1.68, 'FEMALE', '1989-09-02', 'MUSCLE_BUILD', 10000, 7.0, TRUE),
    ('d0e4567e-89b0-12d3-a456-426614174009', 'James Harris', 'james.harris@example.com', '321 Cherry St', 77.0, 1.76, 'MALE', '1993-11-15', 'LOSE_WEIGHT', 9500, 7.5, TRUE)
    ON CONFLICT (email) DO NOTHING;

