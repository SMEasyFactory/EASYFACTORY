package com.feb.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

import com.feb.vo.FebVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

@Repository
public class FebDAO {
	
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public FebDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
/*
    public List<FebVO> getTableData(Date startDate, Date endDate) {
        // 여기에 쿼리를 작성하고, 데이터베이스에서 데이터를 가져오는 작업을 수행
        String SQL = "SELECT * FROM + your_table_name WHERE hiredate BETWEEN ? AND ?;";

        List<FebVO> resultList = jdbcTemplate.query(SQL, 
                                                        new Object[]{startDate, endDate}, 
                                                        new RowMapper<FebVO>() {
            @Override
            public FebVO mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new FebVO(
                    rs.getDouble("opratio"),
                    rs.getInt("temp"),
                    rs.getInt("tr"),
                    rs.getInt("fal"),
                    rs.getInt("stock"),
                    rs.getInt("costs"),
                    rs.getDouble("usingratio"),
                    rs.getDate("hiredate")
                );
            }
        });

        return resultList;
    }
*/    
    // Feb mapper
 	private static class FebMapper implements RowMapper<FebVO> {
         @Override
         public FebVO mapRow(ResultSet rs, int rowNum) throws SQLException {
        	 FebVO febVO = new FebVO();
        	 febVO.setOpratio(rs.getDouble("opratio"));
        	 febVO.setTemp(rs.getInt("temp"));
        	 febVO.setTr(rs.getInt("tr"));
        	 febVO.setFal(rs.getInt("fal"));
        	 febVO.setStock(rs.getInt("stock"));
        	 febVO.setCosts(rs.getInt("costs"));
        	 febVO.setUsingratio(rs.getDouble("usingratio"));
        	 febVO.setHiredate(rs.getDate("hiredate"));
             return febVO;
         }
    }
 	
    
// SelectDAO    
    // 특정 테이블에서 데이터를 가져오는 메서드.
    public List<FebVO> getTableData(String tableName) {
    	String SQL = "SELECT * FROM " + tableName;
    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
		return resultList;
    }
// SelectDAO
    // Chart에 테이블 데이터를 불러오는 메소드.
	    public List<FebVO> getChartData1() {
	    	String SQL = "SELECT * FROM feb1" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData2() {
	    	String SQL = "SELECT * FROM feb2" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData3() {
	    	String SQL = "SELECT * FROM feb3" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData4() {
	    	String SQL = "SELECT * FROM feb4" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData5() {
	    	String SQL = "SELECT * FROM feb5" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData6() {
	    	String SQL = "SELECT * FROM feb6" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData7() {
	    	String SQL = "SELECT * FROM feb7" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
	    public List<FebVO> getChartData8() {
	    	String SQL = "SELECT * FROM feb8" ;
	    	List<FebVO> resultList = jdbcTemplate.query(SQL, new FebMapper());
			return resultList;
	    }
    
    
// Author : kj9303
 	// Feb 데이터 가져오기
 	public List<FebVO> getDatePickerData(Date startDate, Date endDate) {
 		String SQL = "SELECT * FROM + your_table_name WHERE hiredate BETWEEN ? AND ?";
 		List<FebVO> datepickerList = jdbcTemplate.query(SQL, new Object[]{startDate, endDate}, new FebMapper());
 		return datepickerList;
 	}
// Author : kj9303
 	
// insertDAO
 	public void insertTable(String tableName) {
        Random random = new Random();

        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        for (int i = 0; i < 365; i++) {
            double opratio = Math.round((70 + random.nextDouble() * (100 - 70)) * 100.0) / 100.0;
            int temp = random.nextInt(15) + 1;
            int tr = random.nextInt(10000) + 1;
            int fal = random.nextInt(100) + 1;
            int stock = random.nextInt(1000) + 1;
            int costs = random.nextInt(1000) + 1;
            double usingratio = Math.round(random.nextDouble() * 100 * 100.0) / 100.0;

            temp = Math.max(0, Math.min(15, temp));

            LocalDateTime currentDateTime = dateTime.plusDays(i);
            java.sql.Date currentDate = java.sql.Date.valueOf(currentDateTime.toLocalDate());

            try {
                String SQL = "INSERT INTO " + tableName + " (opratio, temp, tr, fal, stock, costs, usingratio, hiredate) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                int result = jdbcTemplate.update(SQL, opratio, temp, tr, fal, stock, costs, usingratio, currentDate);
                
                System.out.println("[" + tableName.toUpperCase() + " 테이블의 INSERT 작업이 완료되었습니다.]");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(">>> 날짜 " + currentDate + " INSERT 작업 중 예외가 발생하였습니다: " + e.getMessage());
            }
        }
    }
 	
    public void updateTable(String tableName) {
        Random random = new Random();

        // 2023년 1월 1일부터 365일간의 데이터를 생성.
        LocalDateTime dateTime = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        for (int i = 0; i < 365; i++) {

            // 랜덤한 값을 생성.
            double opratio = Math.round((70 + random.nextDouble() * (100 - 70)) * 100.0) / 100.0;
            int temp = random.nextInt(15) + 1;
            int tr = random.nextInt(10000) + 1;
            int fal = random.nextInt(100) + 1;
            int stock = random.nextInt(1000) + 1;
            int costs = random.nextInt(1000) + 1;
            double usingratio = Math.round(random.nextDouble() * 100 * 100.0) / 100.0;

            // 온도 값이 0 미만일 경우 0으로, 15 초과할 경우 15로 맞춰줌.
            temp = Math.max(0, Math.min(15, temp));

            LocalDateTime currentDateTime = dateTime.plusDays(i);
            java.sql.Date currentDate = java.sql.Date.valueOf(currentDateTime.toLocalDate());

            try {
                String SQL = "UPDATE " + tableName + " SET opratio = ?, temp = ?, tr = ?, fal = ?, stock = ?, costs = ?, usingratio = ? WHERE hiredate = ?";
                int result = jdbcTemplate.update(SQL, opratio, temp, tr, fal, stock, costs, usingratio, currentDate);

                System.out.println("[" + tableName.toUpperCase() + " 테이블의 업데이트 작업이 완료되었습니다.]");

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(">>> 날짜 " + currentDate + " 업데이트 작업 중 예외가 발생하였습니다: " + e.getMessage());
            }
        }
    }
// insertDAO    
 
// 임시 (추후 삭제예정)
// MonthDataDao
    public void printMonthlyAvgOperationalRate() {
        String sql = "SELECT EXTRACT(MONTH FROM hiredate) AS \"월\", AVG(opratio) AS \"가동률 평균\" FROM feb1 GROUP BY EXTRACT(MONTH FROM hiredate) ORDER BY EXTRACT(MONTH FROM hiredate)";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");

        for (Map<String, Object> row : resultList) {
        	BigDecimal avgOpratio = (BigDecimal) row.get("가동률 평균");
            System.out.println("월: " + row.get("월") + ", 가동률 평균: " + decimalFormat.format(avgOpratio));
        }
    }

    public void printYearlyAvgTemperature() {
        String sql = "SELECT TO_CHAR(hiredate, 'YYYY') AS \"당해\", AVG(TEMP) AS \"온도 평균\" FROM feb1 GROUP BY TO_CHAR(hiredate, 'YYYY')";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        
        for (Map<String, Object> row : resultList) {
            BigDecimal avgTemp = (BigDecimal) row.get("온도 평균");
            System.out.println("당해: " + row.get("당해") + ", 온도 평균: " + decimalFormat.format(avgTemp));
        }
    }

    public void printMonthlyElectricityUsage() {
        String sql = "SELECT EXTRACT(MONTH FROM hiredate) AS \"월\", SUM(usingratio) AS \"월별 전기사용량\" FROM feb1 GROUP BY EXTRACT(MONTH FROM HIREDATE) ORDER BY EXTRACT(MONTH FROM HIREDATE)";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> row : resultList) {
            System.out.println("월: " + row.get("월") + ", 월별 전기사용량: " + row.get("월별 전기사용량"));
        }
    }

    public void printMonthlyCost() {
        String sql = "SELECT EXTRACT(MONTH FROM hiredate) AS \"월\", SUM(costs) AS \"월별 비용\" FROM feb1 GROUP BY EXTRACT(MONTH FROM HIREDATE) ORDER BY EXTRACT(MONTH FROM HIREDATE)";
        List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> row : resultList) {
            System.out.println("월: " + row.get("월") + ", 월별 비용: " + row.get("월별 비용"));
        }
    }
// MonthDataDao
}
