package chat.hola.com.app.live_stream.utility;

import android.util.Patterns;

import java.io.Serializable;

/**
 * Created by moda on 11/21/2018.
 */
public class CheckFlagForValidity implements Serializable {
    private boolean nFlg, eFlg, pFlg, mFlg, bNFlg;

    public boolean isnFlg() {
        return nFlg;
    }

    public void setnFlg(boolean nFlg) {
        this.nFlg = nFlg;
    }

    public boolean iseFlg() {
        return eFlg;
    }

    public void seteFlg(boolean eFlg) {
        this.eFlg = eFlg;
    }

    public boolean ispFlg() {
        return pFlg;
    }

    public void setpFlg(boolean pFlg) {
        this.pFlg = pFlg;
    }

    public boolean ismFlg() {
        return mFlg;
    }

    public void setmFlg(boolean mFlg) {
        this.mFlg = mFlg;
    }

    public boolean isbNFlg() {
        return bNFlg;
    }

    public void setbNFlg(boolean bNFlg) {
        this.bNFlg = bNFlg;
    }

    public boolean validEmail(String email) {
        //Validation for Invalid Email Address
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && Patterns.DOMAIN_NAME.matcher(email).matches();
    }

    public boolean validateFname(final String firstname) {
        return firstname != null || !"".equals(firstname);
    }
}

