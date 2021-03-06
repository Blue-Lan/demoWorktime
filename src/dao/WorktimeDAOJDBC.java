package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import model.NoSubmitWorktime;
import model.SubmissionHistory;
import model.WorktimeDetail;

public class WorktimeDAOJDBC implements WorktimeDAO{
	Connection conn = null;
	ResultSet rs = null;
	Statement stmt = null;
	PreparedStatement pstmt = null;
	
	private void close() {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (stmt != null)
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (pstmt != null)
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
		if (conn != null)
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO 自動產生的 catch 區塊
				e.printStackTrace(System.err);
			}
	}
	
	@Override
	public List<SubmissionHistory> getWorktime(String empno) {
		// TODO Auto-generated method stub
		List<SubmissionHistory> worktimeList = new ArrayList<SubmissionHistory>();
		
		Calendar c = Calendar.getInstance();   // this takes current date
	    c.set(Calendar.DAY_OF_MONTH, 1);
	    SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd");
	    String firstDayOfMonth = sdfDate.format(c.getTime());
	    
	    SimpleDateFormat sdfDateEnd = new SimpleDateFormat("YYYY-MM");
		String endDate = sdfDateEnd.format(c.getTime());
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select * from submission_history where empno = ?");
			sql.append("and week_first_day >= TO_DATE(?,'YYYY-MM-DD')");
			sql.append("and week_first_day < ADD_MONTHS(TO_DATE(?,'YYYY-MM'),1)" );
			sql.append("order by week_first_day");
			
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,empno);
			pstmt.setString(2,firstDayOfMonth);
			pstmt.setString(3,endDate);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				worktimeList.add(createWorktime(rs));
			}
				
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return worktimeList;
	}

	private SubmissionHistory createWorktime(ResultSet rs) {
		// TODO Auto-generated method stub
		SubmissionHistory worktime = new SubmissionHistory();
		try {
			worktime.setEmpNo(rs.getString("EMPNO"));
			worktime.setWeekFirstDay(rs.getDate("WEEK_FIRST_DAY"));
			worktime.setStatus(rs.getString("STATUS"));
			worktime.setId(rs.getInt("id"));
			return worktime;
		} catch (SQLException e) {
			// TODO 自動產生的 catch 區塊
			throw new RuntimeException("資料庫錯誤. " + e.getMessage());
		}
	}

	@Override
	public void insertWorktime(String empno) {
		// TODO Auto-generated method stub
		//取得第一個星期日
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1); 
		while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
		{
			cal.add(Calendar.DATE, 1);
		}
		
		SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd");
		int currentMonth =cal.get(Calendar.MONTH)+1;
		int nextMonth = cal.get(Calendar.MONTH)+1;
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO SUBMISSION_HISTORY (EMPNO, WEEK_FIRST_DAY, STATUS, NOTE)");
			sql.append("VALUES (?, TO_DATE(?,'YYYY-MM-DD'), '未繳交', null)");
			
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,empno);
			
			while(currentMonth == nextMonth) {
			    String firstDayOfMonth = sdfDate.format(cal.getTime());
				pstmt.setString(2, firstDayOfMonth);
				rs = pstmt.executeQuery();
				cal.add(Calendar.DAY_OF_MONTH, 7);
				nextMonth = cal.get(Calendar.MONTH)+1;
				System.out.println("test");
			}
				
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		
	}

	@Override
	public List<Integer> getHours(List<SubmissionHistory> worktimeList) {
		// TODO Auto-generated method stub
		List<Integer> hours = new ArrayList<>();
		SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd");
		Calendar calBegin = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		calBegin.set(Calendar.DATE, 1); 
		while (calBegin.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
		{
			calBegin.add(Calendar.DATE, 1);
		}
		
		calEnd.set(Calendar.DATE, calEnd.getActualMaximum(Calendar.DATE));
    	while (calEnd.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY)
		{
    		calEnd.add(Calendar.DATE, 1);
		}
    	calEnd.add(Calendar.DATE, 1);
    	String endDate = sdfDate.format(calEnd.getTime());
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select sum(hours) from working_records where empno = ?");
			sql.append("and working_date = TO_DATE(?,'YYYY-MM-DD')");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,worktimeList.get(0).getEmpNo());
			while(!calBegin.getTime().equals(calEnd.getTime())){
				
				String beginDate = sdfDate.format(calBegin.getTime());
				
				pstmt.setString(2,beginDate);
				
				rs = pstmt.executeQuery();
				
				if(rs.next()) {
					hours.add(rs.getInt(1));
				}
				calBegin.add(Calendar.DATE, 1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return hours;
	}

	@Override
	public List<SubmissionHistory> getWorktime(String empno, String searchMonth) {
		// TODO Auto-generated method stub
		List<SubmissionHistory> worktimeList = new ArrayList<SubmissionHistory>();
	    
		try {
			
			StringBuilder sql = new StringBuilder();
			sql.append("select * from submission_history ");
			sql.append("where week_first_day >= TO_DATE(?,'YYYY-MM') ");
			sql.append("and week_first_day < ADD_MONTHS(TO_DATE(?,'YYYY-MM'),1) ");
			if(empno != null) {
				sql.append("and empno = ? ");
			}
			sql.append("order by week_first_day");
			
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			pstmt.setString(1,searchMonth);
			pstmt.setString(2,searchMonth);
			if(empno != null) {
				pstmt.setString(3,empno);
			}
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				worktimeList.add(createWorktime(rs));
			}	
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return worktimeList;
	}

	@Override
	public boolean checkCurrentMonth(String empno) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdfDate = new SimpleDateFormat("YYYY-MM-dd");
		Calendar calBegin = Calendar.getInstance();
		calBegin.set(Calendar.DATE, 1); 
		while (calBegin.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)
		{
			calBegin.add(Calendar.DATE, 1);
		}
		String beginDate = sdfDate.format(calBegin.getTime());
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT * FROM SUBMISSION_HISTORY ");
			sql.append("WHERE EMPNO= ? ");
			sql.append("AND WEEK_FIRST_DAY = TO_DATE(?,'YYYY-MM-DD') ");
			
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,empno);
			pstmt.setString(2,beginDate);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return true;
	}

	@Override
	public List<SubmissionHistory> mgrSearchWorktime(String nameOrEmpno, String inputSearch,String inputMonth) {
		// TODO Auto-generated method stub
		List<SubmissionHistory> worktimeList = new ArrayList<SubmissionHistory>();
		
		
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select employee.empno,  employee.name, submission_history.* ");
			sql.append("from submission_history ");
			sql.append("inner join employee on submission_history.empno = employee.empno ");
			if(nameOrEmpno.equals("name")) {
				sql.append("where employee.name = ?");
			}else {
				sql.append("where employee.empno = ?");
			}
			if(!inputMonth.isEmpty()){
				sql.append("and week_first_day >= TO_DATE(?,'YYYY-MM') ");
				sql.append("and week_first_day < ADD_MONTHS(TO_DATE(?,'YYYY-MM'),1) ");
			}
			sql.append("order by week_first_day");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,inputSearch);
			if(!inputMonth.isEmpty()){
				pstmt.setString(2,inputMonth);
				pstmt.setString(3,inputMonth);
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				worktimeList.add(createWorktime(rs));
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return worktimeList;	
	}

	@Override
	public List<NoSubmitWorktime> getNoSubmitWorktime() {
		// TODO Auto-generated method stub
		List<NoSubmitWorktime> noSubmitWorktimeList = new ArrayList<NoSubmitWorktime>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    	String deadline = sdf.format(c.getTime());
    	System.out.println(deadline);
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT submission_history.id, employee.name, submission_history.empno, submission_history.week_first_day, count(urge_history.id) urge_times ");
			sql.append("FROM submission_history ");
			sql.append("left join employee on submission_history.empno = employee.empno ");
			sql.append("left join urge_history on submission_history.id = urge_history.submission_id ");
			sql.append("where submission_history.status = '未繳交' ");
			sql.append("and submission_history.week_first_day < TO_DATE(? ,'YYYY-MM-DD') ");
			sql.append("group by submission_history.id, submission_history.status, submission_history.empno, employee.name, submission_history.week_first_day ");
			sql.append("order by submission_history.week_first_day");
			
			System.out.println("SUCCESS");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,deadline);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				NoSubmitWorktime noSubmitWorktime = new NoSubmitWorktime();
				noSubmitWorktime.setWeekFirstdate(rs.getDate("WEEK_FIRST_DAY"));
				noSubmitWorktime.setEmpno(rs.getString("EMPNO"));
				noSubmitWorktime.setName(rs.getString("NAME"));
				noSubmitWorktime.setUrgeTimes(rs.getInt("URGE_TIMES"));
				System.out.println(rs.getInt("URGE_TIMES"));
				System.out.println("C");
				noSubmitWorktimeList.add(noSubmitWorktime);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return noSubmitWorktimeList;	
	}

	@Override
	public List<NoSubmitWorktime> getNewstUrgeDate() {
		// TODO Auto-generated method stub
		List<NoSubmitWorktime> noSubmitWorktimeList = new ArrayList<NoSubmitWorktime>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    	Calendar c = Calendar.getInstance();
    	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
    	String deadline = sdf.format(c.getTime());
    	System.out.println(deadline);
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT submission_history.id, employee.name, submission_history.empno, submission_history.week_first_day, max(urge_history.urge_date) news_urge_date,employee.email ");
			sql.append("FROM submission_history ");
			sql.append("left join employee on submission_history.empno = employee.empno ");
			sql.append("left join urge_history on submission_history.id = urge_history.submission_id ");
			sql.append("where submission_history.status = '未繳交'  ");
			sql.append("and submission_history.week_first_day < TO_DATE(?,'YYYY-MM-DD') ");
			sql.append("group by submission_history.id, submission_history.status, submission_history.empno, employee.name, submission_history.week_first_day,employee.email ");
			sql.append("order by submission_history.week_first_day");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			pstmt.setString(1,deadline);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				NoSubmitWorktime noSubmitWorktime = new NoSubmitWorktime();
				noSubmitWorktime.setWeekFirstdate(rs.getDate("WEEK_FIRST_DAY"));
				noSubmitWorktime.setEmpno(rs.getString("EMPNO"));
				noSubmitWorktime.setName(rs.getString("NAME"));
				noSubmitWorktime.setUrgeDate(rs.getDate("news_urge_date"));
				noSubmitWorktime.setId(rs.getInt("ID"));
				noSubmitWorktime.setEmail(rs.getString("EMAIL"));
				noSubmitWorktimeList.add(noSubmitWorktime);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally {
			close();
		}
		return noSubmitWorktimeList;
	}

	@Override
	public void urgeEmployee(List<NoSubmitWorktime> noSubmotWorktimeList) {
		// TODO Auto-generated method stub
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("insert into urge_history(urge_date, submission_id ) ");
			sql.append("values(sysdate,?)");
			
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());

			System.out.println("noSubmitWorktimeList size:"+ noSubmotWorktimeList.size());
			for(int i = 0;i<noSubmotWorktimeList.size();i++) {
				pstmt.setInt(1, noSubmotWorktimeList.get(i).getId());
				pstmt.executeUpdate();
				System.out.println("WHAT");
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}

	@Override
	public void checkPass(String submssionId) {
		// TODO Auto-generated method stub
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE SUBMISSION_HISTORY ");
			sql.append("SET STATUS = '已通過' ");
			sql.append("WHERE ID = ? ");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, submssionId);
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}

	@Override
	public void checNokPass(String submssionId, String noPassReason) {
		// TODO Auto-generated method stub
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE SUBMISSION_HISTORY ");
			sql.append("SET STATUS = '未通過'  , NOTE = ? ");
			sql.append("WHERE ID = ? ");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, noPassReason);
			pstmt.setString(2, submssionId);
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
	}

	@Override
	public int getNoPassAndNoSubmit(String empno, String month) {
		// TODO Auto-generated method stub
		System.out.println(empno);
		System.out.println(month);
		
		int days = 0;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("select count(status) ");
			sql.append("from submission_history ");
			sql.append("where status in ('未繳交','未通過') ");
			sql.append("and empno = ?  ");
			sql.append("and week_first_day < TO_DATE( ? ,'YYYY-MM') ");
			conn = ConnectionHelper.getConnection();
			pstmt = conn.prepareStatement(sql.toString());
			
			pstmt.setString(1, empno);
			pstmt.setString(2, month);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				days = rs.getInt(1);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		System.out.println(days);
		return days;
	}

}
