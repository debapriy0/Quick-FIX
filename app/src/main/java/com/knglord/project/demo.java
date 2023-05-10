package com.knglord.project;

import android.graphics.Bitmap;

import java.util.List;

public class demo {

    public  String person;
    public  String devicename;
    public  List<String> problems;
    public  String additionprob;
    public  Bitmap bill;
    public  Bitmap currentimg;
    public  Integer id;
    public  Integer status;
    public  Integer rating;
    public  String orderdate;
    public  String deliverydate;
    public  Integer cost;

    public demo(){
    }

    public demo(String person, String devicename,List<String> problems,String additionprob, Integer id, Integer status, Integer rating,String orderdate, String deliverydate, Integer cost){
        this.person = person;
        this.devicename = devicename;
        this.problems = problems;
        this.additionprob = additionprob;
        this.bill = bill;
        this.currentimg = currentimg;
        this.id = id;
        this.status = status;
        this.rating = rating;
        this.orderdate = orderdate;
        this.deliverydate = deliverydate;
        this.cost = cost;
    }

}
