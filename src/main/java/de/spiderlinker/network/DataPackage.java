package de.spiderlinker.network;

import de.spiderlinker.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class DataPackage implements Serializable {

	private String id;
	private ArrayList<Object> data;

	public DataPackage(final String id, final Object... data) {
		this.setID(id);
		this.setData(data);
	}

	private void setID(final String id) {
    this.id = StringUtils.requireNonNullOrEmpty(id);
	}

	private void setData(final Object[] obj) {
		this.data = new ArrayList<>();
		this.addData(obj);
	}

	public void addData(final Object... data) {
		if (data != null) {
			Collections.addAll(this.data, data);
		}
	}

	public String getID() {
		return this.id;
	}

	public Object get(final int i) {
		return this.data.get(i);
	}

	public List<Object> get() {
		return this.data;
	}

	@Override
	public String toString() {
		return String.format("[%s] %s", this.id, this.data.toString());
	}

}
