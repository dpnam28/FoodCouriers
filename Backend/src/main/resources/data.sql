INSERT INTO locations (city) VALUES
                                ('Hà Nội'),
                                ('Hồ Chí Minh');
INSERT INTO categories (name)
VALUES
    ('Gỏi - Salad'),
    ('Món thịt gà'),
    ('Món thịt bò'),
    ('Món thịt heo'),
    ('Món hải sản');

INSERT INTO users (location_id, address, email, full_name, password, phone_number, role)
VALUES
    (1, '123 Nguyen Trai', 'rs1@gmail.com', 'Nhà Hàng Hương Việt', '$2a$10$RTrJm/YeBygqf7e7zCR2Pe5Z/MYzIvl0YewqG0RHgaIL5ntzaGNl6', '0901111111', 'ROLE_RESTAURANT'),
    (1, '45 Le Loi', 'rs2@gmail.com', 'Nhà Hàng Biển Xanh', '$2a$10$RTrJm/YeBygqf7e7zCR2Pe5Z/MYzIvl0YewqG0RHgaIL5ntzaGNl6', '0902222222', 'ROLE_RESTAURANT'),
    (1, '89 Vo Van Tan', 'rs3@gmail.com', 'Nhà Hàng Phố Cổ', '$2a$10$RTrJm/YeBygqf7e7zCR2Pe5Z/MYzIvl0YewqG0RHgaIL5ntzaGNl6', '0903333333', 'ROLE_RESTAURANT');

INSERT INTO restaurants (restaurant_id, banner_image, description)
VALUES
    (1, 'https://res.cloudinary.com/dkuoubq6x/image/upload/v1765913862/evclzjw5pkyfc56598ax.jpg', 'Ẩm thực Việt truyền thống'),
    (2, 'https://images.unsplash.com/photo-1552566626-52f8b828add9', 'Hải sản tươi sống cao cấp'),
    (3, 'https://images.unsplash.com/photo-1528605248644-14dd04022da1', 'Không gian cổ điển, món ngon ba miền');

INSERT INTO foods (name, price, category_id, restaurant_id, description, image)
VALUES
('Gỏi gà', 55000, 1, 1, 'Gỏi gà trộn rau thơm', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Gỏi bò tái chanh', 60000, 1, 1, 'Gỏi bò tái chanh thơm ngon', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b'),
('Gỏi cuốn tôm', 50000, 1, 1, 'Gỏi cuốn tôm với rau sống', 'https://images.unsplash.com/photo-1559561853-d9c489cbab5b'),
('Gỏi xoài tôm', 54000, 1, 1, 'Gỏi xoài xanh với tôm', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Gỏi rau củ', 48000, 1, 1, 'Gỏi rau củ tổng hợp', 'https://images.unsplash.com/photo-1592861954380-049087d83a55'),

('Cơm gà xối mỡ', 85000, 2, 1, 'Cơm gà xối mỡ vàng giòn', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),
('Gà nướng mật ong', 80000, 2, 1, 'Gà nướng mật ong thơm béo', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Gà rán giòn', 78000, 2, 1, 'Gà rán giòn thơm', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Gà xào sả ớt', 82000, 2, 1, 'Gà xào sả ớt cay nhẹ', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Phở gà', 87000, 2, 1, 'Phở gà thanh ngọt', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),

-- Món thịt bò
('Bò lúc lắc', 95000, 3, 1, 'Bò lúc lắc xào hành', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Bò né', 90000, 3, 1, 'Bò né nóng hổi ăn kèm trứng', 'https://images.unsplash.com/photo-1553621042-f6e147245754'),
('Bò kho', 92000, 3, 1, 'Bò kho mềm hầm gia vị', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Bò nướng lá lốt', 97000, 3, 1, 'Bò nướng lá lốt thơm lừng', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Phở bò', 91000, 3, 1, 'Phở bò truyền thống', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),

('Sườn heo nướng', 88000, 4, 1, 'Sườn heo nướng BBQ', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Thịt heo kho tàu', 83000, 4, 1, 'Thịt heo kho tàu đậm đà', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),
('Nem nướng', 86000, 4, 1, 'Nem nướng đặc sản', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Heo quay', 84000, 4, 1, 'Heo quay da giòn', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Bún thịt heo', 89000, 4, 1, 'Bún thịt heo thơm ngon', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Cá kho tộ', 98000, 5, 1, 'Cá kho tộ đậm đà', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Tôm chiên trứng muối', 102000, 5, 1, 'Tôm chiên trứng muối béo', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Mực chiên giòn', 96000, 5, 1, 'Mực chiên giòn thơm', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b'),
('Lẩu hải sản', 100000, 5, 1, 'Lẩu hải sản tươi ngon', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Cá nướng muối ớt', 99000, 5, 1, 'Cá nướng muối ớt cay nồng', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Gỏi đu đủ bò', 53000, 1, 2, 'Gỏi đu đủ trộn bò tái', 'https://images.unsplash.com/photo-1592861954380-049087d83a55'),
('Gỏi tai heo', 58000, 1, 2, 'Gỏi tai heo giòn thơm', 'https://images.unsplash.com/photo-1559561853-d9c489cbab5b'),
('Gỏi tôm thịt', 56000, 1, 2, 'Gỏi tôm thịt chua ngọt', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Gỏi rau củ', 50000, 1, 2, 'Gỏi rau củ tổng hợp', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Gỏi xoài tôm', 54000, 1, 2, 'Gỏi xoài xanh chua cay', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b'),

('Gà xào sả ớt', 87000, 2, 2, 'Gà xào sả ớt cay nhẹ', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),
('Gà rán giòn', 82000, 2, 2, 'Gà rán giòn thơm', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Gà nướng sa tế', 90000, 2, 2, 'Gà nướng sa tế đậm vị', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Gà hầm nước dừa', 86000, 2, 2, 'Gà hầm nước dừa béo thơm', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Phở gà nấm', 89000, 2, 2, 'Phở gà với nấm tươi', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),

('Bò tái chanh', 92000, 3, 2, 'Bò tái chanh cay nhẹ', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Bò xào rau củ', 94000, 3, 2, 'Bò xào rau củ tổng hợp', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Bò nướng mật ong', 96000, 3, 2, 'Bò nướng mật ong thơm', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Bò sốt tiêu', 93000, 3, 2, 'Bò sốt tiêu đậm đà', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Bò xào sả ớt', 95000, 3, 2, 'Bò xào sả ớt cay', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),

('Heo nướng BBQ', 86000, 4, 2, 'Heo nướng BBQ thơm', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Nem nướng đặc biệt', 88000, 4, 2, 'Nem nướng đặc biệt nhà hàng', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Heo quay giòn da', 84000, 4, 2, 'Heo quay da giòn rụm', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Bánh mì heo quay', 89000, 4, 2, 'Bánh mì heo quay thơm ngon', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Bún thịt heo', 87000, 4, 2, 'Bún thịt heo đậm vị', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Cá lóc nướng trui', 102000, 5, 2, 'Cá lóc nướng trui đặc sản', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Tôm nướng muối ớt', 104000, 5, 2, 'Tôm nướng muối ớt thơm', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b'),
('Lẩu cá kèo', 100000, 5, 2, 'Lẩu cá kèo cay ngọt', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Hải sản tổng hợp', 103000, 5, 2, 'Đĩa hải sản tổng hợp hấp dẫn', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Súp hải sản', 101000, 5, 2, 'Súp hải sản đậm đà', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Gỏi tôm bắp cải', 56000, 1, 3, 'Gỏi tôm bắp cải tươi ngon', 'https://images.unsplash.com/photo-1594944430639-20a3c855f92a'),
('Gỏi cuốn', 50000, 1, 3, 'Gỏi cuốn tôm thịt với rau sống', 'https://images.unsplash.com/photo-1559561853-d9c489cbab5b'),
('Gỏi xoài khô bò', 58000, 1, 3, 'Gỏi xoài với bò khô chua cay', 'https://images.unsplash.com/photo-1592861954380-049087d83a55'),
('Gỏi rau củ', 52000, 1, 3, 'Gỏi rau củ tổng hợp', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd'),
('Gỏi sứa', 60000, 1, 3, 'Gỏi sứa thanh mát', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),

('Cà ri gà', 85000, 2, 3, 'Cà ri gà nấu nước cốt dừa', 'https://images.unsplash.com/photo-1606201407638-29f60d02db0a'),
('Gà nướng lá chanh', 83000, 2, 3, 'Gà nướng lá chanh thơm lừng', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Gà xé phay trộn rau', 80000, 2, 3, 'Gà xé phay trộn rau thơm', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),
('Phở gà truyền thống', 90000, 2, 3, 'Phở gà nước trong thơm ngon', 'https://images.unsplash.com/photo-1562967916-eb82221dfb17'),
('Gà chiên giòn', 82000, 2, 3, 'Gà chiên giòn vàng rụm', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Bún bò Huế', 92000, 3, 3, 'Bún bò Huế cay đậm vị', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Bò lúc lắc', 95000, 3, 3, 'Bò lúc lắc mềm ngon', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Phở bò tái', 91000, 3, 3, 'Phở bò tái thơm ngọt', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b'),
('Bò nướng lá lốt', 94000, 3, 3, 'Bò nướng lá lốt đậm đà', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Bò xào sả ớt', 93000, 3, 3, 'Bò xào sả ớt cay nhẹ', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),

('Bún chả', 88000, 4, 3, 'Bún chả Hà Nội với nước mắm chua ngọt', 'https://images.unsplash.com/photo-1588196749597-9ff075ee6b5b'),
('Thịt kho trứng', 85000, 4, 3, 'Thịt kho trứng đậm đà', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),
('Heo quay', 87000, 4, 3, 'Heo quay da giòn', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Nem nướng', 86000, 4, 3, 'Nem nướng thơm đặc sản', 'https://images.unsplash.com/photo-1604754742628-8f5f6123ddc8'),
('Bánh mì thịt heo', 89000, 4, 3, 'Bánh mì thịt heo hấp dẫn', 'https://images.unsplash.com/photo-1551183053-bf91a1d81141'),

('Bánh xèo hải sản', 98000, 5, 3, 'Bánh xèo nhân hải sản', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Chả cá Lã Vọng', 102000, 5, 3, 'Chả cá Lã Vọng đặc sản Hà Nội', 'https://images.unsplash.com/photo-1589302168068-964664d93dc0'),
('Cá nướng sả ớt', 96000, 5, 3, 'Cá nướng sả ớt thơm cay', 'https://images.unsplash.com/photo-1552566626-52f8b828add9'),
('Lẩu hải sản', 100000, 5, 3, 'Lẩu hải sản tươi ngon', 'https://images.unsplash.com/photo-1586190848861-99aa4a171e90'),
('Mực xào sa tế', 99000, 5, 3, 'Mực xào sa tế cay nồng', 'https://images.unsplash.com/photo-1552332386-f8dd00dc2f7b');