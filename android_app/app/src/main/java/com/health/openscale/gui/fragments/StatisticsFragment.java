/* Copyright (C) 2014  olie.xdev <olie.xdev@googlemail.com>
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU General Public License as published by
*    the Free Software Foundation, either version 3 of the License, or
*    (at your option) any later version.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU General Public License for more details.
*
*    You should have received a copy of the GNU General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/

package com.health.openscale.gui.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.health.openscale.R;
import com.health.openscale.core.OpenScale;
import com.health.openscale.core.datatypes.ScaleMeasurement;
import com.health.openscale.core.datatypes.ScaleUser;
import com.health.openscale.core.utils.DateTimeHelpers;
import com.health.openscale.gui.views.BoneMeasurementView;
import com.health.openscale.gui.views.FatMeasurementView;
import com.health.openscale.gui.views.HipMeasurementView;
import com.health.openscale.gui.views.LBWMeasurementView;
import com.health.openscale.gui.views.MeasurementView;
import com.health.openscale.gui.views.MuscleMeasurementView;
import com.health.openscale.gui.views.WaistMeasurementView;
import com.health.openscale.gui.views.WaterMeasurementView;
import com.health.openscale.gui.views.WeightMeasurementView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.health.openscale.gui.views.MeasurementView.MeasurementViewMode.STATISTIC;

public class StatisticsFragment extends Fragment implements FragmentUpdateListener {

    private View statisticsView;

    private TextView txtGoalWeight;
    private TextView txtGoalDiff;
    private TextView txtGoalDayLeft;

    private TextView txtLabelGoalWeight;
    private TextView txtLabelGoalDiff;
    private TextView txtLabelDayLeft;

    private TableLayout tableWeekAveragesLayoutColumnA;
    private TableLayout tableWeekAveragesLayoutColumnB;
    private TableLayout tableMonthAveragesLayoutColumnA;
    private TableLayout tableMonthAveragesLayoutColumnB;

    private SharedPreferences prefs;
    private ScaleUser currentScaleUser;
    private ScaleMeasurement lastScaleMeasurement;

    private ArrayList <MeasurementView> viewMeasurementsListWeek;
    private ArrayList <MeasurementView> viewMeasurementsListMonth;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        statisticsView = inflater.inflate(R.layout.fragment_statistics, container, false);

        prefs = PreferenceManager.getDefaultSharedPreferences(statisticsView.getContext());

        txtGoalWeight = (TextView) statisticsView.findViewById(R.id.txtGoalWeight);
        txtGoalDiff = (TextView) statisticsView.findViewById(R.id.txtGoalDiff);
        txtGoalDayLeft = (TextView) statisticsView.findViewById(R.id.txtGoalDayLeft);

        txtLabelGoalWeight = (TextView) statisticsView.findViewById(R.id.txtLabelGoalWeight);
        txtLabelGoalDiff = (TextView) statisticsView.findViewById(R.id.txtLabelGoalDiff);
        txtLabelDayLeft = (TextView) statisticsView.findViewById(R.id.txtLabelDayLeft);

        tableWeekAveragesLayoutColumnA = (TableLayout) statisticsView.findViewById(R.id.tableWeekAveragesLayoutColumnA);
        tableWeekAveragesLayoutColumnB = (TableLayout) statisticsView.findViewById(R.id.tableWeekAveragesLayoutColumnB);
        tableMonthAveragesLayoutColumnA = (TableLayout) statisticsView.findViewById(R.id.tableMonthAveragesLayoutColumnA);
        tableMonthAveragesLayoutColumnB = (TableLayout) statisticsView.findViewById(R.id.tableMonthAveragesLayoutColumnB);

        viewMeasurementsListWeek = new ArrayList<>();

        viewMeasurementsListWeek.add(new WeightMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new WaterMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new MuscleMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new LBWMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new FatMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new BoneMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new WaistMeasurementView(statisticsView.getContext()));
        viewMeasurementsListWeek.add(new HipMeasurementView(statisticsView.getContext()));

        int i=0;

        for (MeasurementView measurement : viewMeasurementsListWeek) {
            measurement.setEditMode(STATISTIC);
            measurement.updatePreferences(prefs);

            if (measurement.isVisible()) {
                if ((i % 2) == 0) {
                    tableWeekAveragesLayoutColumnA.addView(measurement);
                } else {
                    tableWeekAveragesLayoutColumnB.addView(measurement);
                }
                i++;
            }
        }

        viewMeasurementsListMonth = new ArrayList<>();

        viewMeasurementsListMonth.add(new WeightMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new WaterMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new MuscleMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new LBWMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new FatMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new BoneMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new WaistMeasurementView(statisticsView.getContext()));
        viewMeasurementsListMonth.add(new HipMeasurementView(statisticsView.getContext()));

        i=0;

        for (MeasurementView measurement : viewMeasurementsListMonth) {
            measurement.setEditMode(STATISTIC);
            measurement.updatePreferences(prefs);

            if (measurement.isVisible()) {
                if ((i % 2) == 0) {
                    tableMonthAveragesLayoutColumnA.addView(measurement);
                } else {
                    tableMonthAveragesLayoutColumnB.addView(measurement);
                }
                i++;
            }
        }

        OpenScale.getInstance(getContext()).registerFragment(this);

        return statisticsView;
    }

    @Override
    public void updateOnView(List<ScaleMeasurement> scaleMeasurementList) {
        if (scaleMeasurementList.isEmpty()) {
            lastScaleMeasurement = new ScaleMeasurement();
        } else {
            lastScaleMeasurement = scaleMeasurementList.get(0);
        }

        currentScaleUser = OpenScale.getInstance(getContext()).getSelectedScaleUser();

        updateStatistics(scaleMeasurementList);
        updateGoal(scaleMeasurementList);
    }

    private void updateGoal(List<ScaleMeasurement> scaleMeasurementList) {
        ScaleMeasurement goalScaleMeasurement = new ScaleMeasurement();
        goalScaleMeasurement.setConvertedWeight(currentScaleUser.getGoalWeight(), currentScaleUser.getScaleUnit());

        txtGoalWeight.setText(goalScaleMeasurement.getConvertedWeight(currentScaleUser.getScaleUnit()) + " " + ScaleUser.UNIT_STRING[currentScaleUser.getScaleUnit()]);

        double weight_diff = goalScaleMeasurement.getConvertedWeight(currentScaleUser.getScaleUnit()) - lastScaleMeasurement.getConvertedWeight(currentScaleUser.getScaleUnit());
        txtGoalDiff.setText(String.format("%.1f " + ScaleUser.UNIT_STRING[currentScaleUser.getScaleUnit()], weight_diff));

        Calendar goalCalendar = Calendar.getInstance();
        goalCalendar.setTime(currentScaleUser.getGoalDate());
        int days = Math.max(0, DateTimeHelpers.daysBetween(Calendar.getInstance(), goalCalendar));
        txtGoalDayLeft.setText(getResources().getQuantityString(R.plurals.label_days, days, days));

        lastScaleMeasurement.setUserId(currentScaleUser.getId());

        ScaleMeasurement goalData = new ScaleMeasurement();
        goalData.setConvertedWeight(currentScaleUser.getGoalWeight(), currentScaleUser.getScaleUnit());
        goalData.setUserId(currentScaleUser.getId());

        txtLabelGoalWeight.setText(
                Html.fromHtml(
                        getResources().getString(R.string.label_goal_weight) +
                                " <br> <font color='grey'><small>" +
                                getResources().getString(R.string.label_bmi) +
                                ": " +
                                String.format("%.1f", goalData.getBMI(currentScaleUser.getBodyHeight())) +
                                " </small></font>"
                )
        );
        txtLabelGoalDiff.setText(
                Html.fromHtml(
                        getResources().getString(R.string.label_weight_difference) +
                                " <br> <font color='grey'><small>" +
                                getResources().getString(R.string.label_bmi) +
                                ": " +
                                String.format("%.1f", lastScaleMeasurement.getBMI(currentScaleUser.getBodyHeight()) - goalData.getBMI(currentScaleUser.getBodyHeight()))  +
                                " </small></font>"
                )
        );
        txtLabelDayLeft.setText(
                Html.fromHtml(
                        getResources().getString(R.string.label_days_left) +
                                " <br> <font color='grey'><small>" +
                                getResources().getString(R.string.label_goal_date_is) +
                                " "
                                + DateFormat.getDateInstance(DateFormat.LONG).format(currentScaleUser.getGoalDate()) +
                                " </small></font>"
                )
        ); // currentScaleUser.goalDate
    }

    private void updateStatistics(List<ScaleMeasurement> scaleMeasurementList) {

        Calendar histDate = Calendar.getInstance();
        Calendar weekPastDate = Calendar.getInstance();
        Calendar monthPastDate = Calendar.getInstance();

        weekPastDate.setTime(lastScaleMeasurement.getDateTime());
        weekPastDate.add(Calendar.DATE, -7);

        monthPastDate.setTime(lastScaleMeasurement.getDateTime());
        monthPastDate.add(Calendar.DATE, -30);

        int weekSize = 0;
        int monthSize = 0;

        ScaleMeasurement averageWeek = new ScaleMeasurement();
        ScaleMeasurement averageMonth = new ScaleMeasurement();

        for (ScaleMeasurement measurement : scaleMeasurementList) {
            histDate.setTime(measurement.getDateTime());

            if (weekPastDate.before(histDate)) {
                averageWeek.add(measurement);
                weekSize++;
            }

            if (monthPastDate.before(histDate)) {
                averageMonth.add(measurement);
                monthSize++;
            }
        }

        averageWeek.divide(weekSize);
        averageMonth.divide(monthSize);

        for (MeasurementView measurement : viewMeasurementsListWeek) {
            measurement.loadFrom(averageWeek, null);
        }

        for (MeasurementView measurement : viewMeasurementsListMonth) {
            measurement.loadFrom(averageMonth, null);
        }
    }
}
