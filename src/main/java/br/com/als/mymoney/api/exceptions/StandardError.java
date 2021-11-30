package br.com.als.mymoney.api.exceptions;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StandardError {

	private Integer status;
	private String type;
	private String title;
	private String detail;
	private String userDetail;
	private LocalDateTime dateTime;

	public StandardError() {
		super();
	}

	public StandardError(Integer status, String type, String title, String detail, String userDetail,
			LocalDateTime dateTime) {
		super();
		this.status = status;
		this.type = type;
		this.title = title;
		this.detail = detail;
		this.userDetail = userDetail;
		this.dateTime = dateTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(String userDetail) {
		this.userDetail = userDetail;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
}
