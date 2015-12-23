package tsutsumi.accounts.common.reference;

public class Category extends AbstractReferenceData {
	private boolean editable;
	private int type;
	private boolean selectable;
	
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	public boolean isEditable() {
		return editable;
	}
	
	public void setTypeId(int i) {
		type = i;
	}
	
	public int getTypeId() {
		return type;
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public boolean isSelectable() {
		return selectable;
	}
}
