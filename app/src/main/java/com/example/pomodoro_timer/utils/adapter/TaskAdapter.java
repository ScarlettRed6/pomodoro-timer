package com.example.pomodoro_timer.utils.adapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pomodoro_timer.R;
import com.example.pomodoro_timer.databinding.ItemTaskBinding;
import com.example.pomodoro_timer.model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    //Fields
   private List<TaskModel> taskList = new ArrayList<>();
   private TaskItemMenuListener menuListener;

   //Interface for callback
    public interface TaskItemMenuListener{
        void onEditTask(TaskModel task);
        void onDeleteTask(TaskModel task);
   }

   //Constructor
   public TaskAdapter(TaskItemMenuListener listener){
        menuListener = listener;
   }


   //This sets the list
   public void setTasks(List<TaskModel> tasks){
        this.taskList = tasks;
        //Log.d("TaskAdapter", "TEST SET TASKS!");
   }
   public List<TaskModel> getTaskList(){
        return taskList;
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

    public class TaskViewHolder extends RecyclerView.ViewHolder{
       private ItemTaskBinding binding;

       public TaskViewHolder(ItemTaskBinding binding){
           super(binding.getRoot());
           this.binding = binding;
       }

       public void bind(TaskModel task){
            binding.setTaskModel(task);
            binding.executePendingBindings();

            binding.taskOptionsMenuId.setOnClickListener(v -> {
                Context wrapperTheme = new ContextThemeWrapper(v.getContext(), R.style.PopupMenuStyle);
                PopupMenu popupMenu = new PopupMenu(wrapperTheme, v);
                popupMenu.inflate(R.menu.task_item_options_menu);
                popupMenu.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.edit_task) {
                        //ThisHandle edit task
                        menuListener.onEditTask(task);
                        //Log.d("TaskAdapter", "EDIT TASKED CLICKED!");
                        return true;
                    } else if (itemId == R.id.delete_task) {
                        //This Handle delete task
                        menuListener.onDeleteTask(task);
                        //Log.d("TaskAdapter", "DELETE TASK CLICKED!");
                        return true;
                    }
                    return false;
                });//End of setOnMenuItemClickListener
                popupMenu.show();
            });
       }//End of bind
   }//End of TaskViewHolder

}
