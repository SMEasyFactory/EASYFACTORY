package com.issue.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.issue.service.EzFileService;
import com.issue.service.IssueService;
import com.issue.service.ReplyIssueService;
import com.issue.vo.Criteria;
import com.issue.vo.EzFileVO;
import com.issue.vo.IssueVO;
import com.issue.vo.PageMaker;
import com.issue.vo.ReplyIssueVO;

@Controller
@RequestMapping("/issue")
public class IssueController {

	@Autowired
    private IssueService issueService;
    
    public void setIssueService(IssueService issueService) {
		this.issueService = issueService;
	}
    
    // 댓글 Service
    @Autowired
    private ReplyIssueService replyIssueService;
    
    public void setReplyIssueService(ReplyIssueService replyIssueService) {
		this.replyIssueService = replyIssueService;
	}
    
    // 파일 Service
    @Autowired
    private EzFileService ezFileService;
    
    public void setEzFileService(EzFileService ezFileService) {
		this.ezFileService = ezFileService;
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
    	String strNo = request.getParameter("no");
    	int no = (strNo != null && strNo.isEmpty() != true) ? Integer.parseInt(strNo) : 0;
    	
    	//int no = request.getParameter("no").toString() == null ? 0 : Integer.parseInt(request.getParameter("no"));
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
    public String write(Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) throws Exception {
        String SAVEFOLDER = "C:\\easyfactory_file";
        int MAXSIZE = 50 * 1024 * 1024; // 50MB

        // 파일 아이템을 저장할 리스트 생성
        List<FileItem> fileItems = new ArrayList<>();

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            upload.setSizeMax(MAXSIZE);

            List<FileItem> items = upload.parseRequest(request);
            System.out.println("length : " + items.size());
            for (FileItem item : items) {
                if (!item.isFormField()) {
                    // 파일 아이템이면 리스트에 추가
                    fileItems.add(item);
                }
            }

            // 다른 필드 값들은 여기서 처리
            String title = "";
            String content = "";
            String author = "";

            for (FileItem item : items) {
                if (item.isFormField()) {
                    if ("title".equals(item.getFieldName())) {
                        title = item.getString("UTF-8");
                    } else if ("content".equals(item.getFieldName())) {
                        content = item.getString("UTF-8");
                    } else if ("author".equals(item.getFieldName())) {
                        author = item.getString("UTF-8");
                    }
                }
            }

            System.out.println("title ::::: " + title + ", content ::::: " + content + ", author ::::: " + author);
            issueService.write(title, content, author);

            List<EzFileVO> fileList = new ArrayList<>();
            int totfilesize = 0;
            for (FileItem fileItem : fileItems) {
                String originalname = fileItem.getName();
                String savename = System.currentTimeMillis() + "_" + originalname;
                int filesize = (int) fileItem.getSize();
                if(filesize > 0) {
                    totfilesize += filesize;
	                File uploadedFile = new File(SAVEFOLDER, savename);
	                fileItem.write(uploadedFile);
	
	                System.out.println("originalname ::::: " + originalname + ", savename ::::: " + savename + ", filesize ::::: " + filesize);
	
	                EzFileVO ezFileVO = new EzFileVO();
	                ezFileVO.setOriginalname(originalname);
	                ezFileVO.setSavename(savename);
	                ezFileVO.setFilesize(filesize);
	                fileList.add(ezFileVO);
                }
            }

            if(totfilesize > 0) {
            	int no = ezFileService.getDynamicIssueNo();
            	ezFileService.uploadFile(fileList, no);
            	HttpSession session = request.getSession();
            	session.setAttribute("fileList", fileList);
            }
            
            System.out.println("fileList.size() :::: " + fileList.size());
            System.out.println("totfilesize :::: " + totfilesize);
        }

        return "redirect:/issue/list";
    }
    
    // 글 수정 페이지
    @RequestMapping(value="/update", method=RequestMethod.GET)
    public void updateView(@ModelAttribute Criteria cri, HttpSession session, HttpServletRequest request, Model model) throws Exception {
    	
    	int no = Integer.parseInt(request.getParameter("no"));
    	IssueVO issueVO = issueService.viewContent(no);
    	
    	session.getAttribute("member");
        
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
    	
    	return "redirect:/issue/list?no="+no+"&page="+page+"&perPageNum="+perPageNum+"&searchType="+searchType+"&keyword="+keyword+"&startDate="+startDate+"&endDate="+endDate;
    }
    
    // 댓글 작성 ajax
    @RequestMapping(value="/writeReply", method=RequestMethod.POST)
    @ResponseBody
    public String writeReply(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
    	
    	int no = Integer.parseInt(request.getParameter("no"));
    	String content = request.getParameter("content");
    	String author = request.getParameter("author");
    	
    	if(session.getAttribute("member") == null) {
			return "fail";
		} else {
			replyIssueService.writeReply(no, content, author);
			return "InsertSuccess";
		}
    }
    
    // 댓글 출력 ajax
    @RequestMapping(value="/viewReply", method=RequestMethod.GET)
    public String replylist(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	int no = Integer.parseInt(request.getParameter("no"));
    	int replyCnt = replyIssueService.issueReplyListCnt(no); // 댓글 갯수
    	List<ReplyIssueVO> replyList= replyIssueService.getReplyList(no); // 댓글 목록
    	
    	model.addAttribute("replyList", replyList);
    	model.addAttribute("replyCnt", replyCnt);
        return "issue/reply";
    }
    
    // 댓글 수정 ajax
    @RequestMapping(value="/updateReply", method=RequestMethod.POST)
    @ResponseBody
    public void updateReply(HttpServletRequest request) {
    	int reno = Integer.parseInt(request.getParameter("reno"));
    	String content = request.getParameter("content");
    	replyIssueService.updateReply(reno, content);
    }
    
    
    // 댓글 삭제 ajax
    @RequestMapping(value="/deleteReply", method=RequestMethod.POST)
    @ResponseBody
    public void deleteReply(HttpServletRequest request) throws Exception {
    	int reno = Integer.parseInt(request.getParameter("reno"));
        
    	replyIssueService.deleteReply(reno);
    }
    
    // 파일 목록 출력 ajax
    @RequestMapping(value="/viewFileList", method=RequestMethod.GET)
    public String viewFileList(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	String strNo = request.getParameter("no");
    	int no = (strNo != null && strNo.isEmpty() != true) ? Integer.parseInt(strNo) : 0;
    	
    	System.out.println("nononononononononnononnonononononnoon" + no);
    	int fileCnt = ezFileService.issueFileListCnt(no); // 파일 갯수
    	List<EzFileVO> fileList= ezFileService.getFileList(no); // 파일 목록
    	
    	for (EzFileVO file : fileList) {
    		int fileSize = file.getFilesize();
            String formattedSize = formatFileSize(fileSize);
            file.setFilesizeFormatted(formattedSize);
        }
    	
    	model.addAttribute("fileList", fileList);
    	model.addAttribute("fileCnt", fileCnt);
        return "issue/fileList";
    }
    
    // 파일 사이즈 포맷 변환
    private String formatFileSize(long fileSize) {
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(fileSize) / Math.log10(1024));

        if (digitGroups >= units.length) {
            digitGroups = units.length - 1;
        }

        double size = fileSize / Math.pow(1024, digitGroups);
        return String.format("%.2f %s", size, units[digitGroups]);
    }
}
