package com.issue.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.issue.service.IssueService;
import com.issue.vo.Criteria;
import com.issue.vo.IssueVO;
import com.issue.vo.PageMaker;

@Controller
@RequestMapping("/issue")
public class IssueController {

	@Autowired
    private IssueService issueService;
    
    public void setIssueService(IssueService issueService) {
		this.issueService = issueService;
	}
    
    // 글 목록
    @RequestMapping(value="/list", method=RequestMethod.GET)
    public String getissueList(@ModelAttribute("cri") Criteria cri, Model model) throws Exception {
    	int totCnt = issueService.issueListCnt();
   	 	model.addAttribute("totCnt", totCnt);
    	List<IssueVO> issueList = issueService.issueList(cri);
    	model.addAttribute("issueList", issueList);
    	 
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	 
    	pageMaker.setTotalCount(totCnt);
    	model.addAttribute("pageMaker", pageMaker);
		
    	return "issue/list";
    }
    
    // 글 검색
    @RequestMapping(value="/list/search", method=RequestMethod.GET)
    public String search(HttpServletRequest request, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {
    	
    	String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
    	String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
    	String startDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
    	String endDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
    	String page = request.getParameter("page") == null ? "" : request.getParameter("page").toString();
    	String perPageNum = request.getParameter("perPageNum") == null ? "" : request.getParameter("perPageNum").toString();
    	
    	int totCnt = issueService.issueSearchCnt(searchType, keyword, startDate, endDate);
   	 	model.addAttribute("totCnt", totCnt);
   	 	
    	List<IssueVO> searchIsList = issueService.search(searchType, keyword, startDate, endDate, cri);
    	model.addAttribute("issueList", searchIsList);
    	model.addAttribute("page", page);
    	model.addAttribute("perPageNum", perPageNum);
    	model.addAttribute("searchType", searchType);
   	 	model.addAttribute("keyword", keyword);
   	 	model.addAttribute("startDate", startDate);
   	 	model.addAttribute("endDate", endDate);
    	
    	PageMaker pageMaker = new PageMaker();
    	pageMaker.setCri(cri);
    	 
    	pageMaker.setTotalCount(totCnt);
    	model.addAttribute("pageMaker", pageMaker);
    	
    	return "issue/list";
    }
    
    // 글 보기
    @RequestMapping(value="/view", method=RequestMethod.GET)
    public void viewContent(@ModelAttribute("cri") Criteria cri, HttpServletRequest request, Model model) throws Exception {
    	int no = Integer.parseInt(request.getParameter("no"));
    	IssueVO issueVO = issueService.viewContent(no);
    	
    	String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
    	String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
    	String startDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
    	String endDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
    	
    	model.addAttribute("searchType", searchType);
   	 	model.addAttribute("keyword", keyword);
   	 	model.addAttribute("startDate", startDate);
   	 	model.addAttribute("endDate", endDate);
   	 	
    	model.addAttribute("issue", issueVO);
    	model.addAttribute("cri", cri);
    }
    
    // 글 쓰기 페이지
    @RequestMapping(value="/write", method=RequestMethod.GET)
    public String writeView(HttpServletRequest request, Model model) throws Exception {
    	HttpSession session = request.getSession();
    	session.getAttribute("member");
		return "issue/write";
    }
    
    // 글 쓰기 기능
    @RequestMapping(value="/writeSubmit", method=RequestMethod.POST)
    public String write(@ModelAttribute IssueVO issueVO, Model model) throws Exception {
    	issueService.write(issueVO);
		return "redirect:/issue/list";
    }
    
    // 글 수정 페이지
    @RequestMapping(value="/update", method=RequestMethod.GET)
    public void updateView(@ModelAttribute Criteria cri, HttpServletRequest request, Model model) throws Exception {
    	
    	int no = Integer.parseInt(request.getParameter("no"));
    	IssueVO issueVO = issueService.viewContent(no);
    	
    	String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
    	String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
    	String startDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
    	String endDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
    	
    	model.addAttribute("searchType", searchType);
   	 	model.addAttribute("keyword", keyword);
   	 	model.addAttribute("startDate", startDate);
   	 	model.addAttribute("endDate", endDate);
   	 	
    	model.addAttribute("issue", issueVO);
    	model.addAttribute("cri", cri);
    }
    
    // 글 수정 기능
    @RequestMapping(value="/updateSubmit", method=RequestMethod.POST)
    public String update(@ModelAttribute IssueVO issueVO, @ModelAttribute Criteria cri, HttpServletRequest request) {
    	int no = Integer.parseInt(request.getParameter("no"));
    	System.out.println("dddddddddddddddddddddddd"+no);
    	int page = cri.getPage();
    	int perPageNum = cri.getPerPageNum();
    	
    	issueService.update(issueVO);
    	return "redirect:/issue/view?no="+no+"&page="+page+"&perPageNum="+perPageNum;
    }
    
    // 글 삭제
    @RequestMapping(value="/delete", method=RequestMethod.GET)
    public String delete(@ModelAttribute IssueVO issueVO, @ModelAttribute Criteria cri, HttpServletRequest request, Model model) {
    	int no = Integer.parseInt(request.getParameter("no"));
    	issueService.delete(no);
    	
    	String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
    	String keyword = request.getParameter("keyword") == null ? "" : request.getParameter("keyword");
    	String startDate = request.getParameter("startDate") == null ? "" : request.getParameter("startDate");
    	String endDate = request.getParameter("endDate") == null ? "" : request.getParameter("endDate");
    	
    	int page = cri.getPage();
    	int perPageNum = cri.getPerPageNum();
    	
    	System.out.println("++++++++++++++++++++++++++++++"+startDate+"++++++++++++++++++"+endDate);
    	
    	return "redirect:/issue/list?no="+no+"&page="+page+"&perPageNum="+perPageNum+"&searchType="+searchType+"&keyword="+keyword+"&startDate="+startDate+"&endDate="+endDate;
    }
}
