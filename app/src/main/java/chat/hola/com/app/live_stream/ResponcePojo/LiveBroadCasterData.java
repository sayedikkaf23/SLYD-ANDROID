package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by moda on 12/3/2018.
 */
public class LiveBroadCasterData implements Serializable
{
   private ArrayList<AllStreamsData>streams;

    public ArrayList<AllStreamsData> getStreams() {
        return streams;
    }


}
