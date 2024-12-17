const targetTime = new Date();
targetTime.setHours(16, 0, 0); // ex) 버튼 활성화 시간 설정 (시, 분, 초)

// '예매하기' 버튼에 모든 함수가 접근 가능하도록 전역 변수로 선언
let reserveButton; 

function updateButton() {
    const now = new Date();
    const timeDiff = targetTime - now;

    if (timeDiff <= 0) {
        reserveButton.disabled = false;
        reserveButton.textContent = '예매하기';
    } else {
        const hours = Math.floor(timeDiff / (1000 * 60 * 60));
        const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

        // 시간, 분, 초를 각각 두 자리 숫자로 만들어 "00:00:00" 형식으로
        reserveButton.textContent = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        
        // 매 초마다 실시간으로 화면에 보여주기 위한 설정
        setTimeout(updateButton, 1000);
    }
}

function startReservation() {
    // 여기에 예매 절차 로직 넣기!!
    alert('예매 시작!');
    
}

function init() {
    // reserveButton을 초기화
    reserveButton = document.querySelector('.bookTicket'); 
    
    // 페이지 로드 되면 자동으로 타이머 이벤트 함수 시작
    updateButton();

    // 버튼이 활성화될 때 한 번만 클릭 이벤트 리스너 추가
    reserveButton.addEventListener('click', startReservation);
}

window.addEventListener('load', init);
