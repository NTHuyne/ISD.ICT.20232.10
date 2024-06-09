package com.hust.ict.aims.controller;

import com.hust.ict.aims.persistence.dao.media.MediaDAO;
import com.hust.ict.aims.entity.media.Media;

import java.sql.SQLException;
import java.util.List;

/**
 * This class controls the flow of events in homescreen
 * @author
 */
public class HomeController extends BaseController {


    /**
     * this method gets all Media in DB and return back to home to display
     * @return List[Media]
     * @throws SQLException
     */
    public static List<Media> getAllMedia() throws SQLException{
        return new MediaDAO().getAllMedia();
    }
}