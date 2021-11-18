package cn.eakoo.data;

import lombok.Data;

/**
 * @author rui.zhou
 * @date 2021/11/18 15:11
 **/
@Data
public class DataSource {

    /**
    * 用户
    */
    private String user;

    /**
     * 密码
     */
    private String password;

    /**
     * url
     */
    private String url;

}
