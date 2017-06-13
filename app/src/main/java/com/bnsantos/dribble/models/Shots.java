package com.bnsantos.dribble.models;

import com.bnsantos.dribble.db.DB;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.Date;

@Table(database = DB.class)
public class Shots extends BaseModel{
  @PrimaryKey
  private long id;

  @Column
  private String title;

  @Column
  private String description;

  @Column
  private int width;

  @Column
  private int height;

  @Column
  private String image;

  @Column
  private long views;

  @Column
  private long likes;

  @Column
  private Date createdAt;

  @Column
  private Date updatedAt;

  @Column
  private String url;

  @Column
  private boolean animated;

  @ForeignKey(saveForeignKeyModel = true, stubbedRelationship = false)
  private User creator;

  public Shots() { }

  public Shots(long id, String title, String description, int width, int height, String image, long views, long likes, Date createdAt, Date updatedAt, String url, boolean animated, User creator) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.width = width;
    this.height = height;
    this.image = image;
    this.views = views;
    this.likes = likes;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.url = url;
    this.animated = animated;
    this.creator = creator;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public long getViews() {
    return views;
  }

  public void setViews(long views) {
    this.views = views;
  }

  public long getLikes() {
    return likes;
  }

  public void setLikes(long likes) {
    this.likes = likes;
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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public boolean isAnimated() {
    return animated;
  }

  public void setAnimated(boolean animated) {
    this.animated = animated;
  }

  public User getCreator() {
    return creator;
  }

  public void setCreator(User creator) {
    this.creator = creator;
  }

  @Override
  public String toString() {
    return "Shots{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", width=" + width +
        ", height=" + height +
        ", image='" + image + '\'' +
        ", views=" + views +
        ", likes=" + likes +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        ", url='" + url + '\'' +
        ", animated=" + animated +
        ", creator=" + creator +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Shots)) return false;

    Shots shots = (Shots) o;

    if (id != shots.id) return false;
    if (width != shots.width) return false;
    if (height != shots.height) return false;
    if (views != shots.views) return false;
    if (likes != shots.likes) return false;
    if (animated != shots.animated) return false;
    if (title != null ? !title.equals(shots.title) : shots.title != null) return false;
    if (description != null ? !description.equals(shots.description) : shots.description != null)
      return false;
    if (image != null ? !image.equals(shots.image) : shots.image != null) return false;
    if (createdAt != null ? !createdAt.equals(shots.createdAt) : shots.createdAt != null)
      return false;
    if (updatedAt != null ? !updatedAt.equals(shots.updatedAt) : shots.updatedAt != null)
      return false;
    if (url != null ? !url.equals(shots.url) : shots.url != null) return false;
    return creator != null ? creator.equals(shots.creator) : shots.creator == null;

  }

  @Override
  public int hashCode() {
    int result = (int) (id ^ (id >>> 32));
    result = 31 * result + (title != null ? title.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + width;
    result = 31 * result + height;
    result = 31 * result + (image != null ? image.hashCode() : 0);
    result = 31 * result + (int) (views ^ (views >>> 32));
    result = 31 * result + (int) (likes ^ (likes >>> 32));
    result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
    result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
    result = 31 * result + (url != null ? url.hashCode() : 0);
    result = 31 * result + (animated ? 1 : 0);
    result = 31 * result + (creator != null ? creator.hashCode() : 0);
    return result;
  }
}
