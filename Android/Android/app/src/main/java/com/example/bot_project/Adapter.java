package com.example.bot_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private  ArrayList<String> iotlist;
    private  ArrayList<String> devicekey;
    private Context context;
    private  View.OnClickListener onClickListener;
    private itemClick itemclick = null;

    public Adapter(Context context, ArrayList<String> itemlist, ArrayList<String>devicekey){
        this.context = context;
        this.iotlist =itemlist;
        this.devicekey = devicekey;
    }

    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.item_btn, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView iotimage;

        public ViewHolder(View itemView){
            super(itemView);
            iotimage = itemView.findViewById(R.id.item_btn);

         itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(itemclick != null){
                            itemclick.onItemClickListener(v,pos,iotlist.get(pos), devicekey.get(pos));
                        }
                    }
                }
            });

        }

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        String item = iotlist.get(position);

        if(item.equals("lamp")){
            holder.iotimage.setImageResource(R.drawable.iotact);

        }else if(item.equals("gas")){
            holder.iotimage.setImageResource(R.drawable.gas);

        }else if(item.equals("tmp")){
            holder.iotimage.setImageResource(R.drawable.thermometer);

        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

    }



    @Override
    public int getItemCount() {
        return iotlist.size();
    }

    public void setOnClickListener(itemClick listener){
        this.itemclick = listener;
    }

    public interface itemClick{
        void onItemClickListener(View v, int position, String iotname, String devicekey);
    }



}
