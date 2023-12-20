package org.project.entity;

import lombok.AllArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFormat {

    private Date date;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String nameInvoice;
    private String material;
    private String description;
    private String size;
    private Double unityPrice;
    private Integer	qtyShip;
    private Double extendedPrice;
    private Double exchangeRate;
    private Double valuePLN;
    private String connectDocuments="null";
    private Double valueConnectDocuments=0.00;

    public DataFormat(String dateToPut, String nameInvoice, String material, String description, String size, Double unityPrice,
                      Integer qtyShip, Double extendedPrice, Double exchangeRate, Double valuePLN) {

        setDate(dateToPut);
        setNameInvoice(nameInvoice);
        setMaterial(material);
        setDescription(description);
        setSize(size);
        setUnityPrice(unityPrice);
        setQtyShip(qtyShip);
        setExtendedPrice(extendedPrice);
        setExchangeRate(exchangeRate);
        setValuePLN(valuePLN);

    }

    public SimpleDateFormat getdateFormat() {

        return dateFormat;
    }

    public String getDate() {

        SimpleDateFormat rokMiesiacDzien=new SimpleDateFormat("yyyy-MM-dd");
        return rokMiesiacDzien.format(date);
    }

    public Date getOriginalDate() {
        return date;
    }

    public void setDate(String dateToPut) {
        try {
            this.date = getdateFormat().parse(dateToPut);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getNameInvoice() {
        return nameInvoice;
    }

    public void setNameInvoice(String nameInvoice) {
        if (!nameInvoice.equals(""))
            this.nameInvoice = nameInvoice;
        else {
            this.nameInvoice = "NULL_nameInvoice";
        }
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        if (!material.equals(""))
            this.material = material;
        else
            this.material = "NULL_material";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        if (!description.equals(""))
            this.description = description;
        else
            this.description = "NULL_description";
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        if (!size.equals(""))
            this.size = size;
        else
            this.size="NULL_size";
    }

    public Double getUnityPrice() {
        return unityPrice;
    }

    public void setUnityPrice(Double unityPrice) {
        double tempValue=(unityPrice*100);
        tempValue=Math.round(tempValue);
        tempValue/=100;
        this.unityPrice = tempValue;
    }

    public Integer getQtyShip() {
        return qtyShip;
    }

    public void setQtyShip(Integer qtyShip) {
        this.qtyShip = qtyShip;
    }

    public Double getExtendedPrice() {
        return extendedPrice;
    }

    public void setExtendedPrice(Double extendedPrice) {
        double tempValue=(extendedPrice*100);
        tempValue=Math.round(tempValue);
        tempValue/=100;

        this.extendedPrice = tempValue;
    }

    public Double getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(Double exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Double getValuePLN() {
        return valuePLN;
    }

    public void setValuePLN(Double valuePLN) {
        double tempValue=(valuePLN*100);
        tempValue=Math.round(tempValue);
        tempValue/=100;

        this.valuePLN = tempValue;
    }

    public String getConnectDocuments() {
        return connectDocuments;
    }

    public void setConnectDocuments(String connectDocuments) {

        if(this.getConnectDocuments().equals("null")) {
            this.connectDocuments= connectDocuments;
        }
        else
            this.connectDocuments+= connectDocuments;
    }

    public Double getValueConnectDocuments() {
        return valueConnectDocuments;
    }

    public void setValueConnectDocuments(Double valueConnectDocuments) {
        double tempValue=(valueConnectDocuments*1000000);
        tempValue=Math.round(tempValue);
        tempValue/=1000000;

        this.valueConnectDocuments+= tempValue;
    }

}
