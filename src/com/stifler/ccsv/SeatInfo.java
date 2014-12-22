package com.stifler.ccsv;

public class SeatInfo {

	public static final int SEAT_TYPE_SELECT = 1;
	public static final int SEAT_TYPE_SOLD = 2;
	public static final int SEAT_TYPE_LOVER_LEFT = 3;
	public static final int SEAT_TYPE_LOVER_RIGHT = 6;
	public static final int SEAT_TYPE_PASSAGE = 4;
	public static final int SEAT_TYPE_SELECTED = 5;

	private String locNo = "";
	private int row;
	private int cell;
	private int type;
	private int originalType;
	private String cellContent = "";
	private String rowContent = "";

	public SeatInfo() {

	}

	public SeatInfo(int row, int cell) {
		this.row = row;
		this.cell = cell;
		this.type = SEAT_TYPE_SELECTED;
		this.originalType = SEAT_TYPE_SELECTED;
	}

	public SeatInfo(int row, int cell, int type) {
		this.row = row;
		this.cell = cell;
		this.type = type;
		this.originalType = type;
	}

	public String getLocNo() {
		return locNo;
	}

	public void setLocNo(String locNo) {
		this.locNo = locNo;
	}

	public String getRowContent() {
		return rowContent;
	}

	public void setRowContent(String rowContent) {
		this.rowContent = rowContent;
	}

	public void setCellContent(String cellContent) {
		this.cellContent = cellContent;
	}

	public String getCellContent() {
		return cellContent;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCell() {
		return cell;
	}

	public void setCell(int cell) {
		this.cell = cell;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getOriginalType() {
		return originalType;
	}

	public void setOriginalType(int originalType) {
		this.originalType = originalType;
	}

}