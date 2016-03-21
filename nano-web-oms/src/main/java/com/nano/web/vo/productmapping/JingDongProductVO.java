package com.nano.web.vo.productmapping;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/9/12.
 */
public class JingDongProductVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private Long platformProductId; // 商品平台数字ID
	private String productOuterId; // 商品货号
	private Long platformSkuId; // SKU平台ID
	private String title; // 商品平台名称
	private String name; // 本地商品名称
	private Long num; // 商品平台数量
	private String listTime; // 上架时间
	private String deListTime; // 下架时间
	private String productPrice; // 商品平台价格
	private String approveStatus; // 商品平台上下架状态
	private String shopId; // 店铺ID
	private String shopName; // 店铺名称
	private String properties; // SKU平台属性
	private Long quantity; // SKU平台数量
	private String skuPrice; // SKU平台价格
	private String spec; // SKU本地规格
	private String skuOuterId; // 商品货号
	private String barcode; // SKU条形码
	private String productId; //本地商品ID
	private String skuId; // 本地SKUID
	private String contact; // 尚品联系人
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getPlatformProductId() {
		return platformProductId;
	}
	public void setPlatformProductId(Long platformProductId) {
		this.platformProductId = platformProductId;
	}
	public String getProductOuterId() {
		return productOuterId;
	}
	public void setProductOuterId(String productOuterId) {
		this.productOuterId = productOuterId;
	}
	public Long getPlatformSkuId() {
		return platformSkuId;
	}
	public void setPlatformSkuId(Long platformSkuId) {
		this.platformSkuId = platformSkuId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public String getListTime() {
		return listTime;
	}
	public void setListTime(String listTime) {
		this.listTime = listTime;
	}
	public String getDeListTime() {
		return deListTime;
	}
	public void setDeListTime(String deListTime) {
		this.deListTime = deListTime;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getApproveStatus() {
		return approveStatus;
	}
	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}
	public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
	public String getShopName() {
		return shopName;
	}
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	public String getProperties() {
		return properties;
	}
	public void setProperties(String properties) {
		this.properties = properties;
	}
	public Long getQuantity() {
		return quantity;
	}
	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}
	public String getSkuPrice() {
		return skuPrice;
	}
	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	public String getSkuOuterId() {
		return skuOuterId;
	}
	public void setSkuOuterId(String skuOuterId) {
		this.skuOuterId = skuOuterId;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
}
