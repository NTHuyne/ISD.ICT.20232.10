CREATE DATABASE AIMS;
USE AIMS;

-- Table: User
CREATE TABLE User(
	user_id 				INTEGER 		NOT NULL AUTO_INCREMENT,
	username				VARCHAR(50)		NOT NULL,
	password				VARCHAR(50)		NOT NULL,
	email					VARCHAR(50)		NOT NULL,
	isAdmin					BOOLEAN			NOT NULL,
	PRIMARY KEY (user_id)
);

-- Table: Media
CREATE TABLE Media (
	media_id				INTEGER			NOT NULL AUTO_INCREMENT,
	title					VARCHAR(50)		NOT NULL,
	price					INTEGER			NOT NULL,
	totalQuantity			INTEGER			NOT NULL,
	weight					REAL			NOT NULL,
	rushOrderSupported		BOOLEAN,
	imageUrl				VARCHAR(200),

	barcode					VARCHAR(50)		NOT NULL,
	description				VARCHAR(255)	NOT NULL,
	productDimension		VARCHAR(50),
	importDate				DATE,

	PRIMARY KEY (media_id)
);

-- Table: Book
CREATE TABLE Book (
	media_id				INTEGER			NOT NULL,
	authors					VARCHAR(50) 	NOT NULL,
	coverType				VARCHAR(50) 	NOT NULL,
	publisher 				VARCHAR(50) 	NOT NULL,
	publicationDate			DATE 			NOT NULL,

	pages					INTEGER,
	language				VARCHAR(50),
	genre					VARCHAR(50),
	PRIMARY KEY (media_id),
	FOREIGN KEY (media_id) REFERENCES Media (media_id) ON DELETE CASCADE
);

-- Table: Cd and LP
CREATE TABLE CD_and_LP (
	media_id				INTEGER			NOT NULL,
    isCD					BOOLEAN			NOT NULL,
	artists					VARCHAR(50) 	NOT NULL,
	recordLabel 			VARCHAR(50) 	NOT NULL,
	trackList				VARCHAR(200)	NOT NULL,
	genre					VARCHAR(50)		NOT NULL,

	releaseDate				DATE,
	PRIMARY KEY (media_id),
	FOREIGN KEY (media_id) REFERENCES Media (media_id) ON DELETE CASCADE
);

-- Table: DVD
CREATE TABLE DVD (
	media_id				INTEGER			NOT NULL,
	dvdType					VARCHAR(50)		NOT NULL,
	director				VARCHAR(50) 	NOT NULL,
	runtime		 			INTEGER		 	NOT NULL,
	studio					VARCHAR(50)		NOT NULL,
	language				VARCHAR(50) 	NOT NULL,
	subtitles				VARCHAR(50) 	NOT NULL,

	releasedDate			DATE,
	genre					VARCHAR(50),
	PRIMARY KEY (media_id),
	FOREIGN KEY (media_id) REFERENCES Media (media_id) ON DELETE CASCADE
);



-- Table: DeliveryInfo
CREATE TABLE DeliveryInfo (
	delivery_id				INTEGER 		NOT NULL AUTO_INCREMENT,
	name 					VARCHAR(50)		NOT NULL,
	phone					VARCHAR(15)		NOT NULL,
	email					VARCHAR(50)		NOT NULL,
	province				VARCHAR(50)		NOT NULL,
	address					VARCHAR(200)	NOT NULL,
	message					VARCHAR(200),
	PRIMARY KEY (delivery_id)
);

-- Table: OrderInfo
CREATE TABLE OrderInfo(
	order_id 				INTEGER 		NOT NULL AUTO_INCREMENT,
	shippingFees			INTEGER			NOT NULL,
	subtotal				INTEGER			NOT NULL,
	status					VARCHAR(20)		NOT NULL,	-- 1 pending, 2 success?

	delivery_id				INTEGER			NOT NULL,
	PRIMARY KEY (order_id),
	FOREIGN KEY (delivery_id) REFERENCES DeliveryInfo(delivery_id) ON DELETE CASCADE
);

-- Table: Order_Media
CREATE TABLE Order_Media (
	media_id				INTEGER			NOT NULL,
	order_id				INTEGER			NOT NULL,
	quantity				INTEGER			NOT NULL,
    orderType				INTEGER			NOT NULL,
	PRIMARY KEY (media_id, order_id),
	FOREIGN KEY (media_id) REFERENCES Media(media_id) ON DELETE CASCADE,
	FOREIGN KEY (order_id) REFERENCES OrderInfo(order_id) ON DELETE CASCADE
);



-- Table: RushOrderInfo
CREATE TABLE RushOrderInfo (
	rush_id 				INTEGER 		NOT NULL AUTO_INCREMENT,
	deliveryTime			TIMESTAMP		NOT NULL,
	instruction				VARCHAR(200)	NOT NULL,
	order_id				INTEGER			NOT NULL,
	PRIMARY KEY (rush_id),
	FOREIGN KEY (order_id) REFERENCES OrderInfo(order_id) ON DELETE CASCADE
);

-- Table: PaymentTransaction
CREATE TABLE PaymentTransaction(
	transaction_id 			INTEGER 		NOT NULL AUTO_INCREMENT,
	paymentTime				TIMESTAMP		NOT NULL,
	paymentAmount			INTEGER			NOT NULL,
	content					VARCHAR(50)		NOT NULL,

	bankTransactionId		VARCHAR(50)		NOT NULL,
	cardType				VARCHAR(50)		NOT NULL,
	PRIMARY KEY (transaction_id)
);

-- Table: Invoice
CREATE TABLE Invoice(
	invoice_id 				INTEGER 		NOT NULL AUTO_INCREMENT,
	totalAmount				INTEGER			NOT NULL,
	transaction_id			INTEGER 		NOT NULL,
	order_id				INTEGER			NOT NULL,
	PRIMARY KEY (invoice_id),
	FOREIGN KEY (transaction_id) REFERENCES PaymentTransaction(transaction_id),		-- Should not be deleted
	FOREIGN KEY (order_id) REFERENCES OrderInfo(order_id) ON DELETE CASCADE
	
);


-- Index: delivery_id_index
CREATE INDEX delivery_id_index ON OrderInfo (delivery_id);

-- Index: transaction_id_index, order_id_index
CREATE INDEX invoice_transaction_id_index ON Invoice (transaction_id);
CREATE INDEX invoice_order_id_index ON Invoice (order_id);

-- Index: order_id_index
CREATE INDEX rush_order_id_index ON RushOrderInfo (order_id);