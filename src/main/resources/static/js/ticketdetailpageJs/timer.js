let bookTicketLink;
let roundButtons;

function updateButton() {
    const now = new Date();
    const targetDateStr = document.body.getAttribute('data-target-date');
    const targetDate = new Date(targetDateStr);

    const timeDiff = targetDate - now;
    const calendar = document.querySelector('.calendar');

    if (timeDiff <= 0) {
        bookTicketLink.classList.remove('disabled');
        bookTicketLink.textContent = '티켓예매';
        roundButtons.style.display = 'block';
        calendar.style.display = 'block';
        document.querySelector('.BeforeTicketOpen').style.display = 'none';
    } else {
        const days = Math.floor(timeDiff / (1000 * 60 * 60 * 24));
        const hours = Math.floor((timeDiff % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeDiff % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeDiff % (1000 * 60)) / 1000);

        bookTicketLink.textContent = `${days}일 ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
        bookTicketLink.classList.add('disabled');
        roundButtons.style.display = 'none';
        calendar.style.display = 'none';
        document.querySelector('.BeforeTicketOpen').style.display = 'block';

        setTimeout(updateButton, 1000);
    }
}

function init() {
    bookTicketLink = document.getElementById('bookTicketLink');
    roundButtons = document.getElementById('roundButtons');

    updateButton();
}

window.addEventListener('load', init);
