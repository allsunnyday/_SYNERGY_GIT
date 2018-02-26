//날씨 메뉴의 viewPager Adapter

package com.example.geehy.hangerapplication.gridview_home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.R;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class AutoScrollAdapter extends PagerAdapter {
    int likes;
    Context context;
    ArrayList<CoordyItem> data;
    TextView likeNum;
    boolean islike = false;

    public AutoScrollAdapter(Context context, ArrayList<CoordyItem> data) {
        this.context = context;
        this.data = data;
    }

//    public AutoScrollAdapter(Context context, ArrayList<CoordyItem> codi){
//
//    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_viewpager,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.image);
        TextView textView = (TextView) v.findViewById(R.id.whoesCodi);
        final ImageButton imgbtn = (ImageButton)v.findViewById(R.id.imgbtn);
        likeNum= (TextView)v.findViewById(R.id.likesNum);
        //Log.d("getposition", data.get(position));
        CoordyItem ci = data.get(position);
        String codiPath = ci.getFullCodiImgURL();
        String whoesCodi = ci.getCodiName();
        likes= ci.getLikes();

        textView.setText(whoesCodi);
        if (ci.getLikes() !=0 ) {
            likeNum.setText(ci.getLikes());
        }
        else{
            imgbtn.setImageResource(R.drawable.like_blank);
        }
        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!islike) {
                    imgbtn.setImageResource(R.drawable.like_magenta);
                    likes++;
                    likeNum.setText(likes);
                    islike = true;
                }
                else{
                    imgbtn.setImageResource(R.drawable.like_blank);
                    likes--;
                    likeNum.setText(likes);
                    islike = false;
                }

            }
        });*/
//
//        imgbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imgbtn.setImageResource(R.drawable.like_magenta);
//                likes ++ ;
//                likeNum.setText(likes);
//            }
//        });

        Glide.with(context)
                .load("http://218.38.52.180/Android_files/"+ codiPath)
                .into(image_container);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
