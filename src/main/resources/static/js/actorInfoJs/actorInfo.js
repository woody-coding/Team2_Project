/**
 * 
 */
$(function() {
	// var memberNo = $('#memberNo').val(); // hidden input 에서 가져오는 상태
	var status = $('.actor_like').data('status');
	console.log("current status : " + status);
	
	if(status === 'Y'){
		$('#heart').css({
			'background-image': 'url("/images/actor/full_heart.png")',
			'width': '52px',
			'height': '56px'
		});
		$('.frame').css('background-color', '#9DDAF6');
		$('.like').text('취소');
	} else {
		$('#heart').css({
			'background-image': 'url("/images/actor/heart.png")',
			'width': '45px',
			'height': '45px'
		});
		$('.frame').css('background-color', '#ff9500');
		$('.like').text('좋아요');
	}
	
	
});

function toggleImg(){
	let heart = document.getElementById("heart");

	const currentImg = heart.style.backgroundImage;
	const isLiked = currentImg.includes("full_heart.png");
	
	var memberNo = parseInt($('#memberNo').val(), 10);
	if(!memberNo){
		alert("로그인 해주세요.");
		console.error("memberNo is not available");
		return;
	}
	var actorNo = parseInt($('#actorNo').val(), 10);
	if(!actorNo){
		console.error("ActorNo is not available");
		return;
	}
	const showNo = null;
		
	
	
	let status = isLiked ? 'N' : 'Y'; // isLiked 함수가 참 -> 'N', 거짓 -> 'Y'로 변경
	
	console.log(status);

	if(isLiked){
		//원래 이미지로
		heart.style.backgroundImage = "url('/images/actor/heart.png')";
		heart.style.width = "45px";
		heart.style.height = "45px";
		$('.frame').css('background-color', '#ff9500');
		$('.like').text('좋아요');
	} 
	else {
		heart.style.backgroundImage = "url('/images/actor/full_heart.png')";
		heart.style.width = "52px";
		heart.style.height = "56px";
		$('.frame').css('background-color', '#9DDAF6');
		$('.like').text('취소');
	}  
	
	// AJAX 요청
	$.ajax({
		url: '/like-toggle',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({
			memberNo: memberNo,
			actorNo: actorNo,
			showNo: showNo,
			status: status
		}),
		success: function(response){
			console.log(response.message);
		},
		error: function(){
			alert("통신 실패");
		}
	});
	
}