package com.example.pomodoro_timer.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.databinding.ItemViewTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class ViewTaskAdapter extends RecyclerView.Adapter<ViewTaskAdapter.ViewTaskHolder> {

    //Fields
    private List<TaskModel> taskList = new ArrayList<>();
    private final ViewTaskMenuListener menuListener;

    //Interface callback
    public interface ViewTaskMenuListener{
        void onDeleteTask(TaskModel task);
    }

    //Constructor
    public ViewTaskAdapter(ViewTaskMenuListener listener){
        menuListener = listener;
    }

    //This sets the list
    public void setTasks(List<TaskModel> tasks){
        this.taskList = tasks;
        notifyDataSetChanged();
        //Log.d("TaskAdapter", "TEST SET TASKS!");
    }
    public List<TaskModel> getTaskList(){
        return taskList;
    }

    @NonNull
    @Override
    public ViewTaskAdapter.ViewTaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewTaskBinding binding = ItemViewTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewTaskHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTaskAdapter.ViewTaskHolder holder, int position) {
        holder.bind(taskList.get(position));
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public class ViewTaskHolder extends RecyclerView.ViewHolder {
        private final ItemViewTaskBinding binding;

        public ViewTaskHolder(@NonNull ItemViewTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(TaskModel task){
            binding.setTaskModel(task);
            binding.executePendingBindings();
            binding.deleteTaskBtn.setOnClickListener(v -> menuListener.onDeleteTask(task));
        }//End of bind
    }//End of ViewTaskHolder
}
