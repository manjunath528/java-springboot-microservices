CREATE TABLE IF NOT EXISTS activity (
                          id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                          user_id UUID NOT NULL,
                          workout_type VARCHAR(50) NOT NULL,
                          duration_minutes INT,
                          calories_burned INT,
                          activity_date DATE NOT NULL,
                          status VARCHAR(30) NOT NULL,
                          created_at TIMESTAMP DEFAULT now(),
                          updated_at TIMESTAMP DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_activity_user_date
    ON activity (user_id, activity_date);
