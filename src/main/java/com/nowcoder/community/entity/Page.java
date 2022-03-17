package com.nowcoder.community.entity;

/**
 * @author 刘逸菲
 * @create 2022-03-15 21:31
 * 分页插件类
 **/
public class Page {

    //当前页码
    private int current=1;
    //每页显示的数值,默认值为10
    private int limit=10;
    //数据总数
    private int rows;
    //分页查询跳转的url
    private String path;


    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1){
            this.current = current;
        }

    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if(limit>=1&&limit<=100){
            this.limit = limit;
        }

    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 查询偏移量
     * @return
     */
    public int getOffset(){
        return (current-1)*limit;
    }

    /**
     * 查询总页数
     * @return
     */
    public int getTotal(){
        if(rows%limit==0){
            return rows/limit;
        }else{
            return rows/limit+1;
        }
    }


    /**
     * 获得起始页[当前页码左数两页]
     * @return
     */
    public int getFrom(){
        int  from =current-2<1?1:current-2;
        return from;
    }

    /**
     * 获得尾页【当前页码右数两页】
     * @return
     */
    public int getTo(){
        int total=getTotal();
        int to=current+2>total?total:current+2;
        return to;
    }
}
