package com.example.pomodoro_timer.utils.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.databinding.ItemTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    //Fields
   private List<TaskModel> taskList = new ArrayList<>();

   //This sets the list
   public void setTasks(List<TaskModel> tasks){
        this.taskList = tasks;
        //Log.d("TaskAdapter", "TEST SET TASKS!");
   }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTaskBinding binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder{
       private ItemTaskBinding binding;

       public TaskViewHolder(ItemTaskBinding binding){
           super(binding.getRoot());
           this.binding = binding;
       }

       public void bind(TaskModel task){
            binding.setTaskModel(task);
            binding.executePendingBindings();
       }
   }

}
