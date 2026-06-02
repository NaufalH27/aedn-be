CREATE TABLE website_profile (
    id UUID PRIMARY KEY,
    photo_profile TEXT,
    personal_description TEXT,
    whatsapp_url VARCHAR(30),
    instagram_url VARCHAR(100),
    facebook_url VARCHAR(255),
    vgen_url VARCHAR(100),
    twitter_url VARCHAR(100),
    discord_url VARCHAR(100),
    email VARCHAR(255)
);

INSERT INTO website_profile (
    id,
    photo_profile,
    personal_description,
    whatsapp_url,
    instagram_url,
    facebook_url,
    vgen_url,
    discord_url,
    twitter_url,
    email
)
VALUES (
    gen_random_uuid(),
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL,
    NULL
);
