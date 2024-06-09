package com.hust.ict.aims.persistence.dao.order;

/**
 * @author
 */
public class OrderDAO {

//    public List<Media> getOrder(int id) throws SQLException {
//        Connection conn = null;
//        try {
//            String sql = "SELECT Media.title, Order_Media.Quantity\n" +
//                    "FROM Order_Media\n" +
//                    "LEFT JOIN Media\n" +
//                    "ON Order_Media.media_id = Media.media_id\n" +
//                    "where orderId =" + id + ";";
//
//            conn = ConnectJDBC.getConnection();
//            Statement stmt = conn.createStatement();
//            // Get data
//            ResultSet rs = stmt.executeQuery(sql);
//
//            int i = 1;
//            int sum = 0;
//            List<Media> medium = new ArrayList<>();
//            while (rs.next()) {
//                Media media = new Media(i,
//                        rs.getString("title"),
//                        rs.getInt("price"),
//                        rs.getString("category"),
//                        rs.getInt("quantity"));
//                i++;
//                medium.add(media);
//            }
//            return medium;
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
