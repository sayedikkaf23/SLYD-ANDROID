package chat.hola.com.app.Utilities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
/**
 * <h1>App_permission_23</h1>
 * <P>
 *   Method is going to Mange all the required run time permission.
 * </P>
 * @author 3Embed.
 * @since 17/10/2016.*/
public class App_permission_23
{
    private final static int REQUEST_CODE_PERMISSIONS_FIRST=786;
    private final static int REQUEST_CODE_PERMISSIONS_SECOND=787;
    private ArrayList<String> permissionsList=null;
    private ArrayList<String> rotationList=null;
    private AlertDialog dialog_parent=null;
    private String TAG="";
    private Activity mactivity;
    private Fragment mfragment;
    private androidx.fragment.app.Fragment v4fragment;
    private Permission_Callback mpermission_callback;


    public enum Permission
    {
        LOCATION,CAMERA,READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,PHONE,RECORD_AUDIO,CONTACTS,CALL
    }

//    /**
//     * Single tone class object.*/
//    private static App_permission_23  app_permission=new App_permission_23();
    /**
     * <P>
     *   constructor.
     *   @Params Activity
     * </P>
     */
    public App_permission_23(Activity activity)
    {
        this.mactivity = activity;
    }


    /**
     * <h2>getPermission_for_Sup_v4Fragment</h2>
     * <P>
     *     Handel this for the support v4Fragment.
     * </P>*/
    public void getPermission_for_Sup_v4Fragment(String tag, final ArrayList<Permission> permission_list, androidx.fragment.app.Fragment fragment, Permission_Callback permission_callback)
    {
        this.TAG=tag;
        this.v4fragment=fragment;
        this.mfragment=null;
        this.mpermission_callback=permission_callback;
        handel_request(permission_list);
    }
    /**
     * <h2>getPermission</h2>
     * <P>
     *
     * </P>
     * */
    public void getPermission(String tag,final ArrayList<Permission> permission_list,Fragment fragment,Permission_Callback permission_callback)
    {
        this.TAG=tag;
        //this.mactivity=activity;
        this.mfragment=fragment;
        this.v4fragment=null;
        this.mpermission_callback=permission_callback;
        handel_request(permission_list);
    }
    /**
     * <h2>handel_request</h2>
     * <p>
     *     Method handel the all request permission details.
     *     weather it is granted or not.
     * </p>
     * @param permission_list  contains the requested permission list data.
     * */
    private void handel_request(final ArrayList<Permission> permission_list)
    {
        /**
         * Creating the List if not created .
         * if created then clear the list for refresh use.*/
        if(permissionsList==null||rotationList==null)
        {
            permissionsList= new ArrayList<>();
            rotationList=new ArrayList<>();
        }else
        {
            permissionsList.clear();
            rotationList.clear();
        }

        if(dialog_parent!=null&&dialog_parent.isShowing())
        {
            dialog_parent.dismiss();
            dialog_parent.cancel();
        }
        /**
         * Checking if the build version is smaller then 23 then no need ask for runtime permission.*/
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.M)
        {
            if(mpermission_callback!=null)
            {
                mpermission_callback.onPermissionGranted(true,this.TAG);
            }
            return;
        }
        /**
         * Making permission array for run time permission.*/
        for(int count=0;permission_list!=null&&count<permission_list.size();count++)
        {
            switch (permission_list.get(count))
            {
                case CONTACTS:
                    if(!checkPermissionGranted(Manifest.permission.READ_CONTACTS))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.READ_CONTACTS,mactivity))
                        {
                            rotationList.add(Manifest.permission.READ_CONTACTS);
                        }else
                        {
                            permissionsList.add(Manifest.permission.READ_CONTACTS);
                        }
                    }
                    break;
                case LOCATION:
                    if(!checkPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.ACCESS_FINE_LOCATION,mactivity))
                        {
                            rotationList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }else
                        {
                            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }
                    break;
                case RECORD_AUDIO:
                    if(!checkPermissionGranted(Manifest.permission.RECORD_AUDIO))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.RECORD_AUDIO,mactivity))
                        {
                            rotationList.add(Manifest.permission.RECORD_AUDIO);
                        }else
                        {
                            permissionsList.add(Manifest.permission.RECORD_AUDIO);
                        }
                    }
                    break;
                case CALL:
                    if(checkPermissionPermanentlyDenied(Manifest.permission.CALL_PHONE,mactivity))
                    {
                        rotationList.add(Manifest.permission.CALL_PHONE);
                    }else
                    {
                        permissionsList.add(Manifest.permission.CALL_PHONE);
                    }
                break;
                case CAMERA:
                    if(!checkPermissionGranted(Manifest.permission.CAMERA))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.CAMERA,mactivity))
                        {
                            rotationList.add(Manifest.permission.CAMERA);
                        }else
                        {
                            permissionsList.add(Manifest.permission.CAMERA);
                        }
                    }
                    break;
                case READ_EXTERNAL_STORAGE:
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    {
                        if(!checkPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE))
                        {
                            if(checkPermissionPermanentlyDenied(Manifest.permission.READ_EXTERNAL_STORAGE,mactivity))
                            {
                                rotationList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }else
                            {
                                permissionsList.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                            }
                        }
                    }
                    break;
                case WRITE_EXTERNAL_STORAGE:
                    if(!checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE,mactivity))
                        {
                            rotationList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }else
                        {
                            permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        }
                    }
                    break;
                case PHONE:
                    if(!checkPermissionGranted(Manifest.permission.READ_PHONE_STATE))
                    {
                        if(checkPermissionPermanentlyDenied(Manifest.permission.READ_PHONE_STATE,mactivity))
                        {
                            rotationList.add(Manifest.permission.READ_PHONE_STATE);
                        }else
                        {
                            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        if(permissionsList.size() > 0)
        {
            if(mpermission_callback!=null)
            {
                mpermission_callback.onPermissionDenied(permissionsList,TAG);
            }
        }else if(rotationList.size()>0)
        {
            if(mpermission_callback!=null)
            {
                mpermission_callback.onPermissionRotation(rotationList,TAG);
            }
        } else
        {
            if(mpermission_callback!=null)
            {
                mpermission_callback.onPermissionGranted(true,TAG);
            }
        }

    }
    /**
     * <h2>onRequestPermissionsResult</h2>
     * <P>
     *    Method handel the permission result of the requested permission.
     * </P>*/
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode==REQUEST_CODE_PERMISSIONS_FIRST)
        {
            if(veryFyie_Permission(permissions)&&rotationList.size()==0)
            {
                mpermission_callback.onPermissionGranted(true,TAG);
            }else
            {
                mange_Rotational_Permission_Request(permissions);
            }

        }else if(requestCode==REQUEST_CODE_PERMISSIONS_SECOND)
        {
            if(veryFyie_Permission(permissions))
            {
                mpermission_callback.onPermissionGranted(true,TAG);
            }else
            {
                mpermission_callback.onPermissionPermanent_Denied(TAG);
            }
        }
    }
    /**
     * <h2>veryFyie_Permission</h2>
     * <P>
     *     Method accepts the permission list and verfyie that the permission is approved or not.
     *     if approved then return true or else return false;
     * </P>
     * @param permissions contans the list of permission to verify*/
    private boolean veryFyie_Permission(String permissions[])
    {
        boolean isPermission_Approved=true;
        for(int count_number=0;count_number<permissions.length&&isPermission_Approved;count_number++)
        {
            if (!checkPermissionGranted(permissions[count_number]))
            {
                isPermission_Approved=false;
            }
        }
        return isPermission_Approved;
    }
    /**
     * <h2>mange_Rotational_Permission_Request</h2>
     * <P>
     *   Method check weather the permission is rotational or not.By the help of create_For_Rotational_List method.
     * </P>
     * @param permission_list contains the list of permission to verify that the permission are rotational or not.*/
    private void mange_Rotational_Permission_Request(String permission_list[])
    {
        ArrayList<String> required_permission_list = create_For_Rotational_List(permission_list);
        if(required_permission_list!=null&&required_permission_list.size()>0)
        {
            mpermission_callback.onPermissionRotation(required_permission_list,TAG);
        }else
        {
            mpermission_callback.onPermissionPermanent_Denied(TAG);
        }
    }

    /**
     * <h2>check_for_rotational</h2>
     * <P>
     *    For rotational asking with new request code .
     * </P>
     * @param permission_list contains the list of permission to be asked again.*/
    private ArrayList<String> create_For_Rotational_List(String permission_list[])
    {
        ArrayList<String> rotational_permission_lit=new ArrayList<>();
        for (String aPermission_list : permission_list)
        {
            if (checkPermissionPermanentlyDenied(aPermission_list, mactivity))
            {
                rotational_permission_lit.add(aPermission_list);
            }
        }
        if(rotationList.size()>0)
        {
            rotational_permission_lit.addAll(rotationList);
        }
        return rotational_permission_lit;
    }
    /**
     * <h2>checkPermissionGranted</h2>
     * <P>
     *    Method check weather the given permission's granted or not.
     * </P>
     * @param permission contains the permission is granted or not.
     */
    public boolean checkPermissionGranted(String permission)
    {
        return ContextCompat.checkSelfPermission(mactivity,permission)== PackageManager.PERMISSION_GRANTED;
    }

    /**
     * <h2>checkPermissionPermanentLyDenied</h2>
     * <P>
     *
     * </P>*/
    private boolean checkPermissionPermanentlyDenied(String permission,Activity activity)
    {
        boolean isRotational=false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            isRotational=activity.shouldShowRequestPermissionRationale(permission);
        }
        return isRotational;
    }
    /**
     * <h2>check_for_Permission</h2>
     * <P>
     *     Method request permission for the required permission.
     * </P>*/
    private void check_for_Permission(String permissions[], Activity mactivity, Fragment fragment, androidx.fragment.app.Fragment v4fragment)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(v4fragment!=null)
            {
                v4fragment.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_FIRST);

            }else if(fragment!=null)
            {
                fragment.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_FIRST);
            }else
            {
                mactivity.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_FIRST);
            }

        }
    }
    /**
     * <h2>check_for_rotational_Permission</h2>
     * <P>
     *     Method request permission for the required permission.
     * </P>*/
    private void check_for_rotational_Permission(String permissions[], Activity mactivity, Fragment fragment, androidx.fragment.app.Fragment v4fragment)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(v4fragment!=null)
            {
                v4fragment.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_SECOND);
            }else if(fragment!=null)
            {
                fragment.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_SECOND);
            }else
            {
                mactivity.requestPermissions(permissions,REQUEST_CODE_PERMISSIONS_SECOND);
            }
        }
    }

    /**
     * <h2>ask_permission_directory</h2>
     * <P>
     *     This method directly asked for permission without checking the permission.
     * </P>*/
    public void ask_permission_rotational(String permission_list[])
    {
        check_for_rotational_Permission(permission_list,mactivity,mfragment,v4fragment);
    }

    /**
     * <h2>ask_permission_directory</h2>
     * <P>
     *     This method directly asked for permission without checking the permission.
     * </P>*/
    public void ask_permission_directory(String permission_list[])
    {
        check_for_Permission(permission_list,mactivity,mfragment,v4fragment);
    }
    /**
     * <h2>show_Aleret_Permission</h2>
     * <P>
     *
     * </P>
     * @param title contains the aleret box title
     * @param message contains the aleret box message.
     **/
//    public void show_Aleret_Permission(final String title,String message,final String permission_list[])
//    {
//        ProgressDialog.Builder alertDialog = new ProgressDialog.Builder(mactivity,R.style.Aleret_dialog_theme);
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.setIcon(R.drawable.login_splash_picogram_logo);
//        alertDialog.setPositiveButton(mactivity.getString(R.string.yes_text), new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.dismiss();
//                dialog.cancel();
//                check_for_Permission(permission_list,mactivity,mfragment,v4fragment);
//            }
//        });
//        alertDialog.setNegativeButton(mactivity.getString(R.string.no_text), new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.cancel();
//            }
//        });
//        dialog_parent = alertDialog.show();
//        dialog_parent.show();
//    }

    /**
     * <h2>show_Aleret_Permission</h2>
     * <P>
     *
     * </P>
     * @param title contains the aleret box title
     * @param message contains the aleret box message.
     * @param mactivity contains the activity reference.
     **/
//    public void show_Aleret_Denied_Permission(final String title,String message,final Activity mactivity)
//    {
//        ProgressDialog.Builder alertDialog = new ProgressDialog.Builder(mactivity,R.style.Aleret_dialog_theme);
//        alertDialog.setTitle(title);
//        alertDialog.setMessage(message);
//        alertDialog.setIcon(R.drawable.login_splash_picogram_logo);
//        alertDialog.setPositiveButton(mactivity.getString(R.string.retry_text), new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//                Intent intent = new Intent();
//                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package",mactivity.getPackageName(), null);
//                intent.setData(uri);
//                mactivity.startActivity(intent);
//                dialog.dismiss();
//                dialog.cancel();
//            }
//        });
//        alertDialog.setNegativeButton(mactivity.getString(R.string.imsure_text), new DialogInterface.OnClickListener()
//        {
//            public void onClick(DialogInterface dialog, int which)
//            {
//                dialog.cancel();
//            }
//        });
//        dialog_parent = alertDialog.show();
//        dialog_parent.show();
//    }
    /**
     *<h2>Permission_Callback</h2>
     * <P>
     *    Permission call back listener of the permission request.
     * </P>*/
    public interface Permission_Callback
    {
        /**
         * Permission granted call back.
         * @param tag contains the asked tag.*/
        void onPermissionGranted(boolean isAllGranted, String tag);
        /**
         * Permission not granted call back.
         * @param tag contains the asked tag.*/
        void onPermissionDenied(ArrayList<String> deniedPermission, String tag);

        void onPermissionRotation(ArrayList<String> rotationPermission, String tag);
        /**
         * Permission granted permanently denied call back.
         * @param tag contains the asked tag.*/
        void onPermissionPermanent_Denied(String tag);

    }

}