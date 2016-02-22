package com.okawa.pedro.producthunt.model.response;

import java.util.List;

import greendao.Vote;

/**
 * Created by pokawa on 22/02/16.
 */
public class VoteResponse {

    private List<Vote> votes;

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
