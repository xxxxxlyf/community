$(function(){
	$("#follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		// 关注TA
		//发送关注请求
		$.post(
			"/follow",
			{"entityType":3,"entityId":entityId,"userId":$(btn).prev().value()},
			function(data) {
               data=$.parseJSON(data);
               if(data.code==0){
               	   //重新加载页面
				   window.location.reload();
			   }else{
               	  //错误时，弹出警示窗口
               	  alert(data.msg)
			   }
			}
		);
		//修改信息
		//$(btn).text("已关注")
		$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		"/unfollow",
			{"entityType":3,"entityId":entityId,"userId":$(btn).prev().value()},
			function(data) {
				data=$.parseJSON(data);
				if(data.code==0){
					//重新加载页面
					window.location.reload();
				}else{
					//错误时，弹出警示窗口
					alert(data.msg)
				}
			}
		$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");

	}
}