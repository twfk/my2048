package com.example.marco.my2048;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private GridLayout broad;
    private int columCount =4;
    private  Item[][] items;
    private int itemWidth;
    private int divider;
    private int margin;
    private List<Point> points;
    private float downX;
    private float downY;
    private int lastNum =-1;
    private List<Integer> nums;
    private  int maxScore =0;

    private TextView tvCurrscore;
    private TextView tvHistoryscore;
    private Button btRestart;
    private Button btSelectColum;
    private final static  String LEVEL_4 = "LEVEL4";
    private final static  String LEVEL_5 = "LEVEL5";
    private final static  String LEVEL_6 = "LEVEL6";

    private String level = LEVEL_4;
    private Animation animScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        broad = (GridLayout) findViewById(R.id.main_game_broad);
        tvCurrscore = (TextView) findViewById(R.id.main_curr_max);
        tvHistoryscore = (TextView) findViewById(R.id.main_history_max);
        btRestart = (Button)findViewById(R.id.main_restart);
        btSelectColum =(Button)findViewById(R.id.main_select_column);
        btSelectColum.setOnClickListener(clickLis);
        btRestart.setOnClickListener(clickLis);
        init();
        SelectColum();
        }

        private View.OnClickListener clickLis = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.main_restart:
                        restart();
                        break;
                    case R.id.main_select_column:
                        SelectColum();
                        break;
                }
            }
        };

    private void SelectColum(){
        new AlertDialog.Builder(this)
                .setItems(new String[]{"4X4","5X5","6X6"},dialogClickLis)
                .show()
                .setCanceledOnTouchOutside(false);
    }

    private void init(){
        divider = getResources().getDimensionPixelSize(R.dimen.divider);
        margin = getResources().getDimensionPixelSize(R.dimen.activity_main_width);
        //int screenWidth = getResources().getDisplayMetrics().widthPixels;
        //itemWidth = (screenWidth -2*margin -(columCount+1)*divider) /columCount;
        points = new LinkedList<>();
        nums = new LinkedList<>();
        animScale = AnimationUtils.loadAnimation(this, R.anim.anim_scale);
    }

    private void initGame(){
        broad.removeAllViews();// first to remove all grid
        broad.setColumnCount(columCount);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        itemWidth = (screenWidth -2*margin -(columCount+1)*divider) /columCount;

        tvHistoryscore.setText(String.format("li shi zui gao: %d", getMaxScore()));


        items = new Item[columCount][columCount];
        for (int i = 0; i < columCount; i++) {
            for(int j = 0; j < columCount; j++){
                items[i][j] = new Item(this);
                items[i][j].setNum(0);
                items[i][j].setPoint(i,j);
                items[i][j].setSize(itemWidth,divider,columCount);
                broad.addView(items[i][j]);
            }
        }
        addNum(2);
    }

    private  void  checkNull(){
        for (int i = 0; i <columCount ; i++) {
            for(int j =0; j<columCount ; j++){
                if(items[i][j].getNum()== 0){
                    points.add(items[i][j].getPoint());

                }
            }

        }

    }

    private void creatNum(){
        int index = (int) (Math.random()*points.size());
        Point point = points.get(index);
        int num= Math.random()>0.1 ? 2 :4;
        items[point.y][point.x].setNum(num);
        items[point.y][point.x].startAnimation(animScale);
        points.remove(index);
    }

    private void addNum(int count){
        points.clear();
        checkNull();
        for (int i = 0; i < count; i++) {
            if(points.size()>0){
                creatNum();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                downX = x;
                downY = y;
                break;
            case MotionEvent.ACTION_UP:
                float dx = x - downX;
                float dy = y - downY;
                if(Math.abs(dx)<50 && Math.abs(dy)<50)
                    return super.onTouchEvent(event);
                if(canMove()) {
                    if(move(getMoveOrataion(dx, dy))){
                        addNum(1);
                        tvCurrscore.setText(String.format("dang qian zui gao: %d", maxScore));
                    }
                }
                else{
                    gameOver();
                }
                break;

        }
        return super.onTouchEvent(event);
    }

    private void gameOver() {
        String msg = "";

        if(maxScore<254)
            msg = "ni zheng de ruo bao le";
        else if(maxScore<512)
            msg = "tai ruo le ";
        else if(maxScore <1024)
            msg ="ji xu jia you ";
        else if(maxScore <2048)
            msg ="bu cuo ma ,keyi";
        else if(maxScore <4096)
            msg = "hao chong bai ni";

        new AlertDialog.Builder(this)
                .setMessage(msg)
                .setNegativeButton("chong xin kai shi ", dialogClickLis)
                .setNeutralButton("tui chu you xi ", dialogClickLis)
                .show()
                .setCanceledOnTouchOutside(false);

        saveScore();

    }

    private void restart(){
        saveScore();

        tvHistoryscore.setText(String.format("li shi zui gao: %d", getMaxScore()));
        for (int i = 0; i < columCount; i++) {
            for (int j = 0; j < columCount; j++) {
                items[i][j].setNum(0);
            }
            addNum(2);

        }
    }

    private DialogInterface.OnClickListener dialogClickLis = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    restart();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    finish();
                    break;
                case 0:
                    columCount =4;
                    level = LEVEL_4;
                    initGame();
                    break;
                case 1:
                    columCount =5;
                    level = LEVEL_5;
                    initGame();
                    break;
                case 2:
                    columCount =6;
                    level = LEVEL_6;
                    initGame();
                    break;

            }
        }
    };

    private boolean canMove(){
        for (int i = 0; i <columCount ; i++) {
            for(int j = 0; j < columCount; j++){
                int num = items[i][j].getNum();
                if(num == 0)
                    return true;
                if(i-1>=0 && num == items[i-1][j].getNum()) return true;
                if(i+1 < columCount && num== items[i+1][j].getNum()) return true;
                if(j-1>=0 && num == items[i][j-1].getNum()){
                    return true;
                }
                if(j+1 < columCount && num == items[i][j+1].getNum()){
                    return true;
                }
            }

        }

        return false;

    }
    private char getMoveOrataion(float x,float y){
        if(Math.abs(x) > Math.abs(y)){
            if(x > 0)
                return 'r';
            return 'l';
        }
        else{
            if(y>0)
                return 'b';
            return 't';
        }
    }

    private boolean move(char orientation){
        switch(orientation){
            case 'l':
                return MoveToleft();
            case 'r':
                return MoveToRight();
            case 't':
                return MoveToTop();
            case 'b':
                return MoveToBottom();
            default:
                return false;
        }
    }

    private boolean MoveToleft() {
        boolean f = false;
        int zeroIndex = -1;
        for (int i = 0; i <columCount ; i++) {
            for(int j =0; j < columCount; j++){
                int num = items[i][j].getNum();
                if (num != 0) {
                    if (zeroIndex != -1 && j > zeroIndex) f = true;
                    if (lastNum == -1) {
                        lastNum = num;
                    } else {
                        if (lastNum == num) {
                            f = true;
                            nums.add(num * 2);
                            lastNum = -1;
                        } else {
                            nums.add(lastNum);
                            lastNum = num;
                        }
                    }
                }
                else{
                    zeroIndex = j;
                }

            }
            if(lastNum!= -1){
                nums.add(lastNum);
            }
            for (int j = 0; j <columCount ; j++) {
                if(j<nums.size())
                {
                    int num = nums.get(j);
                    if(maxScore <num)
                        maxScore = num;
                    items[i][j].setNum(nums.get(j));
                }
                else
                    items[i][j].setNum(0);
            }
            nums.clear();
            lastNum =-1;
            zeroIndex =-1;
        }
        return f;
    }

    private boolean MoveToRight() {
        boolean f = false;
        int zeroIndex = -1;
        for (int i = 0; i <columCount ; i++) {
            for(int j =columCount -1; j >= 0; j--){
                int num = items[i][j].getNum();
                if (num != 0) {
                    if (zeroIndex != -1 && j < zeroIndex) f = true;
                    if (lastNum == -1) {
                        lastNum = num;
                    } else {
                        if (lastNum == num) {
                            f = true;
                            nums.add(num * 2);
                            lastNum = -1;
                        } else {
                            nums.add(lastNum);
                            lastNum = num;
                        }
                    }
                }
                else{
                    zeroIndex = j;
                }

            }
            if(lastNum!= -1){
                nums.add(lastNum);
            }
            for (int j = 0; j <columCount ; j++) {
                if(columCount-1-j<nums.size()) {
                    int num = nums.get(columCount - 1 - j);
                    if (maxScore < num)
                        maxScore = num;
                    items[i][j].setNum(nums.get(columCount - 1 - j));
                }
                else
                    items[i][j].setNum(0);
            }
            nums.clear();
            lastNum =-1;
            zeroIndex =-1;
        }
        return f;
    }

    private boolean MoveToTop() {
        boolean f = false;
        int zeroIndex = -1;
        for (int i = 0; i <columCount ; i++) {
            for(int j =0; j < columCount; j++){
                int num = items[j][i].getNum();
                if (num != 0) {
                    if (zeroIndex != -1 && j > zeroIndex) f = true;
                    if (lastNum == -1) {
                        lastNum = num;
                    } else {
                        if (lastNum == num) {
                            f = true;
                            nums.add(num * 2);
                            lastNum = -1;
                        } else {
                            nums.add(lastNum);
                            lastNum = num;
                        }
                    }
                }
                else{
                    zeroIndex = j;
                }

            }
            if(lastNum!= -1){
                nums.add(lastNum);
            }
            for (int j = 0; j <columCount ; j++) {
                if(j<nums.size()) {
                    int num = nums.get(j);
                    if (maxScore < num)
                        maxScore = num;
                    items[j][i].setNum(nums.get(j));
                }
                else
                    items[j][i].setNum(0);
            }
            nums.clear();
            lastNum =-1;
            zeroIndex =-1;
        }
        return f;
    }

    private boolean MoveToBottom() {
        boolean f = false;
        int zeroIndex = -1;
        for (int i = 0; i <columCount ; i++) {
            for(int j =columCount -1; j >= 0; j--){
                int num = items[j][i].getNum();
                if (num != 0) {
                    if (zeroIndex != -1 && j < zeroIndex) f = true;
                    if (lastNum == -1) {
                        lastNum = num;
                    } else {
                        if (lastNum == num) {
                            f = true;
                            nums.add(num * 2);
                            lastNum = -1;
                        } else {
                            nums.add(lastNum);
                            lastNum = num;
                        }
                    }
                }
                else{
                    zeroIndex = j;
                }

            }
            if(lastNum!= -1){
                nums.add(lastNum);
            }
            for (int j = 0; j <columCount ; j++) {
                if(columCount-1-j<nums.size()) {
                    int num = nums.get(columCount - 1 - j);
                    if (maxScore < num)
                        maxScore = num;
                    items[j][i].setNum(nums.get(columCount - 1 - j));
                }
                else
                    items[j][i].setNum(0);
            }
            nums.clear();
            lastNum =-1;
            zeroIndex =-1;
        }
        return f;
    }

    private void saveScore(){
        int history = getMaxScore();

        if(history<maxScore) {
            SharedPreferences.Editor e = getSharedPreferences("level", MODE_PRIVATE).edit();
            e.putInt(level, maxScore);
            e.commit();
        }
    }

    private int getMaxScore(){
        return getSharedPreferences("level", MODE_PRIVATE).getInt(level,0);
    }

}
