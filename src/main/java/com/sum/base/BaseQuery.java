package com.sum.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

/**
 * 查询入参基类
 */
@Data
public class BaseQuery {

    /**
     * 当前页码
     */
    private int currentPage = 1;

    /**
     * 妹页显示的条数
     */
    private int pageSize = 24;

    /**
     * 总条数
     */
    private int total = 0;

    /**
     * 获取总页数
     *
     * @return
     */
    public int pageCount() {
        if (this.getTotal() == 0) {
            return 1;
        }
        return this.getTotal() % this.getPageSize() == 0 ? (this.getTotal() / this.getPageSize()) : (this.getTotal() / this.getPageSize()) + 1;
    }

    public int pageCount(int total) {
        if (total == 0) {
            return 1;
        }
        return total % this.getPageSize() == 0 ? (total / this.getPageSize()) : (total / this.getPageSize()) + 1;
    }

    /**
     * 获取分页参数
     *
     * @return
     */
    public Page getPage() {
        return new Page(this.getCurrentPage(), this.getPageSize());
    }
}
