package com.energy.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.energy.vo.EnergyVO;

public class EnergyDao1 {
    private JdbcTemplate jdbcTemplate;

    public EnergyDao1(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    
    @RequestMapping(value = "/energy", method = RequestMethod.GET)
    public String energy() {
        return "energyData";
    }
  
    public List<EnergyVO> getOpratio(String startDate, String endDate) {
        String sql = "SELECT ROUND(AVG(avg_opratio), 2) AS average_opratio "
                + "FROM FEB_DSUM "
                + "WHERE hiredate BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";

        List<EnergyVO> list = jdbcTemplate.query(sql, new Object[]{startDate, endDate}, new ResultSetExtractor<List<EnergyVO>>() {
            @Override
            public List<EnergyVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<EnergyVO> list = new ArrayList<EnergyVO>();
                while (rs.next()) {
                    EnergyVO energyData = new EnergyVO();
                    energyData.setOpratio(rs.getDouble("average_opratio"));
                    list.add(energyData);
                }
                return list;
            }
        });

        return list;
    }
    
    public List<EnergyVO> getTemp(String startDate, String endDate) {
        String sql = "SELECT ROUND(AVG(AVG_TEMP), 0) AS AVG_TEMP "
                + "FROM FEB_DSUM "
                + "WHERE hiredate BETWEEN TO_DATE(?, 'YYYY-MM-DD') AND TO_DATE(?, 'YYYY-MM-DD')";

        List<EnergyVO> list = jdbcTemplate.query(sql, new Object[]{startDate, endDate}, new ResultSetExtractor<List<EnergyVO>>() {
            @Override
            public List<EnergyVO> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<EnergyVO> list = new ArrayList<EnergyVO>();
                while (rs.next()) {
                    EnergyVO energyData = new EnergyVO();
                    energyData.setTemp(rs.getInt("avg_temp"));
                    list.add(energyData);
                }
                return list;
            }
        });

        return list;
    }
}

       