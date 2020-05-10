package edu.umkc.lvp4b.budgettracker.ui.charts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PieChartView extends View {
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint slicePaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint slicePaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PieChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        textPaint.setStyle((Paint.Style.FILL));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);

        slicePaintStroke.setStyle(Paint.Style.STROKE);
        slicePaintStroke.setColor(Color.BLACK);
        slicePaintStroke.setStrokeWidth(3);

        slicePaintFill.setStyle(Paint.Style.FILL);
    }

    private List<SliceData> slices = new ArrayList<>();

    public void setSlices(List<Slice> slices) {
        this.slices = slices.stream().map(SliceData::new).collect(Collectors.toList());
        this.slices.sort(Comparator.comparingDouble(Slice::getValue));

        final int[] colors = new int[]{ Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW,
            Color.MAGENTA, Color.CYAN, Color.WHITE};
        for (int i = 0; i < this.slices.size(); i++) {
            this.slices.get(i).color = colors[i % colors.length];
        }

        double total = this.slices.stream().mapToDouble(Slice::getValue).sum();
        this.slices.forEach(slice -> slice.update(total));

        invalidate();
    }

    private Point center = new Point();
    private RectF circle = new RectF();

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        center.set(w / 2, h / 2);
        circle.set(0, 0, w, h);
        circle.inset(2, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float angle = 0f;
        for (SliceData slice : slices) {
            slicePaintFill.setColor(slice.color);
            canvas.drawArc(circle, angle, slice.angle, true, slicePaintFill);
            canvas.drawArc(circle, angle, slice.angle, true, slicePaintStroke);

            double centerAngle = (angle + slice.angle / 2) * Math.PI / 180;
            float textX = (float) (Math.cos(centerAngle) * center.x * 0.7) + center.x;
            float textY = (float) (Math.sin(centerAngle) * center.y * 0.7) + center.y;
            textPaint.setColor(slice.textColor);
            canvas.drawText(slice.label, textX, textY, textPaint);

            angle += slice.angle;
        }
    }

    public static class Slice {
        protected final double value;
        protected final String label;

        public Slice(double value, String label) {
            this.value = value;
            this.label = label;
        }

        public double getValue() {
            return value;
        }
    }

    private class SliceData extends Slice {
        float angle;
        int color;
        int textColor;

        SliceData(Slice slice){
            super(slice.value, slice.label + " " + NumberFormat.getCurrencyInstance().format(slice.value));
        }

        void update(double total) {
            angle = (float) (value / total * 360);
            textColor = Color.luminance(color) > 0.5 ? Color.BLACK : Color.WHITE;
        }
    }
}
