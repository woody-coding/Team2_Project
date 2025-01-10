/**
 * 
 */

var memberNo;
var showNo;
var seatPrice;
var showDate;

let selectedSeats;
let selectedCountElement;
let totalAmountElement;

window.onload = function (){
	memberNo = $('#memberNo').val();
	showNo = $('#showNo').val();
	seatPrice = document.querySelector('#totalAmount').dataset.price;
	showDate = $('#showDate').val(); //선택한 날짜
	
	selectedSeats = []; // 선택된 좌석 배열
	selectedCountElement = document.getElementById('selectedCount'); // 선택한 좌석 수를 표시할 span
	totalAmountElement = document.getElementById('totalAmount'); // 총 금액을 표시할 span

	let seats = document.querySelectorAll('.seat');
	
	$.ajax({
		url: `/show/seatInfo?showNo=${showNo}&showDate=${showDate}`,
		type:'GET',
		success: function(response){
			response.forEach(function(seat){
				const seatNumber = $(`.${seat.seatSpace}`);
				
				// isBook 속성 확인
				if(seat.isBook === 'Y'){
					seatNumber.addClass('booked');
				}
			})
		},
		error: function(error){
			console.error("모든 좌석 정보 불러오기 ajax 요청 에러", error);
		}
		
	});
	
	seats.forEach(function(seat) {
		seat.addEventListener('click', function() {
			
			if (this.classList.contains('selected')) {
				// 이미 선택된 좌석인 경우 선택 취소
				this.classList.remove('selected');
				selectedSeats.splice(selectedSeats.indexOf(this), 1);
			} else if (selectedSeats.length < 2) {
				// 선택되지 않은 좌석이고 최대 선택 가능 개수보다 작을 경우
				this.classList.add('selected');
				selectedSeats.push(this);
			} else {
				// 최대 선택 좌석 초과 시 알림
				alert('좌석은 최대 2개 선택 가능합니다.');
			}
			// 좌석 정보 업데이트
			updateSeatInfo();
		});
	});
}


// 좌석 수와 금액 업데이트 함수
function updateSeatInfo() {
	let selectedCount = selectedSeats.length; // 선택된 좌석 수
	let totalAmount = selectedCount * seatPrice; // 총 금액 계산

	// HTML 내용 업데이트
	selectedCountElement.textContent = selectedCount; // 선택한 좌석 수 갱신
	totalAmountElement.textContent = totalAmount; // 총 금액 갱신
}

function goPay(){	
	const seats = document.querySelectorAll('.selected');
	
	const seatNumbers = Array.from(seats).map(seat => {
		const seatClass = seat.classList[1];
		console.log(seatClass);
		const rowChar = seatClass.charAt(0);
		const seatNum = parseInt(seatClass.substring(1));
		
		// ? - a 라 a-a =0 -> b-a = 1 이런식
		const rowNum = rowChar.toLowerCase(0).charCodeAt(0) - 'a'.charCodeAt(0);
		const result = rowNum * 9 + seatNum;
		console.log(result);
		return result;
	});
	
	document.getElementById('selectedSeats').value = seatNumbers.join(',');
	
	console.log('Form will be submitted');
	
	// 폼 제출
	document.getElementById('paymentForm').submit();
	
}