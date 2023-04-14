package chat.hola.com.app.Networking.connection;

import android.content.Context;

/**
 * @since  12/20/2017.
 * @author 3Embed.
 * @version 1.0.
 */
public class NetworkTimer extends Thread
{
    private Context appContext;
    private int SLEEP_TIME=1000;
    public NetworkTimer(Context context)
    {
        appContext=context.getApplicationContext();
    }

    private TimerChecker timerChecker;
    public void scheduleAtFixedRate(TimerChecker temp,int sleep_time)
    {
        timerChecker=temp;
        if(sleep_time>SLEEP_TIME)
        {
            SLEEP_TIME=sleep_time;
        }
        this.start();
    }
    @Override
    public void run()
    {
        super.run();
        while(appContext!=null)
        {
            try
            {
                if(timerChecker!=null)
                {
                    timerChecker.run();
                }
                Thread.sleep(SLEEP_TIME);
            } catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        cancel();
    }


    public void cancel()
    {
        try
        {
            this.interrupt();
            this.stop();
        }catch (Exception e) {}
    }
}
