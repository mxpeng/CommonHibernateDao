package com.mxpeng.commonhibernate.core;

import java.io.Serializable;
import java.util.List;

public class Paging<T> implements Serializable {

	private int total;

	private int start;

	private int limit;

	private List<T> rows;

	public Paging() {
		this.total = 0;
		this.rows = null;
		this.start = 0;
		this.limit = 15;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Override
	public String toString() {
		return "Paging{" +
				"total=" + total +
				", start=" + start +
				", limit=" + limit +
				", rows=" + rows +
				'}';
	}
}
