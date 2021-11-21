package cn.eakoo.data;

import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/19 10:19
 **/
@Data
public class Result {

    /**
    * 错误码
    */
    private Integer code;

    /**
    * 错误信息
    */
    private String message;

    /**
    * 异常
    */
    private Exception exception;

    public Result() {
    }

    public Result(Integer code, String message, Exception exception) {
        this.code = code;
        this.message = message;
        this.exception = exception;
    }

    /**
     * 是否成功
     *
     * @return boolean
     */
    public boolean isSuccess(){
        return code == 0;
    }

}
