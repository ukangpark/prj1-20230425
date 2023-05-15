let checkEmail = true;
let checkNickName = true;
let checkPassword = true;

function enableSubmit() {

	if (checkEmail && checkNickName && checkPassword) {
		$("#updateSubmit").removeAttr("disabled");
	} else {
		$("#updateSubmit").Attr("disabled", "");

	}
}

$("#inputNickName").keyup(function() {
	checkNickName = false;
	$("#availableNickName").addClass("d-none");
	$("#notAvailableNickName").addClass("d-none");
	enableSubmit();
})

$("#inputEmail").keyup(function() {
	$("#availableEmail").addClass("d-none");
	$("#notAvailableEmail").addClass("d-none");
	checkEmail = false;
})


$("#checkNickNameBtn").click(function() {
	const userNewNcikName = $("#inputNickName").val();
	$.ajax("/member/checkNickName/" + userNewNcikName, {
		success: function(data) {
			if (data.available) {
				$("#availableNickName").removeClass("d-none");
				$("#notAvailableNickName").addClass("d-none");
				checkNickName = true;
			} else {
				$("#availableNickName").addClass("d-none");
				$("#notAvailableNickName").removeClass("d-none");
				checkNickName = false;
			}
		},
		complete : enableSubmit
	})
})

$("#checkEmailBtn").click(function() {
	const userNewEmail = $("#inputEmail").val();
	$.ajax("/member/checkEmail/" + userNewEmail, {
		success: function(data) {
			if (data.available) {
				$("#availableEmail").removeClass("d-none");
				$("#notAvailableEmail").addClass("d-none");
				checkEmail = true;
			} else {
				$("#availableEmail").addClass("d-none");
				$("#notAvailableEmail").removeClass("d-none");
				checkEmail = false;
			}
		},
		complete : enableSubmit
	})
})


// 패스워드, 패스워드체크 인풋에 키업 이벤트가 발생하면
$("#inputPassword, #inputPasswordCheck").keyup(function() {
	// 패스워드에 입력한 값
	const pw1 = $("#inputPassword").val();
	// 패스워드확인에 입력한 값이
	const pw2 = $("#inputPasswordCheck").val();
	//같으면
	if (pw1 == pw2) {
		// 업데이트버튼 활성화
		$("#updateSubmit").removeAttr("disabled");
		// 일치하다는 문구출력
		$("#passwordSuccessText").removeClass("d-none");
		$("#passwordFailText").addClass("d-none");
		checkPassword = true;
	} else {
		// 다르면
		// 업데이트버튼 비활성화
		$("#updateSubmit").attr("disabled");
		// 일치하지않다는 문구출력
		$("#passwordFailText").removeClass("d-none");
		$("#passwordSuccessText").addClass("d-none");
		checkPassword = false;
		
		enableSubmit();
	}
})