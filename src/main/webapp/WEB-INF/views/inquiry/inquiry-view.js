// 달력 기능 구현
document.addEventListener('DOMContentLoaded', function() {
  const dateRangeButtons = document.querySelectorAll('.date-range');
  const inquiryTypeSelect = document.getElementById('inquiry-type');
  const calendar = document.querySelector('.calendar');
  const calendarDays = document.getElementById('calendarDays');
  const monthYear = document.getElementById('monthYear');
  const prevMonth = document.getElementById('prevMonth');
  const nextMonth = document.getElementById('nextMonth');

  let currentDate = new Date();
  let selectedDateRange = null;
  let startDate = null;
  let endDate = null;

  function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}.${month}.${day}`;
  }

  function parseDate(dateString) {
    const [year, month, day] = dateString.split('.');
    return new Date(year, month - 1, day);
  }

  function updateDateRange(start, end) {
    dateRangeButtons[0].querySelector('.date-text').textContent = formatDate(start);
    dateRangeButtons[1].querySelector('.date-text').textContent = formatDate(end);
  }

  function renderCalendar(date) {
    calendarDays.innerHTML = '';
    const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);

    monthYear.textContent = `${date.toLocaleString('default', { month: 'long' })} ${date.getFullYear()}`;

    for (let i = 1; i <= lastDay.getDate(); i++) {
      const dayElement = document.createElement('div');
      dayElement.classList.add('calendar-day');
      dayElement.textContent = i;
      dayElement.addEventListener('click', () => selectDate(new Date(date.getFullYear(), date.getMonth(), i)));
      
      if (selectedDateRange === dateRangeButtons[0] && startDate && i === startDate.getDate() && date.getMonth() === startDate.getMonth() && date.getFullYear() === startDate.getFullYear()) {
        dayElement.style.backgroundColor = '#ff9400';
        dayElement.style.color = 'white';
      } else if (selectedDateRange === dateRangeButtons[1] && endDate && i === endDate.getDate() && date.getMonth() === endDate.getMonth() && date.getFullYear() === endDate.getFullYear()) {
        dayElement.style.backgroundColor = '#ff9400';
        dayElement.style.color = 'white';
      }
      
      calendarDays.appendChild(dayElement);
    }
  }

  function selectDate(date) {
    if (selectedDateRange === dateRangeButtons[0]) {
      startDate = date;
      if (endDate && startDate > endDate) {
        endDate = new Date(startDate);
      }
    } else {
      endDate = date;
      if (startDate && endDate < startDate) {
        alert("올바른 날짜 값을 넣어주세요");
        return;
      }
    }

    if (startDate && endDate) {
      const diffInMonths =
        (endDate.getFullYear() - startDate.getFullYear()) * 12 +
        (endDate.getMonth() - startDate.getMonth());
      if (diffInMonths > 3 || (diffInMonths === 3 && endDate.getDate() > startDate.getDate())) {
        alert("1:1 문의내역 조회는 최대 3개월까지 가능합니다.");
        return;
      }
    }

    updateDateRange(startDate || new Date(), endDate || new Date());
    renderCalendar(currentDate);
    hideCalendar();
  }

  function showCalendar(buttonIndex) {
    selectedDateRange = dateRangeButtons[buttonIndex];
    const rect = selectedDateRange.getBoundingClientRect();
    calendar.style.display = 'block';
    calendar.style.position = 'absolute';
    calendar.style.top = `${rect.bottom}px`;
    calendar.style.left = `${rect.left}px`;

    const selectedDateText = selectedDateRange.querySelector('.date-text').textContent;
    if (selectedDateText !== '시작 날짜 선택' && selectedDateText !== '종료 날짜 선택') {
      currentDate = parseDate(selectedDateText);
    } else {
      currentDate = new Date();
    }

    renderCalendar(currentDate);
  }

  function hideCalendar() {
    calendar.style.display = 'none';
  }

  inquiryTypeSelect.addEventListener('change', function() {
    const selectedOption = this.value;
    const currentDate = new Date();
    let start = new Date(currentDate);
    let end = new Date(currentDate);

    switch (selectedOption) {
      case 'inquiryView10s':
        start.setSeconds(currentDate.getSeconds() - 10);
        break;
      case 'inquiryView1m':
        start.setMonth(currentDate.getMonth() - 1);
        break;
      case 'inquiryView3m':
        start.setMonth(currentDate.getMonth() - 3);
        break;
      default:
        return;
    }

    startDate = start;
    endDate = end;
    updateDateRange(start, end);
  });

  dateRangeButtons.forEach((button, index) => {
    button.addEventListener('click', (event) => {
      event.preventDefault(); // 기본 동작 방지
      event.stopPropagation();
      showCalendar(index);
    });
  });

  prevMonth.addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() - 1);
    renderCalendar(currentDate);
  });

  nextMonth.addEventListener('click', () => {
    currentDate.setMonth(currentDate.getMonth() + 1);
    renderCalendar(currentDate);
  });

  document.addEventListener('click', function(event) {
    if (!calendar.contains(event.target) && !event.target.classList.contains('date-range')) {
      hideCalendar();
    }
  });

  // 초기 날짜 설정
  const today = new Date();
  startDate = new Date(today);
  startDate.setMonth(today.getMonth() - 1); // 기본값으로 1개월 전 날짜 설정
  endDate = new Date(today);
  updateDateRange(startDate, endDate);

  renderCalendar(currentDate);
  hideCalendar();
});









// &#9650; 위화살표      &#9660; 아래화살표
// 컨텐츠 화살표 토글
document.addEventListener('DOMContentLoaded', function() {
  const customDateEvent = document.querySelector('.inquiry-board');

  customDateEvent.addEventListener('click', function(event) {
    // 클릭된 요소가 title bar인지 확인
    const customDate = event.target.closest('.inquiry-titlebar');
    if (customDate) {
      const inquiryToggle = customDate.nextElementSibling; // 해당 제목 바의 다음 형제 요소
      const openClose = customDate.querySelector('.open-close'); // 클릭한 제목 바 안의 open-close 요소

      if (inquiryToggle.style.display === 'none' || inquiryToggle.style.display === '') {
          inquiryToggle.style.display = 'block'; // 보이기
          openClose.innerHTML = '&#9650;'; // 위 화살표로 변경
      } else {
          inquiryToggle.style.display = 'none'; // 숨기기
          openClose.innerHTML = '&#9660;'; // 아래 화살표로 변경
      }
    }
  });
});


// '직접설정' 부분에 드롭다운에 따라 화살표 모양 변경 
document.addEventListener('DOMContentLoaded', function() {
  const selectElement = document.getElementById('inquiry-type');
  const selectArrow = document.getElementById('selectArrow');

  selectElement.addEventListener('mousedown', function() {
      selectArrow.innerHTML = '&#9650;'; // 위 화살표
  });

  selectElement.addEventListener('blur', function() {
      selectArrow.innerHTML = '&#9660;'; // 아래 화살표
  });

  // 초기 상태 설정
  selectArrow.innerHTML = '&#9660;'; // 아래 화살표
});



// 프론트단) '답변완료' 이면 '수정' 버튼 안보이도록 설정
document.addEventListener('DOMContentLoaded', function() {
  const answerBoxes = document.querySelectorAll('.answer-box');
  
  answerBoxes.forEach(box => {
      const answerStatus = box.querySelector('.answer-button');
      const modifyButton = box.querySelector('.inquiry-modify');
      
      if (answerStatus && answerStatus.textContent.trim() === '답변완료') {
          if (modifyButton) {
              modifyButton.style.display = 'none';
          }
      }
  });
});
