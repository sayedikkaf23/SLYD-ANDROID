package chat.hola.com.app.Utilities.aws;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferNetworkLossHandler;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.File;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.models.CongnitoResponse;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * Created by Ali on 28/1/16.
 */
public class UploadFileAmazonS3 {

    private static UploadFileAmazonS3 uploadAmazonS3;
    private CognitoCachingCredentialsProvider credentialsProvider = null;
    private AmazonS3Client s3Client = null;
    private TransferUtility transferUtility = null;
    private DeveloperAuthenticationProvider developerProvider;

    /**
     * Creating single tone object by defining private.
     * <p>
     * At the time of creating
     * </P>
     */
    public UploadFileAmazonS3(Context context) {
        /**
         * Creating the object of the getCredentialProvider object. */
        /* credentialsProvider=getCredentialProvider(context,canito_pool_id);
         *//**
         * Creating the object  of the s3Client *//*
        s3Client=getS3Client(context,credentialsProvider);
        s3Client.setRegion(Region.getRegion(Regions.AP_SOUTHEAST_1));
        s3Client.setEndpoint("https://s3-ap-southeast-1.amazonaws.com/");

        *//**
         * Creating the object of the TransferUtility of the Amazone.*//*
        transferUtility=getTransferUtility(context,s3Client);*/

    }

    public static UploadFileAmazonS3 getInstance(Context context) {
        if (uploadAmazonS3 == null) {
            uploadAmazonS3 = new UploadFileAmazonS3(context);
        }
        return uploadAmazonS3;
    }

    /**
     * <h3>Upload_data</h3>
     * <p>
     * Method is use to upload data in the amazone server.
     *
     * </P>
     */

    public void Upload_data(Context context, final String folder, final File file, final UploadCallBack callBack) {

        getCognitoToken(context, folder, file, callBack);
    }


    public void getCognitoToken(Context context, String folder, File file, UploadCallBack callBack) {

        HowdooService service = ServiceFactory.createRetrofitService(HowdooService.class);

        Observable<Response<CongnitoResponse>> obsResponseBody = service.onTOGetCognitoToken(AppController.getInstance().getApiToken(), Constants.LANGUAGE);
        obsResponseBody.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CongnitoResponse>>() {
                    @Override
                    public void onNext(Response<CongnitoResponse> response) {
                        if (response.code() == 200) {
                            if (response.body() != null && response.body().getData() != null) {
                                developerProvider = new DeveloperAuthenticationProvider(null, response.body().getData().getIdentityId(), Regions.fromName(response.body().getData().getRegion()));
                                ApiOnServer.BUCKET = response.body().getData().getBucket();
                                ApiOnServer.COGNITO_ID = response.body().getData().getIdentityId();
                                ApiOnServer.COGNITO_TOKEN = response.body().getData().getToken();
                                ApiOnServer.REGION = response.body().getData().getRegion();
                                onSuccessCognitoTo(context, folder, file, callBack,response.body().getData().getRegion());
                            }
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("8889",e.getMessage());

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void onSuccessCognitoTo(Context context, String folder, File file, UploadCallBack callBack, String regions) {
        getCredentialProvider(context,regions);

        if (transferUtility != null && file != null) {
            TransferNetworkLossHandler.getInstance(context);
            TransferObserver observer = transferUtility.upload(ApiOnServer.BUCKET,
                    folder,
                    file,
                    CannedAccessControlList.PublicRead);

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    Log.d("aws", "onStateChanged: " + id + ", " + state.toString());

                    // If upload error, failed or network disconnect
                    if (state == TransferState.FAILED || state == TransferState.WAITING_FOR_NETWORK) {
                        Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show();
                    } else if (state.equals(TransferState.COMPLETED)) {
                        callBack.sucess("https://" + ApiOnServer.BUCKET + "." + "s3."+regions+".amazonaws.com/"
                                + folder + "/"
                                + file.getName());

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                    int percentDone = (int) percentDonef;
                    Log.d("aws", "ID:" + id + " bytesCurrent: " + bytesCurrent + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
                }

                @Override
                public void onError(int id, Exception ex) {
                    Log.e("aws", id + ":" + ex.toString());
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
     * @param folder   Bucket name
     * @param fileName The name which the file is to be uploaded
     * @param file     The file to be uploaded
     * @param callBack Overidden method
     */
    public void UploadDataWithName(final String folder, final String fileName, final File file, final UploadCallBack callBack) {

        if (transferUtility != null && file != null) {

            TransferObserver observer = transferUtility.upload(folder, fileName, file);

            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state.equals(TransferState.COMPLETED)) {
                        callBack.sucess("https://" + ApiOnServer.BUCKET + "." + ApiOnServer.BASE_URL
                                + folder + "/"
                                + file.getName());

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
     * <h2>delete_Item</h2>
     * <P>Deleting the item from aws.</P>
     *
     * @param bucketName Aws bucket name
     * @param keyName    file name in the aws folder.
     */
    public void delete_Item(String bucketName, String keyName) {
        try {
            if (s3Client != null)
                // s3Client.deleteObject(bucketName,keyName);
                s3Client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }


    /**
     * This method is used to get the CredentialProvider and we provide only context as a parameter.
     *  @param context Here, we are getting the context from calling Activity.
     * @param regions
     */
    private CognitoCachingCredentialsProvider getCredentialProvider(Context context, String regions) {
        //Call this method from success response of fetch token
        if (!ApiOnServer.COGNITO_TOKEN.isEmpty() && !ApiOnServer.COGNITO_ID.isEmpty()) {

            developerProvider.updateCredentials();

            credentialsProvider = new CognitoCachingCredentialsProvider(context, developerProvider, Regions.fromName(regions));
            /* *
             * Creating the object  of the s3Client */
            s3Client = getS3Client();

            /* *
             * Creating the object of the TransferUtility of the Amazone.*/
            transferUtility = getTransferUtility(context, s3Client);
        }

       /* if (credentialsProvider == null)
        {
            credentialsProvider = new CognitoCachingCredentialsProvider(
                    context.getApplicationContext(),
                    pool_id, // Identity Pool ID
                    Regions.US_EAST_1 // Region//us-east-1
                    //Regions.AP_NORTHEAST_1 // Region
            );
        }*/
        return credentialsProvider;
    }

    /**
     * This method is used to get the AmazonS3 Client
     * and we provide only context as a parameter.
     * and from here we are calling getCredentialProvider() function.
     * Here, we are getting the context from calling Activity.
     */
    private AmazonS3Client getS3Client() {
        s3Client = new AmazonS3Client(credentialsProvider);
      /*  if (s3Client == null)
        {
            s3Client = new AmazonS3Client(cognitoCachingCredentialsProvider);
        }*/
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
