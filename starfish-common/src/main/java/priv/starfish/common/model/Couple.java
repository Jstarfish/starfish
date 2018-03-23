package priv.starfish.common.model;

import java.io.Serializable;

/**
 * 二元祖
 * 
 * @author koqiui
 * 
 * @param <T1>
 * @param <T2>
 */
public class Couple<T1, T2> implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	public Couple() {
		//
	}

	public Couple(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public static <T1, T2> Couple<T1, T2> newOne() {
		return new Couple<T1, T2>();
	}

	public static <T1, T2> Couple<T1, T2> newOne(T1 first, T2 second) {
		return new Couple<T1, T2>(first, second);
	}

	protected T1 first;
	protected T2 second;

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

	public String toString() {
		return this.first + "," + this.second;
	}
}
