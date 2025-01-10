document.addEventListener('DOMContentLoaded', () => {
    const bookTicketLink = document.getElementById('bookTicketLink');
    const calendarDays = document.getElementById('calendarDays');
    const monthYear = document.getElementById('monthYear');
    const prevMonth = document.getElementById('prevMonth');
    const nextMonth = document.getElementById('nextMonth');
    const roundButtons = document.querySelectorAll('.RoundBtn');

    const today = new Date();
    let currentMonth = today.getMonth();
    let currentYear = today.getFullYear();
    let selectedDate = null;
    let selectedRoundTime = null;

    // HTML에서 showNo 값 가져오기
    const showNo = bookTicketLink.getAttribute('data-show-no'); // 또는 bookTicketLink.dataset.showNo

    // 특정 기간 설정 (예시)
    const periodStart = document.body.getAttribute('data-period-start');
    const periodEnd = document.body.getAttribute('data-period-end');

    // Helper function: Generate days for the calendar
    function generateCalendar(year, month) {
        calendarDays.innerHTML = '';

        const firstDay = new Date(year, month, 1);
        const lastDay = new Date(year, month + 1, 0);
        const firstDayIndex = firstDay.getDay();
        const daysInMonth = lastDay.getDate();

        for (let i = 0; i < firstDayIndex; i++) {
            const emptyCell = document.createElement('div');
            calendarDays.appendChild(emptyCell);
        }

        for (let day = 1; day <= daysInMonth; day++) {
            const dayCell = document.createElement('div');
            dayCell.textContent = day;

            const currentDate = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;

            if (currentDate >= periodStart && currentDate <= periodEnd) {
                dayCell.classList.add('special-period');
            }

            if (currentDate < periodStart || currentDate > periodEnd) {
                dayCell.classList.add('disabled-date');
                dayCell.style.pointerEvents = 'none';
            } else {
                dayCell.addEventListener('click', () => {
                    document.querySelectorAll('.selected').forEach(el => el.classList.remove('selected'));
                    dayCell.classList.add('selected');
                    selectedDate = currentDate;

                    bookTicketLink.classList.remove('disabled');
                });
            }

            calendarDays.appendChild(dayCell);
        }

        monthYear.textContent = `${year}년 ${month + 1}월`;
    }

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

    // 회차 버튼 클릭 이벤트
    roundButtons.forEach(button => {
        button.addEventListener('click', () => {
            document.querySelectorAll('.RoundBtn').forEach(btn => btn.classList.remove('selected'));
            button.classList.add('selected');
            selectedRoundTime = button.querySelector('span')?.innerText || null;
        });
    });

    // 예매 버튼 클릭 시 확인
    bookTicketLink.addEventListener('click', (event) => {
        if (!selectedDate) {
            alert('날짜를 선택해주세요.');
            event.preventDefault(); // 링크 이동 방지
            return;
        }

        if (!selectedRoundTime) {
            alert('회차를 선택해주세요.');
            event.preventDefault(); // 링크 이동 방지
            return;
        }

        // 선택된 값으로 링크 동적 설정
        const memberNo = 123; // 실제 값으로 대체
        bookTicketLink.href = `/show/bookSeat/${showNo}/${memberNo}/${selectedDate}`;
    });

    generateCalendar(currentYear, currentMonth);
});
