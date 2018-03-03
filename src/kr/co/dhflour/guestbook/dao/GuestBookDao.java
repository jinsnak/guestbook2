package kr.co.dhflour.guestbook.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import kr.co.dhflour.guestbook.vo.GuestBookVo;

public class GuestBookDao {
	
	public List<GuestBookVo> fetchList(){
		List<GuestBookVo> list = new ArrayList<GuestBookVo>();
		
		Connection conn = null; //지역변수는 반드시 초기화해주어야 한다.(=null)
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			conn = getConnection();
			
			stmt = conn.createStatement();
			String sql = "select no, name, to_char(reg_date, 'yyyy-mm-dd hh:mi:ss') as reg_date, contents from guestbook order by no desc";
			rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				//가져와서 변수에 담는다.
				long no = rs.getLong(1);
				String name = rs.getString(2);
				String regDate = rs.getString(3);
				String contents = rs.getString(4);
				//VO 객체를 만들어서 변수 값을 넣는다.
				GuestBookVo vo = new GuestBookVo();
				vo.setNo(no);
				vo.setName(name);
				vo.setRegDate(regDate);
				vo.setContents(contents);
				//리스트에 담는다.
				list.add(vo);
			}
			
		} catch (SQLException e) {
			System.out.println("연결실패 -_-+" + e);
			//e.printStackTrace();
		} finally {
			try {
				//conn, pstmt이 null인 상태에서 들어온 경우 Error발생을 막기위해.
				if(rs != null) {
					rs.close();
				}
				if(stmt != null) {
					stmt.close();
				}
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return list;
	}
	
	public boolean insertVo(GuestBookVo vo) {
		boolean result = false;
		
		Connection conn = null; //지역변수는 반드시 초기화해주어야 한다.(=null)
		PreparedStatement pstmt = null;
		
		try {
			
			conn = getConnection();
			
			//3. Statenemt 준비
			String sql = "insert into guestbook values(SEQ_GUESTBOOK.NEXTVAL, ?, ?, sysdate, ?)";
			pstmt = conn.prepareStatement(sql);
			
			//4. 값 바인딩
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getContents());
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();
			
			//6. 성공유무
			if (count == 1) {
				result = true;
			} else {
				result = false;
			}
			
		} catch (SQLException e) {
			System.out.println("연결실패 -_-+" + e);
			//e.printStackTrace();
		} finally {
			//6. 자원정리
			try {
				
				if (pstmt != null) {
					pstmt.close();
				}
				//conn이 null인 상태에서 들어온 경우 Error발생을 막기위해.
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	public boolean deleteVo(GuestBookVo vo) {
		boolean result = false;
		
		Connection conn = null; //지역변수는 반드시 초기화해주어야 한다.(=null)
		PreparedStatement pstmt = null;
		
		try {
			
			conn = getConnection();
			
			//3. Statenemt 준비
			String sql = "delete guestbook where no = ? and password = ?";
			pstmt = conn.prepareStatement(sql);
			
			//4. 값 바인딩
			pstmt.setLong(1, vo.getNo());
			pstmt.setString(2, vo.getPassword());
			
			//5. SQL문 실행
			int count = pstmt.executeUpdate();
			
			//6. 성공유무
			if (count == 1) {
				result = true;
			} else {
				result = false;
			}
			
		} catch (SQLException e) {
			System.out.println("연결실패 -_-+" + e);
			//e.printStackTrace();
		} finally {
			//6. 자원정리
			try {
				
				if (pstmt != null) {
					pstmt.close();
				}
				//conn이 null인 상태에서 들어온 경우 Error발생을 막기위해.
				if(conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	
	//jdbc드라이버 로딩 및 Connection 객체생성(효율적인 코딩 방식(리팩토링)
	private Connection getConnection() {
		
		Connection conn = null;
		
		try {
			//1. JDBC 드라이버 로딩
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			//2. Connection 가져오기
			String url = "jdbc:oracle:thin:@localhost:1521:xe";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 로딩 실패 -_-");
			//e.printStackTrace();
		} catch (SQLException e) {
			System.out.println("연결실패 -_-+" + e);
			//e.printStackTrace();
		}
		return conn;
		
	}
}
