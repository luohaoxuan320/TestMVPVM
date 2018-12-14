package com.lehow.testmvp.testpaging;

/**
 * desc:
 * author: luoh17
 * time: 2018/10/20 19:37
 */
public class PageEntity {
  private int id;
  private String name;

  public PageEntity(int id, String name) {
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
