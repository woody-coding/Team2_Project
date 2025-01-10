/**
 * 
 */
let interval;

// 이미지 배열
let images = ['/images/showMain/ad1.png', '/images/showMain/ad2.jpg', '/images/showMain/ad3.jpg'];
let currentIdx = 0;

// 페이지 로드 후 실행될 함수
window.onload = function () {
	// 해당 이미지가 들어간 div
	const imgArea = document.getElementById("adImg");

	function chImg() {
		// 다음 이미지로 순환
		currentIdx = (currentIdx + 1) % images.length;
		imgArea.style.backgroundImage = `url(${images[currentIdx]})`;

        //버튼도 변경 
		buttons.forEach(function(btn){
        	btn.classList.remove('btn_hover');
			btn.classList.add('btn_default');
		});
		buttons[currentIdx].classList.remove('btn_default');
		buttons[currentIdx].classList.add('btn_hover');
	}

   // 7초마다 chImg 실행
   interval = setInterval(chImg, 7000);    

   //버튼 눌러서 변경
   const buttons = document.querySelectorAll("#dotArea > div");

   buttons.forEach(function(button, index){
	button.addEventListener('click', function(){
		//모든 버튼에서 hover 제거 및 default 추가
		buttons.forEach(function(btn){
			btn.classList.remove('btn_hover');
			btn.classList.add('btn_default');
		});
		
		//클릭한 버튼에만 hover 추가
		button.classList.remove('btn_default');
		button.classList.add('btn_hover');
               
		imgArea.style.backgroundImage = `url(${images[index]})`;

		currentIdx = index;
	});
       });
	      
   };

   function goPrev(){
       const imgDiv = document.getElementById("adImg");
       const btns = document.querySelectorAll("#dotArea > div");

       if(currentIdx == 0){
           currentIdx = images.length-1;
       }
       else{
           currentIdx -= 1;
       }
       //이미지바꾸기
       imgDiv.style.backgroundImage = `url(${images[currentIdx]})`;
       //버튼바꾸기
       btns.forEach(function(btn){
           btn.classList.remove('btn_hover');
           btn.classList.add('btn_default');
       });
       btns[currentIdx].classList.remove('btn_default');
       btns[currentIdx].classList.add('btn_hover');
   };

   function goNext(){
       const imgDiv = document.getElementById("adImg");
       const btns = document.querySelectorAll("#dotArea > div");

       currentIdx = (currentIdx + 1) % images.length;

       //이미지바꾸기
       imgDiv.style.backgroundImage = `url(${images[currentIdx]})`;
       //버튼바꾸기
       btns.forEach(function(btn){
           btn.classList.remove('btn_hover');
           btn.classList.add('btn_default');
       });
       btns[currentIdx].classList.remove('btn_default');
       btns[currentIdx].classList.add('btn_hover');
   };
   
   //오픈임박 더보기
   function toggleMore(){
		const hiddenPosts = document.querySelectorAll('.hidden');
		const moreBtn = document.querySelector('.more_btn');
		
		//숨겨진 콘텐츠가 있으면 보이고 없으면 숨기기
		if(hiddenPosts.length > 0){
			hiddenPosts.forEach(post => {
				post.classList.remove('hidden'); //hidden 클래스를 지워 보이게함.
				post.classList.add('poster');
			});
			moreBtn.textContent = '접기 -'; //버튼 txt 변경
		}else{
			//모든 콘텐츠 tnarlrl
			const allPosts = document.querySelectorAll('.poster');
			allPosts.forEach((post, idx) => {
				if (idx > 3){
					post.classList.add('hidden');
					post.classList.remove('poster');
				}
			});
			moreBtn.textContent = '오픈 임박 티켓 더보기 +'; //버튼 txt 변경
		}
   };
