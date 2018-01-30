/**
 * 使用JFreeChart画出折线图
 */
package com.github.wzt3309.dss.aca

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.JFreeChart
import org.jfree.chart.axis.CategoryAxis
import org.jfree.chart.axis.CategoryLabelPositions
import org.jfree.chart.axis.NumberAxis
import org.jfree.chart.axis.NumberTickUnit
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator
import org.jfree.chart.plot.CategoryPlot
import org.jfree.chart.plot.IntervalMarker
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.category.LineAndShapeRenderer
import org.jfree.chart.title.LegendTitle
import org.jfree.data.category.CategoryDataset
import org.jfree.data.general.DatasetUtilities
import org.jfree.ui.Layer
import org.jfree.ui.LengthAdjustmentType
import org.jfree.ui.RectangleEdge
import org.jfree.ui.RectangleInsets
import org.jfree.util.UnitType

import java.awt.*

/**
 * 随机产生series组数据，每组数据共24个double类型数，大小在[min,max)之间
 * --------------------------
 * |    | 类别 | 类别 | 类别 |
 * --------------------------
 * |数据1|   |   |   |   |   |
 * |数据2|   |   |   |   |   |
 * |数据3|   |   |   |   |   |
 * |数据n|   |   |   |   |   |
 * @param series    代表折线的数量，也代表类别
 * @param min       最小数据
 * @param max       最大数据
 * @return
 */
static CategoryDataset  createDataset(int series, int min, int max ) {
    def rowKeys = ('A'..'Z')[0..series - 1].collect { "demo-series-$it"} as String[]
    def colKeys = (0..23).collect {"$it:00"} as String[]
    def data = {
        (1..series).collect {
            new Random().with {
                (0..23).collect { nextDouble() * (max - min) + min}
            }
        } as double[][]
    }
    return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data())
}

/**
 * 根据数据产生折线图
 * @param categoryDataset
 * @return
 */
static JFreeChart createChart(CategoryDataset categoryDataset) {
    /* 定义图表对象 */
    JFreeChart jChart = ChartFactory.createLineChart(
            'Demo title',                  // 标题

            'Category-X',       // 横轴标题

            'Value-Y',            // 纵轴标题

            categoryDataset,                   // 数据
            PlotOrientation.VERTICAL,          // 图表方向
            true,                       // 是否显示图例

            false,                     // 是否显示工具栏
            false                         // 是否显示url
    )
    /* 生成图形，设置图形属性*/
    CategoryPlot plot = jChart.getCategoryPlot()
    plot.setBackgroundPaint(Color.WHITE)            // 设置背景色
    plot.setDomainGridlinesVisible(true)            // 设置背景网格线是否可见 - 与y轴平行
    plot.setDomainGridlinePaint(Color.BLACK)        // 设置背景网格线颜色 - 与y轴平行
    plot.setRangeGridlinePaint(Color.GRAY)          // 设置背景网格线颜色 - 与x轴平行
    plot.setNoDataMessage('null')                   // 设置缺少数据时文字显示

    /* 数据渲染，对折线操作*/
    plot.getRendererCount().times {
        LineAndShapeRenderer renderer = plot.getRenderer(it) as LineAndShapeRenderer
        renderer.with {
            setBaseItemLabelsVisible(true)                                          // 设置提示折点数据是否可见
            setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator())
            setBaseItemLabelFont(new Font('Dialog', 1, 10))         // 设置提示折点数据字体

            setBaseShapesVisible(true)             // 数据点是否可见
            setBaseLinesVisible(true)              // 数据点连线是否可见
            setBaseShapesFilled(true)              // 数据点是否填充
        }
        renderer.setSeriesPaint(it,
                new Random().with {
                    new Color(nextInt(255), nextInt(255), nextInt(255))})

    }

    /* 设置x轴 */
    CategoryAxis domainAxis = plot.domainAxis
    domainAxis.maximumCategoryLabelLines = 5                              // x轴坐标可以显示内容的最大行数
    domainAxis.categoryLabelPositions = CategoryLabelPositions.DOWN_90    // x轴坐标竖着显示

    /* 设置y轴 */
    NumberAxis valueAxis = plot.rangeAxis as NumberAxis
    valueAxis.upperMargin = 0.18D
    valueAxis.tickUnit = new NumberTickUnit(2)

    /* 设置图例 */
    LegendTitle legend = jChart.legend
    legend.position = RectangleEdge.RIGHT

    /* 图标marker部分 */
    IntervalMarker intervalMarker = new IntervalMarker(3, 8)
    intervalMarker.paint = Color.LIGHT_GRAY
    intervalMarker.labelFont = new Font('SansSerif', 1,12)
    intervalMarker.labelOffsetType = LengthAdjustmentType.EXPAND
    intervalMarker.labelOffset = new RectangleInsets(UnitType.RELATIVE,0d, 0.1d, 0d, 0d)
    intervalMarker.label = 'Normal value'
    plot.addRangeMarker(intervalMarker, Layer.BACKGROUND)

    return jChart
}

/**
 * 保存折线图在当前工作目录下，格式为jpg
 * @param chart
 * @param outPath
 * @param weight
 * @param height
 */
static void save(JFreeChart chart, String outPath, int weight, int height) {
    File file = new File(outPath)
    if (!file.isFile()) {
        file = file.toPath().resolve("tmp.${System.currentTimeMillis()}.jpg").toFile()
    }
    file.createNewFile()
    ChartUtilities.saveChartAsJPEG(file, 1f, chart, weight, height)
}


save(createChart(createDataset(2, 1, 10)),'.' ,800, 400)

