package com.example.diplomproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private OnItemSelectedListener listener;
    private final LayoutInflater inflater;
    private final List<ListElem> list;
    private final Context myCtx;

    public RecyclerAdapter(Context myCtx, List<ListElem> list, OnItemSelectedListener listener) {
        this.listener = listener;
        this.inflater = LayoutInflater.from(myCtx);
        this.list = list;
        this.myCtx = myCtx;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        ListElem elem = list.get(position);

        holder.imageItem.setImageResource(elem.imageResource);
        holder.nameItem.setText(elem.name);
        holder.sizeItem.setText(elem.size);



        if(elem.type == 0){
            holder.imageItem.setImageResource(elem.imageResource);
            holder.nameItem.setText(elem.name);
            holder.sizeItem.setText(elem.size);
        }else{
            holder.imageItem.setImageResource(elem.imageResource);
            holder.nameItem.setText(elem.name);
            holder.sizeItem.setVisibility(View.INVISIBLE);
            holder.loadItemButton.setVisibility(View.INVISIBLE);
        }

        holder.loadItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = holder.getAdapterPosition();
                    listener.onLoadAction(position);
                }
            }
        });

        holder.menuItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(myCtx, holder.menuItemButton);
                popup.inflate(R.menu.item_menu);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (listener != null) {
                            int position = holder.getAdapterPosition();
                            listener.onMenuAction(position, item);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
        void onMenuAction(int position, MenuItem item);
        void onLoadAction(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageItem;
        final TextView nameItem;
        final TextView sizeItem;
        final ImageButton loadItemButton;
        final ImageButton menuItemButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageItem = itemView.findViewById(R.id.image_list_item);
            nameItem = itemView.findViewById(R.id.name_list_item);
            sizeItem = itemView.findViewById(R.id.size_list_item);
            loadItemButton = itemView.findViewById(R.id.load_list_item);
            menuItemButton = itemView.findViewById(R.id.menu_list_item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (listener != null) {
                listener.onItemSelected(position);
            }
        }
    }
}
