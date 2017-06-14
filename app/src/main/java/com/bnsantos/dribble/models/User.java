package com.bnsantos.dribble.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.bnsantos.dribble.db.DB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(database = DB.class)
public class User extends BaseModel implements Parcelable {
  @PrimaryKey
  private long id;

  @Column
  private String name;

  @Column
  private String avatar;

  @Column
  private String bio;

  @Column
  private String location;

  @Column
  private String web;

  @Column
  private String twitter;

  @Column
  private long followers;

  @Column
  private long likes;

  @Column
  private boolean pro;

  @Column
  private Date createdAt;

  @Column
  private Date updatedAt;


  public User() { }

  public User(long id, String name, String avatar, String bio, String location, String web, String twitter, long followers, long likes, boolean pro, Date createdAt, Date updatedAt) {
    this.id = id;
    this.name = name;
    this.avatar = avatar;
    this.bio = bio;
    this.location = location;
    this.web = web;
    this.twitter = twitter;
    this.followers = followers;
    this.likes = likes;
    this.pro = pro;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAvatar() {
    return avatar;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getWeb() {
    return web;
  }

  public void setWeb(String web) {
    this.web = web;
  }

  public String getTwitter() {
    return twitter;
  }

  public void setTwitter(String twitter) {
    this.twitter = twitter;
  }

  public long getFollowers() {
    return followers;
  }

  public void setFollowers(long followers) {
    this.followers = followers;
  }

  public long getLikes() {
    return likes;
  }

  public void setLikes(long likes) {
    this.likes = likes;
  }

  public boolean isPro() {
    return pro;
  }

  public void setPro(boolean pro) {
    this.pro = pro;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Date createdAt) {
    this.createdAt = createdAt;
  }

  public Date getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Date updatedAt) {
    this.updatedAt = updatedAt;
  }

  @Override
  public String toString() {
    return "User{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", avatar='" + avatar + '\'' +
        ", bio='" + bio + '\'' +
        ", location='" + location + '\'' +
        ", web='" + web + '\'' +
        ", twitter='" + twitter + '\'' +
        ", followers=" + followers +
        ", likes=" + likes +
        ", pro=" + pro +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof User)) return false;

    User user = (User) o;

    if (id != user.id) return false;
    if (followers != user.followers) return false;
    if (likes != user.likes) return false;
    if (pro != user.pro) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
    if (bio != null ? !bio.equals(user.bio) : user.bio != null) return false;
    if (location != null ? !location.equals(user.location) : user.location != null) return false;
    if (web != null ? !web.equals(user.web) : user.web != null) return false;
    if (twitter != null ? !twitter.equals(user.twitter) : user.twitter != null) return false;
    if (createdAt != null ? !createdAt.equals(user.createdAt) : user.createdAt != null)
      return false;
    return updatedAt != null ? updatedAt.equals(user.updatedAt) : user.updatedAt == null;

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
    result = 31 * result + (bio != null ? bio.hashCode() : 0);
    result = 31 * result + (location != null ? location.hashCode() : 0);
    result = 31 * result + (web != null ? web.hashCode() : 0);
    result = 31 * result + (twitter != null ? twitter.hashCode() : 0);
    result = 31 * result + (int) (followers ^ (followers >>> 32));
    result = 31 * result + (int) (likes ^ (likes >>> 32));
    result = 31 * result + (pro ? 1 : 0);
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
    return result;
  }


  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(this.id);
    dest.writeString(this.name);
    dest.writeString(this.avatar);
    dest.writeString(this.bio);
    dest.writeString(this.location);
    dest.writeString(this.web);
    dest.writeString(this.twitter);
    dest.writeLong(this.followers);
    dest.writeLong(this.likes);
    dest.writeByte(this.pro ? (byte) 1 : (byte) 0);
    dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
    dest.writeLong(this.updatedAt != null ? this.updatedAt.getTime() : -1);
  }

  protected User(Parcel in) {
    this.id = in.readLong();
    this.name = in.readString();
    this.avatar = in.readString();
    this.bio = in.readString();
    this.location = in.readString();
    this.web = in.readString();
    this.twitter = in.readString();
    this.followers = in.readLong();
    this.likes = in.readLong();
    this.pro = in.readByte() != 0;
    long tmpCreatedAt = in.readLong();
    this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    long tmpUpdatedAt = in.readLong();
    this.updatedAt = tmpUpdatedAt == -1 ? null : new Date(tmpUpdatedAt);
  }

  public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
    @Override
    public User createFromParcel(Parcel source) {
      return new User(source);
    }

    @Override
    public User[] newArray(int size) {
      return new User[size];
    }
  };
}
