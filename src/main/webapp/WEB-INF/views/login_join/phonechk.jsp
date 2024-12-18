<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>본인 인증</title>
</head>
<style>
		table {
        width: auto;
        height: auto;
        margin: auto;
        font-size: 15px;
        border-collapse: separate;
   		border-spacing: 15px;
       	}
       	input[type="text"], input[type="password"] {
        width: 200px;
        height: 35px;
        font-size: 15px;
        border: 0;
        border-radius: 15px;
        outline: none;
        padding-left: 15px;
        background-color: rgb(233,233,233);
    	}
        div {
        width: 300px;
        height: 150px;
        margin: auto;
        font-size: 15px;
        }
        input[type="button"]{
        	background-color : rgb(255,162,32);
        	border: none;
        	border-radius: 10px;
        }
        input[type="button"]:active {
        background-color: rgb(61, 135, 255);
    	}
    	input[type="submit"]{
        	background-color : rgb(255,162,32);
        	border: none;
        	border-radius: 10px;
        }
        input[type="submit"]:active {
        background-color: rgb(61, 135, 255);
    	}
    	#Timer{
    		color: rgb(255,162,32);
    		text-align: left;
    		font-size: 15px;
    	}
</style>
<script type="text/javascript"> 
    function otptest() {
      var p1 = document.getElementById('OTP').value;
      var p2 = '16613'; //향후 자바 단으로 넣을 예정
      var p3 =  document.getElementById('Timer');
 	 	if( p1 != p2 ) {
        alert("인증 번호가 일치 하지 않습니다.");
     	return false;
      } else{
    	alert("인증 완료");
    	localStorage.setItem('chk',true);
    	window.close();
       	return true;
      }
   	}
</script>
<script type="text/javascript">

let index = 0; // 다른 함수에서도 사용하기 위해 
let value = 180;
let time = 0;

window.onload = function timer() {
	clearInterval(time); // timer 초기화
	time = setInterval('TimerStart()', 1000); // 1초마다 noodleTimer()함수 호출
}

function TimerStart() {
	value = value - 1; // 1씩 빼나가도록 한다.
	// 1초마다 실행
	document.getElementById('Timer').innerHTML = + value + "초 남았습니다.";
	if(value == 0){
		clearInterval(time);
		alert("인증 시간이 만료 되었습니다.");
		window.close();
	}
}
</script>
<body>
	<div></div>
	<form>
	<table>
        <tr>
            <th>인증 번호</th>
            <td><input type="text" id="OTP" placeholder="인증번호를 입력 해주세요."></td>   
        </tr>
        <tr>
        	<th/>
        	<td><span id="Timer"></span></td>
        </tr>
        <tr>
        	<th/>
        	<td><input type="submit" onclick="otptest()" value="인증 완료" style="width:220px; height:35px"></td>
        </tr>
     </table>
   	</form>
</body>
</html>