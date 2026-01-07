INSERT INTO app_user(
                     id,
                     created_at,
                     updated_at,
                     created_by_id,
                     updated_by_id,
                     is_active,
                     full_name,
                     email,
                     password_hash)
    VALUES (
            '00000000-0000-0000-0000-000000000001',
            now(),
            null,
            '00000000-0000-0000-0000-000000000001',
            null,
            true,
            'system',
            'system@app.local',
            'system'
);