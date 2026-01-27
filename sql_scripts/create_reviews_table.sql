-- Tạo bảng reviews nếu chưa tồn tại
IF NOT EXISTS (SELECT * FROM sys.tables WHERE name = 'reviews')
BEGIN
    CREATE TABLE reviews (
        id INT IDENTITY(1,1) PRIMARY KEY,
        user_id INT NOT NULL,
        product_id INT NOT NULL,
        rate INT NOT NULL CHECK (rate >= 1 AND rate <= 5),
        comment NVARCHAR(MAX),
        created_at DATETIME DEFAULT GETDATE(),
        FOREIGN KEY (user_id) REFERENCES users(id),
        FOREIGN KEY (product_id) REFERENCES products(id),
        UNIQUE(user_id, product_id)
    );
    
    CREATE INDEX idx_reviews_product ON reviews(product_id);
    CREATE INDEX idx_reviews_user ON reviews(user_id);
END;
