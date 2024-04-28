package com.example.artexplorer;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Artwork {

    @JsonProperty("id")
    private String artId;
    @JsonProperty("artist_id")
    private String artistId;
    @JsonProperty("date_end")
    private String dateEnd;
    @JsonProperty("place_of_origin")
    private String origin;
    @JsonProperty("artwork_type_id")
    private String typeId;
    @JsonProperty("department_id")
    private String deptId;

    public Artwork() {
    }

    public Artwork(String id, String artistId, String dateEnd, String origin, String typeId, String deptId) {
        this.artId = id;
        this.artistId = artistId;
        this.dateEnd = dateEnd;
        this.origin = origin;
        this.typeId = typeId;
        this.deptId = deptId;
    }

    public String getArtId() {
        return artId;
    }

    public void setArtId(String artId) {
        this.artId = artId;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String date_End) {
        this.dateEnd = date_End;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String toString() {
        return (artId + "," + artistId + "," + dateEnd + "," + origin + "," + typeId + "," + deptId);
    }

}
