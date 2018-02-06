package com.example.geehy.hangerapplication.DialogFragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.graphics.Palette;
import android.graphics.drawable.Drawable;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;


import com.bumptech.glide.Glide;
import com.example.geehy.hangerapplication.Fragments.HomeFragment;
import com.example.geehy.hangerapplication.GrabcutActivity;
import com.example.geehy.hangerapplication.MainActivity;
import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.gridview_home.dressItem;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOError;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import static com.squareup.picasso.Picasso.*;


public class AddInfoFragment extends DialogFragment {
    private Dialog dialog;
    private View view;
    private FragmentManager manager;
    ArrayList<dressItem> mlist;
    private Button CompleteBTN;
    private Button CancelBTN;
    private Spinner spinner;
    private ImageView img;
    private int id;
    private EditText addTag;
    private EditText categorytext;
    static String imgurl;
    static String category;
    private int flagWho;
    private Bitmap bitmap;

    public static AddInfoFragment newInstance(dressItem ds) {
        AddInfoFragment addInfoFragment = new AddInfoFragment();
        //  Supply items as an argument.
        Bundle args = new Bundle();
        args.putSerializable("dress", ds);
        imgurl = ds.getImgURL();
        category = ds.getCat1();
        addInfoFragment.setArguments(args);


        return addInfoFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context c = getActivity().getApplicationContext();

        dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCanceledOnTouchOutside(false);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.activity_edit_closet, null);
        dialog.setContentView(view);
        //Log.d("AddInfoFragment", "AddinfoFragment Runing");
        /*String tempString;
        String stirng [] = tempString.split(", ");
        list = ((MainPageActivity)getActivity()).getList();*/
        //list;


        Spinner seasonSpinner = (Spinner) view.findViewById(R.id.seasonspinner);
        ArrayAdapter seasonAdapter = ArrayAdapter.createFromResource(getActivity().getApplication(),
                R.array.season, android.R.layout.simple_spinner_item);
        seasonAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        seasonSpinner.setAdapter(seasonAdapter);
        init(c);



        return dialog;


    }

    private void init(Context context) {

        CompleteBTN = (Button) view.findViewById(R.id.additem_insertbutton);
        CancelBTN = (Button) view.findViewById(R.id.cancel_button);
        categorytext = (EditText)view.findViewById(R.id.categorytext2);
        img = (ImageView) view.findViewById(R.id.additem_imagethumb);
        Glide.with(getActivity()).load("http://218.38.52.180/Android_files/"+ imgurl).into(img);
        categorytext.setText(category);
        Log.d("Here", "Runingsssssss");
/*
        //Picasso.with(context).load("http://218.38.52.180/Android_files/"+ imgurl)
        try {
            bitmap = BitmapFactory.
        }catch (IOException e){
        }

        Palette.from(bitmap)
                .generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch textSwatch = palette.getVibrantSwatch();
                        if(textSwatch == null){
                            Log.d("Here", "null Runing");
                            return;
                        }
                        categorytext.setBackgroundColor(textSwatch.getRgb());
                        Log.d("Here2", "null Runing");
                    }
                });
        // Log.d("Here", "Runing");


*/
        Event();

    }

    private void Event() {

        CompleteBTN.setOnClickListener(new View.OnClickListener() {//편집 버튼
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(), "저장완료", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });


        CancelBTN.setOnClickListener(new View.OnClickListener() {//취소 버튼
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(
                        getContext().getApplicationContext(), // 현재 화면의 제어권자
                        GrabcutActivity.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다

            }

        });


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}