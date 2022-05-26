$(function() {

	$(".goods-panel").hover(function() {
		$(this).css("box-shadow", "0px 0px 8px #888888");
	}, function() {
		$(this).css("box-shadow", "");
	})

	$(".add-fav").toggle(function() {
		$(this).html("<span class='fa fa-heart-o'></span>加入收藏");
	}, function() {
		$(this).html("<span class='fa fa-heart'></span>取消收藏");
	})
})