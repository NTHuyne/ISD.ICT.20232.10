package com.hust.ict.aims.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hust.ict.aims.entity.media.Book;
import com.hust.ict.aims.entity.media.CdAndLp;
import com.hust.ict.aims.entity.media.Dvd;
import com.hust.ict.aims.entity.media.Media;
import com.hust.ict.aims.entity.order.Order;
import com.hust.ict.aims.entity.order.OrderMedia;
import com.hust.ict.aims.entity.order.OrderMedia.OrderType;
import com.hust.ict.aims.entity.order.RushOrder;
import com.hust.ict.aims.entity.shipping.DeliveryInfo;

public class AllDAOData {
	private static Book initBook() {
		Media trueMedia = new Media(
			1,
        	"Sample Book",
        	200000,
        	100,
        	1.0,
        	true,
        	"sample_book.jpg",
        	"12345",
        	"This is a sample book",
        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
        	"12x6x9"
		);
					
		return new Book(
			trueMedia,
        	"Author Name",
        	"Hardcover",
        	"Publisher Name",
        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
        	300,
        	"English",
        	"Fiction"
        );
	}
	
	private static CdAndLp initCD() {
		Media trueMedia = new Media(
        	2,
        	"Sample CD",
        	150000,
        	50,
        	0.25,
        	false,
        	"sample_cd.jpg",
        	"56789",
        	"This is a sample CD",
        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
        	"15x6x9"
		);
	
		return new CdAndLp(
			trueMedia,
        	"None of Them",
        	"Record Label",
        	"Track 1, Track 2, Track 3",
        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
        	"Pop",
        	true
        );
	}
	
	private static Dvd initDVD() {
		Media trueMedia = new Media(
			3,
        	"Sample DVD",
        	250000,
        	75,
        	0.25,
        	true,
        	"sample_dvd.jpg",
        	"98465",
        	"This is a sample DVD",
        	java.sql.Date.valueOf(LocalDate.parse("2022-01-01")),
        	"17x6x9"
		);
	
		return new Dvd(
			trueMedia,
        	"Movie",
        	"Steven Spielberg",
        	120,
        	"Universal Pictures",
        	"English",
        	"English",
        	java.sql.Date.valueOf(LocalDate.parse("1993-06-11")),
        	"Adventure"
        );
	}
	
	private static DeliveryInfo initDelivery() {
		return new DeliveryInfo(
			1,
			"John Doe",
			"0123456789",
			"john@example.com",
			"Hanoi",
			"123 ABC Street",
			"Do not drop it"
		);
	}
	
	private static List<OrderMedia> initOrderMedias1() {
		List<OrderMedia> mediaList = new ArrayList<>();
		mediaList.add(new OrderMedia(existingBook,	3, OrderType.NORMAL));
		mediaList.add(new OrderMedia(existingCd,	2, OrderType.RUSH));
		mediaList.add(new OrderMedia(existingDvd,	7, OrderType.NORMAL));
		
		return mediaList;
	}

	
	private static RushOrder initRushOrder() {
		Order existingOrder = new Order(
			1,
			100,
			500,
			Order.OrderStatus.PENDING,
			initDelivery(),
			initOrderMedias1()
		);
		
		System.out.println(existingOrder.getLstOrderMedia());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault());

		return new RushOrder(
			existingOrder,
			ZonedDateTime.parse("2022-12-31 23:59:59", formatter).toInstant(),
			"Handle with care"
		);
				
	}
	
	private static Book existingBook;
	private static CdAndLp existingCd;
	private static Dvd existingDvd;

	private static Order existingNormalOrder;
	private static RushOrder existingRushOrder;
	
	private static Map<Integer, Media> allMediaMap;

	
	static {
		existingBook = initBook();
		existingCd = initCD();
		existingDvd = initDVD();
		
		existingRushOrder = initRushOrder();
		
		allMediaMap = new HashMap<>();
	    allMediaMap.put(existingBook.getMediaId(), existingBook);
	    allMediaMap.put(existingCd.getMediaId(), existingCd);
	    allMediaMap.put(existingDvd.getMediaId(), existingDvd);
	}
	
	

	public static Book getExistingBook() {
		return existingBook;
	}

	public static CdAndLp getExistingCd() {
		return existingCd;
	}

	public static Dvd getExistingDvd() {
		return existingDvd;
	}

	public static Order getExistingNormalOrder() {
		return existingNormalOrder;
	}

	public static RushOrder getExistingRushOrder() {
		return existingRushOrder;
	}



	public static Map<Integer, Media> getAllMediaMap() {
		return allMediaMap;
	}
	
	
}
