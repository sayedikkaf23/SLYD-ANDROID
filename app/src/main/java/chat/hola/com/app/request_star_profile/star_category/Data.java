package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.Data}</h1>
 * <p>Model class of star category.</p>
 * @author: Hardik Karkar
 * @since : 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatResponse}
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("categorie")
    @Expose
    private String categorie;
    @SerializedName("categorieInOtherLang")
    @Expose
    private CategorieInOtherLang categorieInOtherLang;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusText")
    @Expose
    private String statusText;
    @SerializedName("icon")
    @Expose
    private String icon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public CategorieInOtherLang getCategorieInOtherLang() {
        return categorieInOtherLang;
    }

    public void setCategorieInOtherLang(CategorieInOtherLang categorieInOtherLang) {
        this.categorieInOtherLang = categorieInOtherLang;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public class CategorieInOtherLang implements Serializable{

        @SerializedName("vi")
        @Expose
        private String vi;

        public String getVi() {
            return vi;
        }

        public void setVi(String vi) {
            this.vi = vi;
        }

    }

}
