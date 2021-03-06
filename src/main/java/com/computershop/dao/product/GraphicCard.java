package com.computershop.dao.product;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Table;
import com.computershop.dao.Product;

@Entity
@Table(name = "GraphicCards")
public class GraphicCard extends Product {

	@Column(name = "dimensions")
	private String dimensions;

	@Column(name = "weight")
	private String weight;

	@Column(name = "vga_memory")
	private String VGAMemory;

	@Column(name = "bandwidth")
	private String bandwidth;

	@Column(name = "voltage")
	private String voltage;

	public GraphicCard(Product product) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

	}

	public GraphicCard(Product product, String dimensions, String weight, String vGAMemory, String bandwidth,
			String voltage) {
		super(product.getId(), product.getName(), product.getBrand(), product.getProductImages(), product.getRatings(),
				product.getCategory(), product.getManufactures(), product.getDescription(), product.getPrice(),
				product.getSaleOff(), product.getAmount(), product.getQuantitySold(), product.getWarranty(),
				product.getCreateAt(), product.getUpdateAt(), product.getOrderItems());

		this.dimensions = dimensions;
		this.weight = weight;
		this.VGAMemory = vGAMemory;
		this.bandwidth = bandwidth;
		this.voltage = voltage;
	}

	public GraphicCard() {
		super();
	}

	public String getDimensions() {
		return dimensions;
	}

	public void setDimensions(String dimensions) {
		this.dimensions = dimensions;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getVGAMemory() {
		return VGAMemory;
	}

	public void setVGAMemory(String vGAMemory) {
		VGAMemory = vGAMemory;
	}

	public String getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(String bandwidth) {
		this.bandwidth = bandwidth;
	}

	public String getVoltage() {
		return voltage;
	}

	public void setVoltage(String voltage) {
		this.voltage = voltage;
	}

}
