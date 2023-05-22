listComment();

$("#sendCommentBtn").click(function() {
	const boardId = $("#boardIdText").text().trim();
	const content = $("#commentTextArea").val();
	const data = { boardId, content };
	$.ajax("/comment/add", {
		method: "post",
		contentType: "application/json",
		data: JSON.stringify(data),
		complete: function(jqXHR) {
			listComment();
			$(".toast-body").text(jqXHR.responseJSON.message);
			toast.show();
		}
	})
})


$("#updateCommentBtn").click(function() {
	const commentId = $("#commentUpdateIdInput").val();
	const content = $("#commentUpdateTextArea").val();
	const data = {
		id: commentId,
		content: content
	}
	$.ajax("/comment/update", {
		method: "put",
		contentType: "application/json",
		data: JSON.stringify(data),
		complete: function(jqXHR) {
			listComment();
			$(".toast-body").text(jqXHR.responseJSON.message);
			toast.show();
		}
	})
})

function listComment() {

	const boardId = $("#boardIdText").text().trim();
	$.ajax("/comment/list?board=" + boardId, {
		method: "get",
		success: function(comments) {
			//console.log(data);
			$("#commentListContainer").empty();
			for (const comment of comments) {
				const editButtons = `
				<button
					 	id="commentDeleteBtn${comment.id}" 
					 	class="commentDeleteButton btn btn-danger" 
					 	data-comment-id="${comment.id}"><i class="fa-solid fa-trash-can"></i>
					 </button>
					 <button
					 	id="commentUpdateBtn${comment.id}"
						class="commentUpdateButton btn btn-secondary"
						data-bs-toggle="modal" data-bs-target="#commentUpdateModal"
						data-comment-id="${comment.id}">
							<i class="fa-regular fa-pen-to-square"></i>
				 	</button>
				`
				//console.log(comments)
				$("#commentListContainer").append(`
				<li class="list-group-item d-flex justify-content-between align-items-start">
					<div class="ms-2 me-auto">
						<div class="fw-bold"> <i class="fa-solid fa-user"></i> ${comment.memberId}</div>
							<div style="white-space: pre-wrap;">${comment.content}</div>
						</div>
						<div>
							<span class="badge bg-primary rounded-pill">${comment.inserted}</span>
							<div class="text-end mt-2">
								${comment.editable ? editButtons : ''}
							</div>
						</div>
				</li>
			`)
			}

			// 수정버튼이 클릭되면
			$(".commentUpdateButton").click(function() {
				const id = $(this).attr("data-comment-id");
				$.ajax("/comment/id/" + id, {
					success: function(data) {
						$("#commentUpdateIdInput").val(data.id);
						$("#commentUpdateTextArea").val(data.content);
					}
				})
			})
			// for문이 끝나고나서 버튼이 클릭되면
			$(".commentDeleteButton").click(function() {
				const commentId = $(this).attr("data-comment-id");
				$("#deleteCommentModalButton").attr("data-comment-id", commentId);
			})
		}
	})
}
$("#deleteCommentModalButton").click(function() {
	const commentId = $(this).attr("data-comment-id");
	$.ajax("/comment/id/" + commentId, {
		method: "delete",
		complete: function(jqXHR) {
			listComment();
			$(".toast-body").text(jqXHR.responseJSON.message);
			toast.show();
		}
	})
})
