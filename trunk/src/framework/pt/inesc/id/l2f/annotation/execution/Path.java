package pt.inesc.id.l2f.annotation.execution;

public class Path {
	protected String _path;
	
	public Path(String path) {
		_path = path;
	}

	/**
	 * 
	 * @return the path
	 */
	public String getPath() {
		return _path;
	}

	/**
	 * 
	 * @param path the path to set
	 */
	public void setPath(String path) {
		_path = path;
	}
}
