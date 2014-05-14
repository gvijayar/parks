package org.openshift.model;

public class ParkData {

	private String Name;
	private float[] pos;
	
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public float[] getPos() {
		return pos;
	}
	public void setPos(float[] pos) {
		this.pos = pos;
	}
	
}
