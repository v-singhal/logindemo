package photos.vbstudio.login.pojo;

/**
 * Created by vaibhavsinghal on 2/02/16.
 */
public class CountryInfo {

    private String countryCode;
    private String countryName;
    private String countryISDCode;


    @Override
    public boolean equals(Object object) {

            if (this.countryName.equals((String) object)) {
                return true;
            }
        return false;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISDCode() {
        return countryISDCode;
    }

    public void setCountryISDCode(String countryISDCode) {
        this.countryISDCode = countryISDCode;
    }
}
