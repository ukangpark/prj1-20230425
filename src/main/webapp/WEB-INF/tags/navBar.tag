<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="current"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<div style="margin-bottom:10px"></div>
<nav class="navbar navbar-expand-lg bg-body-tertiary mb-3 sticky-top">
	<div class="container-lg">
		<a class="navbar-brand" href="/list">
			<img src="/img/spring logo.png" alt="logo" height="30" />
		</a> 
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0">
				<li class="nav-item"><a class="nav-link ${current eq 'list' ? 'active' : '' }" href="/list">목록</a></li>
				
				<sec:authorize access="isAuthenticated()">
				<li class="nav-item"><a class="nav-link ${current eq 'add' ? 'active' : '' }" href="/add">글작성</a></li>
				</sec:authorize>
				
				<sec:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link ${current eq 'signup' ? 'active' : '' }" href="/member/signup">회원가입</a></li>
				</sec:authorize>
				
				<sec:authorize access="hasAuthority('admin')">
				<li class="nav-item"><a class="nav-link ${current eq 'memberList' ? 'active' : '' }" href="/member/memberlist">회원목록</a></li>
				</sec:authorize>
				
				<sec:authorize access="isAuthenticated()">
				<li class="nav-item"><a class="nav-link ${current eq 'memberInfo' ? 'active' : '' }" href="/member/info?id=<sec:authentication property="name"/>">회원정보</a></li>
				</sec:authorize>
				
				<sec:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link ${current eq 'login' ? 'active' : '' }" href="/member/login">로그인</a></li>
				</sec:authorize>
				
				<sec:authorize access="isAuthenticated()">
				<li class="nav-item"><a class="nav-link ${current eq 'logout' ? 'active' : '' }" href="/member/logout">로그아웃</a></li>
				</sec:authorize>
				
				
			</ul>
			<form action="/list" class="d-flex" role="search">
				
				<select class="form-select" name="type" id="">
					<option value="all">전체</option>
					<option value="title" ${param.type eq 'title' ? 'selected' : '' }>제목</option>
					<option value="body" ${param.type eq 'body' ? 'selected' : '' }>본문</option>
					<option value="writer" ${param.type eq 'writer' ? 'selected' : '' }>작성자</option>
				</select>
				
				<input value="${param.search }" name="search" class="form-control me-2" type="search" placeholder="Search"
					aria-label="Search">
				<button class="btn btn-outline-success" type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
			</form>
		</div>
	</div>
</nav>
<%-- 프로그램에서 안보이게 삭제함 
<div>
	<sec:authentication property="principal"/>
</div>
 --%>
<%-- <div>
	<sec:authorize access="isAuthenticated()" var="loggedIn">
		로그인한 상태
	</sec:authorize>
</div>
<div>
	<sec:authorize access="isAnonymous()">
		로그아웃한 상태
	</sec:authorize>
</div>

<div>
	<sec:authorize access="${loggedIn }">
		로그인한 상태
	</sec:authorize>
</div> --%>