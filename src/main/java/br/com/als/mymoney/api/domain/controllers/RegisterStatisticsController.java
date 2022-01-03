package br.com.als.mymoney.api.domain.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByCategory;
import br.com.als.mymoney.api.domain.model.dto.statistics.RegisterStatisticsByDay;
import br.com.als.mymoney.api.domain.services.RegisterService;

@RestController
@RequestMapping(path = "/registers")
public class RegisterStatisticsController {

	@Autowired
	private RegisterService service;

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping("/statistics/by-category")
	public ResponseEntity<List<RegisterStatisticsByCategory>> byCategory(
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		List<RegisterStatisticsByCategory> listDTO = service.statisticsByCategory(date);
		return ResponseEntity.ok(listDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping("/statistics/by-day")
	public ResponseEntity<List<RegisterStatisticsByDay>> byDay(
			@RequestParam(name = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		List<RegisterStatisticsByDay> listDTO = service.statisticsByDay(date);
		return ResponseEntity.ok(listDTO);
	}

	@PreAuthorize("hasAuthority('ROLE_PESQUISAR_LANCAMENTO') and hasAuthority('SCOPE_read')")
	@GetMapping("/reports/by-person")
	public ResponseEntity<byte[]> byPerson(
			@RequestParam(name = "dateFrom", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
			@RequestParam(name = "dateUntil", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateUntil)
			throws Exception {
		var report = service.reportByPerson(dateFrom, dateUntil);
		
		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
				.body(report);
	}
}