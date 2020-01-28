package com.awintech.transpo;

public class Dashboard_Company_Data {

    String Company_name,Company_message,Company_Id, Company_Image;

    public Dashboard_Company_Data(){}

    public Dashboard_Company_Data(String company_name, String company_message, String company_Image, String company_Id) {
        Company_name = company_name;
        Company_message = company_message;
        Company_Image = company_Image;
        Company_Id = company_Id;
    }

    public void setCompany_name(String company_name) {
        Company_name = company_name;
    }

    public void setCompany_message(String company_message) {
        Company_message = company_message;
    }

    public void setCompany_Image(String company_Image) {
        Company_Image = company_Image;
    }

    public String getCompany_name() {
        return Company_name;
    }

    public void setCompany_Id(String company_Id) {
        Company_Id = company_Id;
    }

    public String getCompany_Id() {
        return Company_Id;
    }

    public String getCompany_message() {
        return Company_message;
    }

    public String getCompany_Image() {
        return Company_Image;
    }
}
