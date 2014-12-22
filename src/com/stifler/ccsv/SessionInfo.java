package com.stifler.ccsv;

import java.util.ArrayList;
import java.util.List;

public class SessionInfo {

	private String hallName = "";
	private String hallNo = "";
	private String payInfo = "";
	private List<Seat> seatData = new ArrayList<Seat>();
	private List<OrderSeat> orderSeat = new ArrayList<OrderSeat>();

	public static final int SEAT_TYPE_NORMAL = 1;
	public static final int SEAT_TYPE_PASSAGE = 3;
	public static final int SEAT_TYPE_LOVER_LEFT = 5;
	public static final int SEAT_TYPE_PASSAGE_RIGHT = 6;

	public static final String SEAT_TYPE_ZL = "ZL";

	public static final int SEAT_STATUS_NORAML = 0;
	public static final int SEAT_TYPE_SOLD = 1;
	public static final int SEAT_TYPE_REPAIR = 2;
	public static final int SEAT_TYPE_SELECTED = 3;

	public class OrderSeat {
		private String num = "";
		private String pai = "";
		private String locNo = "";

		public String getLocNo() {
			return locNo;
		}

		public void setLocNo(String locNo) {
			this.locNo = locNo;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getPai() {
			return pai;
		}

		public void setPai(String pai) {
			this.pai = pai;
		}

	}

	public class Seat {
		// "locNo": "01",
		// "seatType": "1",
		// "seatStatus": "0",
		// "columnNo": "01",
		// "rowNo": "1"
		private String locNo = "";
		private String seatType = "";
		private String seatStatus = "";
		private String columnNo = "";
		private String rowNo;

		public Seat() {

		}

		public String getLocNo() {
			return locNo;
		}

		public void setLocNo(String locNo) {
			this.locNo = locNo;
		}

		public String getSeatType() {
			return seatType;
		}

		public void setSeatType(String seatType) {
			this.seatType = seatType;
		}

		public String getSeatStatus() {
			return seatStatus;
		}

		public void setSeatStatus(String seatStatus) {
			this.seatStatus = seatStatus;
		}

		public String getColumnNo() {
			return columnNo;
		}

		public void setColumnNo(String columnNo) {
			this.columnNo = columnNo;
		}

		public String getRowNo() {
			return rowNo;
		}

		public void setRowNo(String rowNo) {
			this.rowNo = rowNo;
		}

	}

	public List<OrderSeat> getOrderSeat() {
		return orderSeat;
	}

	public void setOrderSeat(List<OrderSeat> orderSeat) {
		this.orderSeat = orderSeat;
	}

	public String getHallName() {
		return hallName;
	}

	public void setHallName(String hallName) {
		this.hallName = hallName;
	}

	public String getHallNo() {
		return hallNo;
	}

	public void setHallNo(String hallNo) {
		this.hallNo = hallNo;
	}

	public String getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(String payInfo) {
		this.payInfo = payInfo;
	}

	public List<Seat> getSeatData() {
		return seatData;
	}

	public void setSeatData(List<Seat> seatData) {
		this.seatData = seatData;
	}

}
