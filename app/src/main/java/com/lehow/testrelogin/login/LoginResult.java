package com.lehow.testrelogin.login;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class LoginResult {

  public class SaleInfo implements Serializable {
    private static final long serialVersionUID = -1161788617604970543L;
    public String accountId;
    public String mobile;
    public String nickName;
    public String realName;
    public String domain;
    public String accessToken;
    public String refreshToken;
    public ExtraData extraData;

    public class ExtraData implements Serializable {
      public int userType;
      public String wxNo;
      public String forcedState;
      public String gender;
      public String orgId;
      public String orgName;

      public String accountId;
      public String consultantId;
    }
  }
}
