package alpha.cyber.intelmain.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import alpha.cyber.intelmain.R;

/**
 * Created by wangrui on 2018/1/31.
 */

public class MyTableView extends TableLayout {//一个用于显示简易表格的VIEW

    protected int columnN = 2;//列的数目。该值只能在构造函数中设置，设置之后不能修改。

    int m_LineColor = Color.BLACK;//线的颜色
    int m_LineWidth = 1;//线宽

    protected List<TableRow> tableRowList;
    protected List<List<View>> viewList;
    private Paint paint1;

    private final int ITEM_HEIGHT = 30;

    private final int ITEM_WIDTH = 120;

    public int getColumnN() {
        return columnN;
    }

    public MyTableView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        tableRowList = new ArrayList<TableRow>();
        viewList = new ArrayList<List<View>>();
        paint1 = new Paint();
        this.setWillNotDraw(false);
    }

    public MyTableView(Context context, int n) {//指定列的数目
        super(context);
        // TODO Auto-generated constructor stub
        tableRowList = new ArrayList<TableRow>();
        viewList = new ArrayList<List<View>>();
        if (n > 0) {
            columnN = n;
        } else {
            columnN = 2;
        }

        paint1 = new Paint();
        this.setWillNotDraw(false);
    }

    public void ClearRows() {
        if (tableRowList != null) {
            tableRowList.clear();
        } else {
            tableRowList = new ArrayList<TableRow>();
        }
        if (viewList != null) {
            viewList.clear();
        } else {
            viewList = new ArrayList<List<View>>();
        }
        this.removeAllViews();
    }

    /**
     * 建表
     * 1、增加一行单列标题
     * 2、循环增加一行多列属性
     * <p>
     * 划线
     * 1、画大框
     * 2、画一行单列标题
     * 3、循环画多行多列属性
     *
     * @param objects
     * @return
     */

    public void AddRow(Object objects[], boolean isTitle) {
        if (objects == null) {
            return;
        }

        TableRow tableRow = new TableRow(this.getContext());
        View viewCell = null;
        List<View> rowViews = new ArrayList<View>();

        tableRow.setTag(isTitle);
        if (isTitle) {//添加标题

//            Log.e(Constant.TAG,"添加标题");
            if (objects[0] != null) {
                viewCell = createCellView(objects[0],0,true);
            }

            if (viewCell == null) {
                viewCell = new View(getContext());
            }

            tableRow.addView(viewCell);
            tableRow.setGravity(Gravity.CENTER);
            rowViews.add(viewCell);

            this.addView(tableRow);
        } else {//添加表格

//            Log.e(Constant.TAG,"添加表格");
            for (int i = 0; i < columnN; i++) {
                if (objects[i] != null) {
                    viewCell = createCellView(objects[i],i,false);
                }

                if (viewCell == null) {
                    viewCell = new View(getContext());
                }

                rowViews.add(viewCell);

                tableRow.addView(viewCell);
            }

            this.addView(tableRow);
        }

        tableRowList.add(tableRow);
        viewList.add(rowViews);

    }

    public View GetCellView(int row, int column) {
        if (row < 0 || row >= tableRowList.size()) {
            return null;
        } else {
            if (column < 0 || column >= viewList.get(row).size()) {
                return null;
            } else {
                return viewList.get(row).get(column);
            }
        }
    }

    protected View createCellView(Object obj,int position,boolean istitle) {
        View rView = null;
        String classname = obj.getClass().toString();

        switch (classname) {
            case "class java.lang.String"://这个值是String.class.toString()的结果

                LinearLayout view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.view_table_view_cell, null);
                TextView tvCell = (TextView) view.findViewById(R.id.tv_cell);

                StringBuilder bookName=new StringBuilder();
                bookName.append(obj);

                int round_count=10-obj.toString().length();

                tvCell.setText(bookName);
                if(round_count>0&&position==0&&!istitle){//只给书名做修饰
                    for(int i=0;i<round_count;i++){
                        tvCell.append(getContext().getResources().getString(R.string.chinese_space));
                    }

                }else {
                    tvCell.setText(bookName);
                }

                rView = view;
                break;

            case "class android.graphics.Bitmap":
                ImageView iView = new ImageView(getContext());
                iView.setImageBitmap((Bitmap) obj);
                rView = iView;
                break;

            //在此处识别其它的类型，创建一个View并附给rView

            default:
                rView = null;
                break;
        }
        return rView;
    }

    int titleHeight=0;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //
        if (tableRowList.size() < 1) {
            return;
        }

        int nRLinePosition = 0, nCLinePosition = 0, width = getWidth(), height = getHeight();
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(m_LineWidth);
        paint1.setColor(m_LineColor);

        canvas.drawRect(new Rect(1, 1, width, height - 1), paint1);

        for (int i = 0; i < tableRowList.size() - 1; i++) {//划横线
            nRLinePosition += tableRowList.get(0).getHeight();
//            nRLinePosition += ITEM_HEIGHT;
            canvas.drawLine(0, nRLinePosition, width, nRLinePosition, paint1);

        }

//        for (int j = 0; j < tableRowList.size() - 1; j++) {//划竖线
//            nCLinePosition += viewList.get(j).get(j).getWidth();
////            nCLinePosition += ITEM_WIDTH;
//            canvas.drawLine(nCLinePosition, ITEM_HEIGHT, nCLinePosition, height, paint1);
//
//        }


        for (int j = 0; j < tableRowList.size(); j++) {

            if ((boolean) tableRowList.get(j).getTag()) {//获取标题高度
                titleHeight = tableRowList.get(j).getHeight();
            }

            if (!(boolean) tableRowList.get(j).getTag()) {
                for (int i = 0; i < viewList.get(j).size() - 1; i++) {//画纵线
                    nCLinePosition += viewList.get(j).get(i).getWidth();
                    canvas.drawLine(nCLinePosition, titleHeight, nCLinePosition, height, paint1);
                }
            }
        }

    }
}
