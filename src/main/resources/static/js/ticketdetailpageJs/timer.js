// ex) 버튼 활성화 시간 설정 (날짜, 시각, 분, 초)


let reserveButton;
let roundButtons; // 추가된 변수

function updateButton() {
    const now = new Date();
	const targetDateStr = document.body.getAttribute('data-target-date'); // Retrieve formattedDate from body
    const targetDate = new Date(targetDateStr); // Convert to Date object

    const timeDiff = targetDate - now;
    
    const calendar = document.querySelector('.calendar'); // 달력 요소 가져오기
    


    if (timeDiff <= 0) {
        reserveButton.disabled = false;
        reserveButton.textContent = '예매하기';
        roundButtons.style.display = 'block';
        calendar.style.display = 'block'; // 달력 보이기
        document.querySelector('.BeforeTicketOpen').style.display = 'none';
        
        // 예매 가능한 시간일 때 "1회", "2회" 버튼 보이기
        roundButtons.style.display = 'block';  // 버튼 보이기
    } else {
        const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24)); // 일 계산
        const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); // 시간 계산
        const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60)); // 분 계산
        const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000); // 초 계산

        // 남은 시간 포맷 "nn일 nn:nn:nn" 형식으로 변환
        reserveButton.textContent = `${days}일 ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        
        // 예매할 수 없을 때는 "1회", "2회" 버튼 숨기기
        roundButtons.style.display = 'none';  // 버튼 숨기기
        calendar.style.display = 'none'; // 티켓 오픈 전에는 달력 숨기기
		document.querySelector('.BeforeTicketOpen').style.display = 'block';
		
        setTimeout(updateButton, 1000);
    }
}



function init() {
    reserveButton = document.querySelector('.bookTicket'); 
    roundButtons = document.getElementById('roundButtons');  // roundButtons 요소 가져오기
    
    updateButton();
}

window.addEventListener('load', init);