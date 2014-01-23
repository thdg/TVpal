package is.handlers;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

/**
 * Created by Arnar
 *
 * This class extends SimpleOnGestureListener and listens for a subset of all the gestures, swipes, double clicks
 * It listens for a swipe event, and implements only the onFling method which listens to a swipe event
 *
 *
 * @see is.handlers.SwipeGestureFilter.SimpleGestureListener
 */
public class SwipeGestureFilter extends SimpleOnGestureListener
{
    public final static int SWIPE_LEFT  = 3;
    public final static int SWIPE_RIGHT = 4;

    public final static int MODE_SOLID       = 1;
    public final static int MODE_DYNAMIC     = 2;

    private final static int ACTION_FAKE = -13; //just an unlikely number

    private int mode             = MODE_DYNAMIC;
    private boolean running      = true;
    private boolean tapIndicator = false;

    private static final int SWIPE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    private GestureDetector detector;
    private SimpleGestureListener listener;

    public SwipeGestureFilter(Activity context,SimpleGestureListener sgl)
    {

        this.detector = new GestureDetector(context, this);
        this.listener = sgl;
    }

    public void onTouchEvent(MotionEvent event)
    {
        if(!this.running)
            return;

        boolean result = this.detector.onTouchEvent(event);

        if(this.mode == MODE_SOLID)
            event.setAction(MotionEvent.ACTION_CANCEL);
        else if (this.mode == MODE_DYNAMIC) {

            if(event.getAction() == ACTION_FAKE)
                event.setAction(MotionEvent.ACTION_UP);
            else if (result)
                event.setAction(MotionEvent.ACTION_CANCEL);
            else if(this.tapIndicator){
                event.setAction(MotionEvent.ACTION_DOWN);
                this.tapIndicator = false;
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
    {
        try
        {
            float diffY = e2.getY() - e1.getY();
            float diffX = e2.getX() - e1.getX();

            if (Math.abs(diffX) > Math.abs(diffY))
            {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD)
                {
                    if (diffX > 0)
                    {
                        this.listener.onSwipe(SWIPE_RIGHT);
                    }
                    else
                    {
                        this.listener.onSwipe(SWIPE_LEFT);
                    }
                }
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return false;
    }

    //MainActivity implements this method
    public static interface SimpleGestureListener
    {
        void onSwipe(int direction);
    }
}