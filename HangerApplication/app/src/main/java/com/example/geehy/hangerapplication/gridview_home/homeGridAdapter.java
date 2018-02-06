package com.example.geehy.hangerapplication.gridview_home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;

import java.io.IOException;
import java.util.ArrayList;

public class homeGridAdapter extends BaseAdapter {
    private Context context;
    private int layout; //아이템 레이아웃 정보ㅓ
    private ArrayList<dressItem> list=null;
    LayoutInflater inf;

    public homeGridAdapter() {
        this.context = null;
        this.layout = 0;
        this.list = null;
    }

    public homeGridAdapter(Context context, int layout, ArrayList<dressItem> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;

        inf=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() { return 1;}

    @Override
    public Object getItem(int postion) {
        return list.get(postion);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int postion, View convertView, ViewGroup viewGroup) {
        if(convertView == null){
            convertView = inf.inflate(layout, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.homeitem);

        final dressItem di = list.get(postion);
        TextView tv = (TextView) convertView.findViewById(R.id.item_clothesname);

        String tempString = "";




        tempString+= ("카테고리 : " + di.getCat1() + " > \n");
        tempString+= ("옷 색깔: " + di.getDressColor()+ "\n" );
        tempString+= ("계절 : ");
        int[] tempSeason = di.getSeason();
        for(int i = 0 ; i< tempSeason.length ; i++){
            if(tempSeason[i] == -1){
                tempString+= ("미정 ");
            }else if(tempSeason[i] ==0){
                tempString+= ("봄");
            }else if(tempSeason[i] ==1){
                tempString+= ("여름");
            }else if(tempSeason[i] ==2){
                tempString+= ("가을");
            }else if(tempSeason[i] ==3){
                tempString+= ("겨울");
            }

            if(i != tempSeason.length -1){
                tempString += ", ";
            }
        }
        tempString += "\n";
        tempString += ("해쉬태그: " + di.getDressTag()+ "\n" );

        tv.setText(tempString);
        ImageView iv = (ImageView) convertView.findViewById(R.id.homeitem);


        if(di.getImgURL().equals("") || di.getImgURL() == null){
            imageView.setImageResource(R.drawable.tempimg);
        }else {
                //배치해놓은 ImageView에 set
                Glide.with(this.context).load("http://218.38.52.180/Android_files/"+di.getImgURL()).into(imageView);//보여줄 이미지

        }

        /*imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "옷 명칭 : " + di.getDressName(), Toast.LENGTH_SHORT).show();
                Log.d("Home Gride Item", "Select Postion : " + postion +" / Select Dress Name : " + di.getDressName());
            }
        });*/

        return convertView;
    }
}
