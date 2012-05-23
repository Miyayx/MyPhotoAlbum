package my.photoalbum.datamanager;


import android.graphics.drawable.Drawable;

public class UserInfo {
public static final String USERID = "user_id";
public static final String Kind = "kind";
public static final String SCREENNAME = "screen_name";
public static final String TOKEN = "token";
public static final String TOKENSECRET = "token_secret";
public static final String USERICON = "user_icon";
public static final String EXPIRESIN = "expires_in";

private long userID;
private String screenName;
private String token;
private String tokenSecret;
private Drawable userIcon;
private long expiresIn = 0;
private String kind;


public String getKind() {
	return kind;
}
public void setKind(String kind) {
	this.kind = kind;
}

public String getToken() {
	return token;
}
public void setToken(String token) {
	this.token = token;
}
public long getUserID() {
	return userID;
}
public void setUserID(long userID) {
	this.userID = userID;
}
public String getScreenName() {
	return screenName;
}
public void setScreenName(String screenName) {
	this.screenName = screenName;
}
public String getTokenSecret() {
	return tokenSecret;
}
public void setTokenSecret(String tokenSecret) {
	this.tokenSecret = tokenSecret;
}
public Drawable getUserIcon() {
	return userIcon;
}
public void setUserIcon(Drawable userIcon) {
	this.userIcon = userIcon;
}
public long getExpiresIn() {
	return expiresIn;
}
public void setExpiresIn(long expiresIn) {
	this.expiresIn = expiresIn;
}

public void setExpiresIn(String expiresIn) {
    if (expiresIn != null && !expiresIn.equals("0")) {
        setExpiresIn(System.currentTimeMillis() + Integer.parseInt(expiresIn) * 1000);
    }
}

}
