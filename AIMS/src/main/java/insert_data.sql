INSERT INTO User (username, password, email, isAdmin)
VALUES ('admin', '1234', 'test.admin@gmail.com', 1);

INSERT INTO User (username, password, email, isAdmin)
VALUES ('user', '1234', 'test.user@gmail.com', 0);

-- Insert data into Media table
INSERT INTO Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES (200000, 'Sample Book', 100, 1, 'This is a sample book', '2022-01-01', 1, 'sample_book.jpg', '12x6x9', '12345');

INSERT INTO Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES (150000, 'Sample CD', 50, 0.25, 'This is a sample CD', '2022-01-01', 0, 'sample_cd.jpg', '15x6x9', '56789');

INSERT INTO Media (price, title, totalQuantity, weight, description, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES (250000, 'Sample DVD', 75, 0.25, 'This is a sample DVD', '2022-01-01', 1, 'sample_dvd.jpg', '17x6x9', '98465');



-- Insert data into Book table
INSERT INTO Book (media_id, authors, coverType, publisher, publicationDate, pages, language, genre)
VALUES (1, 'Author Name', 'Hardcover', 'Publisher Name', '2022-01-01', 300, 'English', 'Fiction');

-- Insert data into CD_and_LP table
INSERT INTO CD_and_LP (media_id, isCD, artists, recordLabel, trackList, genre, releaseDate)
VALUES (2, true, 'Artist Name', 'Record Label', 'Track 1, Track 2, Track 3', 'Pop', '2022-01-01');

-- Insert data into DVD table
INSERT INTO DVD (media_id, dvdType, director, runtime, studio, language, subtitles, releasedDate, genre)
VALUES (3, 'Movie', 'Steven Spielberg', 120, 'Universal Pictures', 'English', 'English', '1993-06-11', 'Adventure');


-- Insert data into DeliveryInfo table
INSERT INTO DeliveryInfo (name, phone, email, province, address, message) 
VALUES ('John Doe', '123456789', 'john@example.com', 'Hanoi', '123 ABC Street', 'Do not drop it');

-- Insert data into OrderInfo table
INSERT INTO OrderInfo (shippingFees, subtotal, status, delivery_id) VALUES (100, 500, 1, 1);

-- Insert data into RushOrderInfo table
INSERT INTO RushOrderInfo (deliveryTime, instruction, order_id) VALUES ('2022-12-31 23:59:59', 'Handle with care', 1);


-- Insert data into Order_Media table
INSERT INTO Order_Media (order_id, media_id, quantity) VALUES (1, 2, 2);