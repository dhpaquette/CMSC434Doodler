package com.dylanpaquette.cmsc434doodler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class DoodleView extends View {
    private Map<Path, Integer> colorsMap = new HashMap<Path, Integer>();
    private Map<Path, Float> brushMap = new HashMap<Path, Float>();
    private Paint _paintLine = new Paint();
    private Path _path = new Path();
    List<Path> _pathList = new ArrayList<Path>();
    private List<Path> savedPaths = new ArrayList<Path>();
    private boolean _clear;

    public DoodleView(Context context) {
        super(context);
        init(null, 0);
    }

    public DoodleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DoodleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        _paintLine.setColor(Color.BLUE);
        _paintLine.setAntiAlias(true);
        _paintLine.setStyle(Paint.Style.STROKE);
        _paintLine.setStrokeWidth(8);
    }


    public void clearView(){
        _clear = true;
        invalidate();
    }
    public void undoPath(){
        if (_pathList.size()>0)
        {
            savedPaths.add(_pathList.remove(_pathList.size() - 1));
            invalidate();
        }
    }
    public void redoPath(){
        if (savedPaths.size()>0)
        {
            _pathList.add(savedPaths.remove(savedPaths.size()-1));
            invalidate();
        }
    }
    public void changeColor(String color){
        switch (color){
            case "Blue":
                _paintLine.setColor(Color.BLUE);
                break;
            case "Red":
                Path _path2 = new Path();
                _paintLine.setColor(Color.RED);
                break;
            case "Green":
                _paintLine.setColor(Color.GREEN);
                break;
            case "Yellow":
                _paintLine.setColor(Color.YELLOW);
                break;
            case "Magenta":
                _paintLine.setColor(Color.MAGENTA);
                break;
            default:
                _paintLine.setColor(Color.BLUE);
                break;
        }
    }
    public void changeBrushSize(String brush){
        int value = Integer.parseInt(brush);
        _paintLine.setStrokeWidth(value);
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (_clear) {
            canvas.drawColor(Color.BLACK);
            _clear = false;
            _pathList.clear();
            savedPaths.clear();
        } else {

            for (Path p : _pathList) {
                _paintLine.setColor(colorsMap.get(p));                ;
                _paintLine.setStrokeWidth(brushMap.get(p));
                canvas.drawPath(p, _paintLine);
            }

        }
    }
    public boolean onTouchEvent(MotionEvent motionEvent) {
        float eventX = motionEvent.getX();
        float eventY = motionEvent.getY();

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                _path = new Path();
                _pathList.add(_path);
                colorsMap.put(_path, _paintLine.getColor());
                brushMap.put(_path, _paintLine.getStrokeWidth());
                _path.moveTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_MOVE:
                _path.lineTo(eventX, eventY);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        invalidate();
        return true;
    }
}
