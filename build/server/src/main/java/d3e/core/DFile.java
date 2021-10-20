package d3e.core;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DFile {

    private String name;
    @Id
    private String id;
    private long size;
    private transient boolean notSaved = true;

    private String mimeType;
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public void _markSaved() {
    	this.notSaved = false;
    }
    
    public boolean _isNotSaved() {
    	return notSaved;
    }
    
    public String getMimeType() {
		return mimeType;
	}
    
    public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
