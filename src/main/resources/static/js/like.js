function like(btn,entityType,entityId) {
    $.get(
        //请求路径
        "/like",
        //请求参数
        {"entityType":entityType,"entityId":entityId},
        //处理返回值
        function (data) {
            //序列化成为json对象
            data = $.parseJSON(data);
            //点赞成功,动态更新信息
            if(data.code==0){
                $(btn).children("i").text(data.likeQty);
                $(btn).children("b").text(data.likeStatus==1?"已赞":"赞");
            }
        }

    );
}