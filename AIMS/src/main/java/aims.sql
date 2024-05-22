CREATE DATABASE AIMS;
USE AIMS;


-- Table: User
CREATE TABLE User(
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    username				VARCHAR(50)		NOT NULL,
    password				VARCHAR(50)		NOT NULL,
    isAdmin					BOOLEAN			NOT NULL,
    PRIMARY KEY (id)
);

-- Table: Media
CREATE TABLE Media (
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    category				VARCHAR(50) 	NOT NULL,
    price 					INTEGER			NOT NULL,
    value 					INTEGER			NOT NULL,
    title 					VARCHAR(50)		NOT NULL,
    description 			VARCHAR(255)	NOT NULL,
    quantity           		INTEGER      	NOT NULL,
    importDate         		DATE,
    rushOrderSupported 		BOOLEAN,
    barcode					VARCHAR(50)		NOT NULL,
    productDimension		VARCHAR(50),
    imageUrl           		VARCHAR(200),
    PRIMARY KEY (id)
);

-- Table: Book
CREATE TABLE Book (
	id 						INTEGER 		NOT NULL,
    authors					VARCHAR(50) 	NOT NULL,
    hardCover 				VARCHAR(50) 	NOT NULL,
    publisher 				VARCHAR(50) 	NOT NULL,
    publicationDate			DATE 			NOT NULL,
    pages					INTEGER,
    language				VARCHAR(50),
    bookCategory			VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES Media (id) ON DELETE CASCADE
);

-- Table: Cd and LP
CREATE TABLE CD_and_LP (
	id 						INTEGER 		NOT NULL,
    artists					VARCHAR(50) 	NOT NULL,
    recordLabel 			VARCHAR(50) 	NOT NULL,
    trackList				VARCHAR(200)	NOT NULL,
    releaseDate				DATE,
    musicType				VARCHAR(50)		NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES Media (id) 	ON DELETE CASCADE
);

-- Table: DVD
CREATE TABLE DVD (
	id 						INTEGER 		NOT NULL,
    dvdType					VARCHAR(50)		NOT NULL,
    director				VARCHAR(50) 	NOT NULL,
    runtime		 			INTEGER		 	NOT NULL,
    studio					VARCHAR(50)		NOT NULL,
    language				VARCHAR(50) 	NOT NULL,
    subtitles				VARCHAR(50) 	NOT NULL,
    releasedDate			DATE,
    filmType				VARCHAR(50),
    PRIMARY KEY (id),
    FOREIGN KEY (id) REFERENCES Media (id) 	ON DELETE CASCADE
);

-- Table: RushOrderInfo
CREATE TABLE RushOrderInfo (
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
	deliveryTime			DATETIME		NOT NULL,
    instructions			VARCHAR(200)	NOT NULL,
	PRIMARY KEY (id)
);

-- Table: DeliveryInfo
CREATE TABLE DeliveryInfo (
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    name 					VARCHAR(50)		NOT NULL,
    phone					VARCHAR(15)		NOT NULL,
    email					VARCHAR(50)		NOT NULL,
    province				VARCHAR(50)		NOT NULL,
    district				VARCHAR(50)		NOT NULL,
    address					VARCHAR(200)	NOT NULL,
    rushOrderID				INTEGER,
    message					VARCHAR(200),
    PRIMARY KEY (id),
    FOREIGN KEY (rushOrderID) REFERENCES RushOrderInfo (id)
);

-- Table: OrderInfo
CREATE TABLE OrderInfo(
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
	shippingFees			INTEGER			NOT NULL,
    subtotal				INTEGER			NOT NULL,
    deliveryInfoId			INTEGER			NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (deliveryInfoId) REFERENCES DeliveryInfo (id)
);

-- Table: Order_Media
CREATE TABLE Order_Media(
	orderID				INTEGER 		NOT NULL,
    mediaID				INTEGER			NOT NULL,
    quantity			INTEGER			NOT NULL,
    price				INTEGER			NOT NULL,
	FOREIGN KEY (orderID) REFERENCES OrderInfo (id),
    FOREIGN KEY (mediaID) REFERENCES Media (id)
);

-- Table: Invoice
CREATE TABLE Invoice(
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    totalAmount				INTEGER			NOT NULL,
    orderId					INTEGER 		NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (orderId) REFERENCES OrderInfo (id)
);

-- Table: VnPay
CREATE TABLE VnPAY(
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    imageQRUrl				VARCHAR(50)		NOT NULL,
    paymentAccountName		VARCHAR(50)		NOT NULL,
    paymentAccountNumber	VARCHAR(50)		NOT NULL,
    bank					VARCHAR(50)		NOT NULL,
	PRIMARY KEY (id)
);

-- Table: PaymentTransaction
CREATE TABLE PaymentTransaction(
	id 						INTEGER 		NOT NULL AUTO_INCREMENT,
    createAt				DATETIME		NOT NULL,
    paymentTime				DATETIME		NOT NULL,
    content					VARCHAR(50)		NOT NULL,
    vnPayId					INTEGER			NOT NULL,
    method					VARCHAR(50)		NOT NULL,
    status					VARCHAR(50)		NOT NULL,
    invoiceId				INTEGER			NOT NULL,
    PRIMARY KEY (id),
	FOREIGN KEY (vnPayId) REFERENCES vnPay (id),
	FOREIGN KEY (invoiceId) REFERENCES Invoice (id)
);


-- Index: orderId_index
CREATE INDEX orderId_index ON Invoice (orderId);

-- Index: deliveryInfoId_index
CREATE INDEX deliveryInfoId_index ON OrderInfo (deliveryInfoId);

-- Index: vnPayId_index
CREATE INDEX vnPayId_index ON PaymentTransaction (vnPayId);

-- Index: invoiceId_index
CREATE INDEX invoiceId_index ON PaymentTransaction (invoiceId);