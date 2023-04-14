package chat.hola.com.app.hastag;

/**
 * @since  3/2/17.
 */
public class Tag_pojo_item
{
    private boolean isHash_tag=false;
    private String username;
    private String profilePicUrl;
    private String fullName;
    private String Count;
    private String hashtag;

    public boolean isHash_tag() {
        return isHash_tag;
    }

    public void setHash_tag(boolean hash_tag) {
        isHash_tag = hash_tag;
    }
    public String getCount ()
    {
        return Count;
    }

    public void setCount (String Count)
    {
        this.Count = Count;
    }

    public String getHashtag ()
    {
        return hashtag;
    }

    public void setHashtag (String hashtag)
    {
        this.hashtag = hashtag;
    }

    public String getUsername ()
    {
        return username;
    }

    public void setUsername (String username)
    {
        this.username = username;
    }

    public String getProfilePicUrl ()
    {
        return profilePicUrl;
    }

    public void setProfilePicUrl (String profilePicUrl)
    {
        this.profilePicUrl = profilePicUrl;
    }

    public String getFullName ()
    {
        return fullName;
    }

    public void setFullName (String fullName)
    {
        this.fullName = fullName;
    }

    @Override
    public String toString()
    {
        if(this.isHash_tag())
        {
            return this.getHashtag();
        }else
        {
            return this.getUsername();
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        Tag_pojo_item temp= (Tag_pojo_item) obj;
        boolean isMatched=false;
        if(temp!=null)
        {
            isMatched=temp.getUsername().equals(this.username);
        }
        return isMatched;
    }
}
