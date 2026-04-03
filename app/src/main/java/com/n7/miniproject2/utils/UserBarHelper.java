package com.n7.miniproject2.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.n7.miniproject2.R;
import com.n7.miniproject2.activities.LoginActivity;

public class UserBarHelper {
    public static final int REQUEST_CODE_LOGIN = 1;

    public static void setupUserBar(Activity activity) {
        LinearLayout layoutLoggedIn = activity.findViewById(R.id.layoutLoggedIn);
        LinearLayout layoutLoggedOut = activity.findViewById(R.id.layoutLoggedOut);

        if (layoutLoggedIn == null || layoutLoggedOut == null) return;

        TextView tvWelcomeUser = activity.findViewById(R.id.tvWelcomeUser);
        ImageView ivUserAvatar = activity.findViewById(R.id.ivUserAvatar);
        TextView btnLogout = activity.findViewById(R.id.btnLogout);
        TextView btnLoginUserBar = activity.findViewById(R.id.btnLoginUserBar);
        View btnLoginMain = activity.findViewById(R.id.btnLogin);

        String username = SharedPreferencesUtil.getString(activity, "username", null);
        String avatarUrl = SharedPreferencesUtil.getString(activity, "avatarUrl", null);

        if (username != null) {
            layoutLoggedIn.setVisibility(View.VISIBLE);
            layoutLoggedOut.setVisibility(View.GONE);
            tvWelcomeUser.setText("Xin chào, " + username);

            if (avatarUrl != null && !avatarUrl.isEmpty()) {
                Glide.with(activity)
                        .load(avatarUrl)
                        .placeholder(android.R.drawable.ic_menu_report_image)
                        .error(android.R.drawable.ic_menu_report_image)
                        .circleCrop()
                        .into(ivUserAvatar);
            }

            if (btnLoginMain != null) btnLoginMain.setVisibility(View.GONE);

            btnLogout.setOnClickListener(v -> {
                SharedPreferencesUtil.clear(activity);
                Toast.makeText(activity, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
                setupUserBar(activity);
            });
        } else {
            layoutLoggedIn.setVisibility(View.GONE);
            layoutLoggedOut.setVisibility(View.VISIBLE);

            if (btnLoginMain != null) btnLoginMain.setVisibility(View.VISIBLE);

            btnLoginUserBar.setOnClickListener(v -> {
                Intent intent = new Intent(activity, LoginActivity.class);
                activity.startActivityForResult(intent, REQUEST_CODE_LOGIN);
            });
        }
    }

    public static boolean isUserLoggedIn(Activity activity) {
        String username = SharedPreferencesUtil.getString(activity, "username", null);
        return username != null;
    }
}