/**
 * 
 */
let interval;

// 이미지 배열
const images = ['/images/showMain/ad1.png', '/images/showMain/ad2.jpg', '/images/showMain/ad3.jpg'];
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

   // 1초마다 chImg 실행
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
