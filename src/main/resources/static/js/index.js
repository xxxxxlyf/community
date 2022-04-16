$(function(){
	$("#publishBtn").click(publish);
});

$(function(){
	$("#test").click(test);
});

function test() {
	$("#publishModal").modal("show");
}

function publish() {
	//隐藏发布栏
	$("#publishModal").modal("hide");
	// 获取标题和内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	// 发送异步请求(POST)
	alert(title+content);
	$.post(
		//发送异步请求
		"/discussPost/addPost",
		//请求参数
		{"title":title,"content":content},
		function(data) {
			data = $.parseJSON(data);
			// 在提示框中显示返回消息
			$("#hintBody").text(data.msg);
			// 显示提示框
			$("#hintModal").modal("show");
			// 2秒后,自动隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
				// 刷新页面
				if(data.code == 0) {
					window.location.reload();
				}
			}, 2000);
		}
	);
}