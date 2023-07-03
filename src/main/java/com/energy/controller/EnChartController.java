package com.energy.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.energy.dao.EnergyDao;
import com.energy.vo.EnergyVO;
import com.energy.service.EnergyService;

@Controller
public class EnChartController {
    private EnergyService energyService;

    public EnChartController(EnergyDao energyDao) {
        this.energyService = new EnergyService(energyDao);
    }

    // 날짜 삽입
    @PostMapping("/chart3")
    public void doChart3(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[ChartController] /chart3");

        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter writer = response.getWriter();
        
        // 웹에서 찍은 날짜 확인
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        System.out.printf("Parameter: startDate(%s), endDate(%s)\n", startDate, endDate);
        
        // DB에서 찾은 데이터 jsonarray 형태로 변환후 차트로 전송
        List<EnergyVO> energyDataList = energyService.getSelectDay(startDate, endDate);
        JSONArray jsonArray = energyService.JSONChange(energyDataList);
        writer.print(jsonArray.toJSONString());
    }
}
      /*
        startDate와 endDate를 Date 타입으로 변환
        Date start = Date.valueOf(startDate);
        Date end = Date.valueOf(endDate);
        // 데이터베이스로부터 정보를 검색
        List<EnergyData> energyDataList = energyDao.getEnergyDataBetweenDates(startDate, endDate);
        // 결과를 JSON으로 변환
        JSONObject json = new JSONObject();
        for (EnergyData energyData : energyDataList) {
        	((List) json).add(energyData.getOpratio());
            jsonArray.add(energyData.getCosts());
            jsonArray.add(energyData.getUsingratio());
        JSONArray jsonArray = new JSONArray();
        for (EnergyData energyData : energyDataList) {
            jsonArray.add(energyData.getOpratio());
            jsonArray.add(energyData.getCosts());
            jsonArray.add(energyData.getUsingratio());
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("startDate", energyData.getStartDate().toString());
            jsonObj.put("endDate", energyData.getEndDate().toString());
            jsonObj.put("opratio", energyData.getOpratio());
            jsonObj.put("costs", energyData.getCosts());
            jsonObj.put("usingratio", energyData.getUsingratio());
            jsonArray.add(jsonObj);
        }
        */
        // JSON 결과를 응답에 출력
       // writer.print(jsonArray.toJSONString());
       // writer.print(json.toJSONString());
    
    
