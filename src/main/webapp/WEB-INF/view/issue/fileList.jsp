<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>View</title>
<link rel="stylesheet" href="../../../resources/issue/css/board.css">
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
</script>
</head>
<body>
	<c:if test="${empty fileList}">
		<div>등록된 파일이 없습니다.</div>
	</c:if>
	<c:if test="${not empty fileList}">
		<div>등록된 파일 : ${fileCnt}</div>
		<c:forEach items="${fileList}" var="file">
		    <div>
		        <input type="text" name="originalname" value="${file.originalname}" readonly> 
		        <input type="hidden" name="savename" value="${file.savename}" readonly> 
		        <input type="text" name="fileno" value="${file.fileno}" readonly>
		        <input type="text" name="filesize" value="${file.filesizeFormatted}" readonly>
		        <a href="/issue/downloadFile?savename=${file.savename}"><img src="" alt="다운로드"></a>
		    </div>
		</c:forEach>
	</c:if>
</body>
</html>