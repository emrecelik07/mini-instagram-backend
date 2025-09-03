-- Test Data Script for Mini Instagram Backend
-- Run this after your application starts to populate test data

-- Insert test users
INSERT INTO user_model (user_id, name, username, email, password, is_verified, profile_image_url, bio) VALUES
('user1', 'John Doe', 'john123', 'john@test.com', '$2a$10$encrypted_password_hash', true, 'https://example.com/john.jpg', 'Photography enthusiast'),
('user2', 'Jane Smith', 'jane456', 'jane@test.com', '$2a$10$encrypted_password_hash', true, 'https://example.com/jane.jpg', 'Coffee lover'),
('user3', 'Bob Wilson', 'bob789', 'bob@test.com', '$2a$10$encrypted_password_hash', true, 'https://example.com/bob.jpg', 'Travel blogger'),
('user4', 'Alice Johnson', 'alice101', 'alice@test.com', '$2a$10$encrypted_password_hash', true, 'https://example.com/alice.jpg', 'Foodie');

-- Insert test posts
INSERT INTO post_model (post_id, user_id, caption, image_url, is_private, like_count, comment_count, share_count, view_count, save_count, repost_count, created_at, updated_at) VALUES
('post1', 'user2', 'Beautiful sunset at the beach! 🌅', 'https://example.com/sunset1.jpg', false, 15, 3, 2, 120, 8, 1, NOW() - INTERVAL '2 HOUR', NOW() - INTERVAL '2 HOUR'),
('post2', 'user3', 'Coffee time! ☕', 'https://example.com/coffee1.jpg', false, 8, 1, 0, 45, 3, 0, NOW() - INTERVAL '1 HOUR', NOW() - INTERVAL '1 HOUR'),
('post3', 'user4', 'New recipe: Pasta Carbonara 🍝', 'https://example.com/food1.jpg', false, 25, 7, 5, 200, 15, 2, NOW() - INTERVAL '30 MINUTE', NOW() - INTERVAL '30 MINUTE'),
('post4', 'user2', 'Morning walk in the park 🌳', 'https://example.com/park1.jpg', false, 12, 2, 1, 80, 5, 0, NOW() - INTERVAL '15 MINUTE', NOW() - INTERVAL '15 MINUTE');

-- Insert follow relationships
INSERT INTO follow_model (follower_id, following_id, created_at) VALUES
('user1', 'user2', NOW() - INTERVAL '1 DAY'),    -- John follows Jane
('user1', 'user3', NOW() - INTERVAL '1 DAY'),    -- John follows Bob
('user2', 'user1', NOW() - INTERVAL '12 HOUR'),  -- Jane follows John
('user3', 'user1', NOW() - INTERVAL '6 HOUR'),   -- Bob follows John
('user4', 'user2', NOW() - INTERVAL '3 HOUR');   -- Alice follows Jane

-- Note: You'll need to adjust the user_id references based on your actual database structure
-- The post_id and user_id should reference the actual IDs from your user_model table

