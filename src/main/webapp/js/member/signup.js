let checkId = false;
let checkEmail = false;
let checkNickName = false;
let checkPassword = false;

function enableSubmit() {
		if (checkId && checkEmail && checkNickName && checkPassword) {
			$("#signupSubmit").removeAttr("disabled");
		}else {
			$("#signupSubmit").attr("disabled", "");
			
		}
}

// input 아이디에 키보드 입력 발생시
$("#inputId").keyup(function() {
	// 아이디 중복확인 다시 
	checkId = false;
	$("#availableIdMessage").addClass("d-none");
	$("#notAvailableIdMessage").addClass("d-none");
	// submit 버튼 비활성화 해야함
	enableSubmit();
});

$("#inputNickName").keyup(function() {
	checkNickName=false;
	$("#availableNickName").addClass("d-none");
	$("#notAvailableNickName").addClass("d-none");
	enableSubmit();
})

$("#inputEmail").keyup(function() {
	checkEmail=false;
	$("#availableIdMessage").addClass("d-none");
	$("#notAvailableIdMessage").addClass("d-none");
	enableSubmit();
})

$("#inputPassword").keyup(function() {
	checkPassword=false;
	$("#passwordSuccessText").addClass("d-none");
	$("#passwordFailText").addClass("d-none");
	enableSubmit();
})


// id 중복확인이 버튼이 클릭되면
$("#checkIdBtn").click(function() {
	const userid = $("#inputId").val();
	//입력한 ID와 ajax 요청 보내서
	$.ajax("/member/checkId/"+userid, {
		success : function(data) {
			// `{"available" : true}`
			
			if (data.available) {
				//사용가능하다는 메세지 출력
				$("#availableIdMessage").removeClass("d-none");
				$("#notAvailableIdMessage").addClass("d-none");
				checkId = true;
			}else {
				//사용불가능하다는 메세지 출력
				$("#availableIdMessage").addClass("d-none");
				$("#notAvailableIdMessage").removeClass("d-none");
				checkId = false;
				
			}
		},
		complete : enableSubmit
	})
})


// 별명 중복확인 버튼이 클릭되면,
$("#checkNickNameBtn").click(function() {
	const userNickName = $("#inputNickName").val();
	$.ajax("/member/checkNickName/"+userNickName, {
		// 응답이 성공적이라면,
		success : function(data) {
			
			if (data.available) {
				$("#availableNickName").removeClass("d-none");
				$("#notAvailableNickName").addClass("d-none");
				checkNickName = true;
				
			}else {
				$("#availableNickName").addClass("d-none");
				$("#notAvailableNickName").removeClass("d-none");
				checkNickName = false;
			}
		},
		complete : enableSubmit
	})
})

$("#checkEmailBtn").click(function() {
	const userEmail = $("#inputEmail").val();
	$.ajax("/member/checkEmail/"+userEmail, {
		success : function(data) {
			if (data.available) {
				$("#availableEmail").removeClass("d-none");
				$("#notAvailableEmail").addClass("d-none");
				checkEmail = true;
			}else {
				$("#availableEmail").addClass("d-none");
				$("#notAvailableEmail").removeClass("d-none");
				checkEmail = false;
				
			}
		},
		complete : enableSubmit
	})
})

// 패스워드, 패스워드체크 인풋에 키업 이벤트 발생하면
$("#inputPassword, #inputPasswordCheck").keyup(function() {
//패스워드에 입력한 값
const pw1 = $("#inputPassword").val();
//패스워드확인에 입력한 값이 
const pw2 = $("#inputPasswordCheck").val();
//같으면
if (pw1 == pw2) {
//submit 버튼 활성화
	$("#signupSubmit").removeAttr("disabled");
//패스워드가 같다는 메세지 출력
$("#passwordSuccessText").removeClass("d-none");
$("#passwordFailText").addClass("d-none");
checkPassword = true;

} else {
//그렇지 않으면
//submit버튼 비활정화
	$("#signupSubmit").attr("disabled");	
//패스워드가 다르다는 메세지 출력
$("#passwordFailText").removeClass("d-none");
$("#passwordSuccessText").addClass("d-none");
checkPassword = false;
}

enableSubmit();


})