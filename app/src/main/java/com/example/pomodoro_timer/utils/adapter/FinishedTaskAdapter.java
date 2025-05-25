package com.example.pomodoro_timer.utils.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.databinding.ItemFinishedTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class FinishedTaskAdapter extends RecyclerView.Adapter<FinishedTaskAdapter.FinishedTaskViewHolder> {

    //Fields
    private List<TaskModel> finishedTaskList = new ArrayList<>();
    private final FinishedTaskItemMenuListener menuListener;

    //Interface for callback
    public interface FinishedTaskItemMenuListener{
        void onReAdd(TaskModel task);
    }

    //Constructor
    public FinishedTaskAdapter(FinishedTaskItemMenuListener listener){
        menuListener = listener;
    }

    //This sets the list
    public void setTasks(List<TaskModel> tasks){
        this.finishedTaskList = tasks;
        notifyDataSetChanged();
    }

    public List<TaskModel> getTaskList(){
        return finishedTaskList;
    }

    @NonNull
    @Override
    public FinishedTaskAdapter.FinishedTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFinishedTaskBinding binding = ItemFinishedTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new FinishedTaskAdapter.FinishedTaskViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedTaskAdapter.FinishedTaskViewHolder holder, int position) {
        holder.bind(finishedTaskList.get(position));
    }

    @Override
    public int getItemCount() {
        return finishedTaskList.size();
    }

    public class FinishedTaskViewHolder extends RecyclerView.ViewHolder{

        //Fields
        private final ItemFinishedTaskBinding binding;

        public FinishedTaskViewHolder(ItemFinishedTaskBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(TaskModel task) {
            binding.setTaskModel(task);
            binding.executePendingBindings();
            binding.reAddFinishedTaskIconId.setOnClickListener(v -> menuListener.onReAdd(task));
        }
    }//End of FinishedTaskViewHolder
}
