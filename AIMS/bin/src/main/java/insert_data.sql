INSERT INTO User (username, password, isAdmin)
VALUES ('admin', '1234', 1);

INSERT INTO User (username, password, isAdmin)
VALUES ('user', '1234', 0);

-- Insert data into Media table
INSERT INTO Media (category, price, value, title, description, quantity, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES ('Book', 200000, 30, 'Sample Book', 'This is a sample book', 100, '2022-01-01', 1, 'sample_book.jpg', '12x6x9', '12345');

INSERT INTO Media (category, price, value, title, description, quantity, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES ('CD', 150000, 25, 'Sample CD', 'This is a sample CD', 50, '2022-01-01', 0, 'sample_cd.jpg', '15x6x9', '56789');

INSERT INTO Media (category, price, value, title, description, quantity, importDate, rushOrderSupported, imageUrl, productDimension, barcode)
VALUES ('DVD', 250000, 35, 'Sample DVD', 'This is a sample DVD', 75, '2022-01-01', 1, 'sample_dvd.jpg', '17x6x9', '98465');

-- Insert data into Book table
INSERT INTO Book (id, authors, hardCover, publisher, publicationDate, pages, language, bookCategory)
VALUES (1, 'Author Name', 'Hardcover', 'Publisher Name', '2022-01-01', 300, 'English', 'Fiction');

-- Insert data into CD_and_LP table
INSERT INTO CD_and_LP (id, artists, recordLabel, trackList, releaseDate, musicType)
VALUES (2, 'Artist Name', 'Record Label', 'Track 1, Track 2, Track 3', '2022-01-01', 'Pop');

-- Insert data into DVD table
INSERT INTO DVD (id, dvdType, director, runtime, studio, language, subtitles, releasedDate, filmType)
VALUES (3, 'Movie', 'Steven Spielberg', 120, 'Universal Pictures', 'English', 'English', '1993-06-11', 'Adventure');

-- Insert data into RushOrderInfo table
INSERT INTO RushOrderInfo (deliveryTime, instructions) VALUES ('2022-12-31 23:59:59', 'Handle with care');

-- Insert data into DeliveryInfo table
INSERT INTO DeliveryInfo (name, phone, email, province, district, address, rushOrderID) VALUES ('John Doe', '123456789', 'john@example.com', 'Hanoi', 'Cau Giay', '123 ABC Street', 1);

-- Insert data into OrderInfo table
INSERT INTO OrderInfo (shippingFees, subtotal, deliveryInfoId) VALUES (100, 500, 1);

-- Insert data into Order_Media table
INSERT INTO Order_Media (orderID, quantity, price, MediaId) VALUES (1, 2, 100, 1);