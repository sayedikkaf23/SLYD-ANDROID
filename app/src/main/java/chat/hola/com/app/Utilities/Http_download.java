package chat.hola.com.app.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @since  2/2/17.
 */
public class Http_download
{
    /*
    * * A method to download json data from url
      * to details of the location address.*/
    public String downloadUrl(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try
        {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuilder sb  = new StringBuilder();
            String line;
            while( ( line = br.readLine())  != null)
            {
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
