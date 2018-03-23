package priv.starfish.common.model;

/**
 * created by starfish on 2017-12-15
 */

public class Result<TData> {
    public enum Type {
        unknown(-1), info(0), warn(1), error(2), fatal(4);

        //
        private int value = -1;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return this.value;
        }
    }

    // 原始请求信息
    public Integer requestCode = RequestCode.Unknown;
    public String requestCodeName = null;
    // 结果信息
    public Type type = Type.info;
    public Integer code = ResultCode.Ok;
    public String codeName = null;
    public String message = null;
    public TData data = null;

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Result [");
        if (requestCode != null && RequestCode.Unknown != requestCode) {
            builder.append("requestCode = " + requestCode);
            builder.append(", requestCodeName = " + requestCodeName);
            builder.append(", ");
        }
        builder.append("type = " + type);
        builder.append(", code = " + code);
        builder.append(", codeName = " + codeName);
        builder.append(", message = " + message);
        builder.append(", data = " + data);
        builder.append("]");
        return builder.toString();
    }

    public Result<TData> echo() {
        System.out.println(this.toString());
        //
        return this;
    }

    public static <TData> Result<TData> newOne() {
        return new Result<TData>();
    }
}
