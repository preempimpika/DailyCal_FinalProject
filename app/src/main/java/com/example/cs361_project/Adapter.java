package com.example.cs361_project;

import static com.example.cs361_project.DBHelper.TABLENAME;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    ArrayList<AddMealData>mealArrayList = new ArrayList<>();
    SQLiteDatabase sqLitedatabase;
    int custom_viewmain;
    OnItemDeletedListener onItemDeletedListener;

    public Adapter(Context context, int custom_viewmain, ArrayList<AddMealData> mealArrayList, SQLiteDatabase sqLitedatabase) {
        this.context = context;
        this.mealArrayList = mealArrayList;
        this.sqLitedatabase = sqLitedatabase;
        this.custom_viewmain = custom_viewmain;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.custom_viewmain,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AddMealData addMealData = mealArrayList.get(position);
        holder.foodname.setText(addMealData.getFoodname());
        holder.calories.setText(addMealData.getCalories());

        holder.flowmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.flowmenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if(itemId == R.id.edit_menu){
                            //edit operation
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",addMealData.getId());
                            bundle.putString("foodname",addMealData.getFoodname());
                            bundle.putString("calories",addMealData.getCalories());
                            Intent intent = new Intent(context,AddMealActivity.class);
                            intent.putExtra("mealdata",bundle);
                            context.startActivity(intent);

                        } else if(itemId == R.id.delete_menu){
                            //delete operation
                            DBHelper dbHelper = new DBHelper(context);
                            sqLitedatabase = dbHelper.getReadableDatabase();
                            long recdelete = sqLitedatabase.delete(TABLENAME, "id="+addMealData.getId(),null);
                            mealArrayList.remove(position);
                            notifyDataSetChanged();
                            onItemDeletedListener.onItemDeleted();

                            if(recdelete!=1){
                                Toast.makeText(context,"data deleted", Toast.LENGTH_SHORT).show();
                                //remove position after deleted
                                //mealArrayList.remove(position);
                                //update data
                                //notifyDataSetChanged();

                            }
                        }
                        return false;
                        /*switch (menuItem.getItemId()){
                            case R.id.edit_menu:
                                //edit opearation
                                break;
                            case R.id.delete_menu:
                                //delete operation
                                break;
                            default:
                                return false;
                        }*/
                    }
                });
                //display menu
                popupMenu.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return mealArrayList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView foodname,calories;
        ImageButton flowmenu;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodname = itemView.findViewById(R.id.foodname_input);
            calories = itemView.findViewById(R.id.calories_input);
            flowmenu = itemView.findViewById(R.id.flowmenu);
        }
    }
   public void setOnItemDeletedListener(Object object) {
        onItemDeletedListener = (OnItemDeletedListener) object;
    }
}
