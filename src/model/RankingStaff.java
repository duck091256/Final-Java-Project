package model;

public class RankingStaff {
    private String staffID;
    private int rating;
    private int ranking;

    public RankingStaff(String staffID, int rating, int ranking) {
        this.staffID = staffID;
        this.rating = rating;
        this.ranking = ranking;
    }

    public void setStaffID(String staffID) {this.staffID = staffID;}

    public void setRating(int rating) {this.rating = rating;}

    public void setRanking(int ranking) {this.ranking = ranking;}

    public String getStaffID() {return staffID;}

    public int getRanking() {
        return ranking;
    }

    public int getRating() {return rating;}
}
