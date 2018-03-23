package priv.starfish.common.base;

public interface TargetJudger<T> {
	boolean isTarget(T toBeChecked);
}
