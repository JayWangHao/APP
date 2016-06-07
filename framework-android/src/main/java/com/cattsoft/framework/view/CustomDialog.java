package com.cattsoft.framework.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cattsoft.framework.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义弹出框
 * Created by xueweiwei on 15/4/17.
 */
public class CustomDialog extends Dialog {

    private ArrayList<HashMap<String,String>> dataList;//弹出框的数据源
    private String titleStr;//标题
    private String keyStr;//通过keyStr获取数据源dataList的数据，显示在弹出框数据中
    private View customView;
    private ListView listView;
    private TextView titleText;
    private Button cancleBtn;

    private listViewAdapter adapter;


    private Context context;

    private OnCustomDialogListener customDialogListener;

    public interface OnCustomDialogListener{

        public void back(HashMap<String,String> value);
    }

    /**
     *
     * @param context activity的上下文
     * @param datalist 弹出框要显示的数据
     * @param titleStr 弹出框标题
     * @param keyStr 通过keyStr获取数据源dataList的数据，显示在弹出框数据中
     * @param customDialogListener 回调函数，点击弹出框某数据回调该方法
     */
    public CustomDialog(Context context,ArrayList<HashMap<String,String>> datalist,String titleStr,String keyStr,OnCustomDialogListener customDialogListener){

        super(context,R.style.CustomDialog);

        this.context = context;
        this.titleStr = titleStr;
        this.dataList = datalist;
        this.keyStr = keyStr;


        this.customDialogListener = customDialogListener;

        customView = LayoutInflater.from(context).inflate(R.layout.custom_dialog_view,null);
        titleText = (TextView) customView.findViewById(R.id.dialog_title_text);
        cancleBtn = (Button) customView.findViewById(R.id.cancle_btn);
        listView = (ListView) customView.findViewById(R.id.listview);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(customView);

        titleText.setText(titleStr);

        cancleBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CustomDialog.this.dismiss();
            }
        });

        adapter = new listViewAdapter(dataList);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                customDialogListener.back(dataList.get(i));

                CustomDialog.this.dismiss();
            }
        });

    }

    public void showDialog(){

        Window window = getWindow();

        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.CustomDialogStyle);

        setCanceledOnTouchOutside(true);

        show();

    }

    class listViewAdapter extends BaseAdapter{

        ArrayList<HashMap<String,String>> dataList;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(context.LAYOUT_INFLATER_SERVICE);

        public listViewAdapter(ArrayList<HashMap<String,String>> dataList){

            this.dataList = dataList;
        }
        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {

            ViewHolder holder = null;
            if(convertView == null){

                convertView = inflater.inflate(R.layout.custom_dialog_list_item,null);

                holder = new ViewHolder();

                holder.textView = (TextView) convertView.findViewById(R.id.name);

                convertView.setTag(holder);
            }else{

                holder = (ViewHolder) convertView.getTag();
            }

            holder.textView.setText(dataList.get(i).get(keyStr));


            return convertView;
        }




    }

    class ViewHolder{

        TextView textView;
    }


}
