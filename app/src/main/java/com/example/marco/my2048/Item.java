package com.example.marco.my2048;

import android.content.Context;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.GridLayout;
import android.widget.TextView;

/**
 * Created by marco on 2017/11/8.
 */

public class Item extends TextView {
    private int num;
    private int backgroundColor;
    private Point point;
    private GridLayout.LayoutParams lp;

    public Item(Context context) {
        super(context);
        lp = new GridLayout.LayoutParams();
        setGravity(Gravity.CENTER);
        setLayoutParams(lp);
    }

    public void setNum(int num){
        this.num = num;
        switch(num){
            case 0:
                backgroundColor = R.color.bg_0;
                break;
            case 2:
                backgroundColor = R.color.bg_2;
                break;
            case 4:
                backgroundColor = R.color.bg_4;
                break;
            case 8:
                backgroundColor = R.color.bg_8;
                break;
            case 16:
                backgroundColor = R.color.bg_16;
                break;
            case 32:
                backgroundColor = R.color.bg_32;

                break;
            case 64:
                backgroundColor = R.color.bg_64;

                break;
            case 128:
                backgroundColor = R.color.bg_128;

                break;
            case 256:
                backgroundColor = R.color.bg_256;

                break;
            case 512:
                backgroundColor = R.color.bg_512;

                break;
            case 1024:
                backgroundColor = R.color.bg_1024;

                break;
            case 2048:
                backgroundColor = R.color.bg_2048;

                break;
            case 4096:
                backgroundColor = R.color.bg_4096;
                break;
            case 8192:
                backgroundColor = R.color.bg_8192;
                break;
            default:
                backgroundColor = R.color.bg_default;
                break;


        }

        if(num!= 0){
            setText(String.valueOf(num));
        }
        else{
            setText("");
        }
        setBackgroundResource(backgroundColor);
    }

    public Point getPoint(){
        return  point;

    }

    public void setPoint(int row, int colum){
        if(point== null)
            point = new Point();
        point.x = colum;
        point.y = row;
        lp.columnSpec = GridLayout.spec(colum);
        lp.rowSpec = GridLayout.spec(row);
    }

    public void setSize(int size,int divider,int count){
        lp.width = size;
        lp.height = size;
        int helfDivider = divider/2;
        if(point.x== 0){
            lp.leftMargin = divider;
            lp.rightMargin = helfDivider;
        }
        else if (point.x== count-1){
            lp.rightMargin = divider;
            lp.leftMargin = helfDivider;
        }
        else{
            lp.rightMargin = helfDivider;
            lp.leftMargin = helfDivider;

        }
        if(point.y == 0){
            lp.topMargin = divider;
            lp.bottomMargin = helfDivider;
        }
        else if(point.y == count-1) {
            lp.topMargin = helfDivider;
            lp.bottomMargin = divider;
        }
        else{
            lp.topMargin = helfDivider;
            lp.bottomMargin = helfDivider;
        }
        setTextSize(TypedValue.COMPLEX_UNIT_PX,size /3);
    }

    public int getNum(){
        return num;
    }


}
