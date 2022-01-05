package br.com.als.mymoney.api.events.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.als.mymoney.api.domain.model.dto.RegisterDTO;
import br.com.als.mymoney.api.domain.services.RegisterService;
import br.com.als.mymoney.api.domain.services.SendMailService;
import br.com.als.mymoney.api.domain.services.SendMailService.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SendReportOverdueRegisters {

	@Autowired
	private SendMailService mailService;
	
	@Autowired
	private RegisterService registerService;
	
	@Scheduled(cron = "0 0 6 * * *")
//	@Scheduled(fixedDelay = 10 * 1000)
	public void sendTest() {
		var registers = getOverdueRegisters();
		
		if(registers.isEmpty()) {
			log.info("Overdue Registers Report was not sent, because there's no overdue register");
			return;
		}
		
		mailService.send(Message.builder()
				.body("overdue-registers-mail.html")
				.subject("MyMoney | Lançamentos Vencidos")
				.recipient("als_08.net@hotmail.com")
				.param("registers", getOverdueRegisters())
				.build());

		log.info("Overdue Registers Report Sent by E-mail");
	}

	private List<RegisterDTO> getOverdueRegisters() {
		return registerService.findOverdueRegisters();
	}
}
