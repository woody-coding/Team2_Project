package com.team2.project.sendMSG;

import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.service.DefaultMessageService;

public class sendMSG {
	private int acessnum;
	
	//phoneNum을 매개변수로 입력해주면 메시지를 발송해주는 메소드
	public sendMSG(String phoneNum) {
		DefaultMessageService messageService =  NurigoApp.INSTANCE.initialize("NCSCR3ZMM17I7SEC", "0DQMAOYHIOLEE9LSL4MPFCQWM6OAVIGI", "https://api.coolsms.co.kr");
		// Message 패키지가 중복될 경우 net.nurigo.sdk.message.model.Message로 치환하여 주세요
		acessnum = 16613;
		Message message = new Message();		
		message.setFrom("01064838495");
		message.setTo(phoneNum);
		message.setText(acessnum+"Multi 24에서 보내는 인증 번호입니다.");

		try {
		  // send 메소드로 ArrayList<Message> 객체를 넣어도 동작합니다!
		  messageService.send(message);
		} catch (NurigoMessageNotReceivedException exception) {
		  // 발송에 실패한 메시지 목록을 확인할 수 있습니다!
		  System.out.println(exception.getFailedMessageList());
		  System.out.println(exception.getMessage());
		} catch (Exception exception) {
		  System.out.println(exception.getMessage());
		}

	}

}
