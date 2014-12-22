package com.stifler.ccsv;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

@SuppressLint("DrawAllocation")
public class SeatView extends View {
	private List<SeatInfo> seatList = new ArrayList<SeatInfo>();

	private Paint paint = new Paint();
	private Bitmap seat_already_pick_up;
	private Bitmap seat_can_pick_up;
	private Bitmap seat_lover;
	private Bitmap seat_none;
	private Bitmap seat_sold;
	private int row = 0;
	private int cell = 0;
	private Canvas canvas;
	private List<String> mySeatList;
	private Bitmap SEAT_ALREADY_PICK_UP, SEAT_CAN_PICK_UP, SEAT_LOVER,
			SEAT_NONE, SEAT_SOLD;
	private Bitmap THUMBNAIL_SEAT_ALREADY_PICK_UP, THUMBNAIL_SEAT_CAN_PICK_UP,
			THUMBNAIL_SEAT_LOVER, THUMBNAIL_SEAT_NONE, THUMBNAIL_SEAT_SOLD;
	private float SCREEN_WIDTH, SCREEN_HEIGHT;
	private float SCREEN_ORINGINAL_WIDTH, SCREEN_ORINGINAL_HEIGHT;
	private Context context;
	private float xOffset;
	private float yOffset;
	private float xOriginal;
	private float yOriginal;
	private float imgSizeThumbnail = 0;
	private float cellSizeThumbnail = 0;
	private float imgSizeOriginal = 0;
	private float imgSize = 0;
	private float cellSize = 0;
	private float zoomLevel = 0;
	private float totalZoomLevel = 0;
	private float defaultZoomLevel = 0;
	private int XOFFSET = 20;
	private int YOFFSET = 20;
	private int TEXT_OFFSET = 30;
	private float textSize = 10;
	private float MAX_TEXT_SIZE = 30;

	private float x1;
	private float x2;
	private float y1;
	private float y2;
	private boolean canTouch = true;

	private int mode = 0;
	private float oldDist;
	private float newDist;
	private String oldClick;
	private String newClick;
	private boolean shouldAdjustAfterPointerUp = false;
	private boolean canSelect = false;
	private int thumbnailWidth = 0;
	private int thumbnailHeight = 0;
	private final int THUMBNAIL_RATIO = 3;
	private boolean SHOULD_DRAW_THUMNNAIL_AREA = false;

	private final String TEXT = "Screen Center";

	private PickUpSeatCallBack pickUpSeatCallBack;

	public void setPickUpSeatCallBack(PickUpSeatCallBack pickUpSeatCallBack) {
		this.pickUpSeatCallBack = pickUpSeatCallBack;
	}

	public SeatView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public SeatView(Context context, AttributeSet attr) {
		super(context, attr);
		this.context = context;
		init();
	}

	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}

	public List<SeatInfo> getSeatList() {
		return seatList;
	}

	public void setSeatList(List<SeatInfo> seatList) {
		this.seatList = seatList;
		SCREEN_ORINGINAL_WIDTH = SCREEN_WIDTH;
		SCREEN_ORINGINAL_HEIGHT = getHeight();
		int mount = (cell >= row) ? cell : row;
		imgSize = (SCREEN_WIDTH - XOFFSET * 2) / (mount + mount * 0.5f);
		imgSizeOriginal = imgSize;
		
		cellSize = imgSize * 1.5f;
		
		defaultZoomLevel = imgSizeOriginal;
		
		thumbnailWidth = (int) (SCREEN_WIDTH / THUMBNAIL_RATIO);
		
		imgSizeThumbnail = (thumbnailWidth - XOFFSET * 2 / THUMBNAIL_RATIO)
				/ (mount + mount * 0.5f);
		cellSizeThumbnail = imgSizeThumbnail * 1.5f;
		
		thumbnailHeight = (int) (cellSizeThumbnail * row + YOFFSET * 2
				/ THUMBNAIL_RATIO);
		initBitmap();
		getThumbnailSeatBitmap();
		invalidate();
	}

	private void getThumbnailSeatBitmap() {
		THUMBNAIL_SEAT_ALREADY_PICK_UP = Bitmap.createScaledBitmap(
				SEAT_ALREADY_PICK_UP, (int) imgSizeThumbnail,
				(int) imgSizeThumbnail, true);
		THUMBNAIL_SEAT_CAN_PICK_UP = Bitmap.createScaledBitmap(
				SEAT_CAN_PICK_UP, (int) imgSizeThumbnail,
				(int) imgSizeThumbnail, true);
		THUMBNAIL_SEAT_LOVER = Bitmap.createScaledBitmap(SEAT_LOVER,
				(int) imgSizeThumbnail, (int) imgSizeThumbnail, true);
		THUMBNAIL_SEAT_NONE = Bitmap.createScaledBitmap(SEAT_NONE,
				(int) imgSizeThumbnail, (int) imgSizeThumbnail, true);
		THUMBNAIL_SEAT_SOLD = Bitmap.createScaledBitmap(SEAT_SOLD,
				(int) imgSizeThumbnail, (int) imgSizeThumbnail, true);
	}

	public void setRowAndCell(int row, int cell) {
		this.row = row;
		this.cell = cell;
	}

	public List<String> getMySeatList() {
		return mySeatList;
	}

	private void init() {
		mySeatList = new ArrayList<String>();

		SCREEN_WIDTH = DisplayMetricsUtils.getWidth();
		SCREEN_HEIGHT = DisplayMetricsUtils.getHeight();
		textSize *= DisplayMetricsUtils.getDensity();
		XOFFSET *= DisplayMetricsUtils.getDensity();
		YOFFSET *= DisplayMetricsUtils.getDensity();
		TEXT_OFFSET *= DisplayMetricsUtils.getDensity();
		MAX_TEXT_SIZE *= DisplayMetricsUtils.getDensity();

		SEAT_ALREADY_PICK_UP = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.seat_already_pick_up_big);
		SEAT_CAN_PICK_UP = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_can_pick_up_big);
		SEAT_LOVER = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_lover_big);
		SEAT_NONE = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_none_big);
		SEAT_SOLD = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.seat_sold_big);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;
		if (row != 0 && cell != 0) {
			drawSeat();

			drawXYCoordinateLine();

			drawSeatTitle();

			drawImaginaryLine();

			Log.e("show", "onDraw~~~~~~~~~~~~~");

			if (SHOULD_DRAW_THUMNNAIL_AREA) {
				drawThumbnailRect();
				drawSeatThumbnail();
				drawScreenThumbnailArea();
				drawThumbnailImaginaryLine();
			}
		}
	}

	private void drawThumbnailRect() {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(2);
		Rect re = new Rect(0, 0, thumbnailWidth, thumbnailHeight);
		canvas.drawRect(re, paint);
	}

	private void drawSeatThumbnail() {
		Bitmap targetBitmap = null;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < cell; j++) {
				switch (seatList.get(i * cell + j).getType()) {
				case SeatInfo.SEAT_TYPE_SELECT:
					targetBitmap = THUMBNAIL_SEAT_CAN_PICK_UP;
					break;
				case SeatInfo.SEAT_TYPE_SOLD:
					targetBitmap = THUMBNAIL_SEAT_SOLD;
					break;
				case SeatInfo.SEAT_TYPE_LOVER_LEFT:
				case SeatInfo.SEAT_TYPE_LOVER_RIGHT:
					targetBitmap = THUMBNAIL_SEAT_LOVER;
					break;
				case SeatInfo.SEAT_TYPE_PASSAGE:
					targetBitmap = THUMBNAIL_SEAT_NONE;
					break;
				case SeatInfo.SEAT_TYPE_SELECTED:
					targetBitmap = THUMBNAIL_SEAT_ALREADY_PICK_UP;
					break;
				default:
					targetBitmap = THUMBNAIL_SEAT_ALREADY_PICK_UP;
					break;
				}
				canvas.drawBitmap(
						targetBitmap,
						(XOFFSET / THUMBNAIL_RATIO + 0.25f * imgSizeThumbnail + j
								* (cellSizeThumbnail)),
						(0.25f * imgSizeThumbnail + YOFFSET / THUMBNAIL_RATIO + i
								* (cellSizeThumbnail)), null);
			}
		}
	}

	private void drawScreenThumbnailArea() {
		Paint paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(true);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(1);
		int thumbnailAreaLeft = (int) ((Math.max((Math.abs(xOffset) - XOFFSET),
				0) * imgSizeOriginal / imgSize + XOFFSET)
				/ THUMBNAIL_RATIO - 0.25f * imgSizeThumbnail);
		int thumbnailAreaTop = (int) ((Math.max((Math.abs(yOffset)
				- TEXT_OFFSET - YOFFSET), 0)
				* imgSizeOriginal / imgSize + YOFFSET)
				/ THUMBNAIL_RATIO - 0.25f * imgSizeThumbnail);
		int thumbnailAreaRight = (int) (Math.min(
				thumbnailAreaLeft
						+ ((SCREEN_ORINGINAL_WIDTH - Math.max(
								XOFFSET + xOffset, 0)) / THUMBNAIL_RATIO)
						* imgSizeOriginal / imgSize + 0.25f * imgSizeThumbnail,
				cellSizeThumbnail * cell + XOFFSET / THUMBNAIL_RATIO + 0.5f
						* imgSizeThumbnail));
		int thumbnailAreaBottom = (int) (Math.min(thumbnailAreaTop
				+ (thumbnailAreaRight - thumbnailAreaLeft)
				* (getHeight() - Math.max(YOFFSET + TEXT_OFFSET + yOffset, 0))
				/ (getWidth() - Math.max(XOFFSET + xOffset, 0)) + 0.25f
				* imgSizeThumbnail, cellSizeThumbnail * row + YOFFSET
				/ THUMBNAIL_RATIO + 0.25f * imgSizeThumbnail));
		Rect r = new Rect(thumbnailAreaLeft, thumbnailAreaTop,
				thumbnailAreaRight, thumbnailAreaBottom);
		canvas.drawRect(r, paint);
	}

	private void drawThumbnailImaginaryLine() {
		Paint paint1 = new Paint();
		paint1.setStyle(Paint.Style.STROKE);
		paint1.setColor(Color.RED);
		paint1.setStrokeWidth(1);
		Path path = new Path();
		path.moveTo(XOFFSET / THUMBNAIL_RATIO + imgSizeThumbnail * 1.5f * cell
				/ 2, 0);
		path.lineTo(XOFFSET / THUMBNAIL_RATIO + imgSizeThumbnail * 1.5f * cell
				/ 2, thumbnailHeight);
		PathEffect effects1 = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint1.setPathEffect(effects1);
		canvas.drawPath(path, paint1);
	}

	private void drawSeat() {
		Bitmap targetBitmap = null;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < cell; j++) {
				switch (seatList.get(i * cell + j).getType()) {
				case SeatInfo.SEAT_TYPE_SELECT:
					targetBitmap = seat_can_pick_up;
					break;
				case SeatInfo.SEAT_TYPE_SOLD:
					targetBitmap = seat_sold;
					break;
				case SeatInfo.SEAT_TYPE_LOVER_LEFT:
				case SeatInfo.SEAT_TYPE_LOVER_RIGHT:
					targetBitmap = seat_lover;
					break;
				case SeatInfo.SEAT_TYPE_PASSAGE:
					targetBitmap = seat_none;
					break;
				case SeatInfo.SEAT_TYPE_SELECTED:
					targetBitmap = seat_already_pick_up;
					break;
				default:
					targetBitmap = seat_already_pick_up;
					break;
				}
				canvas.drawBitmap(targetBitmap, xOffset + XOFFSET + 0.25f
						* imgSize + j * (cellSize), yOffset + TEXT_OFFSET
						+ 0.25f * imgSize + YOFFSET + i * (cellSize), null);
			}
		}
	}

	private void drawSeatTitle() {
		Paint pFont = new Paint();
		pFont.setTextSize(Math.min(textSize * imgSize / imgSizeOriginal,
				MAX_TEXT_SIZE));
		pFont.setColor(context.getResources().getColor(R.color.color_9c9c9c));
		Rect rect = new Rect();
		pFont.getTextBounds(TEXT, 0, TEXT.length(), rect);
		canvas.drawText(
				TEXT,
				(((cellSize * cell) - rect.width()) / 2 + xOffset + XOFFSET),
				((TEXT_OFFSET - rect.height()) / 2 + rect.height() + yOffset + YOFFSET),
				pFont);
	}

	private void drawXYCoordinateLine() {
		paint.setColor(context.getResources().getColor(R.color.color_9c9c9c));
		paint.setTextSize(Math.min(textSize * imgSize / imgSizeOriginal,
				MAX_TEXT_SIZE));
		for (int i = 0; i < row; i++) {
			if (seatList.get(i * cell).getRowContent() != null
					&& !seatList.get(i * cell).getRowContent().trim()
							.equals("ZL")) {
				Rect rect = new Rect();
				paint.getTextBounds(seatList.get(i * cell).getRowContent(), 0,
						seatList.get(i * cell).getRowContent().length(), rect);
				canvas.drawText(seatList.get(i * cell).getRowContent(), 0, i
						* cellSize + yOffset + TEXT_OFFSET + YOFFSET
						+ ((cellSize - rect.height()) / 2 + rect.height()),
						paint);
			}
		}

		// // draw x coordinate line
		// paint.setTextSize(textSize + totalZoomLevel / 20);
		// for (int i = 0; i < cell; i++) {
		// canvas.drawText(i + 1 + "", XOFFSET + i * cellSize + xOffset + 0.5f
		// * imgSize
		// + ((cell >= 10) ? 0.06f * cellSize : 0.12f * cellSize),
		// YOFFSET / 2, paint);
		// }
	}

	private void drawImaginaryLine() {
		Paint paint1 = new Paint();
		paint1.setStyle(Paint.Style.STROKE);
		paint1.setColor(Color.DKGRAY);
		paint1.setStrokeWidth(1 * DisplayMetricsUtils.getDensity());
		Path path = new Path();
		path.moveTo((cellSize * cell) / 2 + xOffset + XOFFSET, TEXT_OFFSET
				+ YOFFSET + yOffset);
		path.lineTo((cellSize * cell) / 2 + xOffset + XOFFSET, TEXT_OFFSET
				+ YOFFSET + row * cellSize + yOffset);
		PathEffect effects1 = new DashPathEffect(new float[] { 5, 5, 5, 5 }, 1);
		paint1.setPathEffect(effects1);
		canvas.drawPath(path, paint1);
	}

	private void initBitmap() {
		imgSize = imgSize + zoomLevel;
		if (imgSize <= imgSizeOriginal) {
			imgSize = imgSizeOriginal;
		} else if (imgSize >= imgSizeOriginal * 5) {
			imgSize = imgSizeOriginal * 5;
		}
		cellSize = imgSize * 1.5f;
		Log.d("show", "zoomLevel=" + zoomLevel + ":" + "imgSize=" + imgSize
				+ ":" + "imgSizeOriginal=" + imgSizeOriginal);
		adjustBitmap();
	}

	private void adjustBitmap() {
		seat_already_pick_up = Bitmap.createScaledBitmap(SEAT_ALREADY_PICK_UP,
				(int) (imgSize), (int) (imgSize), true);
		seat_can_pick_up = Bitmap.createScaledBitmap(SEAT_CAN_PICK_UP,
				(int) (imgSize), (int) (imgSize), true);
		seat_lover = Bitmap.createScaledBitmap(SEAT_LOVER, (int) (imgSize),
				(int) (imgSize), true);
		seat_none = Bitmap.createScaledBitmap(SEAT_NONE, (int) (imgSize),
				(int) (imgSize), true);
		seat_sold = Bitmap.createScaledBitmap(SEAT_SOLD, (int) (imgSize),
				(int) (imgSize), true);
	}

	private void resetItem(int itemIndex) {
		mySeatList.remove(mySeatList.indexOf(itemIndex + ""));
		seatList.get(itemIndex).setType(
				seatList.get(itemIndex).getOriginalType());
		if (pickUpSeatCallBack != null) {
			pickUpSeatCallBack.markSeat(itemIndex + "");
		}
	}

	private void addItem(int itemIndex) {
		mySeatList.add(itemIndex + "");
		seatList.get(itemIndex).setType(SeatInfo.SEAT_TYPE_SELECTED);
		if (pickUpSeatCallBack != null) {
			pickUpSeatCallBack.markSeat(itemIndex + "");
		}
	}

	private void dealWithDown(MotionEvent event) {
		xOriginal = event.getX();
		yOriginal = event.getY();
		mode = 1;
		oldClick = getClickPoint(event);
		shouldAdjustAfterPointerUp = false;
		canSelect = true;
	}

	private void dealWithPointerDown(MotionEvent event) {
		mode += 1;
		oldDist = spacing(event);
		shouldAdjustAfterPointerUp = false;
		canSelect = false;
	}

	private void dealWithMove(MotionEvent event) {
		if (imgSize != imgSizeOriginal) {
			SHOULD_DRAW_THUMNNAIL_AREA = true;
		}
		if (mode >= 2) {
			newDist = spacing(event);
			Log.e("show",
					"ACTION_MOVE:" + "xOriginal:" + event.getX(0) + ":"
							+ "yOriginal:" + event.getY(0) + ":" + "xOriginal:"
							+ event.getX(1) + ":" + "yOriginal:"
							+ event.getY(1));
			zoomInOrOutSeatView();
		} else {
			if (shouldAdjustAfterPointerUp) {
				xOriginal = ((Math.abs(event.getX(0) - x1) < 2) ? x1 : x2) - 1;
				yOriginal = ((Math.abs(event.getY(0) - y1) < 2) ? y1 : y2) - 1;
				shouldAdjustAfterPointerUp = false;
			}
			Log.e("show", "ACTION_MOVE:" + "xOriginal:" + event.getX(0) + ":"
					+ "yOriginal:" + event.getY(0));
			xOffset += event.getX() - xOriginal;
			yOffset += event.getY() - yOriginal;
			Log.d("show", "xOffset=" + xOffset + ";" + "yOffset=" + yOffset);
			if (Math.abs(event.getX() - xOriginal) > 5
					|| Math.abs(event.getY() - yOriginal) > 5) {
				canSelect = false;
			}
			xOffset = getXOffsetAfterDispose();
			yOffset = getYOffsetAfterDispose();

			xOriginal = event.getX();
			yOriginal = event.getY();

			Log.d("show", "xOffset:" + xOffset + ";" + "yOffset:" + yOffset);
			if (xOffset != 0 || yOffset != 0) {
				invalidate();
			}
		}
	}

	private void dealWithPointerUp(MotionEvent event) {
		mode -= 1;
		shouldAdjustAfterPointerUp = true;
		x1 = event.getX(0);
		y1 = event.getY(0);
		x2 = event.getX(1);
		y2 = event.getY(1);
		Log.e("show",
				"ACTION_POINTER_UP:" + "xOriginal:" + event.getX(0) + ":"
						+ "yOriginal:" + event.getY(0) + ":" + "xOriginal:"
						+ event.getX(1) + ":" + "yOriginal:" + event.getY(1));
		invalidateWhenPointerUp(event);
	}

	private void dealWithUp(MotionEvent event) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SHOULD_DRAW_THUMNNAIL_AREA = false;
				invalidate();
			}
		}, 1000);
		mode = 0;
		if (canSelect) {
			newClick = getClickPoint(event);
			Log.e("show", "oldClick:" + oldClick + ":" + "newClick:" + newClick);
			if (newClick.equals(oldClick) && !newClick.equals("-1")) {
				dealWithSeatSelected();
			}
			if (!newClick.equals("-1")) {
				if (totalZoomLevel < defaultZoomLevel) {
					int tempRow = Integer.valueOf(newClick) / cell;
					int tempCell = Integer.valueOf(newClick) % cell;
					zoomLevel = (defaultZoomLevel - totalZoomLevel);
					totalZoomLevel = defaultZoomLevel;
					xOffset = ((SCREEN_WIDTH) / 2 - (imgSize + zoomLevel)
							* 1.5f * tempCell - XOFFSET);
					yOffset = (getHeight() / 2 - (imgSize + zoomLevel) * 1.5f
							* tempRow - YOFFSET - TEXT_OFFSET);
					if (xOffset >= 0) {
						xOffset = 0;
					}
					if (yOffset >= 0) {
						yOffset = 0;
					}
					Log.d("show", "zoomLevel:" + zoomLevel);
					if (zoomLevel != 0f) {
						initBitmap();
						invalidate();
					}
				}
			}
		}
		invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (canTouch) {
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				Log.e("show", "MotionEvent.ACTION_DOWN");
				dealWithDown(event);
				Log.e("show", "xOriginal:" + xOriginal + ":" + "yOriginal:"
						+ yOriginal);
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				Log.e("show", "MotionEvent.ACTION_POINTER_DOWN");
				dealWithPointerDown(event);
				break;
			case MotionEvent.ACTION_MOVE:
				Log.e("show", "MotionEvent.ACTION_MOVE");
				dealWithMove(event);
				break;
			case MotionEvent.ACTION_POINTER_UP:
				Log.e("show", "MotionEvent.ACTION_POINTER_UP");
				dealWithPointerUp(event);
				break;
			case MotionEvent.ACTION_UP:
				Log.e("show", "MotionEvent.ACTION_UP");
				dealWithUp(event);
				break;
			}
		}
		return true;
	}

	private void zoomInOrOutSeatView() {
		zoomLevel = (newDist - oldDist) / 20;
		totalZoomLevel += zoomLevel;
		if (totalZoomLevel <= 0) {
			totalZoomLevel = 0;
		}
		if (totalZoomLevel >= imgSizeOriginal * 4) {
			totalZoomLevel = imgSizeOriginal * 4;
		}
		oldDist = newDist;

		Log.d("show", "zoomLevel:" + zoomLevel);
		if (zoomLevel != 0f) {
			initBitmap();
			invalidate();
		}
	}

	private void invalidateWhenPointerUp(MotionEvent event) {
		xOffset = getXOffsetAfterDispose();
		yOffset = getYOffsetAfterDispose();
		xOriginal = event.getX();
		yOriginal = event.getY();

		Log.d("show", "xOffset:" + xOffset + ";" + "yOffset:" + yOffset);
		if (xOffset != 0 || yOffset != 0) {
			invalidate();
		}
	}

	private float getXOffsetAfterDispose() {
		boolean isViewWidthGreaterThanScreenWidth = getWidth() > (cellSize
				* cell + XOFFSET);
		float maxXOffset = isViewWidthGreaterThanScreenWidth ? 0 : -(cellSize
				* cell - getWidth() + XOFFSET);
		if (xOffset >= 0) {
			xOffset = 0;
		} else if (xOffset <= maxXOffset) {
			xOffset = maxXOffset;
		}
		return xOffset;
	}

	private float getYOffsetAfterDispose() {
		boolean isViewHeightGreaterThanScreenHeight = getHeight() > (cellSize
				* row + TEXT_OFFSET + YOFFSET);
		float maxYOffset = isViewHeightGreaterThanScreenHeight ? 0 : -(cellSize
				* row - getHeight() * 0.5f);
		if (yOffset >= 0) {
			yOffset = 0;
		} else if (yOffset <= maxYOffset) {
			yOffset = maxYOffset;
		}
		return yOffset;
	}

	private void dealWithSeatSelected() {
		if (mySeatList.contains(newClick)) {
			deselectSeat();
		} else {
			selectSeat();
		}
	}

	private void selectSeat() {
		if (mySeatList.size() >= 4) {
			Toast.makeText(MyApplication.getApplication(),
					R.string.str_seat_error, Toast.LENGTH_SHORT).show();
		} else {
			int itemIndex = Integer.valueOf(newClick);
			if (seatList.get(itemIndex).getType() == SeatInfo.SEAT_TYPE_SELECT) {
				addItem(itemIndex);
			} else if (seatList.get(itemIndex).getType() == SeatInfo.SEAT_TYPE_LOVER_LEFT) {
				selectLoverSeatLeft(itemIndex);
			} else if (seatList.get(itemIndex).getType() == SeatInfo.SEAT_TYPE_LOVER_RIGHT) {
				selectLoverSeatRight(itemIndex);
			}
		}
	}

	private void selectLoverSeatLeft(int itemIndex) {
		int nextItemIndex = itemIndex + 1;
		if ((nextItemIndex) <= (seatList.size() - 1)
				&& seatList.get(nextItemIndex).getType() == SeatInfo.SEAT_TYPE_LOVER_RIGHT) {
			if (mySeatList.size() >= 3) {
				Toast.makeText(MyApplication.getApplication(),
						R.string.str_seat_error, Toast.LENGTH_SHORT).show();
			} else {
				addItem(itemIndex);
				addItem(nextItemIndex);
			}
		} else {
			Toast.makeText(MyApplication.getApplication(),
					R.string.str_seat_rule, Toast.LENGTH_SHORT).show();
		}
	}

	private void selectLoverSeatRight(int itemIndex) {
		int preItemIndex = itemIndex - 1;
		if ((preItemIndex) >= 0
				&& seatList.get(preItemIndex).getType() == SeatInfo.SEAT_TYPE_LOVER_LEFT) {
			if (mySeatList.size() >= 3) {
				Toast.makeText(MyApplication.getApplication(),
						R.string.str_seat_error, Toast.LENGTH_SHORT).show();
			} else {
				addItem(itemIndex);
				addItem(preItemIndex);
			}
		} else {
			Toast.makeText(MyApplication.getApplication(),
					R.string.str_seat_rule, Toast.LENGTH_SHORT).show();
		}

	}

	private void deselectSeat() {
		int itemIndex = Integer.valueOf(newClick);
		resetItem(itemIndex);
		deselectLoverSeat(itemIndex);
	}

	private void deselectLoverSeat(int itemIndex) {
		if (seatList.get(itemIndex).getOriginalType() == SeatInfo.SEAT_TYPE_LOVER_LEFT) {
			deselectLoverSeatLeft(itemIndex);
		} else if (seatList.get(itemIndex).getOriginalType() == SeatInfo.SEAT_TYPE_LOVER_RIGHT) {
			deselectLoverSeatRight(itemIndex);
		}
	}

	private void deselectLoverSeatLeft(int itemIndex) {
		int nextItemIndex = itemIndex + 1;
		if ((nextItemIndex) <= (seatList.size() - 1)
				&& seatList.get(nextItemIndex).getOriginalType() == SeatInfo.SEAT_TYPE_LOVER_RIGHT) {
			if (mySeatList.contains(nextItemIndex + "")) {
				resetItem(nextItemIndex);
			}
		}
	}

	private void deselectLoverSeatRight(int itemIndex) {
		int preItemIndex = itemIndex - 1;
		if ((preItemIndex) >= 0
				&& seatList.get(preItemIndex).getOriginalType() == SeatInfo.SEAT_TYPE_LOVER_LEFT) {
			if (mySeatList.contains(preItemIndex + "")) {
				resetItem(preItemIndex);
			}
		}
	}

	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private String getClickPoint(MotionEvent event) {
		float currentXPosition = event.getX() - XOFFSET - xOffset;
		float currentYPosition = event.getY() - yOffset - TEXT_OFFSET - YOFFSET;
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < cell; j++) {
				if ((j * cellSize) < currentXPosition
						&& currentXPosition < j * cellSize + cellSize
						&& (i * cellSize) < currentYPosition
						&& currentYPosition < i * cellSize + cellSize) {
					return i * cell + j + "";
				}
			}
		}
		return -1 + "";
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	public interface PickUpSeatCallBack {
		public void markSeat(String seatIndex);
	}
}
