// 패스워드, 패스워드체크 인풋에 키업 이벤트가 발생하면
		$("#inputPassword, #inputPasswordCheck").keyup(function() {
			// 패스워드에 입력한 값
			const pw1 = $("#inputPassword").val();
			// 패스워드확인에 입력한 값이
			const pw2 = $("#inputPasswordCheck").val();
			//같으면
			if (pw1 == pw2) {
				// 업데이트버튼 활성화
				$("#updateSubmit").removeClass("disabled");
				// 일치하다는 문구출력
				$("#passwordSuccessText").removeClass("d-none");
				$("#passwordFailText").addClass("d-none");
			} else {
				// 다르면
				// 업데이트버튼 비활성화
				$("#updateSubmit").addClass("disabled");
				// 일치하지않다는 문구출력
				$("#passwordFailText").removeClass("d-none");
				$("#passwordSuccessText").addClass("d-none");
			}
		})