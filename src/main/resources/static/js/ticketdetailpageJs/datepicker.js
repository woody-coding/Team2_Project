


document.addEventListener('DOMContentLoaded', () => {
    const calendarDays = document.getElementById('calendarDays');
    const monthYear = document.getElementById('monthYear');
    const prevMonth = document.getElementById('prevMonth');
    const nextMonth = document.getElementById('nextMonth');
    const reservationButton = document.querySelector('.bookTicket'); // 예약하기 버튼

    const today = new Date();
    let currentMonth = today.getMonth();
    let currentYear = today.getFullYear();
    let selectedDate = null; // 선택된 날짜 저장
    let selectedRoundTime = null; // 선택된 회차 시간 저장

    // 특정 기간 설정 (YYYY-MM-DD 형식)
    const periodStart = document.body.getAttribute('data-period-start');
    const periodEnd = document.body.getAttribute('data-period-end');
    
    //console.log(periodStart); // 콘솔에서 값 확인
    //console.log(periodEnd);

    // Helper function: Generate days for the calendar
    function generateCalendar(year, month) {
        calendarDays.innerHTML = ''; // Clear existing days

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);

        const firstDayIndex = firstDay.getDay();
        const daysInMonth = lastDay.getDate();

        // Add blank spaces for days before the 1st of the month
        for (let i = 0; i < firstDayIndex; i++) {
            const emptyCell = document.createElement('div');
            calendarDays.appendChild(emptyCell);
        }

        // Add days of the month
        for (let day = 1; day <= daysInMonth; day++) {
            const dayCell = document.createElement('div');
            dayCell.textContent = day;

            const currentDate = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

            // 특정 기간에 해당하면 스타일 적용
            if (currentDate >= periodStart && currentDate <= periodEnd) {
                dayCell.classList.add('special-period'); // CSS 클래스 추가
            }

            // 특정 기간 외 날짜는 비활성화
            if (currentDate < periodStart || currentDate > periodEnd) {
                dayCell.classList.add('disabled-date'); // 비활성화 클래스
                dayCell.style.pointerEvents = 'none'; // 클릭 방지
            } else {
                dayCell.addEventListener('click', () => {
                    document.querySelectorAll('.selected').forEach(el => el.classList.remove('selected'));
                    dayCell.classList.add('selected');
                    selectedDate = currentDate;

                    // 회차 선택 예제 (임시로 첫 번째 회차 선택)
                    selectedRoundTime = document.querySelector('.RoundBtn span')?.innerText || '12:00';

                    // 예약하기 버튼 활성화
                    reservationButton.disabled = false;
                });
            }

            calendarDays.appendChild(dayCell);
        }

        monthYear.textContent = `${year}년 ${month + 1}월`;
    }

    // Event listeners for navigation
    prevMonth.addEventListener('click', () => {
        currentMonth--;
        if (currentMonth < 0) {
            currentMonth = 11;
            currentYear--;
        }
        generateCalendar(currentYear, currentMonth);
    });

    nextMonth.addEventListener('click', () => {
        currentMonth++;
        if (currentMonth > 11) {
            currentMonth = 0;
            currentYear++;
        }
        generateCalendar(currentYear, currentMonth);
    });

    // 예약하기 버튼 클릭 이벤트
    reservationButton.addEventListener('click', () => {
		const showNo = reservationButton.getAttribute('data-show-no'); // 버튼에서 showNo 값 읽기
        const showTitle = document.querySelector('.ticketDeatilTitle')?.innerText || '공연 제목';

        if (!selectedDate || !selectedRoundTime) {
            alert('날짜와 회차를 선택해주세요.');
            return;
        }

        // URL 파라미터로 전달
        window.location.href = `/ticket/reservation/${showNo}?date=${selectedDate}&roundTime=${selectedRoundTime}&title=${encodeURIComponent(showTitle)}`;
    });

    // Initial calendar generation
    generateCalendar(currentYear, currentMonth);
});
