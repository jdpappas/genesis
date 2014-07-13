package hardwareParts;

public abstract class HardwarePart {

	protected String tag;
	protected String partDescription;
	
	public HardwarePart(String tag) {
		this.setTag(tag);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getPartDescription() {
		return partDescription;
	}

	public void setPartDescription(String partDescription) {
		this.partDescription = partDescription;
	}	

	
}
