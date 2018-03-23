package priv.starfish.common.base;
/**
 * id 编码与解码
 * @author koqiui
 *
 * @param <T>
 */
public interface IdCodecoder<T> {
	
	String encode(T id);

	T decode(String encodedId);
}
