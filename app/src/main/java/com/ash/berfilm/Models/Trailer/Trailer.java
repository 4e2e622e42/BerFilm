package com.ash.berfilm.Models.Trailer;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Trailer
{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerResult> trailerResults = null;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<TrailerResult> getTrailerResults()
    {
        return trailerResults;
    }

    public void setResults(List<TrailerResult> trailerResults)
    {
        this.trailerResults = trailerResults;
    }

}
