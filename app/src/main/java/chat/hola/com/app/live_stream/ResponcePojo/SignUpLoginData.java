package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 11/21/2018.
 */
public class SignUpLoginData implements Serializable
{
    /*"message": "Signup Success.",
  "data": {
    "id": "5bf54eddd667985e1d21b6f2",
    "email": "ak@gmail.com",
    "firstName": "Dinesh",
    "lastName": "Gupta",
    "phone": {
      "countryCode": "+91",
      "phone": "8553328349"
    },
    "fcmTopic": "stream_5be03c8ae18a042844bce89d_undefined",
    "mqttTopic": "stream_5be03c8ae18a042844bce89d_undefined",
    "authToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YmY1NGVkZGQ2Njc5ODVlMWQyMWI2ZjIiLCJrZXkiOiJhY2MiLCJhY2Nlc3NDb2RlIjo4MzM4LCJpYXQiOjE1NDI4MDMxNjUsImV4cCI6MTU0MjgwMzI1MSwic3ViIjoidXNlciJ9.9S4m-W6rV_usFdaauLPrKJlxwVMF30QWRQlds4laSGU"
  }*/

    private String id,email,firstName,lastName,fcmTopic,mqttTopic,authToken;

    private PhoneNumberApi phone;


    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFcmTopic() {
        return fcmTopic;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public String getAuthToken() {
        return authToken;
    }

    public PhoneNumberApi getPhone() {
        return phone;
    }

    public class PhoneNumberApi implements Serializable
    {
         /*"countryCode": "+91",
                 "phone": "8553328349"*/

        private String countryCode,phone;

        public String getCountryCode() {
            return countryCode;
        }

        public String getPhone() {
            return phone;
        }
    }
}
