package priv.starfish.common.model;

import java.io.Serializable;

public class Size implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public int width = -1;
	public int height = -1;

	@Override
	public String toString() {
		return "Size [width=" + width + ", height=" + height + "]";
	}

}
