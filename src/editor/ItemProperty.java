package editor;

public class ItemProperty {

	private String description, paramCode, paramX, paramY, paramZ;

	/**
	 * Creates an ItemProperty
	 * @param description User-readable property description ("X", "Y" and "Z" will be replaced with parameter values)
	 * @param paramCode Property code
	 * @param paramX
	 * @param paramY
	 * @param paramZ
	 */
	public ItemProperty(String description, String paramCode, String paramX, String paramY, String paramZ){
		this.description = description;
		this.paramCode = paramCode;
		this.paramX = paramX;
		this.paramY = paramY;
		this.paramZ = paramZ;
	}
	
	/**
	 * Getter for property code
	 * @return
	 */
	public String getParamCode(){
		return paramCode;
	}

	/**
	 * Getter for X parameter
	 * @return
	 */
	public String getParamX(){
		return paramX;
	}

	/**
	 * Getter for Y parameter
	 * @return
	 */
	public String getParamY(){
		return paramY;
	}

	/**
	 * Getter for Z parameter
	 * @return
	 */
	public String getParamZ(){
		return paramZ;
	}
	
	/**
	 * Getter for description
	 * @return
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * Returns a description of this property, with parameter values substituted in
	 */
	@Override
	public String toString() {
		String returnStr = description.replaceAll("#X", paramX);
		returnStr = returnStr.replaceAll("#Y", paramY);
		returnStr = returnStr.replaceAll("#Z", paramZ);
		return returnStr;
	}
	
}
