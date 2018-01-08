package com.aioute.carloan.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aioute.carloan.R;

import java.util.Calendar;


/**
 * Created by Administrator on 2017/5/2.
 */

public class DaySelectAdapter extends RecyclerView.Adapter<DaySelectAdapter.RVHolder> {

    private Context mContext;
    private LayoutInflater inflate;
    private Calendar calendar = Calendar.getInstance();
    private Calendar dayCalendar = Calendar.getInstance();
    private int cellWith;
    private Day startDay;
    private Day lastDay;
    private Day currentDay;
    private int buleColor = Color.parseColor("#FE6E47");
    private int lightBuleColor = Color.parseColor("#FFDDCC");
    private int grayColor = Color.parseColor("#F5F5F5");


    public DaySelectAdapter(Context context) {
        inflate = LayoutInflater.from(context);
        calendar.add(Calendar.YEAR, -5);
        float density = context.getResources().getDisplayMetrics().density;
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        cellWith = (int) ((widthPixels - (30 * density)) / 7);
        mContext = context;
        dayCalendar.add(Calendar.DATE, 1);
        currentDay = new Day(dayCalendar.getTime().getTime());
    }

    @Override
    public RVHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_calendar, null);
        return new RVHolder(view);
    }

    public void clearSelect() {
        startDay = null;
        lastDay = null;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return 100;
    }

    @Override
    public void onBindViewHolder(RVHolder holder, final int position) {

        TextView monthTv = (TextView) holder.getViewHolder().findViewById(R.id.item_month_tv);
        LinearLayout[] linearLayouts = {
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_one_layout),
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_two_layout),
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_three_layout),
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_four_layout),
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_five_layout),
                (LinearLayout) holder.getViewHolder().findViewById(R.id.week_six_layout)
        };
        calendar.add(Calendar.MONTH, +position);
        for (int i = 0; i < linearLayouts.length; i++) {
            linearLayouts[i].removeAllViews();
            linearLayouts[i].setVisibility(View.GONE);
        }
        monthTv.setText(calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1));
        //总天数
        int totalDayNum = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        //获取星期几   Week1 = "周天";
        int begin = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int index = 0;
        for (int i = 0; i < totalDayNum + begin; i++) {
            if (i >= begin) {
                TextView itemDay = (TextView) inflate.inflate(R.layout.item_day_layout, null);
                linearLayouts[index].addView(itemDay, cellWith, -1);
                itemDay.setText(String.valueOf(i - begin + 1));
                linearLayouts[index].setVisibility(View.VISIBLE);
                final Day day = new Day(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, i - begin + 1);
                dayCalendar.set(day.getYear(), day.getMonth() - 1, day.getDay());
                day.setTime(dayCalendar.getTime().getTime());
                if (Day.isToday(day, startDay) || Day.isToday(day, lastDay)) {
                    itemDay.setBackgroundColor(buleColor);
                    itemDay.setTextColor(Color.WHITE);
                } else if (Day.isToday(startDay, lastDay, day)) {
                    itemDay.setBackgroundColor(lightBuleColor);
                } else {
                    itemDay.setBackgroundColor(Color.TRANSPARENT);
                    itemDay.setTextColor(mContext.getResources().getColor(R.color.colorGrayText));
                }
                if (day.getTime() < currentDay.getTime()) {
                    itemDay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (startDay == null && lastDay == null) {
                                startDay = day;
//                                v.setBackgroundColor(mContext.getResources().getColor(R.color.read_bg_color));
                                if (mContext instanceof onLastDayClickListener) {
                                    ((onLastDayClickListener) mContext).clickStartDay();
                                }
                            } else if (lastDay == null && startDay.getTime() < day.getTime()) {
//                                v.setBackgroundColor(mContext.getResources().getColor(R.color.read_bg_color));
                                lastDay = day;
                                if (mContext instanceof onLastDayClickListener) {
                                    ((onLastDayClickListener) mContext).clickLastDay();
                                }
                            } else {
                                lastDay = null;
                                startDay = day;
//                                v.setBackgroundColor(mContext.getResources().getColor(R.color.read_bg_color));
                                if (mContext instanceof onLastDayClickListener) {
                                    ((onLastDayClickListener) mContext).clickAgain();
                                }
                            }
                            notifyDataSetChanged();

                        }
                    });
                } else {
                    itemDay.setBackgroundColor(grayColor);
                }


            }
            if (i % 7 == 6) {
                index++;
            }
        }
        calendar.add(Calendar.MONTH, -position);
    }

    public Day getStartDay() {
        return startDay;
    }

    public Day getLastDay() {
        return lastDay;
    }

    public static class Day {
        private int year;
        private int month;
        private int day;
        private long time;

        public static boolean isToday(Day currentDay, Day day) {
            if (day != null && day.getDay() == currentDay.getDay() && day.getMonth() == currentDay.getMonth() && day.getYear() == currentDay.getYear()) {
                return true;
            }
            return false;
        }

        public static boolean isToday(Day startDay, Day endDay, Day curDay) {
            if (startDay == null || endDay == null || curDay == null) {
                return false;
            }
            if (startDay.getTime() < curDay.getTime() && curDay.getTime() < endDay.getTime()) {
                return true;
            }
            return false;
        }

        public Day(int year, int month, int day) {
            this.year = year;
            this.day = day;
            this.month = month;
        }

        public Day(long time) {
            this.time = time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public int getYear() {
            return year;
        }

        public int getMonth() {
            return month;
        }

        public int getDay() {
            return day;
        }

        public long getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "" + year + (month < 10 ? ("0" + month) : month) + (day < 10 ? ("0" + day) : day);
        }
    }

    public static class RVHolder extends RecyclerView.ViewHolder {
        private View viewHolder;

        public RVHolder(View itemView) {
            super(itemView);
            viewHolder = itemView;
        }

        public View getViewHolder() {
            return viewHolder;
        }
    }

    public interface onLastDayClickListener {
        void clickLastDay();

        void clickStartDay();

        void clickAgain();
    }
}