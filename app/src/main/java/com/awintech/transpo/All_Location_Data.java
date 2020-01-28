package com.awintech.transpo;

public class All_Location_Data {
    public String From, To,Company_Name, Company_Id;

    public All_Location_Data() {

    }

    public All_Location_Data(String from, String to, String company_Name, String company_Id) {
        From = from;
        To = to;
        Company_Name = company_Name;
        Company_Id = company_Id;
    }


    public void setFrom(String from) {
        From = from;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public void setTo(String to) {
        To = to;
    }

    public void setCompany_Name(String company_Name) {
        Company_Name = company_Name;
    }

    public String getFrom() {
        return From;
    }

    public String getCompany_Id() {
        return Company_Id;
    }

    public String getCompany_Name() {
        return Company_Name;
    }

    public String getTo() {
        return To;
    }
}

