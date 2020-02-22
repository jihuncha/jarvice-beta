package huni.techtown.org.jarvice.common.data;

import java.io.Serializable;

public class SalesObject implements Serializable {
    private String TAG = SalesObject.class.getSimpleName();

    private String barcode;
    private String discount;
    private String discountType;
    private String firstOrder;
    private long id;
    private String paymentTime;
    private String plusSales;
    private String productCode;
    private String productCount;
    private String productName;
    private String realSales;
    private String receiptNo;
    private String sell;
    private String sellDate;
    private String sort;
    private String tableNo;
    private String vat;
    private String category;

    public SalesObject(){}

    public SalesObject(String barcode, String discount,
                       String discountType, String firstOrder,
                       long id, String paymentTime, String plusSales,
                       String productCode, String productCount,
                       String productName, String realSales,
                       String receiptNo, String sell,
                       String sellDate, String sort,
                       String tableNo, String vat,
                       String category) {
        this.barcode = barcode;
        this.discount = discount;
        this.discountType = discountType;
        this.firstOrder = firstOrder;
        this.id = id;
        this.paymentTime = paymentTime;
        this.plusSales = plusSales;
        this.productCode = productCode;
        this.productCount = productCount;
        this.productName = productName;
        this.realSales = realSales;
        this.receiptNo = receiptNo;
        this.sell = sell;
        this.sellDate = sellDate;
        this.sort = sort;
        this.tableNo = tableNo;
        this.vat = vat;
        this.category = category;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public String getFirstOrder() {
        return firstOrder;
    }

    public void setFirstOrder(String firstOrder) {
        this.firstOrder = firstOrder;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPlusSales() {
        return plusSales;
    }

    public void setPlusSales(String plusSales) {
        this.plusSales = plusSales;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRealSales() {
        return realSales;
    }

    public void setRealSales(String realSales) {
        this.realSales = realSales;
    }

    public String getReceiptNo() {
        return receiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        this.receiptNo = receiptNo;
    }

    public String getSell() {
        return sell;
    }

    public void setSell(String sell) {
        this.sell = sell;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "SalesObject{" +
                "barcode='" + barcode + '\'' +
                ", discount='" + discount + '\'' +
                ", discountType='" + discountType + '\'' +
                ", firstOrder='" + firstOrder + '\'' +
                ", id=" + id +
                ", paymentTime='" + paymentTime + '\'' +
                ", plusSales='" + plusSales + '\'' +
                ", productCode='" + productCode + '\'' +
                ", productCount='" + productCount + '\'' +
                ", productName='" + productName + '\'' +
                ", realSales='" + realSales + '\'' +
                ", receiptNo='" + receiptNo + '\'' +
                ", sell='" + sell + '\'' +
                ", sellDate='" + sellDate + '\'' +
                ", sort='" + sort + '\'' +
                ", tableNo='" + tableNo + '\'' +
                ", vat='" + vat + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}
