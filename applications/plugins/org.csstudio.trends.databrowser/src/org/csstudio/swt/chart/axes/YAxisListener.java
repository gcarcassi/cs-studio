package org.csstudio.swt.chart.axes;

/** Listener for YAxis.
 *  @see YAxis
 *  @author Kay Kasemir
 */
public interface YAxisListener
{
    /** The axis label changed.
     *  Requires redraw of the axis, but since the size of the axis
     *  might be affected when the label requires a different number
     *  of text lines, this can also require a whole graph redraw.
     */
    public static final int LABEL = 0;
    
    /** The axis was selected or de-selected.
     *  Requires redraw of the axis and maybe the grid,
     *  i.e. typically the whole plot.
     */
    public static final int SELECTION = 1;
    
    /** A marker was added or removed.
     *  Requires redraw of the graph.
     */
    public static final int MARKER = 2;
    
    /** The axis range changed.
     *  Requires redraw of the whole plot.
     */
    public static final int RANGE = 3;
    
    /** Something changed on the Y Axis
     *  @param what One of LABEL, SELECTION, ...
     *  @param yaxis The axis that issued the event
     */ 
    public void changedYAxis(int what, YAxis yaxis);
}
