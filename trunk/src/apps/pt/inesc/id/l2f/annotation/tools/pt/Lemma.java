package pt.inesc.id.l2f.annotation.tools.pt;

import java.util.HashMap;
import java.util.Map;

public class Lemma {
	private String _gstem;
	
//	private String _pstem;
	
	private Map<String, String> _features;
	
	public Lemma() {
		_features = new HashMap<String, String>();
	}
	
	public Lemma(String stem) {
		this();
		
		_gstem = stem;
	}
	
	/**
	 * @return the _gstem
	 */
	public String getGStem() {
		return _gstem;
	}

	/**
	 * @param _gstem the _gstem to set
	 */
	public void setGStem(String _gstem) {
		this._gstem = _gstem;
	}

	/**
	 * @return the _cat
	 */
	public String get_cat() {
		return _features.get("CAT");
	}

	/**
	 * @return the _subcat
	 */
	public String get_subcat() {
		return _features.get("SCT");
	}

	/**
	 * @return the _features
	 */
	public java.util.Map<String, String> getFeatures() {
		return _features;
	}

	/**
	 * @param _features the _features to set
	 */
	public void set_features(java.util.Map<String, String> _features) {
		this._features = _features;
	}
	
	/**
	 * 
	 * 
	 * @param key
	 * @param value
	 */
	public void setFeature(String key, String value) {
		_features.put(key, value);
		
	}
}
