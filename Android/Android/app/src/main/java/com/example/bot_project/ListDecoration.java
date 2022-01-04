package com.example.bot_project;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ListDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if(parent.getChildAdapterPosition(view)!= parent.getAdapter().getItemCount()-1){//아이템이 마지막이 아닌경우 공백 30을 줌
            outRect.right =80;
            outRect.left = 80;
        }
    }
}
