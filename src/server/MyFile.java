package server;

public class MyFile {
	  private int id;
	    private String name;
	    private byte[] data;
	    private String fileExtension;

	    public MyFile(int id, String name, byte[] data, String fileExtension) {
	        this.id = id;
	        this.name = name;
	        this.data = data;
	        this.fileExtension = fileExtension;
	    }

	    
	    /**
	     * @return the id
	     */
	    public int getId() {
	        return id;
	    }

	    /**
	     * @param id the id to set
	     */
	    public void setId(int id) {
	        this.id = id;
	    }

	    /**
	     * @return the name
	     */
	    public String getName() {
	        return name;
	    }

	    /**
	     * @param name the name to set
	     */
	    public void setName(String name) {
	        this.name = name;
	    }

	    /**
	     * @return the data
	     */
	    public byte[] getData() {
	        return data;
	    }

	    /**
	     * @param data the data to set
	     */
	    public void setData(byte[] data) {
	        this.data = data;
	    }

	    /**
	     * @return the fileExtension
	     */
	    public String getFileExtension() {
	        return fileExtension;
	    }

	    /**
	     * @param fileExtension the fileExtension to set
	     */
	    public void setFileExtension(String fileExtension) {
	        this.fileExtension = fileExtension;
	    }
	    

}
