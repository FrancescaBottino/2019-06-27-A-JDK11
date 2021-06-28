package it.polito.tdp.crimes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.crimes.model.Adiacenza;
import it.polito.tdp.crimes.model.Event;


public class EventsDao {
	
	public List<Event> listAllEvents(){
		String sql = "SELECT * FROM events" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Event> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Event(res.getLong("incident_id"),
							res.getInt("offense_code"),
							res.getInt("offense_code_extension"), 
							res.getString("offense_type_id"), 
							res.getString("offense_category_id"),
							res.getTimestamp("reported_date").toLocalDateTime(),
							res.getString("incident_address"),
							res.getDouble("geo_lon"),
							res.getDouble("geo_lat"),
							res.getInt("district_id"),
							res.getInt("precinct_id"), 
							res.getString("neighborhood_id"),
							res.getInt("is_crime"),
							res.getInt("is_traffic")));
				} catch (Throwable t) {
					t.printStackTrace();
					System.out.println(res.getInt("id"));
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getAllCategorie(){
		
		String sql = "SELECT DISTINCT offense_category_id FROM events ";
		
		List<String> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					result.add(res.getString("offense_category_id"));
					
					
				} catch (Throwable t) {
					t.printStackTrace();
					
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
		
		
	}
	
	public List<Integer> getAllAnni(){
		
		String sql = "SELECT DISTINCT Year(reported_date) as anno "
				+ "FROM events ";
		
		List<Integer> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					result.add(res.getInt("anno"));
					
					
				} catch (Throwable t) {
					t.printStackTrace();
					
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
		
	}
	
	public List<String> getVertici(Integer anno, String categoria){
		
		String sql = "SELECT DISTINCT offense_type_id as type "
				+ "FROM events "
				+ "WHERE offense_category_id = ? AND Year(reported_date) = ? ";
		
			List<String> result = new ArrayList<>() ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					result.add(res.getString("type"));
					
					
				} catch (Throwable t) {
					t.printStackTrace();
					
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
			
		
		
		
	}
	
	
	public List<Adiacenza> getAdiacenze(Integer anno, String categoria){
		
		String sql = "SELECT DISTINCT e1.offense_type_id as t1, e2.offense_type_id as t2, count(distinct e1.district_id) as peso "
				+ "FROM events e1,events e2 "
				+ "WHERE e1.district_id = e2.district_id "
				+ "AND e1.offense_type_id > e2.offense_type_id "
				+ "AND e1.offense_category_id = e2.offense_category_id "
				+ "AND e1.offense_category_id = ? "
				+ "AND Year(e1.reported_date) = Year(e2.reported_date) "
				+ "AND Year(e1.reported_date) = ? "
				+ "GROUP BY e1.offense_type_id, e2.offense_type_id ";
		
		
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setString(1, categoria);
			st.setInt(2, anno);
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					result.add(new Adiacenza(res.getString("t1"), res.getString("t2"), res.getInt("peso")));
					
					
					
					
				} catch (Throwable t) {
					t.printStackTrace();
					
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
			
		
	}

}
