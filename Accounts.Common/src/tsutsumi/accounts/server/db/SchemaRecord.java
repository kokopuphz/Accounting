package tsutsumi.accounts.server.db;

public class SchemaRecord {

	private String schemaName;
	private String schemaJods;
	private boolean updateable;
	
	public SchemaRecord(String schemaName, String schemaJods, boolean updateable) {
		this.schemaName = schemaName;
		this.schemaJods = schemaJods;
		this.updateable = updateable;
		//this.privilege = "SELECT";
	}
	
	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}
	
	public String getJodsName() {
		return schemaJods;
	}
	/**
	 * @param schemaName the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	/**
	 * @return the privilege
	 */
	public boolean isUpdateable() {
		return updateable;
	}
	/**
	 * @param privilege the privilege to set
	 */
	public void setUpdateable(boolean updateable) {
		this.updateable = updateable;
	}
	
	public String getLabelString() {
		String listText = getSchemaName();
		if (!isUpdateable()){
			listText += "(SELECT)";
		}
		return listText;
	}
	
	public boolean equals(Object o) {
		if (o instanceof SchemaRecord) {
			if (((SchemaRecord)o).getSchemaName().equals(this.getSchemaName()) && (((SchemaRecord)o).isUpdateable()==this.isUpdateable())) {
				return true;
			}
		}
		return false;
	}
	
}
