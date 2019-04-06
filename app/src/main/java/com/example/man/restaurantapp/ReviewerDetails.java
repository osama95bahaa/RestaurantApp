package com.example.man.restaurantapp;

public class ReviewerDetails {

    String rev_image;
    String rev_name;
    String rev_date;
    String rev_rating_text;
    String rev_review_text;
    double review_ratingbar;

    public String getRev_image() {
        return rev_image;
    }

    public void setRev_image(String rev_image) {
        this.rev_image = rev_image;
    }

    public String getRev_name() {
        return rev_name;
    }

    public void setRev_name(String rev_name) {
        this.rev_name = rev_name;
    }

    public String getRev_date() {
        return rev_date;
    }

    public void setRev_date(String rev_date) {
        this.rev_date = rev_date;
    }

    public String getRev_rating_text() {
        return rev_rating_text;
    }

    public void setRev_rating_text(String rev_rating_text) {
        this.rev_rating_text = rev_rating_text;
    }

    public String getRev_review_text() {
        return rev_review_text;
    }

    public void setRev_review_text(String rev_review_text) {
        this.rev_review_text = rev_review_text;
    }

    public double getReview_ratingbar() {
        return review_ratingbar;
    }

    public void setReview_ratingbar(double review_ratingbar) {
        this.review_ratingbar = review_ratingbar;
    }
}
