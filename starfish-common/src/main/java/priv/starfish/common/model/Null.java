package priv.starfish.common.model;

import java.io.Serializable;

public class Null implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final Null VALUE = new Null();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Null";
	}

}
