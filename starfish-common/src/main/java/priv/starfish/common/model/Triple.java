package priv.starfish.common.model;

import java.io.Serializable;

/**
 * 三元组
 * 
 * @author koqiui
 * 
 * @param <T1>
 * @param <T2>
 * @param <T3>
 */
public class Triple<T1, T2, T3> implements Serializable {
	private static final long serialVersionUID = 1L;

	public Triple() {
		//
	}

	public Triple(T1 first, T2 second, T3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public static <T1, T2, T3> Triple<T1, T2, T3> newOne() {
		return new Triple<T1, T2, T3>();
	}

	public static <T1, T2, T3> Triple<T1, T2, T3> newOne(T1 first, T2 second, T3 third) {
		return new Triple<T1, T2, T3>(first, second, third);
	}

	protected T1 first;
	protected T2 second;
	protected T3 third;

	public T1 getFirst() {
		return first;
	}

	public void setFirst(T1 first) {
		this.first = first;
	}

	public T2 getSecond() {
		return second;
	}

	public void setSecond(T2 second) {
		this.second = second;
	}

	public T3 getThird() {
		return third;
	}

	public void setThird(T3 third) {
		this.third = third;
	}

	public String toString() {
		return this.first + "," + this.second + "," + this.third;
	}
}
