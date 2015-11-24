package com.ort.num172159_180968.fut5;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.EdgeDetail;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.charts.SeriesLabel;
import com.hookedonplay.decoviewlib.events.DecoEvent;

public class Statistics extends AppCompatActivity {
    final private int COLOR_ORANGE = Color.parseColor("#FFA500");
    final private int COLOR_GREEN = Color.parseColor("#32CD32");
    final private int COLOR_RED = Color.parseColor("#B22222");
    final private int COLOR_BACK = Color.parseColor("#0166BB66");
    float mSeriesMax = 100f;
    private int mSeriesWin;
    private int mSeriesTie;
    private int mSeriesLoss;
    private int mBack1Index;
    private int win;
    private int tie;
    private int loss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        win = 42;
        tie = 4;
        loss = 31;
        mSeriesMax = win + tie + loss;

        DecoView decoView = (DecoView)findViewById(R.id.dynamicArcView);
        float circleInset = getDimension(23) - (getDimension(46) * 0.3f);
        SeriesItem seriesBack1Item = new SeriesItem.Builder(COLOR_BACK)
                .setRange(0, mSeriesMax, mSeriesMax)
                .setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .setInset(new PointF(circleInset, circleInset))
                .build();

        mBack1Index = decoView.addSeries(seriesBack1Item);

        SeriesItem series1Item = new SeriesItem.Builder(COLOR_RED)
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46))
                .setSeriesLabel(new SeriesLabel.Builder("LOSS: " + loss).build())
                .setCapRounded(false)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_RED, 0.3f))
                .setShowPointWhenEmpty(false)
                .build();
        mSeriesLoss = decoView.addSeries(series1Item);

        SeriesItem series2Item = new SeriesItem.Builder(COLOR_ORANGE)
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46))
                .setSeriesLabel(new SeriesLabel.Builder("TIE: " + tie).build())
                .setCapRounded(false)
                        //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_ORANGE, 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        mSeriesTie = decoView.addSeries(series2Item);

        SeriesItem series3Item = new SeriesItem.Builder(COLOR_GREEN)
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .setLineWidth(getDimension(46))
                .setSeriesLabel(new SeriesLabel.Builder("WIN: " + win).build())
                .setCapRounded(false)
                        //.setChartStyle(SeriesItem.ChartStyle.STYLE_PIE)
                .addEdgeDetail(new EdgeDetail(EdgeDetail.EdgeType.EDGE_INNER, COLOR_GREEN, 0.3f))
                .setShowPointWhenEmpty(false)
                .build();

        mSeriesWin = decoView.addSeries(series3Item);

        addAnimation(decoView, mSeriesLoss, loss, 3000, COLOR_RED);
        addAnimation(decoView, mSeriesTie, tie, 11000, COLOR_ORANGE);
        decoView.addEvent(new DecoEvent.Builder(loss + tie)
                .setIndex(mSeriesLoss)
                .setDelay(11000)
                .setDuration(5000)
                .build());
        addAnimation(decoView, mSeriesWin, win, 19000, COLOR_GREEN);
        decoView.addEvent(new DecoEvent.Builder(tie + win)
                .setIndex(mSeriesTie)
                .setDelay(19000)
                .setDuration(5000)
                .build());
        decoView.addEvent(new DecoEvent.Builder(loss + tie + win)
                .setIndex(mSeriesLoss)
                .setDelay(19000)
                .setDuration(5000)
                .build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected float getDimension(float base) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, base, getResources().getDisplayMetrics());
    }

    private void addAnimation(final DecoView arcView,
                              int series, float moveTo, int delay,
                              final int color) {
        DecoEvent.ExecuteEventListener listener = new DecoEvent.ExecuteEventListener() {
            @Override
            public void onEventStart(DecoEvent event) {
                //imageView.setImageDrawable(ContextCompat.getDrawable(getActivity(), imageId));

                //showAvatar(true, imageView);

                arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_COLOR_CHANGE, color)
                        .setIndex(mBack1Index)
                        .setDuration(2000)
                        .build());
            }

            @Override
            public void onEventEnd(DecoEvent event) {
                //showAvatar(false, imageView);

                arcView.addEvent(new DecoEvent.Builder(DecoEvent.EventType.EVENT_COLOR_CHANGE, COLOR_BACK)
                        .setIndex(mBack1Index)
                        .setDuration(2000)
                        .build());
            }

        };

        arcView.addEvent(new DecoEvent.Builder(moveTo)
                .setIndex(series)
                .setDelay(delay)
                .setDuration(5000)
                .setListener(listener)
                .build());
    }
}
