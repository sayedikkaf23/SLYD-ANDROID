package chat.hola.com.app.live_stream.utility;

import android.content.Context;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.File;

/**
 * Created by moda on 28/1/16.
 */
public class UploadAmazonS3 {

  private CognitoCachingCredentialsProvider credentialsProvider = null;
  private AmazonS3Client s3Client = null;
  private TransferUtility transferUtility = null;
  private static UploadAmazonS3 uploadAmazonS3;

  /**
   * Creating single tone object by defining private.
   * <P>
   * At the time of creating
   * </P>
   */
  private UploadAmazonS3(Context context, String canito_pool_id) {
    /**
     * Creating the object of the getCredentialProvider object. */
    credentialsProvider = getCredentialProvider(context, canito_pool_id);
    /**
     * Creating the object  of the s3Client */
    s3Client = getS3Client(context, credentialsProvider);
    s3Client.setRegion(Region.getRegion(Regions.US_WEST_2));
    s3Client.setEndpoint("https://s3-ap-southeast-1.amazonaws.com/");

    /**
     * Creating the object of the TransferUtility of the Amazone.*/
    transferUtility = getTransferUtility(context, s3Client);
  }

  public static UploadAmazonS3 getInstance(Context context, String canito_pool_id) {
    if (uploadAmazonS3 == null) {
      uploadAmazonS3 = new UploadAmazonS3(context, canito_pool_id);
      return uploadAmazonS3;
    } else {
      return uploadAmazonS3;
    }
  }

  /**
   * <h3>UploadToAmazonS3</h3>
   * <P>
   * Method is use to upload data in the amazone server.
   *
   * </P>
   */

  public void UploadToAmazonS3(final String bucketName, final File file,
      final UploadCallBack callBack) {

    if (transferUtility != null && file != null) {

      TransferObserver observer = transferUtility.upload(bucketName, file.getName(), file);

      observer.setTransferListener(new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {
          if (state.equals(TransferState.COMPLETED)) {
            callBack.sucess(
                "https://s3-ap-southeast-1.amazonaws.com/" + bucketName + "/" + file.getName());
          }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(int id, Exception ex) {
          callBack.error(id + ":" + ex.toString());
        }
      });
    } else {
      callBack.error("Amamzon s3 is not intialize or File is empty !");
    }
  }

  /**
   * This is the method to upload data in amazon s3. Here file name is passed separately
   *
   * @param bukkate_name Bucket name
   * @param fileName The name which the file is to be uploaded
   * @param file The file to be uploaded
   * @param callBack Overidden method
   */
  public void UploadDataWithName(final String bukkate_name, final String fileName, final File file,
      final UploadCallBack callBack) {

    if (transferUtility != null && file != null) {

      TransferObserver observer = transferUtility.upload(bukkate_name, fileName, file);

      observer.setTransferListener(new TransferListener() {
        @Override
        public void onStateChanged(int id, TransferState state) {
          if (state.equals(TransferState.COMPLETED)) {
            callBack.sucess(
                "https://s3-ap-southeast-1.amazonaws.com/" + bukkate_name + "/" + fileName);
          }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

        }

        @Override
        public void onError(int id, Exception ex) {

          ex.printStackTrace();
          callBack.error(id + ":" + ex.toString());
        }
      });
    } else {
      callBack.error("Amazon s3 is not intialized or the file to be uploaded is empty !");
    }
  }

  /**
   * <h2>delete_Item</h2>
   * <P>Deleting the item from aws.</P>
   *
   * @param bucketName Aws bucket name
   * @param keyName file name in the aws folder.
   */
  public void delete_Item(String bucketName, String keyName) {
    try {
        if (s3Client != null)
        // s3Client.deleteObject(bucketName,keyName);
        {
            s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        }
    } catch (AmazonServiceException ase) {
      ase.printStackTrace();
    } catch (AmazonClientException ace) {
      ace.printStackTrace();

    }
  }

  /**
   * This method is used to get the CredentialProvider and we provide only context as a parameter.
   *
   * @param context Here, we are getting the context from calling Activity.
   */
  private CognitoCachingCredentialsProvider getCredentialProvider(Context context, String pool_id) {
    if (credentialsProvider == null) {
      credentialsProvider =
          new CognitoCachingCredentialsProvider(context.getApplicationContext(), pool_id,
              // Identity Pool ID
              Regions.US_EAST_1 // Region//us-east-1
              //Regions.AP_NORTHEAST_1 // Region
          );
    }
    return credentialsProvider;
  }

  /**
   * This method is used to get the AmazonS3 Client
   * and we provide only context as a parameter.
   * and from here we are calling getCredentialProvider() function.
   *
   * @param context Here, we are getting the context from calling Activity.
   */
  private AmazonS3Client getS3Client(Context context,
      CognitoCachingCredentialsProvider cognitoCachingCredentialsProvider) {
    if (s3Client == null) {
      s3Client = new AmazonS3Client(cognitoCachingCredentialsProvider);
    }
    return s3Client;
  }

  /**
   * This method is used to get the Transfer Utility
   * and we provide only context as a parameter.
   * and from here we are, calling getS3Client() function.
   *
   * @param context Here, we are getting the context from calling Activity.
   */
  private TransferUtility getTransferUtility(Context context, AmazonS3Client amazonS3Client) {
    if (transferUtility == null) {
      transferUtility = new TransferUtility(amazonS3Client, context.getApplicationContext());
    }
    return transferUtility;
  }

  /**
   * Interface for the sucess callback fro the Amazon uploading .
   */
  public interface UploadCallBack {
    /**
     * Method for sucess .
     *
     * @param sucess it is true on sucess and false for falure.
     */
    void sucess(String sucess);

    /**
     * Method for falure.
     *
     * @param errormsg contains the error message.
     */
    void error(String errormsg);
  }
}
