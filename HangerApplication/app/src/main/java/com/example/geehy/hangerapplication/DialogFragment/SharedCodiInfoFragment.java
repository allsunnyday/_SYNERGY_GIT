package com.example.geehy.hangerapplication.DialogFragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.example.geehy.hangerapplication.R;
import com.example.geehy.hangerapplication.gridview_home.dressItem;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


public class SharedCodiInfoFragment extends DialogFragment {

    private Dialog dialog;
    private SharedPreferences appData;
    private View view;
    private GridView gridView;
    private ArrayList<dressItem> list;
    private ShareCodiAdapter shareCodiAdapter;
    private String codiOwner;
    private String codipath;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Bundle mbundle = getArguments();

        codiOwner = mbundle.getString("Codiname"); //코디의 주인
        codipath = mbundle.getString("codipath");  //코디의 path



        dialog = super.onCreateDialog(savedInstanceState);
        appData = this.getActivity().getSharedPreferences("appData", MODE_PRIVATE);     //설정값을 가지고 온다
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.fragment_shared_codi_info,null);

        gridView = (GridView)view.findViewById(R.id.share_codi_gridview);
        shareCodiAdapter = new ShareCodiAdapter(getActivity(),R.layout.item_share_codi_gridview, list);

        gridView.setAdapter(shareCodiAdapter);
        dialog.setContentView(view);
        return dialog;
    }

    public class ShareCodiAdapter extends BaseAdapter{
        LayoutInflater layoutInflater;
        private Context context;
        private int layout;
        private ArrayList<dressItem> list;

        public ShareCodiAdapter(Context context,int layout, ArrayList<dressItem> list){
            this.context = context;
            this.layout = layout;
            this.list = list;
            layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            //가져올 코디의 개수 ?
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null) {

                convertView = layoutInflater.inflate(layout, null);
            }


            return null;
        }
    }
}
