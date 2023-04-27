<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Insert title here</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-KK94CHFLLe+nY2dmCWGMq91rCGa5gtU4mk92HdvYe+M/SXH301p5ILy+dN9+nJOZ" crossorigin="anonymous">
</head>
<body>

	<my:navBar current="list"></my:navBar>

	<%-- <div>${message }</div> --%>
	<!-- 모델명 -->

<%-- 	<c:if test="${not empty message }">
		<div class="container-lg">
			<div class="alert alert-warning alert-dismissible fade show" role="alert">
				${message }
				<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
			</div>
		</div>
	</c:if> --%>
	
	<my:alert></my:alert>

	<h1>게시물 목록</h1>
	<div>
		<a class="btn btn-secondary" href="/add">게시물 추가</a>
	</div>

	<div class="container-lg">
		<table class="table">
			<thead>
				<tr>
					<th>id</th>
					<th>제목</th>
					<th>작성자</th>
					<th>작성일시</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach items="${boardList }" var="list">
					<tr>
						<td>${list.id }</td>
						<td>
							<a href="/id/${list.id }"> ${list.title } </a>
						</td>
						<td>${list.writer }</td>
						<td>${list.inserted }</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ENjdO4Dr2bkBIFxQpeoTz1HIcje39Wm4jDKdf19U8gI4ddQ3GYNS7NTKfAdVQSZe" crossorigin="anonymous"></script>


</body>
</html>