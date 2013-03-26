package com.softevol.appsystemimpl.view;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.softevol.appsystemimpl.util.FileSystem;
import org.apache.http.message.BasicLineFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

/**
 * User: antony
 * Date: 1/23/13
 * Time: 9:48 AM
 */
public class GameView extends View {
    private static final String TAG = GameView.class.getSimpleName();

    private static final int GAME_FIELD_WIDTH = 3;
    private static final int GAME_FIELD_HEIGHT = 3;

    public static interface OnSizeKnownListener {
        public void onSizeKnown();
    }

    public static interface OnPuzzleSolvedListener {
        public void onPuzzleSolved();
    }

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout( " + changed + ", " + left + ", " + top + ", " + right + ", " + bottom + ")");
        Log.d(TAG, "onLayout( " + getWidth() + ", " + getHeight() + ")");

        if (changed && mOnSizeKnownListener != null) {
            mOnSizeKnownListener.onSizeKnown();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint = new Paint();
        Paint textPaint = new Paint();
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(50);

        final int addX = (mBlockWidth * 3) < getWidth() ? (getWidth() - (mBlockWidth * 3)) / 2  : 0;
        final int addY = (mBlockHeight * 3) < getHeight() ? (getHeight() - (mBlockHeight * 3)) / 2  : 0;

        for (int i = 0; i < GAME_FIELD_HEIGHT; i++) {
            for (int j = 0; j < GAME_FIELD_WIDTH; j++) {
                Block block = mGameField[j][i];
                if (block != null) {
                    paint.setColor(block.getColor());
                    block.setRect(new Rect(
                            addX + mBlockWidth * j,
                            addY + mBlockHeight * i,
                            addX + mBlockWidth * j + mBlockWidth,
                            addY + mBlockHeight * i + mBlockHeight)
                    );

                    Paint drawPaint = new Paint();
                    drawPaint.setAntiAlias(false);
                    drawPaint.setFilterBitmap(false);
                    canvas.drawBitmap(block.getBitmap(), null, block.getRect(), drawPaint);
                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            for (int i = 0; i < GAME_FIELD_HEIGHT; i++) {
                for (int j = 0; j < GAME_FIELD_WIDTH; j++) {
                    Block block = mGameField[j][i];
                    if (block != null) {
                        if (block.getRect() != null && block.getRect().contains((int) event.getX(), (int) event.getY())) {
//                        Toast.makeText(getContext(), "Touch " + block.getValue(), Toast.LENGTH_SHORT).show();

                            checkForMove(j, i, false);
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }

    private void checkForMove(int x, int y, boolean mix) {
        final Block block = mGameField[x][y];

        if (block == null) return;

        if (x > 0 && mGameField[x - 1][y] == null) {
            mGameField[x - 1][y] = block;
        } else if (x < GAME_FIELD_WIDTH - 1 && mGameField[x + 1][y] == null) {
            mGameField[x + 1][y] = block;
        } else if (y > 0 && mGameField[x][y - 1] == null) {
            mGameField[x][y - 1] = block;
        } else if (y < GAME_FIELD_HEIGHT - 1 && mGameField[x][y + 1] == null) {
            mGameField[x][y + 1] = block;
        } else if (x >= 2 && mGameField[x - 2][y] == null) {
            Block block2 = mGameField[x - 1][y];
            mGameField[x - 1][y] = block;
            mGameField[x - 2][y] = block2;
        } else if (x < GAME_FIELD_WIDTH - 2 && mGameField[x + 2][y] == null) {
            Block block2 = mGameField[x + 1][y];
            mGameField[x + 1][y] = block;
            mGameField[x + 2][y] = block2;
        } else if (y >= 2 && mGameField[x][y - 2] == null) {
            Block block2 = mGameField[x][y - 1];
            mGameField[x][y - 1] = block;
            mGameField[x][y - 2] = block2;
        } else if (y < GAME_FIELD_HEIGHT - 2 && mGameField[x][y + 2] == null) {
            Block block2 = mGameField[x][y + 1];
            mGameField[x][y + 1] = block;
            mGameField[x][y + 2] = block2;
        } else {
            return;
        }

        mGameField[x][y] = null;
        if (!mix) {
            checkForWin();
        }

        postInvalidate();
    }

    public void checkForWin() {
        boolean win = true;
        int value = 0;
        for (int i = 0; i < GAME_FIELD_HEIGHT; i++) {
            for (int j = 0; j < GAME_FIELD_WIDTH; j++) {
                if (i == GAME_FIELD_WIDTH - 1 && j == GAME_FIELD_HEIGHT - 1) {
                    break;
                }

                Block block = mGameField[j][i];

                Log.d(TAG, "checkForWin() j: " + j + ", i: " + i + ", block: " + block);

                if (block != null && block.getIntValue() == value++) {
                    ;
                } else {
                    win = false;
                    break;
                }
            }
        }

        if (win && mOnPuzzleSolvedListener != null) {
            mOnPuzzleSolvedListener.onPuzzleSolved();

            mGameField[GAME_FIELD_WIDTH - 1][GAME_FIELD_HEIGHT - 1] = mLastBlock;
        }
    }

    public void mix() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            checkForMove(random.nextInt(mGameField.length), random.nextInt(mGameField.length), true);
        }

        Block block = mGameField[GAME_FIELD_WIDTH - 1][GAME_FIELD_HEIGHT - 1];
        if (block != null) {
            breakloops:
            for (int i = 0; i < GAME_FIELD_HEIGHT; i++) {
                for (int j = 0; j < GAME_FIELD_WIDTH; j++) {
                    if (mGameField[j][i] == null) {
                        if (j == GAME_FIELD_WIDTH - 1) {
                            checkForMove(GAME_FIELD_WIDTH - 1, GAME_FIELD_HEIGHT - 1, true);
                        } else if (i == GAME_FIELD_HEIGHT - 1) {
                            checkForMove(GAME_FIELD_WIDTH - 1, GAME_FIELD_HEIGHT - 1, true);
                        } else {
                            checkForMove(j, GAME_FIELD_HEIGHT - 1, true);
                            checkForMove(GAME_FIELD_WIDTH - 1, GAME_FIELD_HEIGHT - 1, true);
                        }

                        break breakloops;
                    }
                }
            }
        }

        postInvalidate();
    }

    public void next(long puzzleId, boolean solved) throws IllegalStateException {
        try {
            File puzzleFile = FileSystem.getPuzzleFileFromId(puzzleId);
            if (puzzleFile == null) throw new IllegalStateException("Puzzle File not found");

            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(
                    new FileInputStream(puzzleFile), false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final float viewWidth = getWidth();
        final float viewHeight = getHeight();

        if (viewHeight == 0 || viewWidth == 0) {
            Log.d(TAG, "viewHeight == 0 || viewWidth == 0");
            return;
        }

        final float imageWidth = mBitmapRegionDecoder.getWidth();
        final float imageHeight = mBitmapRegionDecoder.getHeight();

        final float imageBlockWidth = imageWidth / 3;
        final float imageBlockHeight = imageHeight / 3;

        float scale = Math.min(viewHeight / imageHeight, viewWidth / imageWidth);

        mBlockWidth = (int) ((scale * imageWidth) / 3);
        mBlockHeight = (int) ((scale * imageHeight) / 3);

        Log.d(TAG, "mBlockWidth: " + mBlockWidth);
        Log.d(TAG, "mBlockHeight: " + mBlockHeight);

        int value = 0;
        for (int i = 0; i < GAME_FIELD_WIDTH; i++) {
            for (int j = 0; j < GAME_FIELD_HEIGHT; j++) {
                if (i == GAME_FIELD_WIDTH - 1 && j == GAME_FIELD_HEIGHT - 1) {
                    mLastBlock = new Block(value++, 0);
                    mLastBlock.setBitmap(mBitmapRegionDecoder.decodeRegion(
                            new Rect(
                                    (int) (imageBlockWidth * j),
                                    (int) (imageBlockHeight * i),
                                    (int) (imageBlockWidth * j + imageBlockWidth),
                                    (int) (imageBlockHeight * i + imageBlockHeight)
                            ),
                            null
                    ));

                    if (solved) {
                        mGameField[j][i] = mLastBlock;
                    } else {
                        mGameField[j][i] = null;
                    }
                    break;
                }

                Block block = new Block(value++, Color.rgb(new Random().nextInt(100), new Random().nextInt(255), new Random().nextInt(255)));

                block.setBitmap(mBitmapRegionDecoder.decodeRegion(
                        new Rect(
                                (int) (imageBlockWidth * j),
                                (int) (imageBlockHeight * i),
                                (int) (imageBlockWidth * j + imageBlockWidth),
                                (int) (imageBlockHeight * i + imageBlockHeight)
                        ),
                        null
                ));

                mGameField[j][i] = block;
            }
        }

        mix();
    }

    public void setOnSizeKnownListener(OnSizeKnownListener onSizeKnownListener) {
        mOnSizeKnownListener = onSizeKnownListener;
    }

    public void setOnPuzzleSolvedListener(OnPuzzleSolvedListener onPuzzleSolvedListener) {
        mOnPuzzleSolvedListener = onPuzzleSolvedListener;
    }

    private static class Block {

        public Block(int value, int color) {
            mmValue = value;
            mmColor = color;
        }

        public int getColor() {
            return mmColor;
        }

        public String getValue() {
            return String.valueOf(mmValue);
        }

        public int getIntValue() {
            return mmValue;
        }

        public void setRect(Rect rect) {
            mmRect = rect;
        }

        public Rect getRect() {
            return mmRect;
        }

        @Override
        public String toString() {
            return "value: " + mmValue;
        }

        public Bitmap getBitmap() {
            return mmBitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            mmBitmap = bitmap;
        }

        private Rect mmRect;
        private int mmColor;
        private int mmValue;
        private Bitmap mmBitmap;
    }

    private BitmapRegionDecoder mBitmapRegionDecoder;
    private Block[][] mGameField = new Block[GAME_FIELD_HEIGHT][GAME_FIELD_WIDTH];

    private int mPuzzleIndex = 1;

    private int mBlockWidth;
    private int mBlockHeight;

    private Block mLastBlock;

    private OnSizeKnownListener mOnSizeKnownListener;
    private OnPuzzleSolvedListener mOnPuzzleSolvedListener;
}
