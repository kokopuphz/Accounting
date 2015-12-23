package tsutsumi.accounts.common.reference;

public class Method extends AbstractReferenceData {
	private boolean ccFlag;
	private int sortOrder;
	private boolean selectable;
	private int typeId;

	public Method(int methodId, String methodName, boolean ccFlag, int sortOrder, boolean selectable, int typeId) {
		super(methodId, methodName);
		this.ccFlag = ccFlag;
		this.sortOrder = sortOrder;
		this.selectable =selectable;
		this.typeId = typeId;
	}

	public Method() {
		super();
	}
	/**
	 * @return the ccFlag
	 */
	public boolean isCcFlag() {
		return ccFlag;
	}

	/**
	 * @param ccFlag the ccFlag to set
	 */
	public void setCcFlag(boolean ccFlag) {
		this.ccFlag = ccFlag;
	}

	/**
	 * @return the sortOrder
	 */
	public int getSortOrder() {
		return sortOrder;
	}

	/**
	 * @param sortOrder the sortOrder to set
	 */
	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	/**
	 * @return the selectable
	 */
	public boolean isSelectable() {
		return selectable;
	}

	/**
	 * @param selectable the selectable to set
	 */
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public int getTypeId() {
		return typeId;
	}
	
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
	
}
