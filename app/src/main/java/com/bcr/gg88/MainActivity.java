package com.bcr.gg88;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.*;
import java.util.ArrayList;

public class MainActivity extends Activity {
    ArrayList<String> history = new ArrayList<>();
    TextView stats, road;
    SharedPreferences prefs;

    void add(String x) {
        history.add(x);
        refresh();
    }

    void refresh() {
        int b = 0, p = 0, t = 0;
        StringBuilder s = new StringBuilder();

        for (String x : history) {
            if (x.equals("B")) b++;
            if (x.equals("P")) p++;
            if (x.equals("T")) t++;
            s.append(x).append("  ");
        }

        int n = history.size();
        stats.setText("Tổng: " + n +
            "\nBanker: " + b +
            " | Player: " + p +
            " | Hòa: " + t +
            "\nB: " + (n == 0 ? 0 : 100*b/n) + "%" +
            "  P: " + (n == 0 ? 0 : 100*p/n) + "%" +
            "  T: " + (n == 0 ? 0 : 100*t/n) + "%");

        road.setText(n == 0 ? "Chưa có dữ liệu" : s.toString());
        prefs.edit().putString("history",
            String.join("", history)).apply();
    }

    Button button(LinearLayout root, String label) {
        Button v = new Button(this);
        v.setText(label);
        root.addView(v);
        return v;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs = getSharedPreferences("bcr", MODE_PRIVATE);

        String saved = prefs.getString("history", "");
        for (char c : saved.toCharArray()) {
            if (c == 'B' || c == 'P' || c == 'T')
                history.add(String.valueOf(c));
        }

        ScrollView scroll = new ScrollView(this);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(1);
        root.setPadding(28, 40, 28, 28);
        root.setBackgroundColor(Color.rgb(17, 27, 43));
        scroll.addView(root);

        TextView title = new TextView(this);
        title.setText("BCR GG88 - BACCARAT");
        title.setTextSize(24);
        title.setTextColor(Color.WHITE);
        root.addView(title);

        stats = new TextView(this);
        stats.setTextColor(Color.WHITE);
        stats.setTextSize(19);
        stats.setPadding(0, 25, 0, 25);
        root.addView(stats);

        button(root, "BANKER (B)").setOnClickListener(v -> add("B"));
        button(root, "PLAYER (P)").setOnClickListener(v -> add("P"));
        button(root, "HÒA (T)").setOnClickListener(v -> add("T"));

        TextView label = new TextView(this);
        label.setText("LỊCH SỬ KẾT QUẢ");
        label.setTextColor(Color.YELLOW);
        label.setTextSize(18);
        root.addView(label);

        road = new TextView(this);
        road.setTextColor(Color.WHITE);
        road.setTextSize(18);
        root.addView(road);

        button(root, "HOÀN TÁC").setOnClickListener(v -> {
            if (!history.isEmpty()) {
                history.remove(history.size() - 1);
                refresh();
            }
        });

        button(root, "XÓA LỊCH SỬ").setOnClickListener(v ->
            new AlertDialog.Builder(this)
                .setMessage("Xóa toàn bộ kết quả?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (d, w) -> {
                    history.clear();
                    refresh();
                }).show()
        );

        setContentView(scroll);
        refresh();
    }
}
