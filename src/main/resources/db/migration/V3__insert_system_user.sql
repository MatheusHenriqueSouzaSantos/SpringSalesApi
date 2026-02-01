INSERT INTO app_user(
                     id,
                     created_at,
                     updated_at,
                     created_by_id,
                     updated_by_id,
                     active,
                     full_name,
                     email,
                     password_hash,
                     user_role)
    VALUES (
            '00000000-0000-0000-0000-000000000001',
            now(),
            null,
            '00000000-0000-0000-0000-000000000001',
            null,
            true,
            'system',
            'system@app.local',
            'system',
            'ADMIN'
);