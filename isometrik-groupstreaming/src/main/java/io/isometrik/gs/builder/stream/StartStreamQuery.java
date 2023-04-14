package io.isometrik.gs.builder.stream;

import java.util.List;

/**
 * Query builder class for creating the request for starting a stream.
 */
public class StartStreamQuery {

  private String streamDescription;
  private String streamImage;
  private String createdBy;
  private List<String> members;
  private Boolean isPublic;

  private StartStreamQuery(Builder builder) {
    this.streamDescription = builder.streamDescription;
    this.streamImage = builder.streamImage;
    this.createdBy = builder.createdBy;
    this.members = builder.members;
    this.isPublic = builder.isPublic;
  }

  /**
   * The Builder class for the StartStreamQuery.
   */
  public static class Builder {
    private String streamDescription;
    private String streamImage;
    private String createdBy;
    private List<String> members;
    private Boolean isPublic;

    /**
     * Instantiates a new Builder.
     */
    public Builder() {
    }

    /**
     * Sets stream description.
     *
     * @param streamDescription the description of stream group
     * @return the StartStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StartStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery.Builder
     */
    public Builder setStreamDescription(String streamDescription) {
      this.streamDescription = streamDescription;
      return this;
    }

    /**
     * Sets stream image.
     *
     * @param streamImage the image of the stream group
     * @return the StartStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StartStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery.Builder
     */
    public Builder setStreamImage(String streamImage) {
      this.streamImage = streamImage;
      return this;
    }

    /**
     * Sets created by.
     *
     * @param createdBy the id of the stream group creator
     * @return the StartStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StartStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery.Builder
     */
    public Builder setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    /**
     * Sets isPublic.
     *
     * @param isPublic whether stream group is public or private
     * @return the StartStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StartStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery.Builder
     */
    public Builder setPublic(Boolean isPublic) {
      this.isPublic = isPublic;
      return this;
    }

    /**
     * Sets members.
     *
     * @param members the list of members added to the stream group at time of creation
     * @return the StartStreamQuery.Builder{@link io.isometrik.gs.builder.stream.StartStreamQuery.Builder}
     * instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery.Builder
     */
    public Builder setMembers(List<String> members) {
      this.members = members;
      return this;
    }

    /**
     * Build start stream query.
     *
     * @return the StartStreamQuery{@link io.isometrik.gs.builder.stream.StartStreamQuery} instance
     * @see io.isometrik.gs.builder.stream.StartStreamQuery
     */
    public StartStreamQuery build() {
      return new StartStreamQuery(this);
    }
  }

  /**
   * Gets stream description.
   *
   * @return the description of stream group
   */
  public String getStreamDescription() {
    return streamDescription;
  }

  /**
   * Gets stream image.
   *
   * @return the image of the stream group
   */
  public String getStreamImage() {
    return streamImage;
  }

  /**
   * Gets created by.
   *
   * @return the id of the stream group creator
   */
  public String getCreatedBy() {
    return createdBy;
  }

  /**
   * Gets members.
   *
   * @return the list of members added to the stream group at time of creation
   */
  public List<String> getMembers() {
    return members;
  }

  /**
   * Gets is public.
   *
   * @return whether the stream group to be created is public or private
   */
  public Boolean isPublic() {
    return isPublic;
  }
}
