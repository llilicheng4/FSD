package sg.edu.np.mad.evap2;

import java.util.List;

public class forumchatclass {
    String acontent, atitle, message, username, mName, mImageUrl;
    public forumchatclass(){}
    public forumchatclass(String c, String t, String m, String u, String mname, String url){
        this.acontent = c;
        this.atitle = t;
        this.message = m;
        this.username = u;
        if (mname.trim().equals("")) {
            mname = "No Name";
        }
        mName = mname;
        mImageUrl = url;
    }

    public String getAcontent() {
        return acontent;
    }

    public void setAcontent(String acontent) {
        this.acontent = acontent;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }
}
