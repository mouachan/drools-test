package com.amadeus.droolsfeature.rules;

public class Customer {
	
    private boolean frequent;
    private  BzrMarketDeclare market; 
    private int reduction;
	/**
	 * @return the frequent
	 */
	public boolean isFrequent() {
		return frequent;
	}
	/**
	 * @param frequent the frequent to set
	 */
	public void setFrequent(boolean frequent) {
		this.frequent = frequent;
	}
	/**
	 * @return the market
	 */
	public BzrMarketDeclare getMarket() {
		return market;
	}
	/**
	 * @param market the market to set
	 */
	public void setMarket(BzrMarketDeclare market) {
		this.market = market;
	}
	/**
	 * @return the reduction
	 */
	public int getReduction() {
		return reduction;
	}
	/**
	 * @param reduction the reduction to set
	 */
	public void setReduction(int reduction) {
		this.reduction = reduction;
	}


}
