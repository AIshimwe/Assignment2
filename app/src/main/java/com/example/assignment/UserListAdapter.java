package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Button;

import com.example.assignment.Model.StudentClass;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
    private Context context;
    private List<StudentClass> userList;
    private LayoutInflater inflater;

    public UserListAdapter(Context context, List<StudentClass> userList) {
        this.context = context;
        this.userList = userList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return userList.size();
    }

    @Override
    public Object getItem(int position) {
        return userList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.user_list_item, parent, false);
            holder = new ViewHolder();
            holder.studentIdTextView = convertView.findViewById(R.id.studentIdTextView);
            holder.nameTextView = convertView.findViewById(R.id.nameTextView);
            holder.emailTextView = convertView.findViewById(R.id.emailTextView);
            holder.viewButton = convertView.findViewById(R.id.viewButton);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudentClass user = userList.get(position);

        holder.studentIdTextView.setText("ID: " + user.getStudentId());
        holder.nameTextView.setText(user.getName());
        holder.emailTextView.setText(user.getEmail());

        holder.viewButton.setOnClickListener(v -> {
            if (context instanceof android.app.Activity) {
                android.content.Intent intent = new android.content.Intent(context, UserDetailsActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView studentIdTextView;
        TextView nameTextView;
        TextView emailTextView;
        Button viewButton;
    }
}

