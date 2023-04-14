package chat.hola.com.app.live_stream.ResponcePojo;


import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 02 August 2019
 */
public class StreamRestartEvent implements Serializable {



/*{
            "streamID":"5ccd313d66023fa1d552dcd4",
            "message":"0",

    }*/


        private String message;
        private String streamID;


        public String getMessage() {
            return message;
        }


        public String getStreamID() {
            return streamID;
        }


    }
