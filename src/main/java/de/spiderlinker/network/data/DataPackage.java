package de.spiderlinker.network.data;

import de.spiderlinker.utils.StringUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class DataPackage implements Serializable {

  private final String id;
  private List<Object> dataObjects;

  public DataPackage(final String id, final Object... data) {
    this.id = StringUtils.requireNonNullOrEmpty(id);
    this.setData(data);
  }

  private void setData(final Object[] obj) {
    this.dataObjects = new LinkedList<>();
    this.addData(obj);
  }

  public void addData(final Object... data) {
    if (data != null) {
      Collections.addAll(this.dataObjects, data);
    }
  }

  public void addData(int index, Object data) {
    this.dataObjects.add(index, data);
  }

  public String getID() {
    return this.id;
  }

  public <T extends Object> T get(final int i) {
    return (T) this.dataObjects.get(i);
  }

  public Object getObject(final int i) {
    return this.dataObjects.get(i);
  }

  public List<Object> get() {
    return this.dataObjects;
  }

  @Override
  public String toString() {
    return String.format("[%s] %s", this.id, this.dataObjects.toString());
  }

}
