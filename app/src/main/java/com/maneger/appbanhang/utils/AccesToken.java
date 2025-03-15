package com.maneger.appbanhang.utils;
import android.util.Log;

import com.google.auth.oauth2.GoogleCredentials;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class AccesToken {
    private static final String firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging";
    public String getAccesToken(){
        try {
            String jsionString = "{\n" +
                    "  \"type\": \"service_account\",\n" +
                    "  \"project_id\": \"appbanhang-f3c5a\",\n" +
                    "  \"private_key_id\": \"00ab0651f20c3167248b04284cf1d83234aa8e60\",\n" +
                    "  \"private_key\": \"-----BEGIN PRIVATE KEY-----\\nMIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCPvzkTyTEBGUi5\\ne1XaKamXkEnWie0R/chWBa+v0jdlRluu8Zb0e6M79dOmnvw4rMIOHa9g8djGYuI7\\nVNGvJakR1EmbPN56fUwWDzfDr4CIV1X9DUGGDjN0+BZzLZkyIdMHzgK8AsOZ74Oj\\ntY8/xo8yq6jLontnSGwL7iYTmAoT0JYgf4i42p4YYl1VTyFyxPigc7gLcwBCwVB8\\nch7oO8nQ/xkhFp6TLKPvWPGI86xn6c4ClCy1h9LUrRqUXffINzvA5L4bxlPwbwIz\\nqIH+uDxtgWLtkSe/eJ39phCkVkL2QVzfnxb+Gx1Y//9MayzAAt0RlU1Bat9XHp7x\\nFblzzbpNAgMBAAECggEAKlFbvsjsjcvBPqWiDSNVw4JdUnTxX2oMd9KduY+T8lMn\\nu5Vw4NGjKQxCPluwmgi16kzv7ATW0fSbcGcUgdD++Ho8HyDoyDup0haxzoCUwM6h\\nSGmGIC5/FJDSnx7N6lXN2A7cfDf+GIvL/758CQTt9DdX7ncLiEjoUgzOldcdJF7D\\nKg9EhMNowIFPHVLxqnUxpGfUT4hTFZYsTtCqSDsluetqb9nrmUk9oJLWCeDym/5Z\\nzb94bDhsjka0SJC02n+t6zZC0hymkolzJV/GQU/NXqy5Glbp10V/gFvS5dgrjzVD\\nD105U2c3hj7R835Od9uDv0v4Z/OYLfXo4KOd/YxaawKBgQDLDObXYJ1tz+lEAbS5\\nhkUBW297aG0DuZ/yTOE67KVyjhre4z0czJZKUsfjbn7VCY2zfys+jtBnGT2zrnnJ\\nGLFMWmzW3Fm3cxfOKw8kBBPzEhYEdePJQs/VJdKsNNthJOItwY+YSwH6KeSY74Fm\\ne4WGRd1SSOif6CjfQE509dO+RwKBgQC1O2I0J2eKyke0XPakwWzCEvDXfb/web+c\\nrn6w8+ELV+uo7q1WBqoAje6EuQtXhMXzaLq/4yd6Zeh/g7kNsLQjTxYlRiiUgztN\\nqGbl18wd6LQXVvaGFcGDiSvOFdy4Y020odkp5BKM7ESXCPp+WwUmTRX1wdXEF+Vl\\nsd+FYx9oywKBgQDDnIpGsyz2vXVbrskMce3IgO3FVbSwAgO0yZuxyIka8wuv7frR\\n48Zlk5o94BUP50Q7yCdA/RSIPijTESMCOGeVxwdTdrR3pskoNRNA/yy43pOXeHsZ\\nSOQv02BBxioSTBYZPn+3l4swFWvEafefYyAfWu78doCkjGPTr3/l7W0vwQKBgQCM\\niB7PeFrG+bH+fzPWcBKor01InD8w9weJNJoTFxnD1QptNZ79nS9IPMbe7iqIU/G3\\nnNdrInajZ9CQyJC/t+0HkDUI9E1VVCmxP0uDIQIrkx3LF2kUclxbZgusw7Ei3gYM\\ncpBV9oAX/MH3KEQtpODqYU4NAqCDbVA5umMsKL/bXwKBgQCiDGK67cnCSvRyC6z3\\njRp2gzfSP7+/nWIXQVyPXQefoqHloAxEra+T1cJ4+IB2wSCIW3WmUMtD+1lFgy7d\\nyqjHeeYgYlrQjC42fEOIUxhx/qNoA7Kf/k2hJc6B9Jz6Q58+nzc4QDZb12bagSYO\\nj4iuw0AD13/3gChr5vuoEt1ZyA==\\n-----END PRIVATE KEY-----\\n\",\n" +
                    "  \"client_email\": \"firebase-adminsdk-vdjnu@appbanhang-f3c5a.iam.gserviceaccount.com\",\n" +
                    "  \"client_id\": \"118048286467473984878\",\n" +
                    "  \"auth_uri\": \"https://accounts.google.com/o/oauth2/auth\",\n" +
                    "  \"token_uri\": \"https://oauth2.googleapis.com/token\",\n" +
                    "  \"auth_provider_x509_cert_url\": \"https://www.googleapis.com/oauth2/v1/certs\",\n" +
                    "  \"client_x509_cert_url\": \"https://www.googleapis.com/robot/v1/metadata/x509/firebase-adminsdk-vdjnu%40appbanhang-f3c5a.iam.gserviceaccount.com\",\n" +
                    "  \"universe_domain\": \"googleapis.com\"\n" +
                    "}\n";
            InputStream stream = new ByteArrayInputStream(jsionString.getBytes(StandardCharsets.UTF_8));
            GoogleCredentials googleCredentials = GoogleCredentials.fromStream(stream).createScoped(firebaseMessagingScope);
            googleCredentials.refresh();
            return  googleCredentials.getAccessToken().getTokenValue();
        }catch (Exception e){
            Log.d("AccesToken", "getAccesToken: " + e.getLocalizedMessage());
            return null;
        }
    }
}
