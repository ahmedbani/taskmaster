package com.example.myapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{
    List<Task> allTasks = new ArrayList<Task>();
    OnTaskListener mOnTaskListener;

    public TaskAdapter(List<Task> allTasks, OnTaskListener onTaskListener) {
        this.allTasks = allTasks;
        this.mOnTaskListener=onTaskListener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task,parent,false);
        TaskViewHolder taskViewHolder = new TaskViewHolder(view,mOnTaskListener);
        return taskViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.task = allTasks.get(position);
        TextView title = holder.itemView.findViewById(R.id.taskTitle);
        TextView body = holder.itemView.findViewById(R.id.taskBody);
        TextView state = holder.itemView.findViewById(R.id.taskState);

        title.setText(holder.task.getTitle());
        body.setText(holder.task.getBody());
        state.setText(holder.task.getState());

    }

    @Override
    public int getItemCount() {
        return allTasks.size();
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Task task;
        public View itemView;
        public OnTaskListener onTaskListener;

        public TaskViewHolder(@NonNull View itemView, OnTaskListener onTaskListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.onTaskListener = onTaskListener;
            this.itemView=itemView;
        }

        @Override
        public void onClick(View view) {
            onTaskListener.onTaskClick(getAdapterPosition(),task);
        }
    }
    public interface OnTaskListener{
        void onTaskClick(int position,Task task);
    }
}
