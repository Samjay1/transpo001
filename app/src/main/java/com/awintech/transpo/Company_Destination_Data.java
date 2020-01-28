package com.awintech.transpo;

public class Company_Destination_Data {
    String From,To,Cost, Duration, Company_Name, Company_Id,company_Logo, Date,Time, Bus_No, Depart_Locaton,Seat_No;

    public Company_Destination_Data() {
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public Company_Destination_Data(String from, String to, String cost, String duration, String company_Name, String company_Id,String Company_Logo, String date, String time, String bus_No, String depart_Locaton, String seat_No) {
        From = from;
        To = to;
        Cost = cost;
        Duration = duration;
        Company_Name = company_Name;
        Company_Id = company_Id;
        Company_Logo = company_Logo;
        Date = date;
        Time = time;
        Bus_No = bus_No;
        Depart_Locaton = depart_Locaton;
        Seat_No = seat_No;
    }

    public String getCompany_Id() {
        return Company_Id;
    }

    public void setFrom(String from) {
        From = from;
    }

    public void setTo(String to) {
        To = to;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getCompany_Logo() {
        return company_Logo;
    }

    public void setCompany_Logo(String company_Logo) {
        this.company_Logo = company_Logo;
    }

    public void setDate(String date) {
        Date = date;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setBus_No(String bus_No) {
        Bus_No = bus_No;
    }

    public void setDepart_Locaton(String depart_Locaton) {
        Depart_Locaton = depart_Locaton;
    }

    public void setSeat_No(String seat_No) {
        Seat_No = seat_No;
    }

    public String getFrom() {
        return From;
    }

    public String getTo() {
        return To;
    }

    public String getCost() {
        return Cost;
    }

    public String getDuration() {
        return Duration;
    }

    public String getCompany_Name() {
        return Company_Name;
    }


    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getBus_No() {
        return Bus_No;
    }

    public String getDepart_Locaton() {
        return Depart_Locaton;
    }

    public String getSeat_No() {
        return Seat_No;
    }
}

