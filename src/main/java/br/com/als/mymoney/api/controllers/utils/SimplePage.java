package br.com.als.mymoney.api.controllers.utils;

import java.util.List;

import org.springframework.data.domain.Page;

public class SimplePage<T> {

	private List<?> content;
	private boolean last;
	private boolean first;
	private long totalElements;
	private int totalPages;
	private int size;
	private int number;

	public SimplePage(Page<T> page) {
		this.content = page.getContent();
		this.last = page.isLast();
		this.first = page.isFirst();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.size = page.getSize();
		this.number = page.getNumber();
	}

	public List<?> getContent() {
		return content;
	}

	public boolean isLast() {
		return last;
	}

	public boolean isFirst() {
		return first;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getSize() {
		return size;
	}

	public int getNumber() {
		return number;
	}
}
