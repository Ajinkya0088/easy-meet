package com.nerds.easymeet.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nerds.easymeet.R;
import com.nerds.easymeet.activities.TaskDetailActivity;
import com.nerds.easymeet.data.Constants;
import com.nerds.easymeet.data.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedTasksFragment extends Fragment {

    private View view;
    private List<DocumentSnapshot> tasksData;
    private FirebaseFirestore db;
    private ArrayList<Task> tasks;

    public AssignedTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_users_tasks, container, false);
        db = FirebaseFirestore.getInstance();
        tasks = new ArrayList<>();
        RecyclerView recyclerView = view.findViewById(R.id.tasks_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        String userid = PreferenceManager.getDefaultSharedPreferences(getContext()).getString(Constants.USER_ID, "");
        db.collection(Constants.TASKS_COLLECTION).whereArrayContains("assigner_id", userid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    tasksData = documentSnapshot.getDocuments();
                    for (DocumentSnapshot document : tasksData) {
                        tasks.add(document.toObject(Task.class));
                    }
                    recyclerView.setAdapter(new TasksAdapter());
                });
        return view;
    }

    private class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

        @NonNull
        @Override
        public TasksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new TasksAdapter.ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.task_card_view, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull TasksAdapter.ViewHolder holder, int position) {
            holder.task.setText(tasks.get(position).task);
            db.collection(Constants.USERS_COLLECTION).document(tasks.get(position).assigner_id)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        holder.assigner.setText(String.format("Assigned By %s", documentSnapshot.getData().get("name")));
                    });
        }

        @Override
        public int getItemCount() {
            return tasks.size();
        }

        private class ViewHolder extends RecyclerView.ViewHolder {
            TextView assigner, task;

            private ViewHolder(@NonNull View itemView) {
                super(itemView);
                task = itemView.findViewById(R.id.tasks);
                assigner = itemView.findViewById(R.id.assigner);
                itemView.setOnClickListener(v -> {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TASK_INTENT_EXTRA, tasks.get(getAdapterPosition()));
                    Intent intent = new Intent(getContext(), TaskDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                });
            }
        }
    }

}
