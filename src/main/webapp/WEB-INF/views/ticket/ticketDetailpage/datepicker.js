

document.addEventListener('DOMContentLoaded', () => {
    const calendarDays = document.getElementById('calendarDays');
    const monthYear = document.getElementById('monthYear');
    const prevMonth = document.getElementById('prevMonth');
    const nextMonth = document.getElementById('nextMonth');

    const today = new Date();
    let currentMonth = today.getMonth();
    let currentYear = today.getFullYear();

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
            dayCell.addEventListener('click', () => {
                // Remove existing selected class
                document.querySelectorAll('.selected').forEach(el => el.classList.remove('selected'));
                dayCell.classList.add('selected');
				
				//날짜확인 -> 해당 날짜를 쏴줘서 매칭
                alert(`Selected Date: ${year}-${month + 1}-${day}`);
            });
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

    // Initial calendar generation
    generateCalendar(currentYear, currentMonth);
});