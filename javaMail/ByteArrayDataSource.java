public class ByteArrayDataSource implements DataSource {
  private byte[] data;
  private String type;
 
  public ByteArrayDataSource(byte[] data, String type) {
    super();
    this.data = data;
    this.type = type;
  }
 
  public ByteArrayDataSource(byte[] data) {
    super();
    this.data = data;
  }
 
  public void setType(String type) {
    this.type = type;
  }
 
  public String getContentType() {
    if (type == null)
      return "application/octet-stream";
    else
      return type;
  }
 
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(data);
  }
 
  public String getName() {
    return "ByteArrayDataSource";
  }
 
  public OutputStream getOutputStream() throws IOException {
    throw new IOException("Not Supported");
  }
}