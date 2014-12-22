package com.stifler.ccsv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stifler.ccsv.SeatView.PickUpSeatCallBack;
import com.stifler.ccsv.SessionInfo.Seat;

public class MainActivity extends Activity implements PickUpSeatCallBack{
	private SeatView seat_view;
	private List<String> selectedSeatList = new ArrayList<String>();
	private List<SeatInfo> seatList = new ArrayList<SeatInfo>();
	private static final int HANDLER_CODE_REFRESH_SEAT = 1;
	private int rowCount = 0;
	private int cellCount = 0;
	private String rowNo;
	private SessionInfo sessionInfo;
	String data = "{\"data\": {\"hallName\": \"No.4 Hall\",\"hallNo\": \"HM10001002604\",\"orderSeat\": null,\"payInfo\": 5,\"theatreChainId\": 1,\"seatData\": [{\"locNo\": \"0000000000000001\",\"seatType\": \"3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3,3\",\"seatStatus\": \"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\",\"columnNo\": \"ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL,ZL\",\"rowNo\": \"0\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"1\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"2\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"3\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,0,0,1,1,0,0,1,1,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"4\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,1,1,1,1,0,0,1,1,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"5\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,1,1,1,1,0,0,1,1,1,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"6\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,3,3,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,1,1,1,1,0,0,1,1,0,0,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,ZL,ZL,08,09,10,11,12,13,14,15\",\"rowNo\": \"7\"},{\"locNo\": \"0000000000000001\",\"seatType\": \"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\",\"seatStatus\": \"0,0,0,0,0,1,1,1,1,0,0,1,1,0,0,0,0\",\"columnNo\": \"01,02,03,04,05,06,07,08,09,10,11,12,13,14,15,16,17\",\"rowNo\": \"8\"}]}}";
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			int what = msg.what;
			switch (what) {
			case HANDLER_CODE_REFRESH_SEAT:
				StringBuffer sb = new StringBuffer();
				for (String seatIndex : selectedSeatList) {
					SeatInfo seatInfo = seatList
							.get(Integer.valueOf(seatIndex));
					sb.append(getString(R.string.str_row_cell,
							seatInfo.getRowContent(), seatInfo.getCellContent()));
					sb.append(" ");
				}
				if (sb.length() == 0) {
					sb.append(" ");
				}
				Toast.makeText(MyApplication.getApplication(),
						sb.toString(), Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		seat_view = (SeatView) findViewById(R.id.seat_view);
		seat_view.setPickUpSeatCallBack(this);
		JSONObject jo;
		try {
			jo = new JSONObject(data);
			JSONObject jsonData = jo.optJSONObject("data");
			Gson gson = new Gson();
			sessionInfo = gson.fromJson(jsonData.toString(), SessionInfo.class);
			initSeatList();
			seat_view.setRowAndCell(rowCount, cellCount);
			seat_view.setSeatList(seatList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initSeatList() {
		seatList.clear();
		List<Seat> seatData = sessionInfo.getSeatData();
		if (seatData != null && !seatData.isEmpty()) {
			rowCount = seatData.size();
			for (int i = 0; i < rowCount; i++) {
				Seat seat = seatData.get(i);
				// locNo = seat.getLocNo();
				rowNo = seat.getRowNo() + "";
				String[] columnNoArray = seat.getColumnNo().split(",");
				String[] seatTypeArray = seat.getSeatType().split(",");
				String[] seatStatusArray = seat.getSeatStatus().split(",");
				if (cellCount == 0) {
					cellCount = columnNoArray.length;
				}
				for (int j = 0; j < cellCount; j++) {
					SeatInfo seatInfo = new SeatInfo();
					seatInfo.setLocNo(seat.getLocNo());
					seatInfo.setRow(i);
					seatInfo.setCell(j);
					seatInfo.setType(getSeatType(
							Integer.valueOf(seatStatusArray[j]),
							Integer.valueOf(seatTypeArray[j])));
					seatInfo.setOriginalType(seatInfo.getType());
					seatInfo.setRowContent(rowNo);
					seatInfo.setCellContent(columnNoArray[j]);
					seatList.add(seatInfo);
				}
			}
		}
	}
	
	private int getSeatType(int seatStatus, int seatType) {
		int seatInfoType;
		if (seatStatus == 1 || seatStatus == 2) {
			seatInfoType = SeatInfo.SEAT_TYPE_SOLD;
		} else {
			if (seatType == 1) {
				seatInfoType = SeatInfo.SEAT_TYPE_SELECT;
			} else if (seatType == 3) {
				seatInfoType = SeatInfo.SEAT_TYPE_PASSAGE;
			} else if (seatType == 5) {
				seatInfoType = SeatInfo.SEAT_TYPE_LOVER_LEFT;
			} else if (seatType == 6) {
				seatInfoType = SeatInfo.SEAT_TYPE_LOVER_RIGHT;
			} else {
				seatInfoType = SeatInfo.SEAT_TYPE_SOLD;
			}
		}
		return seatInfoType;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void markSeat(String seatIndex) {
		if (selectedSeatList.contains(seatIndex)) {
			selectedSeatList.remove(seatIndex);
		} else {
			selectedSeatList.add(seatIndex);
		}
		Collections.sort(selectedSeatList, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				return Integer.valueOf(lhs).compareTo(Integer.valueOf(rhs));
			}
		});
		handler.sendEmptyMessage(HANDLER_CODE_REFRESH_SEAT);
	}
}
