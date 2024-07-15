package com.xxmassdeveloper.mpchartexample;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IFillFormatter;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.xxmassdeveloper.mpchartexample.notimportant.DemoBase;

import java.util.ArrayList;
import java.util.List;

public class CubicLineChartActivity extends DemoBase implements OnSeekBarChangeListener {

    private LineChart chart;
    private SeekBar seekBarX, seekBarY;
    private TextView tvX, tvY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        setTitle("CubicLineChartActivity");

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        seekBarX = findViewById(R.id.seekBarX);
        seekBarY = findViewById(R.id.seekBarY);

        chart = findViewById(R.id.chart1);
        chart.setViewPortOffsets(0, 0, 0, 0);
        chart.setBackgroundColor(Color.rgb(104, 241, 175));

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setMaxHighlightDistance(300);

        XAxis x = chart.getXAxis();
        x.setEnabled(false);

        YAxis y = chart.getAxisLeft();
        y.setTypeface(tfLight);
        y.setLabelCount(6, false);
        y.setTextColor(Color.WHITE);
        y.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        y.setDrawGridLines(false);
        y.setAxisLineColor(Color.WHITE);

        chart.getAxisRight().setEnabled(false);

        // add data
        seekBarY.setOnSeekBarChangeListener(this);
        seekBarX.setOnSeekBarChangeListener(this);

        // lower max, as cubic runs significantly slower than linear
        seekBarX.setMax(700);

        seekBarX.setProgress(45);
        seekBarY.setProgress(100);

        chart.getLegend().setEnabled(false);

        chart.animateXY(2000, 2000);

        // don't forget to refresh the drawing
        chart.invalidate();
    }

    private void setData(int count, float range) {
        ArrayList<Entry> values = new ArrayList<>();
        Double[] sampleValues = DataTools.Companion.getMuchValues(count);

        for (int i = 0; i < count; i++) {
            float val = (float) (sampleValues[i].floatValue() * (range + 1)) + 20;
            values.add(new Entry(i, val));
        }

        LineDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) chart.getData().getDataSetByIndex(0);
            set1.setEntries(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            // create a dataset and give it a type
            set1 = new LineDataSet(values, "DataSet 1");

            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set1.setCubicIntensity(0.2f);
            set1.setDrawFilled(true);
            set1.setDrawCircles(false);
            set1.setLineWidth(1.8f);
            set1.setCircleRadius(4f);
            set1.setCircleColor(Color.WHITE);
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setColor(Color.WHITE);
            set1.setFillColor(Color.WHITE);
            set1.setFillAlpha(100);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setFillFormatter(new IFillFormatter() {
                @Override
                public float getFillLinePosition(ILineDataSet dataSet, LineDataProvider dataProvider) {
                    return chart.getAxisLeft().getAxisMinimum();
                }
            });

            // create a data object with the data sets
            LineData data = new LineData(set1);
            data.setValueTypeface(tfLight);
            data.setValueTextSize(9f);
            data.setDrawValues(false);

            // set data
            chart.setData(data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewGithub:
                openGithubLink();
                break;
            case R.id.actionToggleValues:
                toggleValues();
                break;
            case R.id.actionToggleHighlight:
                toggleHighlight();
                break;
            case R.id.actionToggleFilled:
                toggleFilled();
                break;
            case R.id.actionToggleCircles:
                toggleCircles();
                break;
            case R.id.actionToggleCubic:
                toggleCubic();
                break;
            case R.id.actionToggleStepped:
                toggleStepped();
                break;
            case R.id.actionToggleHorizontalCubic:
                toggleHorizontalCubic();
                break;
            case R.id.actionTogglePinch:
                togglePinchZoom();
                break;
            case R.id.actionToggleAutoScaleMinMax:
                toggleAutoScale();
                break;
            case R.id.animateX:
                chart.animateX(2000);
                break;
            case R.id.animateY:
                chart.animateY(2000);
                break;
            case R.id.animateXY:
                chart.animateXY(2000, 2000);
                break;
            case R.id.actionSave:
                handleSave();
                break;
        }
        return true;
    }

    private void openGithubLink() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("https://github.com/AppDevNext/AndroidChart/blob/master/MPChartExample/src/main/java/com/xxmassdeveloper/mpchartexample/CubicLineChartActivity.java"));
        startActivity(i);
    }

    private void toggleValues() {
        for (IDataSet set : chart.getData().getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }
        chart.invalidate();
    }

    private void toggleHighlight() {
        if (chart.getData() != null) {
            chart.getData().setHighlightEnabled(!chart.getData().isHighlightEnabled());
            chart.invalidate();
        }
    }

    private void toggleFilled() {
        toggleDataSetProperty(LineDataSet::isDrawFilledEnabled, LineDataSet::setDrawFilled);
    }

    private void toggleCircles() {
        toggleDataSetProperty(LineDataSet::isDrawCirclesEnabled, LineDataSet::setDrawCircles);
    }

    private void toggleCubic() {
        toggleDataSetMode(LineDataSet.Mode.CUBIC_BEZIER, LineDataSet.Mode.LINEAR);
    }

    private void toggleStepped() {
        toggleDataSetMode(LineDataSet.Mode.STEPPED, LineDataSet.Mode.LINEAR);
    }

    private void toggleHorizontalCubic() {
        toggleDataSetMode(LineDataSet.Mode.HORIZONTAL_BEZIER, LineDataSet.Mode.LINEAR);
    }

    private void togglePinchZoom() {
        chart.setPinchZoom(!chart.isPinchZoomEnabled());
        chart.invalidate();
    }

    private void toggleAutoScale() {
        chart.setAutoScaleMinMaxEnabled(!chart.isAutoScaleMinMaxEnabled());
        chart.notifyDataSetChanged();
    }

    private void handleSave() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            saveToGallery();
        } else {
            requestStoragePermission(chart);
        }
    }

    private void toggleDataSetProperty(TogglePropertyCheck check, TogglePropertySet set) {
        for (ILineDataSet iSet : chart.getData().getDataSets()) {
            LineDataSet set1 = (LineDataSet) iSet;
            set.accept(set1, !check.check(set1));
        }
        chart.invalidate();
    }

    private void toggleDataSetMode(LineDataSet.Mode mode, LineDataSet.Mode alternateMode) {
        for (ILineDataSet iSet : chart.getData().getDataSets()) {
            LineDataSet set1 = (LineDataSet) iSet;
            set1.setMode(set1.getMode() == mode ? alternateMode : mode);
        }
        chart.invalidate();
    }

    @FunctionalInterface
    private interface TogglePropertyCheck {
        boolean check(LineDataSet set);
    }

    @FunctionalInterface
    private interface TogglePropertySet {
        void accept(LineDataSet set, boolean enabled);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        tvX.setText(String.valueOf(seekBarX.getProgress()));
        tvY.setText(String.valueOf(seekBarY.getProgress()));
        setData(seekBarX.getProgress(), seekBarY.getProgress());
        chart.invalidate();
    }

    @Override
    protected void saveToGallery() {
        saveToGallery(chart, "CubicLineChartActivity");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {}

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {}
}
