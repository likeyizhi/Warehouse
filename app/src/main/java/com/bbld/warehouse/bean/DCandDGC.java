package com.bbld.warehouse.bean;

/**
 * Created by likey on 2017/11/13.
 */

public class DCandDGC {
    private int ProductId;
    private int DeliveryCount;
    private int DeliverGiveCount;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getDeliveryCount() {
        return DeliveryCount;
    }

    public void setDeliveryCount(int deliveryCount) {
        DeliveryCount = deliveryCount;
    }

    public int getDeliverGiveCount() {
        return DeliverGiveCount;
    }

    public void setDeliverGiveCount(int deliverGiveCount) {
        DeliverGiveCount = deliverGiveCount;
    }

    public DCandDGC(int productId, int deliveryCount, int deliverGiveCount) {
        ProductId = productId;
        DeliveryCount = deliveryCount;
        DeliverGiveCount = deliverGiveCount;
    }
}
